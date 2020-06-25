package com.nauka.purchases;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.nauka.purchases.R.layout.dialog_view_recycle;

public class LastMonthViewRecycle extends DialogFragment {
    //Объявляем View для отображения myFragmentView
    private View myFragmentView;
    /*Объявляем recyclerView для текущего месяца для заполнения его будущего содержимого
    и привязки к нему адаптера*/
    RecyclerView recyclerView;
    //Объявляем класс DBHelper создания базы данных
    DBHelper dbHelper;
    //Объявляем SQLiteDatabase для обеспечения чтения/записи БД
    SQLiteDatabase db;
    /*Объявляем listItem который заполняется из БД и передается в адаптер для заполнения
    содержимого экземпляра view*/
    List<RecyclerItem> listItem;
    //Объявляем adapterMy для привязки его к recyclerView и заполнения
    MyAdapter adapterMy;
    //Объявляем mContext который привязан к recyclerView
    Context mContext;
    //Объявляем класс calendar для получения из него текущей даты и времени
    Calendar calendar;
    //Объявляем Date для записи в него текущей даты
    int Date;
    //Объявляем Month для записи в него предыдущего месяца
    int Month;
    //Объявляем Year для записи в него текущего года
    int Year;
    //Объявляем today для записи в него текущей даты в виде строки
    String today;
    //Объявляем  no для отображения когда в recyclerView нет ни одного чека
    TextView no;
    //Объявляем Context контекст MainActivity
    Context del;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Инициализируем myFragmentView который создается на основе dialog_view_recycle
        myFragmentView = inflater.inflate(dialog_view_recycle,
                container, false);
        //Инициализируем recyclerView
        recyclerView = myFragmentView.findViewById(R.id.recyclerr);
        //Инициализируем mContext который привязан recyclerView
        mContext = recyclerView.getContext();
        //Инициализируем del который привязан к MainActivity
        del = (MainActivity)getContext();
        /*Инициализируем setHasFixedSize recyclerView тем самым говорим, что высота/ширина будут
        одинаковыми*/
        recyclerView.setHasFixedSize(true);
        //Привязываем setLayoutManager к recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //Инициализируем listItem
        listItem = new ArrayList<>();
        //Инициализируем dbHelper
        dbHelper = new DBHelper(mContext);
        //Инициализируем no который отображается когда нет ни одного чека
        no = myFragmentView.findViewById(R.id.no_checks);
        //Инициализируем adapterMy на основе listItem и контекста del
        adapterMy = new MyAdapter(listItem, del);
        //Привязываем adapterMy к recyclerView
        recyclerView.setAdapter(adapterMy);
        //Инициализируем calendar для получения текущей даты
        calendar = Calendar.getInstance();
        //Инициализируем Date и устанавливаем значение текущий день полученный из calendar
        Date = calendar.get(Calendar.DAY_OF_MONTH);
        //Инициализируем Month и устанавливает значение текущий месяц полученный из calendar
        Month = calendar.get(Calendar.MONTH);
        //Инициализируем Year и устанавливаем значение текущий год полученный из calendar
        Year = calendar.get(Calendar.YEAR);
        //Инициализируем строку с текущим днем взятым из Date
        today = String.valueOf(Date);
        //Инициализируем строку с предыдущим месяцем взятым из Month
        String lastMonth = String.valueOf(Month);

        //Инициализируем db для открытия БД для чтения/записи
        db = dbHelper.getWritableDatabase();
        //Объявляем и инициализируем курсор lastMonthCursor который подгружает данные только за этот месяц
        Cursor lastMonthCursor = db.rawQuery("SELECT*FROM purchasesTable WHERE mount = ? ORDER BY mount;", new String[]{lastMonth});
        //Если курсор вернулся пустым, то
        if (lastMonthCursor.getCount()==0) {
            //отображается надпись НЕТ ЧЕКОВ
            no.setVisibility(View.VISIBLE);
            //и recyclerView становиться невидимым
            recyclerView.setVisibility(View.GONE);
        }
        if (lastMonthCursor.moveToFirst()) {
            //Инициализируем int idColIndex и записываем в него id из БД
            int idColIndex = lastMonthCursor.getColumnIndex("id");
            //Инициализируем int purchases и записываем в него purchases из БД
            int purchases = lastMonthCursor.getColumnIndex("purchases");
            //Инициализируем int summ и записываем в него summ из БД
            int summ = lastMonthCursor.getColumnIndex("summ");
            //Инициализируем int imageaddress и записываем в него imageaddres из БД
            int imageaddress = lastMonthCursor.getColumnIndex("imageaddres");
            //Инициализируем int imageaddesview и записываем в него imageaddesview из БД
            int imageaddesview = lastMonthCursor.getColumnIndex("imageaddesview");
            //Инициализируем int mount и записываем в него mount из БД
            int mount = lastMonthCursor.getColumnIndex("mount");
            do {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                //Инициализируем listItem и добавляем в него все записи из БД
                listItem.add(new RecyclerItem(sdf.format(lastMonthCursor.getLong(purchases)),
                        String.valueOf(lastMonthCursor.getInt(summ)),
                        Uri.parse(lastMonthCursor.getString(imageaddress)), lastMonthCursor.getString(imageaddesview),
                        lastMonthCursor.getInt(idColIndex),String.valueOf(lastMonthCursor.getInt(mount))));
            } while (lastMonthCursor.moveToNext());

        }
        //Закрываем курсор lastMonthCursor
        lastMonthCursor.close();
        return myFragmentView;
    }

    //Переопределение класса onStart для полноэкранного отображения DialogFragment
    @Override
    public void onStart() {
        super.onStart(); Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height); } }
}