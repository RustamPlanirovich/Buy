package com.nauka.purchases;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static com.nauka.purchases.R.layout.setting_fragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Setting extends DialogFragment {
    EditText ballance;
    Button addSum;
    private DBHelper dbHelper;
    private ContentValues cv;
    private SQLiteDatabase db;
    int id;
    Context mContext;
    private View myFragmentView;
    Calendar calendar;
    int Date;
    int Month;
    int Year;
    String thisMonthAmount;
    int mountballanc;
    int ballanceMount;
    int mount;
    int mountInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(setting_fragment,
                container, false);
        ballance = myFragmentView.findViewById(R.id.ballanceSumm);

        mContext = ballance.getContext();
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        cv = new ContentValues();

        calendar = Calendar.getInstance();
        Date = calendar.get(Calendar.DAY_OF_MONTH);
        Month = calendar.get(Calendar.MONTH);
        Year = calendar.get(Calendar.YEAR);
        thisMonthAmount = String.valueOf(Month+1);


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
//He

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
                    db.update("mountBallance", cv, "mountballance = ? OR mount = ?", new String[]{String.valueOf(ballanceMount),String.valueOf(mountInt)});

                    ((MainActivity) getActivity()).reStart();
                    dismiss();
                }
            });
        }


        return myFragmentView;
    }
    @Override
    public void onStart() {
        super.onStart(); Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height); } }
}
