package com.nauka.purchases;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.nauka.purchases.R.layout.setting_fragment;

public class Setting extends DialogFragment {
    /*Объявляем EditText ballance в который загружается балланс на месяц и куда
    мы вводим балланс на месяц*/
    EditText ballance;
    //Объявляем Button addSum при клике которой происходит сохранение данных в БД
    Button addSum;
    //Объявляем класс DBHelper создания базы данных
    private DBHelper dbHelper;
    //Объявляем ContentValues cv данный конструктор служит для сохранения информации в БД
    private ContentValues cv;
    //Объявляем SQLiteDatabase для обеспечения чтения/записи БД
    private SQLiteDatabase db;
    //Объявляем int id где храниться id загруженной строки из БД
    int id;
    //Объявляем mContext который привязан к cashItem
    Context mContext;
    //Объявляем View для отображения myFragmentView
    private View myFragmentView;
    //Объявляем класс calendar для получения из него текущей даты и времени
    Calendar calendar;
    //Объявляем Date для записи в него текущей даты
    int Date;
    //Объявляем Month для записи в него предыдущего месяца
    int Month;
    //Объявляем Year для записи в него текущего года
    int Year;
    //Объявляем thisMonthAmount для записи в него текущего месяца в виде строки
    String thisMonthAmount;
    //Объявляем int mountballanc где храниться id именно mountballanc
    int mountballanc;
    //Объявляем int ballanceMount где храниться id именно ballanceMount
    int ballanceMount;
    //Объявляем int mount где храниться id именно mount
    int mount;
    //Объявляем int mountInt где храниться id именно mountInt
    int mountInt;
    /*Объявляем Button addCadh при клике на которую происходит сохранение
    нового значения в базу*/
    Button addCadh;
    //Объявляем класс AddCash
    AddCash cash;
    /*Объявляем recyclerView  для заполнения его будущего содержимого
    и привязки к нему адаптера*/
    RecyclerView cashItem;
    /*Объявляем listItem который заполняется из БД и передается в адаптер для заполнения
    содержимого экземпляра view*/
    List<CashItem> listItem;
    //Объявляем adapterMy для привязки его к recyclerView и заполнения
    CashAdapter adapterMy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Инициализируем myFragmentView который создается на основе setting_fragment
        myFragmentView = inflater.inflate(setting_fragment,
                container, false);
        //Инициализируем ballance куда загружаем балланс на месяц и тут же его корректируем
        ballance = myFragmentView.findViewById(R.id.ballanceSumm);
        //Инициализируем addCadh кнопка сохранения изменений в БД
        addCadh = myFragmentView.findViewById(R.id.addCash);
        //Инициализируем cashItem для отображения всех записей БД
        cashItem = myFragmentView.findViewById(R.id.cashItems);
        //Инициализируем mContext который привязан cashItem
        mContext = cashItem.getContext();
        //Инициализируем dbHelper
        dbHelper = new DBHelper(mContext);
        //Инициализируем db для открытия БД для чтения/записи
        db = dbHelper.getWritableDatabase();
        //Инициализируем ContentValues cv данный конструктор служит для сохранения информации в БД
        cv = new ContentValues();
        //Инициализируем класс AddCash и создаем новый экземпляр
        cash = new AddCash();
        /*Инициализируем listItem который заполняется из БД и передается в адаптер для заполнения
    содержимого экземпляра view*/
        listItem = new ArrayList<>();
        //Инициализируем adapterMy на основе listItem и контекста mContext
        adapterMy = new CashAdapter(listItem, mContext);
        //Привязываем adapterMy к cashItem
        cashItem.setAdapter(adapterMy);
        /*Инициализируем setHasFixedSize recyclerView тем самым говорим, что высота/ширина будут
        одинаковыми*/
        cashItem.setHasFixedSize(true);
        //Привязываем setLayoutManager к recyclerView
        cashItem.setLayoutManager(new LinearLayoutManager(mContext));
        //Инициализируем calendar для получения текущей даты
        calendar = Calendar.getInstance();
        //Инициализируем Date и устанавливаем значение текущий день полученный из calendar
        Date = calendar.get(Calendar.DAY_OF_MONTH);
        //Инициализируем Month и устанавливает значение текущий месяц полученный из calendar
        Month = calendar.get(Calendar.MONTH);
        //Инициализируем Year и устанавливаем значение текущий год полученный из calendar
        Year = calendar.get(Calendar.YEAR);
        //Инициализируем строку с текущим месяцем взятым из Date
        thisMonthAmount = String.valueOf(Month + 1);

