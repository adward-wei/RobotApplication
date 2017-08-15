package com.ubt.alpha2.upgrade.action;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.ubt.alpha2.upgrade.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: slive
 * @description: 
 * @create: 2017/7/26
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class ActionDao {

    ContentResolver contentResolver;

    public ActionDao(Context content){
        contentResolver = content.getContentResolver();
    }

    public Uri addActionToDb(ActionUpgradeBean.ActionFileBean actionFileBean){
        Uri contentUri = Provider.getContentUri(actionFileBean.actionId);
        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.ACTION_FIELD_ID,actionFileBean.actionId);
        contentValues.put(Provider.ACTION_FIELD_TYPE,actionFileBean.actionType);
        contentValues.put(Provider.ACTION_FIELD_CN_NAME,actionFileBean.actionNameCN);
        contentValues.put(Provider.ACTION_FIELD_EN_NAME,actionFileBean.actionNameEN);
        return contentResolver.insert(contentUri,contentValues);
    }

    public int updateActionToDb(ActionUpgradeBean.ActionFileBean actionFileBean){
        Uri contentUri = Provider.getContentUri(actionFileBean.actionId);
        ContentValues contentValue = new ContentValues();
        contentValue.put(Provider.ACTION_FIELD_ID,actionFileBean.actionId);
        contentValue.put(Provider.ACTION_FIELD_TYPE,actionFileBean.actionType);
        contentValue.put(Provider.ACTION_FIELD_CN_NAME,actionFileBean.actionNameCN);
        contentValue.put(Provider.ACTION_FIELD_EN_NAME,actionFileBean.actionNameEN);
        return contentResolver.update(contentUri,contentValue,null,null);
    }

    public int deleteActionToDb(String actionId){
        Uri contentUri = Provider.getContentUri(actionId);
        return contentResolver.delete(contentUri,null,null);
    }

    public  List<ActionUpgradeBean.ActionFileBean> readActionFromDb(){
        List<ActionUpgradeBean.ActionFileBean> listActionFile = new ArrayList<>();
        Cursor c = null;
        Uri contentUri = Provider.getBaseContentUri();
        try {
            c = contentResolver.query(
                    contentUri,
                    new String[]{Provider.ACTION_FIELD_ID,
                            Provider.ACTION_FIELD_TYPE,
                            Provider.ACTION_FIELD_CN_NAME,
                            Provider.ACTION_FIELD_EN_NAME},
                    null, null, null);
            if (c != null) {
                while (c.moveToNext()) {
                    ActionUpgradeBean.ActionFileBean actionFileBean = new ActionUpgradeBean.ActionFileBean();
                    actionFileBean.actionId = c.getString(c
                            .getColumnIndexOrThrow(Provider.ACTION_FIELD_ID));
                    actionFileBean.actionType = c.getString(c
                            .getColumnIndexOrThrow(Provider.ACTION_FIELD_TYPE));
                    actionFileBean.actionNameCN = c.getString(c
                            .getColumnIndexOrThrow(Provider.ACTION_FIELD_CN_NAME));
                    actionFileBean.actionNameEN = c.getString(c
                            .getColumnIndexOrThrow(Provider.ACTION_FIELD_EN_NAME));

                    if (!TextUtils.isEmpty(actionFileBean.actionNameCN)) {
                        if (!TextUtils.isEmpty(actionFileBean.actionNameEN)) {

                        } else {
                            actionFileBean.actionNameEN = actionFileBean.actionNameCN;
                        }
                    } else if (!TextUtils.isEmpty(actionFileBean.actionNameEN)) {
                        actionFileBean.actionNameCN = actionFileBean.actionNameEN;
                    } else {
                        actionFileBean.actionNameCN = actionFileBean.actionId;
                        actionFileBean.actionNameEN = actionFileBean.actionId;
                    }
                    listActionFile.add(actionFileBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            return listActionFile;
        }
    }

    public static class Provider{
        public static final String authority = "com.ubtechinc.action_info";
        public static final String DB_ACTION_INFO_TABLE = "action_info_table";
        public static final String BASE_CONTENT_URI = "content://" + authority + "/" + DB_ACTION_INFO_TABLE;
        public static final Uri CONTENT_URI = Uri.parse("content://" + authority + "/" + DB_ACTION_INFO_TABLE);
        public static final String ACTION_FIELD_ID ="_id";
        public static final String ACTION_FIELD_TYPE ="type";
        public static final String ACTION_FIELD_CN_NAME ="cn_name";
        public static final String ACTION_FIELD_EN_NAME ="en_name";

        public static Uri getBaseContentUri(){
            return Uri.parse(BASE_CONTENT_URI);
        }

        public static Uri getContentUri(String actionId){
            return Uri.parse(BASE_CONTENT_URI+"/"+actionId);
        }
    }
}
