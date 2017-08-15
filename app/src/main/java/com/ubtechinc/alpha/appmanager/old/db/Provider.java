package com.ubtechinc.alpha.appmanager.old.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 存放跟数据库有关的常量
 * 
 * @author jacp
 *
 */
public class Provider {

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jacp.demo";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.jacp.demo";

	/**
	 * 跟Person表相关的常量
	 * 
	 * @author jacp
	 *
	 */
	public static final class Alpha2AppColumns implements BaseColumns {
		// 这个是每个Provider的标识，在Manifest中使用
		public static final String AUTHORITY = "com.ubtechinc.alpha2services.alpha2app";
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/apps");
		public static final String TABLE_NAME = "app";
		public static final String DEFAULT_SORT_ORDER = "appid desc";

		public static final String NAME = "name";
		public static final String APPID = "appid";

	}

	
	/**
	 * [状态相关的]
	 * 
	 * @author zengdengyi
	 * @version 1.0
	 * @date 2015年9月14日 上午9:16:23
	 * 
	 **/
	    
	public static final class StateColums implements BaseColumns {
		// 这个是每个Provider的标识，在Manifest中使用
		public static final String AUTHORITY = "com.ubtechinc.alpha2services.alpha2state";
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/states");
		public static final String TABLE_NAME = "state";

		public static final String POWER = "power";
		public static final String DEBUG = "debug";
	}
	
	
	/**
	 * [语音相关的表]
	 * 
	 * @author zengdengyi
	 * @version 1.0
	 * @date 2015年10月11日 下午4:14:53
	 * 
	 **/
	    
	public static final class SpeechPluginColumns implements BaseColumns {
		// 这个是每个Provider的标识，在Manifest中使用
		public static final String AUTHORITY = "com.ubtechinc.alpha2services.alpha2speech";
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/speechs");
		public static final String TABLE_NAME = "speech";

		/** 使用的引擎名称 **/     
		public static final String name = "name";
		/** 对应的AIDL intent action **/     
		public static final String action = "action";
	}

	/**
	 * 跟Photo表相关的常量
	 *
	 * @author wzt
	 *
	 */
	public static final class PhotoUrlColumns implements BaseColumns {
		// 这个是每个Provider的标识，在Manifest中使用
		public static final String AUTHORITY = "com.ubtechinc.alpha2services.photo";
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/photoUrls");
		public static final String TABLE_NAME = "photoUrl";
		public static final String DEFAULT_SORT_ORDER = "filePath asc";

		public static final String FILEPATH = "filePath";
		public static final String URL = "url";

	}

	/**
	 * 跟动作名称相关的常量
	 *
	 * @author wzt
	 *
	 */
	public static final class ActionNameColumns implements BaseColumns {
		// 这个是每个Provider的标识，在Manifest中使用
		public static final String AUTHORITY = "com.ubtechinc.alpha2services.action_name";
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/actionNames");
		public static final String TABLE_NAME = "actionName";
		public static final String DEFAULT_SORT_ORDER = "action_id asc";

		public static final String ACTION_ID = "action_id";
		public static final String ACTION_TYPE = "action_type";
		public static final String ACTION_CN_NAME = "action_cn_name";
		public static final String ACTION_EN_NAME = "action_en_name";

	}
}