        //Вызываем метод перезагрузки информации о кошельках
        re();

        //Инициализация кнопки addSum
        addSum = myFragmentView.findViewById(R.id.addSumm);
        //Объявляем и инициализируем курсор mountBallance который подгружает сумму за текущий месяц
        //балланс на месяц
        Cursor mountBallance = db.rawQuery("SELECT * FROM mountBallance WHERE mount = ? ORDER BY mount;", new String[]{thisMonthAmount});
        if (mountBallance.moveToFirst()) {
            //Инициализируем int id и записываем в него id из БД
            id = mountBallance.getColumnIndex("id");
            //Инициализируем int mountballanc и записываем в него mountballanc из БД
            mountballanc = mountBallance.getColumnIndex("mountballance");
            //Инициализируем int mount и записываем в него mount из БД
            mount = mountBallance.getColumnIndex("mount");
            //Инициализируем int mountInt и записываем в него mountInt из БД
            mountInt = mountBallance.getInt(mount);
            //Устанавливаем в ballance считанную сумму(балланс на месяц)
            ballance.setText(String.valueOf(mountBallance.getInt(mountballanc)));
            //Инициализируем int ballanceMount и записываем в него ballanceMount из БД
            ballanceMount = mountBallance.getInt(mountballanc);
        }
        //Закрываем курсор mountBallance
        mountBallance.close();

        //Если в базе нет записей то происходит запись строки в БД с текущим месяцем
        if (mountBallance.getCount() == 0) {
            addSum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cv.put("mountballance", ballance.getText().toString());
                    cv.put("mount", thisMonthAmount);
                    db.insert("mountballance", null, cv);
                    ((MainActivity) getActivity()).reStart();
                    dismiss();
                }
            });
        } else {
            //Если есть записи то идет обновление суммы в БД с текущим месяцем
            addSum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Вставка в базу данных введенных данных
                    cv.put("mountballance", ballance.getText().toString());
                    cv.put("mount", thisMonthAmount);
                    db.update("mountBallance", cv, "mountballance = ? OR mount = ?", new String[]{String.valueOf(ballanceMount), String.valueOf(mountInt)});
                    //Обновление информации в MainActivity
                    ((MainActivity) getActivity()).reStart();
                    //Закрытие диалога
                    dismiss();
                }
            });
        }

        //При клике открывается DialogFragment для добавления нового кошелька
        addCadh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash.show(getFragmentManager(), "detail");
            }
        });
        return myFragmentView;
    }

    //В данном методе перезагружаем информацию о кошельках
    public void re() {
        //сначала очишаем listItem
        listItem.clear();
        //далее обновляем адаптер
        adapterMy.notifyDataSetChanged();
        //делаем запрос в базу
        Cursor cashItemm = db.rawQuery("SELECT * FROM wallets ", null);
        if (cashItemm.moveToFirst()) {
            //Инициализируем int id и записываем в него id из БД
            int id = cashItemm.getColumnIndex("id");
            //Инициализируем int purseId и записываем в него purseId из БД
            int purseId = cashItemm.getColumnIndex("purse");
            //Инициализируем int purseSummId и записываем в него purseSummId из БД
            int purseSummId = cashItemm.getColumnIndex("purseSumm");
            //Инициализируем int typeId и записываем в него typeId из БД
            int typeId = cashItemm.getColumnIndex("type");
            do {
                //Инициализируем listItem и добавляем в него все записи из БД
                listItem.add(new CashItem(cashItemm.getInt(id),
                        cashItemm.getString(purseId),
                        cashItemm.getInt(purseSummId),
                        cashItemm.getString(typeId)));
            } while (cashItemm.moveToNext());
        }
    }

    //Переопределение класса onStart для полноэкранного отображения DialogFragment
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
