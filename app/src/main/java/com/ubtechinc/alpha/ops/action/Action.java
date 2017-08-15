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

package com.ubtechinc.alpha.ops.action;

import com.ubtechinc.alpha.provider.EntityManagerHelper;
import com.ubtechinc.framework.db.annotation.Column;
import com.ubtechinc.framework.db.annotation.Table;

/**
 * @desc : 动作信息实体类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/18
 * @modifier:
 * @modify_time:
 */
@Table(version = EntityManagerHelper.DB_ACTION_INFO_VERSION)
public class Action {

    @com.ubtechinc.framework.db.annotation.Id

    public String Id;

    @Column(defaultValue = "")
    public String cn_name;
    @Column(defaultValue = "")
    public String en_name;

    @Column(defaultValue = "")
    public String desc;

    @Column(defaultValue = "")
    public String type;

    @Column(defaultValue = "-1")
    public int time;

    @Override
    public String toString() {
        return "Action[id="+Id+",cn_name="+cn_name
                +",en_name="+en_name+",desc="+desc
                +",keyType="+type+",time="+time+"]";
    }
}

