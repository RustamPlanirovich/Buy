package com.nauka.purchases;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Объявляем класс для получения даты
    Date mDate;
    //Объявляем TextView для отображения итоговой суммы
    TextView total_amount;
    //Объявляем FloatButton для добавления чека
    FloatingActionButton addPurchases;
    //Объявляем класс DBHelper создания базы данных
    DBHelper dbHelper;
    //Объявляем SQLiteDatabase для обеспечения чтения/записи БД
    SQLiteDatabase db;
    //Объявляем DialogFragment AddPurchasesFragment добавления нового чека
    AddPurchasesFragment purchasesFragment;
    //Объявляем DialogFragment TodayViewRecycle для отображения чеков за текущий день
    TodayViewRecycle mDetailViewRecycle;
    //Объявляем DialogFragment YesterdayViewRecycle для отображения чеков за вчерашний день
    YesterdayViewRecycle mYesterdayViewRecycle;
    //Объявляем DialogFragment ThisMonthViewRecycle для отображения чеков за текущий месяц
    ThisMonthViewRecycle mThisMonthViewRecycle;
    //Объявляем DialogFragment LastMonthViewRecycle для отображения чеков за прошлый месяц
    LastMonthViewRecycle mLastMonthViewRecycle;
    //Объявляем DialogFragment Setting для отображения основных настроек
    Setting settingFragment;
    //Объявляем adapterMy для привязки его к recyclerView и заполнения
    MyAdapter adapterMy;
    /*Объявляем listItem который заполняется из БД и передается в адаптер для заполнения
    содержимого экземпляра view*/
    List<RecyclerItem> listItem;
    /*Объявляем кнопку FloatingActionButton для отображения DialogFragment добавления
    нового чека*/
    FloatingActionButton rec;
    //Объявляем кнопку ImageButton для отображения DialogFragment с основными настройками
    ImageButton settingButt;
    //Объявляем TextView mountbal для отображения балланса на месяц
    TextView mountbal;
    //Объявляем TextView todayAmount для отображения суммы за текущий день
    TextView todayAmount;
    //Объявляем TextView yesterdayAmount для отображения суммы за вчерашний день
    TextView yesterdayAmount;
    //Объявляем TextView forThisMonthAmount для отображения суммы за текущий месяц
    TextView forThisMonthAmount;
    //Объявляем TextView lastMonthAmount для отображения суммы за прошлый месяц
    TextView lastMonthAmount;
    //Объявляем Cursor mountBallance
    Cursor mountBallance;
    //Объявляем TextView currentDate для отображения текущей даты
    TextView currentDate;
    //Объявляем Date для записи в него текущей даты
    private int Date;
    //Объявляем Month для записи в него предыдущего месяца
    private int Month;
    //Объявляем Year для записи в него текущего года
    private int Year;
    //Объявляем класс calendar для получения из него текущей даты и времени
    private Calendar calendar;
    //Объявляем строку today в которую помещаем текущую дату в виде String
    String today;
    //Объявляем строку yesterday в которую помещаем вчерашнюю дату в виде String
    String yesterday;
    //Объявляем строку thisMonthAmount в которую помещаем текущий месяц в виде String
    String thisMonthAmount;
    //Объявляем строку lastMonth в которую помещаем предыдущий месяц
    String lastMonth;
    //Объявляем TextView ballanc в котором отображается оставшийся балланс на месяц
    TextView ballanc;
    //Объявляем CardView todayCardView  текущий день
    CardView todayCardView;
    //Объявляем CardView yesterdayCardView вчерашний день
    CardView yesterdayCardView;
    //Объявляем CardView forThisMonthCardView текущий месяц
    CardView forThisMonthCardView;
    //Объявляем CardView lastMonthCardView предыдущий месяц
    CardView lastMonthCardView;
    //Объявляем переменную хранящую балланс на месяц
    int mountballanc;
    //Объявляем переменную хранящую сумму всех чеков за месяц
    int summballance;

    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //Инициализируем TextView today_anount
        todayAmount = findViewById(R.id.today_amount);
        //Инициализируем TextView yesterday_amount
        yesterdayAmount = findViewById(R.id.yesterday_amount);
        //Инициализируем TextView for_this_month_amount
        forThisMonthAmount = findViewById(R.id.for_this_month_amount);
        //Инициализируем TextView last_month_amount
        lastMonthAmount = findViewById(R.id.last_month_amount);
        //Инициализируем TextView balance
        ballanc = findViewById(R.id.balance);
        //Инициализируем CardView отображающий данные за текущий день
        todayCardView = findViewById(R.id.todayCardView);
        //Инициализируем CardView отображающий данные за вчерашний день
        yesterdayCardView = findViewById(R.id.yesterdayCardView);
        //Инициализируем CardView отображающий данные за текущий месяц
        forThisMonthCardView = findViewById(R.id.forThisMonthCardView);
        //Инициализируем CardView отображающий данные за прошлый месяц
        lastMonthCardView = findViewById(R.id.lastMonthCardView);

        /*В переменные permissionStatus/permissionStatusread записываются данные для сверки
        есть ли разрешение чтения/записи во внутреннюю память*/
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionStatusread = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        final int WRITE_EXTERNAL_STORAGE = 100;

        //Проверка на наличие разрешений на чтение/запись во внутреннуюю память
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE);
        }
        if (permissionStatusread == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE);
        }

        //Инициализируем кнопку settingButton
        settingButt = findViewById(R.id.settingButton);
        //Инициализируем строку mobal
        mountbal = findViewById(R.id.mobal);
        //Инициализируем строку currentDate
        currentDate = findViewById(R.id.currentDate);

        //Инициализируем dbHelper
        dbHelper = new DBHelper(this);

        //Инициализируем и создаем экземпляр класса AddPurchasesFragment
        purchasesFragment = new AddPurchasesFragment();
        //Инициализируем и создаем экземпляр класса Setting
        settingFragment = new Setting();
        //Инициализируем и создаем экземпляр класса TodayViewRecycle
        mDetailViewRecycle = new TodayViewRecycle();
        //Инициализируем и создаем экземпляр класса YesterdayViewRecycle
        mYesterdayViewRecycle = new YesterdayViewRecycle();
        //Инициализируем и создаем экземпляр класса ThisMonthViewRecycle
        mThisMonthViewRecycle = new ThisMonthViewRecycle();
        //Инициализируем и создаем экземпляр класса LastMonthViewRecycle
        mLastMonthViewRecycle = new LastMonthViewRecycle();
        //Инициализируем кнопку addPurchases
        rec = findViewById(R.id.addPurchases);
        //Инициализация класса базы данных
        mDate = new Date();
        //Инициализация TextView "total_amount"
        total_amount = findViewById(R.id.total_amount);
        //Инициализация FloatButton "addPurchases"
        addPurchases = findViewById(R.id.addPurchases);
        //Инициализация listItem для заполнения данными полученными из курсоров
        listItem = new ArrayList<>();
        /*Адаптер для подключения ко всем видам на главном экране
        (текущий/вчерашний день, прошлый/текущий месяц)*/
        adapterMy = new MyAdapter(listItem, MainActivity.this);
        //Инициализируем calendar для получения текущего дня и времени
        calendar = Calendar.getInstance();
        //Получаем текущую дату
        Date = calendar.get(Calendar.DAY_OF_MONTH);
        //Получаем текущий месяц
        Month = calendar.get(Calendar.MONTH);
        //Получаем текущий год
        Year = calendar.get(Calendar.YEAR);
        //Задаем маску для изменения полученную дату в вид "EEE, dd MMMM yyyy г."
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy г.");
        //Здесь приводим дату в вид в соответствии с маской
        String dayOfMounh = dateFormat.format(calendar.getTime());
        //Конвертируем полученную дату в строку
        today = String.valueOf(Date);
        //Конвертируем полученную дату в строку и приводим во вчерашний день
        yesterday = String.valueOf(Date - 1);
        //Конвертируем текущий месяц в строку
        thisMonthAmount = String.valueOf(Month+1);
        //Конвертируем текущий месяц в строку и приводим к пердыдущему месяцу
        lastMonth = String.valueOf(Month);
        //В панель currentDate вставляется текущая дата
        currentDate.setText(dayOfMounh);
        //Вызывается метод readStart
        readStart();
        //Вызывается метод reStart
        reStart();
        //При клике на иконку настроек открывается DialogFragment с основными настройками
        settingButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingFragment.show(getSupportFragmentManager(), "setting");
            }
        });
        /*При клике на карточку текущего дня, открывается DialogFragment
        на весь экран и отображаются записи за текущий день*/
        todayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailViewRecycle.show(getSupportFragmentManager(), "detail");
            }
        });
        /*При клике на карточку вчерашнего дня, открывается DialogFragment
        на весь экран и отображаются записи за предыдущий день*/
        yesterdayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYesterdayViewRecycle.show(getSupportFragmentManager(), "detail");
            }
        });
        /*При клике на карточку текущего месяца, открывается DialogFragment
        на весь экран и отображаются записи за текущий месяц*/
        forThisMonthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThisMonthViewRecycle.show(getSupportFragmentManager(), "detail");
            }
        });
        /*При клике на карточку предыдущего месяца, открывается DialogFragment
        на весь экран и отображаются записи за предыдущий месяц*/
        lastMonthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLastMonthViewRecycle.show(getSupportFragmentManager(), "detail");
            }
        });
    }

    @Override
    public void onClick(View v) {
        /*Метод onClick при нажатии на кнопку добавления новго чека
        открывается DialogFragment где происходит ввод всех данных и добавление в БД*/
        purchasesFragment.show(getSupportFragmentManager(), "dialog");
    }

    //В данном методе делаем все запросы в БД для отображения на домашнем экране.
    public void readStart() {
        //Инициализируем подключение к БД
        db = dbHelper.getWritableDatabase();
        //Курсор с помощью которого получаем сумму всех записей за текущий день
        Cursor todayCursor = db.rawQuery("SELECT SUM(summ) FROM purchasesTable WHERE day = ? ORDER BY day;", new String[]{today});
        //Курсор с помощью которого получаем сумму всех записей за вчерашний день
        Cursor yesterdayCursor = db.rawQuery("SELECT SUM(summ) FROM purchasesTable WHERE day = ? ORDER BY day;", new String[]{yesterday});
        //Курсор с помощью которого получаем сумму всех записей за текущий месяц
        Cursor thisMonthCursor = db.rawQuery("SELECT SUM(summ) FROM purchasesTable WHERE mount = ? ORDER BY mount;", new String[]{thisMonthAmount});
        //Курсор с помощью которого получаем сумму всех записей за прошлый месяц
        Cursor lastMonthCursor = db.rawQuery("SELECT SUM(summ) FROM purchasesTable WHERE mount = ? ORDER BY mount;", new String[]{lastMonth});
        //Курсор с помощью которого получаем все записи из БД
        Cursor c = db.rawQuery("SELECT * FROM purchasesTable ORDER BY purchases DESC;", null);
        //Курсор с помощью которого получаем сумму всех записей за текущий месяц
        Cursor cursor = db.rawQuery("SELECT SUM(summ) FROM purchasesTable WHERE mount = ? ORDER BY mount", new String[]{thisMonthAmount});
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            do {
                //Сумма всех чеков
                if (cursor.moveToFirst()) {
                    do {
                        //Устанавливаем в total_amount сумму всех чеков полученных из БД
                        total_amount.setText(String.valueOf(cursor.getInt(idColIndex)));
                    } while (cursor.moveToNext());
                }
                //Сумма всех чеков за текущий день
                if (todayCursor.moveToFirst()) {
                    do {
                        //Устанавливаем в total_amount сумму всех чеков за
                        // текущий день полученных из БД
                        todayAmount.setText(String.valueOf(todayCursor.getInt(idColIndex)));
                    } while (todayCursor.moveToNext());
                }
                //Сумма всех чеков за прошлый день
                if (yesterdayCursor.moveToFirst()) {
                    do {
                        //Устанавливаем в total_amount сумму всех чеков за
                        // прошлый день полученных из БД
                        yesterdayAmount.setText(String.valueOf(yesterdayCursor.getInt(idColIndex)));
                    } while (yesterdayCursor.moveToNext());
                }
                //Сумма всех чеков за текущий месяц
                if (thisMonthCursor.moveToFirst()) {
                    do {
                        //Устанавливаем в total_amount сумму всех чеков за
                        // этот месяц полученных из БД
                        forThisMonthAmount.setText(String.valueOf(thisMonthCursor.getInt(idColIndex)));
                    } while (thisMonthCursor.moveToNext());
                }
                //Сумма всех чеков за прошлый месяц
                if (lastMonthCursor.moveToFirst()) {
                    do {
                        //Устанавливаем в total_amount сумму всех чеков за
                        // прошлый месяц полученных из БД
                        lastMonthAmount.setText(String.valueOf(lastMonthCursor.getInt(idColIndex)));
                    } while (lastMonthCursor.moveToNext());
                }

            } while (c.moveToNext());
        }
        //Закрываем курсор c
        c.close();
        //Закрываем курсор cursor
        cursor.close();
        //Закрываем курсор todayCursor
        todayCursor.close();
        //Закрываем курсор yesterdayCursor
        yesterdayCursor.close();
        //Закрываем курсор
        thisMonthCursor.close();
        //Закрываем курсор lastMonthCursor
        lastMonthCursor.close();
    }

    //В этом методе происходит обновление информации при удалении чеков
    public void delPosition(String sumPosition, String mounth) {
        /*Если у удаляемого чека месяц совпадает с текущим то только тогда чек
        * будет удален*/
        if (Integer.parseInt(mounth) == Integer.parseInt(thisMonthAmount)) {
            //Считываем общую сумму из total_amount
            int totalAmount = Integer.parseInt(total_amount.getText().toString());
            //Сюда попадает сумма удаляемого чека
            int sumPositionDel = Integer.parseInt(sumPosition);
            /*Далее в total_amount устанавливается общая сумма за вычетом
            суммы удаляемого чека*/
            total_amount.setText(String.valueOf(totalAmount - sumPositionDel));
            /*Здусь формируется сумма полученная после прибавления удаляемой суммы
            * к запланированному бюджету*/
            int delBallancePlus = sumPositionDel + summballance;
            /*Устанавливаем запланированный балланс после операции суммирования
            удаляемой суммы и запланированного балланса*/
            ballanc.setText("Остаток: " + String.valueOf(delBallancePlus));
        }
    }

    /*В данном методе считываем запланированный балланс для отображения
    на главном экране*/
    public void reStart() {
        //Курсор который делает запрос в БД и считывает балланс на месяц
        mountBallance = db.rawQuery("SELECT * FROM mountBallance WHERE mount = ? ORDER BY mount;", new String[]{thisMonthAmount});
        if (mountBallance.moveToNext()) {
            //Устанавливаем в переменную mountballanc считанное значение
            mountballanc = mountBallance.getColumnIndex("mountballance");
            //Устанавливаем считаннове значение в mountbal
            mountbal.setText(String.valueOf("Балланс: " + mountBallance.getInt(mountballanc)));
            //Вычитаем сумму всех чеков за текущий месяц из балланса на месяц
            summballance = mountBallance.getInt(mountballanc) - Integer.parseInt(total_amount.getText().toString());
            /*Устанавливаем новое значение после вычита сумму чеков за текущий
            месяц из балланса на месяц*/
            ballanc.setText("Остаток: " + String.valueOf(summballance));
        }
        //Закрываем курсор mountBallance
        mountBallance.close();
    }
}
