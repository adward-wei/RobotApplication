package com.ubt.alpha2.upgrade.db;

import android.database.sqlite.SQLiteDatabase;

import com.ubt.alpha2.upgrade.UpgradeApplication;

import java.util.List;

/**
 * @author: slive
 * @description:  db 管理类
 * @create: 2017/7/5
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class UpgradeDbManager {

    UpgradeDbHelper upgradeDbHelper;
    SQLiteDatabase db;

    private UpgradeDbManager(){
        upgradeDbHelper = new UpgradeDbHelper(UpgradeApplication.getContext());
        db = upgradeDbHelper.getWritableDatabase();
    }

    public static UpgradeDbManager getInstance(){
        return UpgradeDbManagerHolder._instance;
    }

    private static class UpgradeDbManagerHolder{
        private static UpgradeDbManager _instance = new UpgradeDbManager();
    }

    public long insertUpgradeInfo(UpgradeTable.UpgradeInfo upgradeInfo){
        return UpgradeTable.insert(db,upgradeInfo);
    }

    public List<UpgradeTable.UpgradeInfo> queryUpgradeInfo(String md5,String url,boolean isMain){
        return UpgradeTable.query(db,md5,url,isMain);
    }

    public int updateUpgradeInfo(String url,boolean isMain,int downloadSuccess,String localFilepath){
        return UpgradeTable.update(db,url,isMain,downloadSuccess,localFilepath);
    }

    public int deleteUpgradeInfo(String url,boolean isMain){
        return UpgradeTable.delete(db,url,isMain);
    }

    public int deleteUpgradeInfoByModuleName(String moduleName){
        return UpgradeTable.delete(db,moduleName);
    }
}
