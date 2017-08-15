/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.app;
/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/12/26
 * @Description
 * @modifier
 * @modify_time
 */
public enum DirType {
	log,
	image,
	app,
	cache,
	crash;
	
	public int value()
	{
		return ordinal() + 1;
	}
}
