/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */
package com.ubtechinc.alpha.callback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author paul.zhang
 * @Description: 回调任务上层分发器、支持多线程回调
 * @date 2016/10/20 13:54
 * @copyright UBTECH
 */
public class DispatchMessage implements Runnable {
	private static final int CORE_POOL_SIZE = 2;
	private static final ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newFixedThreadPool(CORE_POOL_SIZE);
	private MessageQueue mQueue;
	private volatile boolean executorRunning;

	public interface Callback {
		void handleMessage();
	}

	public DispatchMessage(MessageQueue queue) {
		this.mQueue = queue;
	}

	public void enqueue(Callback callback) {
		synchronized (this) {
			mQueue.enqueue(callback);
			if (!executorRunning) {
				executorRunning = true;
				getExecutorService().execute(this);
			}
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Callback task = mQueue.poll();
				if (task == null) {
					synchronized (this) {
						task = mQueue.poll();
						if (task == null) {
							executorRunning = false;
							return;
						}
					}
				}
				task.handleMessage();
			}
		} finally {
			executorRunning = false;
		}
	}

	public ExecutorService getExecutorService() {
		return DEFAULT_EXECUTOR_SERVICE;
	}
}
