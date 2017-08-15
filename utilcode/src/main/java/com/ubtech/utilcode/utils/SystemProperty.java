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

package com.ubtech.utilcode.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author kevinzha
 * @ClassName
 * @date 2016/8/3.
 * @Description
 * @modifier by
 * @modify_time
 */
public class SystemProperty {
    /**
     * 获取系统属性
     * @param key
     * @return
     */
    public static String getProperty(String key){
        Process process ;
        String property = null;
        try {
            process = Runtime.getRuntime().exec("getprop "+key);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(ir);
            property = input.readLine().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return property;
    }
}
