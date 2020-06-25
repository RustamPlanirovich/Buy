package com.nauka.purchases;

import android.net.Uri;


public class RecyclerItem {
    //Объявляем String title отсюда запрашивается заголовок CardView
    private String title;
    //Объявляем String description отсюда запрашивается сумма в CardView
    private String description;
    //Объявляем Uri imageaddres отсюда запрашивается путь до изображения чека в CardView
    private Uri imageaddres;
    //Объявляем String imageaddesview отсюда есть ли сохраненное изображение чека в CardView
    private String imageaddesview;
    //Объявляем int id отсюда запрашивается id загружаемой строки в CardView
    private int id;
    //Объявляем String mount отсюда месяц создания чека в CardView
    private String mount;


    public RecyclerItem(String title, String description, Uri imageaddres, String imageaddesview,int id,String mount) {
        /*listItem заполняется при чтении данных из БД*/
        //Инициализируем title и присваеваем ему значение полученное из listItem
        this.title = title;
        //Инициализируем description и присваеваем ему значение полученное из listItem
        this.description = description;
        //Инициализируем imageaddres и присваеваем ему значение полученное из listItem
        this.imageaddres = imageaddres;
        //Инициализируем imageaddesview и присваеваем ему значение полученное из listItem
        this.imageaddesview = imageaddesview;
        //Инициализируем id и присваеваем ему значение полученное из listItem
        this.id = id;
        //Инициализируем mount и присваеваем ему значение полученное из listItem
        this.mount = mount;
    }

    //При запросе title отдается значение считанное из БД
    public String getTitle() {
        return title;
    }
    //При запросе description отдается значение считанное из БД
    public String getDescription() {
        return description;
    }
    //При запросе imageaddres отдается значение считанное из БД
    public Uri  getImageaddres() {
        return imageaddres;
    }
    //При запросе imageaddesview отдается значение считанное из БД
    public String getImageaddesview() {
        return imageaddesview;
    }
    //При запросе id отдается значение считанное из БД
    public int getId() {
        return id;
    }
    //При запросе mount отдается значение считанное из БД
    public String getMount() {
        return mount;
    }
}
