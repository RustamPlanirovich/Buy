package com.nauka.purchases;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    //Объявляем
    private List<RecyclerItem> listItems;
    //Объявляем
    private Context mContext;
    //Объявляем
    DBHelper dbHelper;
    //Объявляем
    SQLiteDatabase db;
    //Объявляем
    String sumPosition;
    //Объявляем
    Context del;
    //Объявляем
    String mounth;
    public MyAdapter(List<RecyclerItem> listItems, Context mContext) {
        this.listItems = listItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RecyclerItem itemList = listItems.get(position);
        holder.txtTitle.setText(itemList.getTitle());
        holder.txtDescription.setText(itemList.getDescription());
        Glide.with(mContext).load(itemList.getImageaddres().toString()).into(holder.foto);
        final int a = itemList.getId();
        mounth = itemList.getMount();

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
                                int lastMounth = Integer.parseInt(((MainActivity)mContext).thisMonthAmount);
                                if (Integer.parseInt(mounth)==lastMounth) {
                                    listItems.remove(position);
                                    db.delete("purchasesTable", "id =" + a, null);
                                    File file = new File(itemList.getImageaddres().toString());
                                    sumPosition = holder.txtDescription.getText().toString();
                                    ((MainActivity) mContext).delPosition(sumPosition, mounth);
                                    ((MainActivity) mContext).readStart();
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "Deleted", Toast.LENGTH_LONG).show();
                                    break;
                                }
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();


            }
        });

        holder.foto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                holder.foto.setVisibility(View.GONE);
                holder.photo.setVisibility(View.VISIBLE);
                holder.photo.setImageURI(itemList.getImageaddres());
                int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width,height);
                holder.photo.setLayoutParams(params);

            }
        });
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                holder.photo.setVisibility(View.GONE);
                holder.foto.setVisibility(View.VISIBLE);

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
