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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.nauka.purchases.R.layout.dialog_view_recycle;

public class TodayViewRecycle extends DialogFragment {
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
        String dayOfMounh = dateFormat.format(calendar.getTime());
        today = String.valueOf(Date);


        db = dbHelper.getWritableDatabase();
        Cursor todayCursor = db.rawQuery("SELECT * FROM purchasesTable WHERE day = ? ORDER BY day;", new String[]{today});
        if (todayCursor.getCount() == 0) {
            no.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        if (todayCursor.moveToFirst()) {
            int idColIndex = todayCursor.getColumnIndex("id");
            int purchases = todayCursor.getColumnIndex("purchases");
            int summ = todayCursor.getColumnIndex("summ");
            int imageaddress = todayCursor.getColumnIndex("imageaddres");
            int imageaddesview = todayCursor.getColumnIndex("imageaddesview");
            int mount = todayCursor.getColumnIndex("mount");
            do {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                listItem.add(new RecyclerItem(sdf.format(todayCursor.getLong(purchases)),
                        String.valueOf(todayCursor.getInt(summ)),
                        Uri.parse(todayCursor.getString(imageaddress)), todayCursor.getString(imageaddesview),
                        todayCursor.getInt(idColIndex), String.valueOf(todayCursor.getInt(mount))));
            } while (todayCursor.moveToNext());

        }
        todayCursor.close();
        return myFragmentView;
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