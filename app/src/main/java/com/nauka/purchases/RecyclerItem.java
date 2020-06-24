package com.nauka.purchases;

import android.graphics.Bitmap;
import android.net.Uri;


public class RecyclerItem {
    //Объявляем
    private String title;
    //Объявляем
    private String description;
    //Объявляем
    private Uri imageaddres;
    //Объявляем
    private String imageaddesview;
    //Объявляем
    private int id;
    //Объявляем
    private String mount;


    public RecyclerItem(String title, String description, Uri imageaddres, String imageaddesview,int id,String mount) {
        this.title = title;
        this.description = description;
        this.imageaddres = imageaddres;
        this.imageaddesview = imageaddesview;
        this.id = id;
        this.mount = mount;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Uri  getImageaddres() {
        return imageaddres;
    }

    public String getImageaddesview() {
        return imageaddesview;
    }

    public int getId() {
        return id;
    }
    public String getMount() {
        return mount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
