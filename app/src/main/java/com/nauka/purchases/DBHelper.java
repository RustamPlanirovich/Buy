package com.nauka.purchases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "purchasesDB", null, 9);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table purchasesTable("
                + "id integer primary key autoincrement,"
                + "purchases integer,"
                + "summ integer,"
                + "imageaddres URI,"
                + "imageaddesview String,"
                + "mountballance String,"
                + "day String,"
                + "mount String,"
                + "year String" + ");");

        db.execSQL("create table mountBallance("
                + "id integer primary key autoincrement,"
                + "mountballance String,"
                + "mount String" + ");");

        db.execSQL("create table wallets("
                + "id integer primary key autoincrement,"
                + "purse String,"
                + "purseSumm String,"
                + "cardWallets String,"
                + "cardWallets1 String,"
                + "cardWallets2 String,"
                + "cardWalletsSumm String,"
                + "cardWalletsSumm1 String,"
                + "cardWalletsSumm2 String,"
                + "type String"+ ");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE purchasesTable ADD COLUMN imageaddesview String");
        }
        if (oldVersion < 4) {
            db.execSQL("create table mountBallance("
                    + "id integer primary key autoincrement,"
                    + "mountballance String" + ");");
            db.execSQL("ALTER TABLE mountBallance ADD COLUMN mountballance String");
        }
        if (oldVersion < 6) {
            db.execSQL("ALTER TABLE purchasesTable ADD COLUMN day String");
            db.execSQL("ALTER TABLE purchasesTable ADD COLUMN mount String");
            db.execSQL("ALTER TABLE purchasesTable ADD COLUMN year String");
        }
        if (oldVersion < 7) {
            db.execSQL("ALTER TABLE mountBallance ADD COLUMN mount String");
        }
        if (oldVersion < 8) {
            db.execSQL("create table wallets("
                    + "id integer primary key autoincrement,"
                    + "purse String,"
                    + "purseSumm String,"
                    + "cardWallets String,"
                    + "cardWallets1 String,"
                    + "cardWallets2 String,"
                    + "cardWalletsSumm String,"
                    + "cardWalletsSumm1 String,"
                    + "cardWalletsSumm2 String" + ");");
        }
        if (oldVersion < 9) {
            db.execSQL("ALTER TABLE wallets ADD COLUMN type String");
        }
    }
}
