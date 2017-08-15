package com.ubtechinc.alpha2ctrlapp.util;

import android.content.Context;
import android.os.Handler;

import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.database.NoticeManager;

public class FileReceive { 
	private Context mContext;
    private String LOG_TAG="FileReceive";
    private NoticeManager mnoticeManager;
	private Alpha2Application mApplication;
    private Handler mHandler;
    public static FileReceive mFileReceive;
	public FileReceive(Context context, NoticeManager noticeManager, Handler handler){
		mContext =context;
		mApplication = (Alpha2Application)mContext.getApplicationContext();
		mnoticeManager= noticeManager;
		mHandler = handler;
	}
	public static FileReceive getInstance(Context context,NoticeManager noticeManager,Handler handler){
		if(mFileReceive==null){
			mFileReceive = new FileReceive(context, noticeManager, handler);
			mFileReceive.startReceive();
		}
		mFileReceive.mHandler = handler;
		return mFileReceive;
	}

	public  void startReceive(){

	        
		}

	
}
