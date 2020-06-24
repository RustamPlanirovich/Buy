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
    //Объявляем
    EditText ballance;
    //Объявляем
    Button addSum;
    //Объявляем
    private DBHelper dbHelper;
    //Объявляем
    private ContentValues cv;
    //Объявляем
    private SQLiteDatabase db;
    //Объявляем
    int id;
    //Объявляем
    Context mContext;
    //Объявляем
    private View myFragmentView;
    //Объявляем
    Calendar calendar;
    //Объявляем
    int Date;
    //Объявляем
    int Month;
    //Объявляем
    int Year;
    //Объявляем
    String thisMonthAmount;
    //Объявляем
    int mountballanc;
    //Объявляем
    int ballanceMount;
    //Объявляем
    int mount;
    //Объявляем
    int mountInt;
    //Объявляем
    Button addCadh;
    //Объявляем
    AddCash cash;
    //Объявляем
    RecyclerView cashItem;
    //Объявляем
    List<CashItem> listItem;
    //Объявляем
    CashAdapter adapterMy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(setting_fragment,
                container, false);
        ballance = myFragmentView.findViewById(R.id.ballanceSumm);
        addCadh = myFragmentView.findViewById(R.id.addCash);
        cashItem = myFragmentView.findViewById(R.id.cashItems);
        mContext = cashItem.getContext();
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        cv = new ContentValues();
        cash = new AddCash();
        listItem = new ArrayList<>();
        adapterMy = new CashAdapter(listItem, mContext);
        cashItem.setAdapter(adapterMy);
        cashItem.setHasFixedSize(true);
        cashItem.setLayoutManager(new LinearLayoutManager(mContext));


        calendar = Calendar.getInstance();
        Date = calendar.get(Calendar.DAY_OF_MONTH);
        Month = calendar.get(Calendar.MONTH);
        Year = calendar.get(Calendar.YEAR);
        thisMonthAmount = String.valueOf(Month + 1);

        re();

        addSum = myFragmentView.findViewById(R.id.addSumm);
        Cursor mountBallance = db.rawQuery("SELECT * FROM mountBallance WHERE mount = ? ORDER BY mount;", new String[]{thisMonthAmount});
        if (mountBallance.moveToFirst()) {
            id = mountBallance.getColumnIndex("id");
            mountballanc = mountBallance.getColumnIndex("mountballance");
            mount = mountBallance.getColumnIndex("mount");
            mountInt = mountBallance.getInt(mount);
            ballance.setText(String.valueOf(mountBallance.getInt(mountballanc)));
            ballanceMount = mountBallance.getInt(mountballanc);
        }
        mountBallance.close();

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
            addSum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cv.put("mountballance", ballance.getText().toString());
                    cv.put("mount", thisMonthAmount);
                    db.update("mountBallance", cv, "mountballance = ? OR mount = ?", new String[]{String.valueOf(ballanceMount), String.valueOf(mountInt)});

                    ((MainActivity) getActivity()).reStart();
                    dismiss();
                }
            });
        }

        addCadh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash.show(getFragmentManager(), "detail");
            }
        });

        return myFragmentView;
    }

    public void re() {
        listItem.clear();
        adapterMy.notifyDataSetChanged();
        Cursor cashItemm = db.rawQuery("SELECT * FROM wallets ", null);
        if (cashItemm.moveToFirst()) {
            int id = cashItemm.getColumnIndex("id");
            int purseId = cashItemm.getColumnIndex("purse");
            int purseSummId = cashItemm.getColumnIndex("purseSumm");
            int typeId = cashItemm.getColumnIndex("type");
            do {
                listItem.add(new CashItem(cashItemm.getInt(id),
                        cashItemm.getString(purseId),
                        cashItemm.getInt(purseSummId),
                        cashItemm.getString(typeId)));
            } while (cashItemm.moveToNext());
        }
    }

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
