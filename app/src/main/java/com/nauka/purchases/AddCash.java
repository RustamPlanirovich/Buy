package com.nauka.purchases;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import static com.nauka.purchases.R.layout.add_cash;
import static com.nauka.purchases.R.layout.setting_fragment;

public class AddCash extends DialogFragment {
    private View myFragmentView;
    private EditText nameCash;
    private Spinner typeCash;
    private EditText summCash;
    private Button addCash;
    private DBHelper dbHelper;
    private ContentValues cv;
    private SQLiteDatabase db;
    private Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(add_cash,
                container, false);
        mContext = myFragmentView.getContext();
        nameCash = myFragmentView.findViewById(R.id.nameCash);
        typeCash = myFragmentView.findViewById(R.id.typeCash);
        summCash = myFragmentView.findViewById(R.id.cashSumm);
        addCash = myFragmentView.findViewById(R.id.addCash);
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        cv = new ContentValues();

        addCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeCash.getSelectedItem().equals("Наличные")) {
                    cv.put("purse", nameCash.getText().toString());
                    cv.put("purseSumm", summCash.getText().toString());
                    cv.put("type", "cash");
                    db.insert("wallets", null, cv);
                    ((Setting)getFragmentManager().findFragmentByTag("setting")).re();
                } else {
                    cv.put("purse", nameCash.getText().toString());
                    cv.put("purseSumm", summCash.getText().toString());
                    cv.put("type", "card");
                    db.insert("wallets", null, cv);

                }
                dismiss();
            }
        });

        return myFragmentView;
    }

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

