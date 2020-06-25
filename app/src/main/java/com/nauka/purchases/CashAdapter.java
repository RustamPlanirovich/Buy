package com.nauka.purchases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CashAdapter extends RecyclerView.Adapter<CashAdapter.ViewHolder> {
    //Объявляем listItems который полумаем из класса Setting(формируется в нем)
    private List<CashItem> listItems;
    //Объявляем Context текущего класса
    private Context mContext;
    //Объявляем класс DBHelper создания базы данных
    DBHelper dbHelper;
    //Объявляем SQLiteDatabase для обеспечения чтения/записи БД
    SQLiteDatabase db;

    public CashAdapter(List<CashItem> listItems, Context mContext) {
        //Инизиализируем listItems который получаем из класса Setting(формируется в нем)
        this.listItems = listItems;
        //Инициализируем mContext который так же получаем из класса Setting
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создаем view компонент экрана который сформирован в cash_item
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_item, parent, false);
        //Инициализируем класс БД - создаем экземпляр
        dbHelper = new DBHelper(mContext);
        //Открываем базу данных для чтения/записи
        db = dbHelper.getWritableDatabase();
        //Возвращаем экзепляр ViewHolder с созданным макетом v
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Считываем itemList построчно и добавлеяум содержимое в соответствующие поля
        final CashItem itemList = listItems.get(position);
        //Устанавливаем значение txtTitle из itemList.getPurse()
        holder.txtTitle.setText(itemList.getPurse());
        /*Устанавливаем значение txtDescription из String.valueOf(itemList.getPurseSumm())
        предварительно конвертировав в строку*/
        holder.txtDescription.setText(String.valueOf(itemList.getPurseSumm()));
        //Устанавливаем значение idcount из itemList.getId()
        final int idcount = itemList.getId();

        //Дейстивие при клике на txtOptionDigit
        holder.txtOptionDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Меню параметров при нажатии на 3 точки которые отображаются в txtOptionDigit
                PopupMenu popupMenu = new PopupMenu(mContext, holder.txtOptionDigit);
                //Устанавливаем вид popupMenu который прописан в layoute option_menu
                popupMenu.inflate(R.menu.option_menu);
                //Действие при выборе пункта меню
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //switch всех пунктов меню
                        switch (item.getItemId()) {
                            //Если кликнули на пункт Delete
                            case R.id.mnu_item_delete:
                                //Delete item

                        }
                        return false;
                    }
                });
                //Отображаем меню
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    //В данном классе объявляем и инициализируем все View компоненты
    class ViewHolder extends RecyclerView.ViewHolder{
        //Объявляем txtTitle здесь отображается название кошелька
        public TextView txtTitle;
        //Объявляем txtDescription сюда загружаем сумму в кошельке
        public TextView txtDescription;
        //Объявляем txtOptionDigit который служит кнопкой для показа меню
        public TextView txtOptionDigit;

        @SuppressLint("WrongViewCast")
        public ViewHolder(View itemView) {
            super(itemView);
            //Инициализируем txtTitle
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            //Инициализируем txtDescription
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            //Инициализируем txtOptionDigit
            txtOptionDigit = (TextView) itemView.findViewById(R.id.txtOptionDigit);
        }
    }
}
