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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    //Объявляем listItems который полумаем из всех представлений на главном экране
    private List<RecyclerItem> listItems;
    //Объявляем Context текущего класса
    private Context mContext;
    //Объявляем класс DBHelper создания базы данных
    DBHelper dbHelper;
    //Объявляем SQLiteDatabase для обеспечения чтения/записи БД
    SQLiteDatabase db;
    //Объявляем строку в которой храниться сумма текущей позиции
    String sumPosition;
    //Объявляем контекс MainActivity для удаления чеков
    Context del;
    //Объявляем строку в которой храниться текущий месяц
    String mounth;
    public MyAdapter(List<RecyclerItem> listItems, Context mContext) {
        //Инизиализируем listItems который полумаем из всех представлений на главном экране
        this.listItems = listItems;
        //Инициализируем mContext который так же получаем из MainActivity
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создаем view компонент экрана который сформирован в recycler_item
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
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
        final RecyclerItem itemList = listItems.get(position);
        //Устанавливаем значение txtTitle из itemList.getTitle()
        holder.txtTitle.setText(itemList.getTitle());
        //Устанавливаем значение txtTitle из itemList.getDescription()
        holder.txtDescription.setText(itemList.getDescription());
        //Загружаем и оптимизируем изображение из памяти
        Glide.with(mContext).load(itemList.getImageaddres().toString()).into(holder.foto);
        //Храним индекс выбранного элемента
        final int a = itemList.getId();
        //Храним месяц чека выбранного элемента
        mounth = itemList.getMount();

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
                //Отображаем меню
                popupMenu.show();


            }
        });
        //При клике на миниатюру открывается увеличенное представление фото
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                //Миниатюра скрывается
                holder.foto.setVisibility(View.GONE);
                //Увеличенное фото становиться видимым
                holder.photo.setVisibility(View.VISIBLE);
                //Берем путь для загрузки фото из itemList.getImageaddres()
                holder.photo.setImageURI(itemList.getImageaddres());
                //Устанавливаем размеры увеличенного изображения
                int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                //Устанавливаем параметры для слоя отображения увеличенного изображения
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width,height);
                //Обновляем параметры слоя
                holder.photo.setLayoutParams(params);
            }
        });
        //При клике на увеличенное изображение
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                //Увеличенное изображение делаем невидимым
                holder.photo.setVisibility(View.GONE);
                //Миниатюру делаем видимой
                holder.foto.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    //В данном классе объявляем и инициализируем все View компоненты
    public class ViewHolder extends RecyclerView.ViewHolder{
        //Объявляем txtTitle здесь отображается надпись СУММА ОПЕРАЦИИ
        public TextView txtTitle;
        //Объявляем txtDescription сюда загружаем сумму чека
        public TextView txtDescription;
        //Объявляем txtOptionDigit который служит кнопкой для показа меню
        public TextView txtOptionDigit;
        //Объявляем ImageView foto куда загружает фото чека
        public ImageView foto;
        //Объявляем ImageView photo куда загружаем фото чека увеличенное
        public ImageView photo;
        //Объявляем карточку где собраны все элементы
        public CardView mCardView;

        @SuppressLint("WrongViewCast")
        public ViewHolder(View itemView) {
            super(itemView);
            //Инициализируем txtTitle
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            //Инициализируем txtDescription
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            //Инициализируем txtOptionDigit
            txtOptionDigit = (TextView) itemView.findViewById(R.id.txtOptionDigit);
            //Инициализируем foto
            foto = (ImageView) itemView.findViewById(R.id.fotoch);
            //Инициализируем mCardView
            mCardView = (CardView) itemView.findViewById(R.id.card);
            //Инициализируем photo
            photo = (ImageView) itemView.findViewById(R.id.photo);
        }
    }
}
