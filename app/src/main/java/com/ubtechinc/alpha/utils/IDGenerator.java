/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */

package com.ubtechinc.alpha.utils;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/26
 * @modifier:
 * @modify_time:
 */

public final class IDGenerator {
    private static IDGenerator instance;
    private int id = 0;
    public static IDGenerator get(){
        if (instance != null)  return instance;
        synchronized (IDGenerator.class){
            if (instance == null) instance = new IDGenerator();
        }
        return instance;
    }

    public int id(){
        return ++id;
    }
}
