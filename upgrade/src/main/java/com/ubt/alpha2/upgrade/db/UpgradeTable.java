package com.ubt.alpha2.upgrade.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ubt.alpha2.upgrade.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: slive
 * @description: upgrade 信息的本地存储
 * @create: 2017/7/5
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */

public class UpgradeTable {
    private static final String TABLE_NAME = "upgradeInfo";
    private static final String FIELD_MODULE_ID = "module_id";
    private static final String FIELD_MODULE_NAME= "module_name";
    private static final String FIELD_MD5= "md5";
    private static final String FIELD_MD5MAIN= "MD5Main";
    private static final String FIELD_URL= "url";
    private static final String FIELD_URLMAIN= "urlMain";
    private static final String FIELD_REMARK= "remark";
    private static final String FIELD_UPGRADETYPE= "upgradeType";
    private static final String FIELD_UPDATETYPE= "updateType";
    private static final String FIELD_FROMVERSION= "fromVersion";
    private static final String FIELD_TOVERSION= "toVersion";
    private static final String FIELD_DOWNLOADSUCCESS= "downloadSuccess";
    private static final String FIELD_LOCALFILEPATH= "localFilepath";
    private static final String FIELD_PATCH_UPGRADE = "isPatch";    //是否是patch 下载
    public static final String CREATE_TABLE = "create table if not exists "+TABLE_NAME+"( "
                            +"_id integer primary key autoincrement ,"
                            +FIELD_MODULE_ID +" varchar ,"
                            +FIELD_MODULE_NAME +" varchar,"
                            +FIELD_MD5 +" text ,"
                            +FIELD_MD5MAIN+" text ,"
                            +FIELD_URL + " text ,"
                            +FIELD_URLMAIN +" text ,"
                            +FIELD_REMARK +" text ,"
                            +FIELD_UPGRADETYPE+" varchar ,"
                            +FIELD_UPDATETYPE+" varchar ,"
                            +FIELD_FROMVERSION+" varchar ,"
                            +FIELD_TOVERSION+" varchar ,"
                            +FIELD_DOWNLOADSUCCESS+" integer ,"
                            +FIELD_PATCH_UPGRADE + " integer ,"
                            +FIELD_LOCALFILEPATH+ " text"
                            +")";

    public static final String[] FIELDS = {FIELD_MODULE_ID,FIELD_MD5,FIELD_MD5MAIN,FIELD_URL,FIELD_URLMAIN,
            FIELD_REMARK,FIELD_UPGRADETYPE,FIELD_UPDATETYPE,FIELD_FROMVERSION,FIELD_TOVERSION,FIELD_DOWNLOADSUCCESS,FIELD_PATCH_UPGRADE,FIELD_LOCALFILEPATH};

    public static final String DROP_TABLE = "drop table if exists "+TABLE_NAME;

