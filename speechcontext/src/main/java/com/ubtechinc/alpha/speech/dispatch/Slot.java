/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech.dispatch;

/**
 * 对应bnf的一个slot实体类
 *
 * @author Administrator
 */
public class Slot {
	private int appId;
	/**
	 * slot的tag的名字
	 */
	private String name;
	/**
	 * slot的具体的指令内容
	 */
	private String content;

	/**
	 * 包含同一个局部上下文状态值的第三方appId数组
	 */
	private int[] ids;

	private int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
