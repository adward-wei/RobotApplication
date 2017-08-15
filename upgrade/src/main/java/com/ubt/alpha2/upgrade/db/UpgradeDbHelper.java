package com.ubt.alpha2.upgrade.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ubt on 2017/7/5.
 */

public class UpgradeDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME= "upgrade";

    public UpgradeDbHelper(Context mc){
        super(mc,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UpgradeTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            db.execSQL(UpgradeTable.DROP_TABLE);
            db.execSQL(UpgradeTable.CREATE_TABLE);
        }
    }
}
