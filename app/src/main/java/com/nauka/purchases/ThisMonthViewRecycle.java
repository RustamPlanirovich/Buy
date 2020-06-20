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

public class ThisMonthViewRecycle extends DialogFragment {
    private View myFragmentView;
    RecyclerView recyclerView;
    DBHelper dbHelper;
    SQLiteDatabase db;
    List<RecyclerItem> listItem;
    MyAdapter adapterMy;
    Context mContext;
    Calendar calendar;
    int Date;
    int Month;
    int Year;
    String today;
    TextView no;
    Context del;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(dialog_view_recycle,
                container, false);
        recyclerView = myFragmentView.findViewById(R.id.recyclerr);
        mContext = recyclerView.getContext();
        del = (MainActivity)getContext();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        listItem = new ArrayList<>();
        dbHelper = new DBHelper(mContext);
        no = myFragmentView.findViewById(R.id.no_checks);

        adapterMy = new MyAdapter(listItem, del);
        recyclerView.setAdapter(adapterMy);

        calendar = Calendar.getInstance();
        Date = calendar.get(Calendar.DAY_OF_MONTH);
        Month = calendar.get(Calendar.MONTH);
        Year = calendar.get(Calendar.YEAR);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy г.");
        today = String.valueOf(Date);
        String thisMonthAmount = String.valueOf(Month+1);


        db = dbHelper.getWritableDatabase();
        Cursor thisMonthCursor = db.rawQuery("SELECT*FROM purchasesTable WHERE mount = ? ORDER BY mount;", new String[]{thisMonthAmount});
        if (thisMonthCursor.getCount()==0) {
            no.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        if (thisMonthCursor.moveToFirst()) {
            int idColIndex = thisMonthCursor.getColumnIndex("id");
            int purchases = thisMonthCursor.getColumnIndex("purchases");
            int summ = thisMonthCursor.getColumnIndex("summ");
            int imageaddress = thisMonthCursor.getColumnIndex("imageaddres");
            int imageaddesview = thisMonthCursor.getColumnIndex("imageaddesview");
            int mount = thisMonthCursor.getColumnIndex("mount");
            do {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                listItem.add(new RecyclerItem(sdf.format(thisMonthCursor.getLong(purchases)),
                        String.valueOf(thisMonthCursor.getInt(summ)),
                        Uri.parse(thisMonthCursor.getString(imageaddress)), thisMonthCursor.getString(imageaddesview),
                        thisMonthCursor.getInt(idColIndex), String.valueOf(thisMonthCursor.getInt(mount))));
            } while (thisMonthCursor.moveToNext());

        }
        thisMonthCursor.close();
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