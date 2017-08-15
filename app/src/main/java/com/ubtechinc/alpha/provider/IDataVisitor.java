/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.provider;

import java.util.List;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/3
 * @Description 提供一个数据库操作接口
 * @modifier
 * @modify_time
 */

public interface IDataVisitor<T> {

	/**
	 * 获取当前所有数据
	 *
	 * @return
	 */
	List<T> getAllData();

	/**
	 * 根据ID查找记录
	 *
	 * @param id
	 * @return
	 */
	 T getDataById(String id);

	/**
	 * 保存
	 *
	 * @param dataList
	 */
	void saveOrUpdateAll(List<T> dataList);

	/**
	 * 保存
	 *
	 * @param dataList
	 */
	void saveOrUpdate(T dataList);

	/**
	 * 删除全部
	 *
	 * @param
	 */
	void deleteAll();

	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	void deleteById(int id);

	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	void deleteById(String id);

	/**
	 * 根据数据集删除
	 *
	 * @param entrity
	 */
	void delete(T entrity);
}
