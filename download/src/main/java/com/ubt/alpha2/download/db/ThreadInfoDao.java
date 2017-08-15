package com.ubt.alpha2.download.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ubt.alpha2.download.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
 */

public class ThreadInfoDao extends AbstractDao<ThreadInfo> {

    private static final String TABLE_NAME = ThreadInfo.class.getSimpleName();

    public ThreadInfoDao(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + TABLE_NAME
                    + "(_id integer primary key autoincrement, id integer, tag text, uri text,file_length long, start long, end long, finished long ,onlywifi integer)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dropTable(SQLiteDatabase db) {
        try {
            db.execSQL("drop table if exists " + TABLE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(ThreadInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            try {
                db.execSQL(
                        "insert into " + TABLE_NAME
                                + "(id, tag, uri, file_length,start, end, finished ,onlywifi) values(?, ?, ?,?, ?, ?, ? ,?)",
                        new Object[] {info.getId(), info.getTag(), info.getUri(),info.getFileLength(), info.getStart(), info.getEnd(),
                                info.getFinished(), info.getOnlywifi()});
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // close();
            }
        }
    }

    public void delete(String tag) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            try {
                db.execSQL("delete from " + TABLE_NAME + " where tag = ?", new Object[] {tag});
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // close();
            }
        }
    }

    public void update(String tag, int threadId, long finished) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            try {
                db.execSQL("update " + TABLE_NAME + " set finished = ?" + " where tag = ? and id = ? ",
                        new Object[] {finished, tag, threadId});
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // close();
            }
        }
    }

    public List<ThreadInfo> getThreadInfos(String tag) {
        List<ThreadInfo> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + TABLE_NAME + " where tag = ?", new String[] {tag});
            while (cursor.moveToNext()) {
                ThreadInfo info = new ThreadInfo();
                info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                info.setTag(cursor.getString(cursor.getColumnIndex("tag")));
                info.setUri(cursor.getString(cursor.getColumnIndex("uri")));
                info.setFileLength(cursor.getLong(cursor.getColumnIndex("file_length")));
                info.setEnd(cursor.getLong(cursor.getColumnIndex("end")));
                info.setStart(cursor.getLong(cursor.getColumnIndex("start")));
                info.setFinished(cursor.getLong(cursor.getColumnIndex("finished")));
                info.setOnlywifi(cursor.getInt(cursor.getColumnIndex("onlywifi")));
                list.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public List<ThreadInfo> getThreadInfos() {
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            ThreadInfo info = new ThreadInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setTag(cursor.getString(cursor.getColumnIndex("tag")));
            info.setUri(cursor.getString(cursor.getColumnIndex("uri")));
            info.setEnd(cursor.getLong(cursor.getColumnIndex("end")));
            info.setStart(cursor.getLong(cursor.getColumnIndex("start")));
            info.setFinished(cursor.getLong(cursor.getColumnIndex("finished")));
            list.add(info);
        }
        cursor.close();
        return list;
    }

    public boolean exists(String tag, int threadId) {
        SQLiteDatabase db = getReadableDatabase();
        boolean isExists = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + TABLE_NAME + " where tag = ? and id = ?",
                    new String[] {tag, threadId + ""});
            isExists = cursor.moveToNext();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExists;
    }

}
