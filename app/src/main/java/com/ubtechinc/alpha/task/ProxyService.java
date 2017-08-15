/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.task;

/**
 * @date 2016/10/17
 * @author paul.zhang@ubtrobot.com
 * @Description 定义任务接口
 * @modifier
 * @modify_time
 */

public interface ProxyService {

	void onCreate();

	void onDestroy();

	void registerEvent();

	void unregisterEvent();
}
