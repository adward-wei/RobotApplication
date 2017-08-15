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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */

public final class MD5Utils {

    public static String md5(String val, int bit){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(val.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            if (bit == 32) {
                return buf.toString().toUpperCase();
            } else {
                return buf.toString().substring(8, 24).toUpperCase();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
