package com.ubtechinc.alpha.appmanager.old.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * @ClassName AppDao
 * @date 3/13/2017
 * @author
 * @Description 第三方App管理数据库
 * @modifier tanghongyu
 * @modify_time
 */
public class AppDao {

	private static synchronized boolean isExist(Context mContext, String appid) {
		Cursor c = null;
		try {
			c = mContext.getContentResolver().query(
					Provider.Alpha2AppColumns.CONTENT_URI,
					new String[] { Provider.Alpha2AppColumns.NAME,
							Provider.Alpha2AppColumns.APPID },
					Provider.Alpha2AppColumns.APPID + "=?",
					new String[] { appid + "" }, null);
			if (c != null && c.moveToFirst()) {
				return true;
			}
		}finally{
			if (c != null) {
				c.close();
			}
		}
		return false;
	}

	private static synchronized int insert(Context mContext, Alpha2App alpha2App) {
		ContentValues values = new ContentValues();
		values.put(Provider.Alpha2AppColumns.NAME, alpha2App.name);
		values.put(Provider.Alpha2AppColumns.APPID, alpha2App.appid);
		Uri uri = mContext.getContentResolver().insert(
				Provider.Alpha2AppColumns.CONTENT_URI, values);
		if (uri == null) return -1;
		Log.i("DbDao", "insert uri=" + uri);
		String lastPath = uri.getLastPathSegment();
		if (TextUtils.isEmpty(lastPath)) {
			Log.i("DbDao", "insert failure!");
		} else {
			Log.i("DbDao", "insert success! the id is " + lastPath);
		}

		return Integer.parseInt(lastPath);
	}

	public static synchronized int insert(Context mContext, String name,
										  String appid) {
		boolean isExist = isExist(mContext, appid);
		if (isExist) {
			return -1;
		}
		Alpha2App p = new Alpha2App();
		p.name = name;
		p.appid = appid;
		int id = insert(mContext, p);
		return id;
	}

	public static synchronized int delete(Context mContext, String appid) {
		int c = mContext.getContentResolver().delete(
				Provider.Alpha2AppColumns.CONTENT_URI,
				Provider.Alpha2AppColumns.APPID + " =? ",
				new String[] { appid });
		return c;
	}

	public static synchronized void update(Context mContext, String name,
										   String appid, String newName, String newAppid) {
		ContentValues values = new ContentValues();
		values.put(Provider.Alpha2AppColumns.NAME, newName);
		values.put(Provider.Alpha2AppColumns.APPID, newAppid);
		int c = mContext.getContentResolver().update(
				Provider.Alpha2AppColumns.CONTENT_URI, values,
				Provider.Alpha2AppColumns.APPID + " =? ",
				new String[] { appid });
	}

	/**
	 * 查询是否是使用SDK开发的应用
	 * @param mContext
	 * @param mAppid
     * @return
     */
	public static synchronized boolean query(Context mContext, String mAppid) {

		if (mContext == null && mAppid == null) {
			return false;
		}
		
		Cursor c = null;
		try {
		c = mContext.getContentResolver().query(
				Provider.Alpha2AppColumns.CONTENT_URI,
				new String[] { Provider.Alpha2AppColumns.NAME,
						Provider.Alpha2AppColumns.APPID },
				Provider.Alpha2AppColumns.APPID + "=?",
				new String[] { mAppid + "" }, null);
		if (c != null && c.moveToFirst()) {
			Alpha2App p = new Alpha2App();
			p.name = c.getString(c
					.getColumnIndexOrThrow(Provider.Alpha2AppColumns.NAME));
			p.appid = c.getString(c
					.getColumnIndexOrThrow(Provider.Alpha2AppColumns.APPID));
			return true;
		} else {
			return false;
		}
		}finally{
			if (c != null) {
				c.close();
			}
		}

	}

	/**
	 * 查询运行在最前端的应用
	 * @param mContext
	 * @return
     */
	public static synchronized Alpha2App getTopApp(Context mContext) {
		if (mContext == null) {
			return null;
		}

		Cursor c = null;
		try {
			c = mContext.getContentResolver().query(
					Provider.Alpha2AppColumns.CONTENT_URI,
					new String[] { Provider.Alpha2AppColumns.NAME,
							Provider.Alpha2AppColumns.APPID }, null, null, null);
			if (c != null && c.moveToFirst()) {
				Alpha2App p = new Alpha2App();
				p.name = c.getString(c
						.getColumnIndexOrThrow(Provider.Alpha2AppColumns.NAME));
				p.appid = c.getString(c
						.getColumnIndexOrThrow(Provider.Alpha2AppColumns.APPID));
				return p;
			}
		}finally{
			if (c != null) {
				c.close();
			}
		}
		return null;
	}

	/**
	 * 清除所有第三方数据
	 * @param mContext
	 * @return
     */
	public static synchronized int clearData(Context mContext) {
		int c = -1;
		try {
			c = mContext.getContentResolver().delete(
					Provider.Alpha2AppColumns.CONTENT_URI, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
}
