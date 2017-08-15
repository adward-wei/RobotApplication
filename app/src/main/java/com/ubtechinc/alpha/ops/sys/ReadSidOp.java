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

package com.ubtechinc.alpha.ops.sys;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.cmds.chest.ReadSidCmd;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc : 读取sid值
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public class ReadSidOp extends BaseSysOp {
    private final static byte length = 32;

    public ReadSidOp() {
        super(NOR_PRIORITY);
    }

    @Override
    protected int waitTime() {
        return 0;
    }

    @Override
    protected SerialCommand createCmd(SerialCommandReceiver receiver) {
        return new ReadSidCmd(receiver, new byte[]{length});
    }

    @Override
    protected OpResult<String> parseResult(SerialCmdResult cmdResult) {
        OpResult<String> result = new OpResult<>();
        result.errorCode = cmdResult.getError();
        result.data = parseData(cmdResult.getResult());
        result.cmd = cmdResult.getCmd();
        return result;
    }

    private String parseData(byte[] result) {
        if (result == null) return null;

        // FIXME: 2017/4/25  以下从原主服务拷贝代码
        int i;
        for (i =0 ; i < result.length; i++){
            if (result[i] == "y".getBytes()[0]){
                break;
            }
            if (i == 12){
                String str = result[i]+ "";
                String regex = "^[a-fA-F0-9]+$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(str);
                if (!matcher.matches())
                    break;
            }
        }
        byte[] realData = new byte[i];
        System.arraycopy(result, 0 , realData, 0 , i);
        return new String(realData).trim();
    }
}
