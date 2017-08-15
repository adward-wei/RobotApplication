/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha2ctrlapp.database;


import com.ubtechinc.framework.db.EntityManager;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/3
 * @Description 提供一个数据库操作抽象基类
 * @modifier
 * @modify_time
 */

public abstract class BaseProvider<T> implements IDataProvider<T> {

    public EntityManager<T> dbMananger;

    public BaseProvider() {
        dbMananger = entityManagerFactory();
    }

    protected abstract EntityManager<T> entityManagerFactory();


}
