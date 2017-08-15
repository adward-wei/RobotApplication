/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @date 2017/1/7
 * @author paul.zhang@ubtrobot.com
 * @Description 定时器任务
 * @modifier
 * @modify_time
 */

public class IoTimer {
	
	public static final int INVALID_TIMER_ID = -1;
	private static final int BASE_MSG_ID = 100;
	private static volatile IoTimer sTimer = null;
	private static final Object slock = new Object();
	private TimerHandler mTimerHandler;
	private Handler mIoHandler;
	private int mBaseTimerId = 1;
	private Map<Integer, TimerTask> mTimersList;
	private Queue<Integer> mValidIdList = null;
	private Looper mIoLooper = null;
	private volatile boolean mInited = false;
	
	public static IoTimer shareTimer()
	{
		if (sTimer == null) {
			synchronized (slock) {
				if (sTimer == null) {
					sTimer = new IoTimer();
					sTimer.init();
				}
			}
		}
		
		return sTimer;
	}
	
	public static void destroyTimer()
	{
		if (sTimer != null) {
			synchronized (slock) {
				final IoTimer timer = sTimer;
				if (timer != null) 
					timer.destroy();
				sTimer = null;
			}
		}
	}
	
	private class TimerTask
	{
		Runnable action;
		int id;
		int loopCount;
		long expiredTimeout;
		
		void loop()
		{
			if (loopCount == 0 || loopCount == 1) {
				remove(id);
			}
			else if (loopCount > 1)
				loopCount --;
			
			Message msg = mTimerHandler.obtainMessage(messageIdFromTimerId(id), this);
			mTimerHandler.sendMessageDelayed(msg, expiredTimeout);
		}
		
		void disable()
		{
			loopCount = 0;
			action = null;
			mTimerHandler.removeMessages(messageIdFromTimerId(id));
		}
	}
	
	private IoTimer() {		
		mTimersList = new HashMap<Integer, TimerTask>();
		mValidIdList = new LinkedList<Integer>();
	}
	
	private void init() {
		if (mInited)
			return;		
		
		HandlerThread thread = new HandlerThread("io_timer") {

			@Override
			protected void onLooperPrepared() {
				
				mIoLooper = getLooper();
				mTimerHandler = new TimerHandler(mIoLooper);
				mIoHandler = new Handler(mIoLooper);
				
				mInited = true;
				synchronized (IoTimer.this) {
					IoTimer.this.notify();
				}
			}			
		};	
		thread.start();
		waitToPrepared();		
	}
	
	private void waitToPrepared() {
		synchronized (this) {
			while (!mInited) {
				try {
					wait(10);
				} catch (InterruptedException e) {
					break;
				}
			}
		}		
	}
	
	private void destroy() {
		if (!mInited)
			return;
		
		clear();
		mInited = false;
		mIoLooper.quit();		
	}
	
	public boolean resetTimer(int tid, long timeout)
	{
		TimerTask task = null;
		synchronized (this) {
			task = mTimersList.get(tid);
		}
		
		if (task == null)
			return false;
		
		int mid = messageIdFromTimerId(tid);
		mTimerHandler.removeMessages(mid);
		task.expiredTimeout = timeout;
		Message msg = mTimerHandler.obtainMessage(mid, task);
		mTimerHandler.sendMessageDelayed(msg, task.expiredTimeout);
		return true;
	}
	
	public boolean resetTimer(int tid)
	{
		if (!mInited)
			throw new IllegalStateException("not inited");
		
		TimerTask task = null;
		synchronized (this) {
			task = mTimersList.get(tid);
		}
		
		if (task == null)
			return false;
		
		int mid = messageIdFromTimerId(tid);
		mTimerHandler.removeMessages(mid);
		Message msg = mTimerHandler.obtainMessage(mid, task);
		mTimerHandler.sendMessageDelayed(msg, task.expiredTimeout);
		return true;
	}
	
	public int scheduleTimer(long timeout, Runnable action)
	{
		return scheduleTimer(timeout, action, 1, timeout);
	}
	
	public int scheduleTimer(long timeout, Runnable action, int loop, long delay) {
		if (timeout < 0 || action == null || loop == 0)
			throw new IllegalArgumentException("timeout is invalid, or action is null, or loop is 0!");
		
		if (!mInited)
			throw new IllegalStateException("not inited");
		
		int id = nextTimerId();
		if (id == -1) {
			return INVALID_TIMER_ID;
		}
		
		TimerTask tt = new TimerTask();
		tt.id = id;
		tt.expiredTimeout = timeout;
		tt.action = action;
		tt.loopCount = loop;
		
		synchronized (this) {
			mTimersList.put(id, tt);
		}
		Message msg = mTimerHandler.obtainMessage(messageIdFromTimerId(id), tt);
		mTimerHandler.sendMessageDelayed(msg, delay);
		return id;
	}
	
	private synchronized TimerTask remove(int tid)
	{
		TimerTask ret = null;
		ret = mTimersList.remove(tid);
		if (!mValidIdList.contains(tid)) {
			if (!mValidIdList.offer(tid)){
			}
		}
		
		return ret;
	}
	
	private synchronized boolean hasTimer(int tid)
	{
		return mTimersList.containsKey(tid);
	}
		
	public void cancelTimer(int tid)
	{
		TimerTask task = remove(tid);
		if (task == null)
			return;
		
		task.disable();
	}
	
	public synchronized void clear()
	{
		mValidIdList.clear();
		mTimersList.clear();		
		mBaseTimerId = 1;
	}
	
	protected synchronized int nextTimerId() {
		if (mValidIdList.size() == 0)
			return mBaseTimerId++;
		int id = mValidIdList.poll();
		return id;
	}
	
	private int messageIdFromTimerId(int id)
	{
		return (BASE_MSG_ID + id);
	}
	
	protected void onTimer(TimerTask task) {
		final Runnable action = task.action;
		if (action != null) { 
			action.run();
		}
	}
	
	class TimerHandler extends Handler
	{

		public TimerHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what < BASE_MSG_ID)
				return;
			
			TimerTask task = (TimerTask) msg.obj;
			if (hasTimer(task.id)) {
				mIoHandler.post(new TimerRunnable(task));
				task.loop();
			}			
		}
	}
	
	
	private class TimerRunnable implements Runnable
	{
		final TimerTask task;
		public TimerRunnable(final TimerTask tt) {
			this.task = tt;
		}
		
		@Override
		public void run() {
			onTimer(task);
		}
		
	}
}