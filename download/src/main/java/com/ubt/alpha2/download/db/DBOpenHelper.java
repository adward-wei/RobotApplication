package com.ubt.alpha2.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
*/
public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "download.db";

    // 升级确保数据正确
    private static final int DB_VERSION = 86;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    void createTable(SQLiteDatabase db) {
        ThreadInfoDao.createTable(db);
    }

    void dropTable(SQLiteDatabase db) {
        ThreadInfoDao.dropTable(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db);
        createTable(db);
    }
}
