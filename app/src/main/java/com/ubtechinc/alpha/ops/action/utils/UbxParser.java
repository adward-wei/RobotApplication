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

package com.ubtechinc.alpha.ops.action.utils;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.FileUtils;
import com.ubtechinc.alpha.ops.action.UbxFlow;

/**
 * @desc : UBX 解析工具
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/5
 * @modifier:
 * @modify_time:
 */

public final class UbxParser {

    public static UbxFlow parseUbxFile(String actionFile){
        byte[] bytes = FileUtils.readFile2Bytes(actionFile);
        if (bytes == null) return null;
        ByteBufferList bbl = new ByteBufferList(bytes);
        UbxFlow  flow = new UbxFlow();
        boolean success = flow.analysis(bbl, bbl.remaining());
        return success? flow : null;
    }

}
