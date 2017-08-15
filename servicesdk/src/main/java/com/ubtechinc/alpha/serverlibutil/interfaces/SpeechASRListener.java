/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.serverlibutil.interfaces;

public interface SpeechASRListener {
	void onBegin();
	void onEnd();
	void onResult(String text);
	void onError(int code);
}
