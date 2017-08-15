package com.ubtechinc.alpha.appmanager.old.db;

public class Alpha2App {

	public String name;
	public String appid;

	public static final String SQL_TABLE = "CREATE TABLE "
			+ Provider.Alpha2AppColumns.TABLE_NAME + " ("
			+ Provider.Alpha2AppColumns._ID + " INTEGER PRIMARY KEY,"
			+ Provider.Alpha2AppColumns.NAME + " TEXT,"
			+ Provider.Alpha2AppColumns.APPID + " TEXT" + ");";
}
