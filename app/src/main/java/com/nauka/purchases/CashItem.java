package com.nauka.purchases;

import android.net.Uri;


public class CashItem {
    private int id;
    private String purse;
    private int purseSumm;
    private String type;


    public CashItem(int id, String purse, int purseSumm, String type) {
        this.id = id;
        this.purse = purse;
        this.purseSumm = purseSumm;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getPurse() {
        return purse;
    }

    public int getPurseSumm() {
        return purseSumm;
    }

    public String getType() {
        return type;
    }

}