    static long insert(SQLiteDatabase db,UpgradeInfo info){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_MODULE_ID,info.module_id);
        contentValues.put(FIELD_MD5,info.md5);
        contentValues.put(FIELD_MD5MAIN,info.MD5Main);
        contentValues.put(FIELD_URL,info.url);
        contentValues.put(FIELD_URLMAIN,info.urlMain);
        contentValues.put(FIELD_REMARK,info.remark);
        contentValues.put(FIELD_UPGRADETYPE,info.upgradeType);
        contentValues.put(FIELD_UPDATETYPE,info.updateType);
        contentValues.put(FIELD_FROMVERSION,info.fromVersion);
        contentValues.put(FIELD_TOVERSION,info.toVersion);
        contentValues.put(FIELD_DOWNLOADSUCCESS,info.downloadSuccess);
        contentValues.put(FIELD_PATCH_UPGRADE,info.isPatch);
        contentValues.put(FIELD_LOCALFILEPATH,info.localFilepath);
        contentValues.put(FIELD_MODULE_NAME,info.module_name);
        return db.insert(TABLE_NAME,null,contentValues);
    }

    static List<UpgradeInfo> query(SQLiteDatabase db,String md5,String url,boolean isMain){
        Cursor cursor;
        String where;
        List<UpgradeInfo> upgradeInfoList = new ArrayList<>();
        if(isMain)
            where = FIELD_MD5MAIN +" = ? and "+FIELD_URLMAIN +" = ? ";
        else
            where = FIELD_MD5 + " = ? and "+ FIELD_URL +" = ? ";
        cursor = db.query(TABLE_NAME,null,where,new String[]{md5,url},null,null,null);
        if(cursor != null){
            LogUtils.d("cursor.count: "+cursor.getCount());
            if(cursor.moveToFirst()){
                while (cursor.moveToNext()){
                    UpgradeInfo upgradeInfo = transForUpgradeInfo(cursor);
                    upgradeInfoList.add(upgradeInfo);
                }
            }
            cursor.close();
        }
        return upgradeInfoList;
    }

    static UpgradeInfo transForUpgradeInfo(Cursor cursor){
        UpgradeInfo upgradeInfo = new UpgradeInfo();
        upgradeInfo.module_id = cursor.getString(cursor.getColumnIndex(FIELD_MODULE_ID));
        upgradeInfo.md5 = cursor.getString(cursor.getColumnIndex(FIELD_MD5));
        upgradeInfo.MD5Main = cursor.getString(cursor.getColumnIndex(FIELD_MD5MAIN));
        upgradeInfo.url = cursor.getString(cursor.getColumnIndex(FIELD_URL));
        upgradeInfo.urlMain = cursor.getString(cursor.getColumnIndex(FIELD_URLMAIN));
        upgradeInfo.updateType = cursor.getString(cursor.getColumnIndex(FIELD_UPDATETYPE));
        upgradeInfo.upgradeType = cursor.getString(cursor.getColumnIndex(FIELD_UPGRADETYPE));
        upgradeInfo.fromVersion = cursor.getString(cursor.getColumnIndex(FIELD_FROMVERSION));
        upgradeInfo.toVersion = cursor.getString(cursor.getColumnIndex(FIELD_TOVERSION));
        upgradeInfo.remark = cursor.getString(cursor.getColumnIndex(FIELD_REMARK));
        upgradeInfo.downloadSuccess = cursor.getInt(cursor.getColumnIndex(FIELD_DOWNLOADSUCCESS));
        upgradeInfo.localFilepath = cursor.getString(cursor.getColumnIndex(FIELD_LOCALFILEPATH));
        upgradeInfo.isPatch = cursor.getInt(cursor.getColumnIndex(FIELD_PATCH_UPGRADE));
        upgradeInfo.module_name = cursor.getString(cursor.getColumnIndex(FIELD_MODULE_NAME));
        return upgradeInfo;
    }

    static int delete(SQLiteDatabase db,String url,boolean isMain){
        String whereCause;
        if(isMain)
            whereCause = FIELD_MD5MAIN +" = ?";
        else
            whereCause = FIELD_URL + " = ? ";

        return db.delete(TABLE_NAME,whereCause,new String[]{url});
    }

    static int delete(SQLiteDatabase db,String moduleName){
        String whereCause = FIELD_MODULE_NAME +" = ? ";
        return db.delete(TABLE_NAME,whereCause,new String[]{moduleName});
    }

    static int update(SQLiteDatabase db ,String url,boolean isMain,int downloadSuccess,String localFilepath){
        String whereCause;
        if(isMain)
            whereCause = FIELD_MD5MAIN +" = ?";
        else
            whereCause = FIELD_URL + " = ? ";
        ContentValues contentValue = new ContentValues();
        contentValue.put(FIELD_DOWNLOADSUCCESS,downloadSuccess);
        contentValue.put(FIELD_LOCALFILEPATH,localFilepath);
        return db.update(TABLE_NAME,contentValue,whereCause,new String[]{url});
    }

    public static class UpgradeInfo{
        public String module_id;
        public String module_name;
        public String md5;
        public String MD5Main;
        public String url;
        public String urlMain;
        public String remark;
        public String upgradeType;
        public String updateType;
        public String fromVersion;
        public String toVersion;
        public int downloadSuccess; //1 success;  other: failed
        public int isPatch;       //1:patch下载，0 or others full_package
        public String localFilepath;
    }

}
