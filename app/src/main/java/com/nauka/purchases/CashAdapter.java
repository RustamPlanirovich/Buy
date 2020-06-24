package com.nauka.purchases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class CashAdapter extends RecyclerView.Adapter<CashAdapter.ViewHolder> {
    private List<CashItem> listItems;
    private Context mContext;
    DBHelper dbHelper;
    SQLiteDatabase db;
    String sumPosition;
    Context del;
    String mounth;
    public CashAdapter(List<CashItem> listItems, Context mContext) {
        this.listItems = listItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_item, parent, false);
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CashItem itemList = listItems.get(position);
        holder.txtTitle.setText(itemList.getPurse());
        holder.txtDescription.setText(String.valueOf(itemList.getPurseSumm()));

        final int a = itemList.getId();


        holder.txtOptionDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display option menu

                PopupMenu popupMenu = new PopupMenu(mContext, holder.txtOptionDigit);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.mnu_item_delete:
                                //Delete item

                        }
                        return false;
                    }
                });
                popupMenu.show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTitle;
        public TextView txtDescription;
        public TextView txtOptionDigit;
        public ImageView foto;
        public ImageView photo;
        public CardView mCardView;
        @SuppressLint("WrongViewCast")
        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtOptionDigit = (TextView) itemView.findViewById(R.id.txtOptionDigit);
            foto = (ImageView) itemView.findViewById(R.id.fotoch);
            mCardView = (CardView) itemView.findViewById(R.id.card);
            photo = (ImageView) itemView.findViewById(R.id.photo);
        }
    }


}
