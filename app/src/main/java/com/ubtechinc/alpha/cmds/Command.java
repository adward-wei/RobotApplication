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

package com.ubtechinc.alpha.cmds;

/**
 * @desc : 命令模式接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/18
 * @modifier:
 * @modify_time:
 */

public interface Command {

    int MAX_TIME = 5 * 1000;
    int MIN_TIME = 20;

    /**
     *  执行命令
     */
    void execute();

    /**
     * 撤销命令
     */
    void undo();
}
