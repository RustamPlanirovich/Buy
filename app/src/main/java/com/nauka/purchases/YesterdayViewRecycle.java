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

public class YesterdayViewRecycle extends DialogFragment {
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
        String yesterday = String.valueOf(Date - 1);


        db = dbHelper.getWritableDatabase();
        Cursor yesterdayCursor = db.rawQuery("SELECT*FROM purchasesTable WHERE day = ? ORDER BY day;", new String[]{yesterday});
        if (yesterdayCursor.getCount()==0) {
            no.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        if (yesterdayCursor.moveToFirst()) {
            int idColIndex = yesterdayCursor.getColumnIndex("id");
            int purchases = yesterdayCursor.getColumnIndex("purchases");
            int summ = yesterdayCursor.getColumnIndex("summ");
            int imageaddress = yesterdayCursor.getColumnIndex("imageaddres");
            int imageaddesview = yesterdayCursor.getColumnIndex("imageaddesview");
            int mount = yesterdayCursor.getColumnIndex("mount");
            do {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                listItem.add(new RecyclerItem(sdf.format(yesterdayCursor.getLong(purchases)),
                        String.valueOf(yesterdayCursor.getInt(summ)),
                        Uri.parse(yesterdayCursor.getString(imageaddress)), yesterdayCursor.getString(imageaddesview),
                        yesterdayCursor.getInt(idColIndex),String.valueOf(yesterdayCursor.getInt(mount))));
            } while (yesterdayCursor.moveToNext());

        }
        yesterdayCursor.close();
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