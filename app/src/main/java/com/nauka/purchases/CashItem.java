package com.nauka.purchases;

public class CashItem {
    //Инициализация id
    private int id;
    //Инициализация purse
    private String purse;
    //Инициализация purseSumm
    private int purseSumm;
    //Инициализация type
    private String type;


    public CashItem(int id, String purse, int purseSumm, String type) {
        this.id = id;
        this.purse = purse;
        this.purseSumm = purseSumm;
        this.type = type;
    }

    //Метод getId с помощью которого вытягиваются данные id из считанной строки из БД
    public int getId() {
        return id;
    }
    //Метод getPurse с помощью которого вытягиваются данные purse из считанной строки из БД
    //purse - это название кошелька
    public String getPurse() {
        return purse;
    }
    //Метод getPurseSumm с помощью которого вытягиваются данные purseSumm из считанной строки из БД
    //purseSumm - это сумма в кошельке
    public int getPurseSumm() {
        return purseSumm;
    }
    //Метод getType с помощью которого вытягиваются данные type из считанной строки из БД
    //type - это тип кошелька(карта или наличные)
    public String getType() {
        return type;
    }

}
