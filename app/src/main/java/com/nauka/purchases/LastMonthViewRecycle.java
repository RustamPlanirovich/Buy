package com.nauka.purchases;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
        String lastMonth = String.valueOf(Month);
        Log.d("M", lastMonth);


        db = dbHelper.getWritableDatabase();
        Cursor lastMonthCursor = db.rawQuery("SELECT*FROM purchasesTable WHERE mount = ? ORDER BY mount;", new String[]{lastMonth});
        if (lastMonthCursor.getCount()==0) {
            no.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        if (lastMonthCursor.moveToFirst()) {
            int idColIndex = lastMonthCursor.getColumnIndex("id");
            int purchases = lastMonthCursor.getColumnIndex("purchases");
            int summ = lastMonthCursor.getColumnIndex("summ");
            int imageaddress = lastMonthCursor.getColumnIndex("imageaddres");
            int imageaddesview = lastMonthCursor.getColumnIndex("imageaddesview");
            int mount = lastMonthCursor.getColumnIndex("mount");
            do {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                listItem.add(new RecyclerItem(sdf.format(lastMonthCursor.getLong(purchases)),
                        String.valueOf(lastMonthCursor.getInt(summ)),
                        Uri.parse(lastMonthCursor.getString(imageaddress)), lastMonthCursor.getString(imageaddesview),
                        lastMonthCursor.getInt(idColIndex),String.valueOf(lastMonthCursor.getInt(mount))));
            } while (lastMonthCursor.moveToNext());

        }
        lastMonthCursor.close();
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