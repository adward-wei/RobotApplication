/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */
package com.ubtechinc.alpha.callback;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MessageQueue {

	private BlockingQueue<DispatchMessage.Callback> mTaskQueue = new LinkedBlockingQueue<DispatchMessage.Callback>();

	public void enqueue(DispatchMessage.Callback task) {
		mTaskQueue.add(task);
	}

	public DispatchMessage.Callback poll() {
		DispatchMessage.Callback task;
		try {
			task = mTaskQueue.poll(2000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			task = null;
		}
		return task;
	}

	public void removeAllQueue() {
		mTaskQueue.clear();
	}

	public int length() {
		return mTaskQueue.size();
	}

	public static MessageQueue newQueue() {
		return new MessageQueue();
	}
}
