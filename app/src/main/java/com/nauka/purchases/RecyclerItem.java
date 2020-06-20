package com.nauka.purchases;

import android.graphics.Bitmap;
import android.net.Uri;


public class RecyclerItem {
    private String title;
    private String description;
    private Uri imageaddres;
    private String imageaddesview;
    private int id;
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
