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

package com.ubtechinc.alpha.provider;

import android.content.Context;

import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.ops.action.Action;
import com.ubtechinc.framework.db.EntityManager;
import com.ubtechinc.framework.db.EntityManagerFactory;

import java.util.List;

/**
 * @desc : 动作信息表
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/18
 * @modifier:
 * @modify_time:
 */

public class ActionInfoVisitor extends BaseVisitor<Action> {
    private static ActionInfoVisitor instance;

    public static ActionInfoVisitor get() {
        if(instance ==null ){
            synchronized(AlarmInfoVisitor.class){
                if(instance ==null){
                    instance =new ActionInfoVisitor(AlphaApplication.getContext());
                }
            }
        }
        return instance;
    }

    public ActionInfoVisitor(Context context) {
        super(context);
    }

    @Override
    protected EntityManager<Action> entityManagerFactory() {
        dbMananger = EntityManagerFactory.getInstance(context != null? context: AlphaApplication.getContext(),
                                                          EntityManagerHelper.DB_VERSION,
                                                          EntityManagerHelper.DB_ACCOUNT,
                                                          null, null)
                .getEntityManager(Action.class,
                        EntityManagerHelper.DB_ACTION_INFO_TABLE);
        return dbMananger;
    }

    public String getActionId(String name){
        List<Action> actions = getAllData();
        for (Action action: actions) {
            if (name.equals(action.cn_name) || name.equals(action.en_name)){
                return action.Id;
            }
        }
        return null;
    }

    public EntityManager<Action> getEntityManager() {
        return dbMananger;
    }

}
