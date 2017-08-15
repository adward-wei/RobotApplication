/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.provider;


import android.content.Context;

import com.ubtechinc.framework.db.EntityManager;

import java.util.List;

/**
 * @date 2016/8/3
 * @author paul.zhang@ubtrobot.com
 * @Description 提供一个数据库操作抽象基类
 * @modifier
 * @modify_time
 */

public abstract class BaseVisitor<T> implements IDataVisitor<T> {

	protected EntityManager<T> dbMananger;
	protected Context context;

	public BaseVisitor(Context context) {
		this.context = context.getApplicationContext();
		dbMananger = entityManagerFactory();
	}

	protected abstract EntityManager<T> entityManagerFactory();

	@Override
	public List<T> getAllData() {
		return dbMananger.findAll();
	}

	@Override
	public T getDataById(String id) {
		return dbMananger.findById(id);
	}

	@Override
	public void saveOrUpdateAll(List<T> dataList) {
		dbMananger.saveOrUpdateAll(dataList);
	}

	@Override
	public void saveOrUpdate(T dataList) {
		dbMananger.saveOrUpdate(dataList);
	}

	@Override
	public void deleteAll() {
		dbMananger.deleteAll();
	}

	@Override
	public void deleteById(int id) {
		dbMananger.deleteById(id+"");
	}

	@Override
	public void deleteById(String id) {
		dbMananger.deleteById(id);
	}

	@Override
	public void delete(T entity) {
		dbMananger.delete(entity);
	}
}
