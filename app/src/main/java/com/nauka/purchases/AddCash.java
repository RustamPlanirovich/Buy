package com.nauka.purchases;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import static com.nauka.purchases.R.layout.add_cash;

public class AddCash extends DialogFragment {
    //Объявляем View для отображения myFragmentView
    private View myFragmentView;
    //Объявляем EditText для названия кошелька
    private EditText nameCash;
    //Объявляем Spinner для выбора типа кошелька
    private Spinner typeCash;
    //Объявляем EditText для ввода суммы в кошельке
    private EditText summCash;
    //Объявляем Button для добавления инф. в базу
    private Button addCash;
    //Объявляем класс DBHelper создания базы данных
    private DBHelper dbHelper;
    //Объявляем ContentValues контейнер для вставки в базу данных
    private ContentValues cv;
    //Объявляем SQLiteDatabase для обеспечения чтения/записи БД
    private SQLiteDatabase db;
    //Объявляем Context контекст данного класса привязанный к myFragmentView
    private Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Инициализация myFragmentView
        myFragmentView = inflater.inflate(add_cash,
                container, false);
        //Инициализация mContext
        mContext = myFragmentView.getContext();
        //Инициализация nameCash
        nameCash = myFragmentView.findViewById(R.id.nameCash);
        //Инициализация typeCash
        typeCash = myFragmentView.findViewById(R.id.typeCash);
        //Инициализация summCash
        summCash = myFragmentView.findViewById(R.id.cashSumm);
        //Инициализация addCash
        addCash = myFragmentView.findViewById(R.id.addCash);
        //Инициализация dbHelper
        dbHelper = new DBHelper(mContext);
        //Инициализация db
        db = dbHelper.getWritableDatabase();
        //Инициализация cv
        cv = new ContentValues();

        /*При клике на addCash происходит запись введенных данных в базу данных.
        * В первом случае если выбранный ТИП в спиннере совпадает с НАЛИЧНЫЕ то в
        * строку type базы данных записывается CASH.
        * Во втором случае если выбранный ТИП в спиннере не совпадает то в строку
        * type базы данных записывается CARD*/
        addCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeCash.getSelectedItem().equals("Наличные")) {
                    cv.put("purse", nameCash.getText().toString());
                    cv.put("purseSumm", summCash.getText().toString());
                    cv.put("type", "cash");
                    db.insert("wallets", null, cv);
                    /*Обновление информации в Setting классе для отображения только что
                    внесенной информации*/
                    ((Setting)getFragmentManager().findFragmentByTag("setting")).re();
                } else {
                    cv.put("purse", nameCash.getText().toString());
                    cv.put("purseSumm", summCash.getText().toString());
                    cv.put("type", "card");
                    db.insert("wallets", null, cv);
                    /*Обновление информации в Setting классе для отображения только что
                    внесенной информации*/
                    ((Setting)getFragmentManager().findFragmentByTag("setting")).re();
                }
                //Закрытие диалога
                dismiss();
            }
        });
        return myFragmentView;
    }
//Переопределение класса onStart для полноэкранного отображения DialogFragment
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}

