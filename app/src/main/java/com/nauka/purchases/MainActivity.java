package com.nauka.purchases;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Объявляем класс базы данных
    Date mDate;
    //Объявляем TextView для отображения итоговой суммы
    TextView total_amount;
    //Объявляем FloatButton для добавления чека
    FloatingActionButton addPurchases;
    //Объявляем объект для данных
    DBHelper dbHelper;
    //Объявляем подключение к БД
    SQLiteDatabase db;
    addPurchasesFragment purchasesFragment;
    TodayViewRecycle mDetailViewRecycle;
    YesterdayViewRecycle mYesterdayViewRecycle;
    ThisMonthViewRecycle mThisMonthViewRecycle;
    LastMonthViewRecycle mLastMonthViewRecycle;
    Setting settingFragment;
    MyAdapter adapterMy;
    List<RecyclerItem> listItem;
    FloatingActionButton rec;
    ImageButton settingButt;
    TextView mountbal;
    TextView todayAmount;
    TextView yesterdayAmount;
    TextView forThisMonthAmount;
    TextView lastMonthAmount;
    Cursor mountBallance;
    TextView currentDate;
    private int Date;
    private int Month;
    private int Year;
    private Calendar calendar;
    String today;
    String yesterday;
    String thisMonthAmount;
    String lastMonth;
    Cursor c;
    TextView ballanc;
    CardView todayCardView;
    CardView yesterdayCardView;
    CardView forThisMonthCardView;
    CardView lastMonthCardView;
    int mountballanc;
    int summballance;

    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        todayAmount = findViewById(R.id.today_amount);
        yesterdayAmount = findViewById(R.id.yesterday_amount);
        forThisMonthAmount = findViewById(R.id.for_this_month_amount);
        lastMonthAmount = findViewById(R.id.last_month_amount);
        ballanc = findViewById(R.id.balance);
        todayCardView = findViewById(R.id.todayCardView);
        yesterdayCardView = findViewById(R.id.yesterdayCardView);
        forThisMonthCardView = findViewById(R.id.forThisMonthCardView);
        lastMonthCardView = findViewById(R.id.lastMonthCardView);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionStatusread = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        final int WRITE_EXTERNAL_STORAGE = 100;

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

        settingButt = findViewById(R.id.settingButton);
        mountbal = findViewById(R.id.mobal);
        currentDate = findViewById(R.id.currentDate);

        dbHelper = new DBHelper(this);

        purchasesFragment = new addPurchasesFragment();
        settingFragment = new Setting();
        mDetailViewRecycle = new TodayViewRecycle();
        mYesterdayViewRecycle = new YesterdayViewRecycle();
        mThisMonthViewRecycle = new ThisMonthViewRecycle();
        mLastMonthViewRecycle = new LastMonthViewRecycle();

        rec = findViewById(R.id.addPurchases);

        //Инициализация класса базы данных
        mDate = new Date();
        //Инициализация TextView "total_amount"
        total_amount = findViewById(R.id.total_amount);
        //Инициализация FloatButton "addPurchases"
        addPurchases = findViewById(R.id.addPurchases);


        listItem = new ArrayList<>();


        adapterMy = new MyAdapter(listItem, MainActivity.this);


        calendar = Calendar.getInstance();
        Date = calendar.get(Calendar.DAY_OF_MONTH);
        Month = calendar.get(Calendar.MONTH);
        Year = calendar.get(Calendar.YEAR);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy г.");
        String dayOfMounh = dateFormat.format(calendar.getTime());
        today = String.valueOf(Date);
        yesterday = String.valueOf(Date - 1);
        thisMonthAmount = String.valueOf(Month+1);
        lastMonth = String.valueOf(Month);

        Log.d("MY", yesterday);
        currentDate.setText(dayOfMounh);

        readStart();
        reStart();

        settingButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingFragment.show(getSupportFragmentManager(), "setting");
            }
        });

        todayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailViewRecycle.show(getSupportFragmentManager(), "detail");
            }
        });

        yesterdayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYesterdayViewRecycle.show(getSupportFragmentManager(), "detail");
            }
        });
        forThisMonthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThisMonthViewRecycle.show(getSupportFragmentManager(), "detail");
            }
        });
        lastMonthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLastMonthViewRecycle.show(getSupportFragmentManager(), "detail");
            }
        });
    }

    @Override
    public void onClick(View v) {
        purchasesFragment.show(getSupportFragmentManager(), "dialog");

    }

    public void readStart() {

        //Инициализируем подключение к БД
        db = dbHelper.getWritableDatabase();

        Cursor todayCursor = db.rawQuery("SELECT SUM(summ) FROM purchasesTable WHERE day = ? ORDER BY day;", new String[]{today});
        Cursor yesterdayCursor = db.rawQuery("SELECT SUM(summ) FROM purchasesTable WHERE day = ? ORDER BY day;", new String[]{yesterday});
        Cursor thisMonthCursor = db.rawQuery("SELECT SUM(summ) FROM purchasesTable WHERE mount = ? ORDER BY mount;", new String[]{thisMonthAmount});
        Cursor lastMonthCursor = db.rawQuery("SELECT SUM(summ) FROM purchasesTable WHERE mount = ? ORDER BY mount;", new String[]{lastMonth});


        Cursor c = db.rawQuery("SELECT * FROM purchasesTable ORDER BY purchases DESC;", null);
        Cursor cursor = db.rawQuery("SELECT SUM(summ) FROM purchasesTable WHERE mount = ? ORDER BY mount", new String[]{thisMonthAmount});
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");

            do {


                //Сумма всех чеков
                if (cursor.moveToFirst()) {
                    do {
                        total_amount.setText(String.valueOf(cursor.getInt(idColIndex)));
                    } while (cursor.moveToNext());
                }
                //Сумма всех чеков за текущий день
                if (todayCursor.moveToFirst()) {
                    do {
                        todayAmount.setText(String.valueOf(todayCursor.getInt(idColIndex)));
                    } while (todayCursor.moveToNext());
                }
                //Сумма всех чеков за прошлый день
                if (yesterdayCursor.moveToFirst()) {
                    do {
                        yesterdayAmount.setText(String.valueOf(yesterdayCursor.getInt(idColIndex)));
                    } while (yesterdayCursor.moveToNext());
                }
                //Сумма всех чеков за текущий месяц
                if (thisMonthCursor.moveToFirst()) {
                    do {
                        forThisMonthAmount.setText(String.valueOf(thisMonthCursor.getInt(idColIndex)));
                    } while (thisMonthCursor.moveToNext());
                }
                //Сумма всех чеков за прошлый месяц
                if (lastMonthCursor.moveToFirst()) {
                    do {
                        lastMonthAmount.setText(String.valueOf(lastMonthCursor.getInt(idColIndex)));
                    } while (lastMonthCursor.moveToNext());
                }

            } while (c.moveToNext());
        }
        c.close();
        cursor.close();
        todayCursor.close();
        yesterdayCursor.close();
        thisMonthCursor.close();
        lastMonthCursor.close();
    }

    public void delPosition(String sumPosition, String mounth) {
        if (Integer.parseInt(mounth) == Integer.parseInt(thisMonthAmount)) {
            int totalAmount = Integer.parseInt(total_amount.getText().toString());
            int sumPositionDel = Integer.parseInt(sumPosition);
            total_amount.setText(String.valueOf(totalAmount - sumPositionDel));
            int delBallancePlus = sumPositionDel + summballance;
            ballanc.setText("Остаток: " + String.valueOf(delBallancePlus));
        }
    }

    public void reStart() {

        mountBallance = db.rawQuery("SELECT * FROM mountBallance WHERE mount = ? ORDER BY mount;", new String[]{thisMonthAmount});
        if (mountBallance.moveToNext()) {
            mountballanc = mountBallance.getColumnIndex("mountballance");
            mountbal.setText(String.valueOf("Балланс: " + mountBallance.getInt(mountballanc)));
            summballance = mountBallance.getInt(mountballanc) - Integer.parseInt(total_amount.getText().toString());
            ballanc.setText("Остаток: " + String.valueOf(summballance));
        }
        mountBallance.close();
    }
}
