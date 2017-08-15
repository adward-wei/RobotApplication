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

package com.ubtechinc.alpha2ctrlapp.database;

import android.content.ContentValues;

import com.google.common.collect.ImmutableMap;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewActionInfo;
import com.ubtechinc.framework.db.EntityManager;
import com.ubtechinc.framework.db.EntityManagerFactory;
import com.ubtechinc.framework.db.sqlite.Selector;
import com.ubtechinc.framework.db.sqlite.WhereBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author tanghongyu
 * @ClassName AppInfoProvider
 * @date 5/18/2017
 * @Description 机器人已下载的动作数据库操作类
 * @modifier
 * @modify_time
 */
public class ActionInfoProvider extends BaseProvider<ActionEntrityInfo> {
    private static final String TAG = "AppInfoProvider";
    //使用volatile关键字保其可见性
    volatile private static ActionInfoProvider INSTANCE = null;


    public static ActionInfoProvider get() {
        try {
            if (INSTANCE == null) {
                //创建实例之前可能会有一些准备性的耗时工作
                Thread.sleep(300);
                synchronized (ActionInfoProvider.class) {
                    if (INSTANCE == null) {//二次检查
                        INSTANCE = new ActionInfoProvider();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }

    EntityManager<ActionEntrityInfo> entityManager;

    @Override
    public EntityManager<ActionEntrityInfo> entityManagerFactory() {
        entityManager = EntityManagerFactory.getInstance(Alpha2Application.getAlpha2(),
                EntityManagerHelper.DB_VERSION,
                EntityManagerHelper.DB_ACCOUNT,
                null, null)
                .getEntityManager(ActionEntrityInfo.class,
                        EntityManagerHelper.DB_ACTION_LIST_TABLE);
        return entityManager;
    }

    @Override
    public List<ActionEntrityInfo> getAllData() {

        return entityManager.findAll();
    }


    @Override
    public ActionEntrityInfo getDataById(String id) {
        return entityManager.findById(id);
    }


    public void save(ActionEntrityInfo info) {
        entityManager.save(info);
    }

    @Override
    public void saveOrUpdate(ActionEntrityInfo info) {
        entityManager.saveOrUpdate(info);
    }

    @Override
    public void saveOrUpdateAll(List<ActionEntrityInfo> dataList) {
        entityManager.saveOrUpdateAll(dataList);
    }

    @Override
    public void deleteAll() {
        entityManager.deleteAll();
    }

    @Override
    public void deleteById(int id) {
        entityManager.deleteById(id + "");
    }

    @Override
    public void deleteById(String id) {
        entityManager.deleteById(id);
    }


    public void update(ActionEntrityInfo action, String... updateColumnNames) {
        entityManager.update(action, updateColumnNames);
    }

    public void updateByParam(ContentValues contentValues,  Map<String , Object> paramMap) {
        WhereBuilder whereBuilder  = null;
        if(paramMap != null) {
            int index = 0;
            for(Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if(index == 0) {
                    whereBuilder =  WhereBuilder.create(entry.getKey(), "=", entry.getValue());
                    index++;
                }else {
                    whereBuilder.and(entry.getKey(), "=", entry.getValue());
                }

            }
        }
        entityManager.update(contentValues, whereBuilder);
    }

    public ActionEntrityInfo getActionByOriginId(String originId) {
        Selector selector = Selector.create().where("actionOriginalId", "=",originId);
        return entityManager.findFirst(selector);
    }

    public List<ActionEntrityInfo> findActionListByParam(  Map<String , Object> paramMap) {
        Selector selector = Selector.create();

        if(paramMap != null) {
            int index = 0;
            for(Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if(index == 0) {
                    selector.where(entry.getKey(), "=", entry.getValue());
                    index++;
                }else {
                    selector.and(entry.getKey(), "=", entry.getValue());
                }

            }
        }
        return entityManager.findAll(selector);
    }

    public void deleteActionListByParam(String serialNo,  Map<String , Object> paramMap) {
        WhereBuilder selector = WhereBuilder.create("serialNo", "=",serialNo);
        if(paramMap != null) {
            Iterator<String> iterator = paramMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                selector.and(key, "=", paramMap.get(key));
            }
        }

         entityManager.delete(selector);
    }

    public ActionEntrityInfo findActionByParam(ImmutableMap<String , Object> paramMap) {
        Selector selector = Selector.create();

        if(paramMap != null) {
            int index = 0;
            for(Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if(index == 0) {
                    selector.where(entry.getKey(), "=", entry.getValue());
                    index++;
                }else {
                    selector.and(entry.getKey(), "=", entry.getValue());
                }

            }
        }

        return entityManager.findFirst(selector);
    }


    public void saveOrUpdateByActionIdAndSerialNo( String serialNo, List<NewActionInfo> lists) {



        for (NewActionInfo action : lists) {

            Selector selector = Selector.create().where("actionOriginalId", "=",action.getActionId()).and("serialNo", "=", serialNo);
            ActionEntrityInfo actionEntrityInfo = entityManager.findFirst(selector);
            if(actionEntrityInfo == null) {
                actionEntrityInfo = new ActionEntrityInfo();
                actionEntrityInfo.setActionOriginalId( action.getActionId());
                actionEntrityInfo.setActionType(action.getActionType());
                actionEntrityInfo.setDownloadState(action.getDownloadState());
                actionEntrityInfo.setActionLanName(action.getActionName());
                actionEntrityInfo.setSerialNo(action.getRobotSerialNo());
                entityManager.save(actionEntrityInfo);
            }else {
                ContentValues values = new ContentValues();
                values.put("actionOriginalId", action.getActionId());
                values.put("downLoadState", action.getDownloadState());
                values.put("actionLanName", action.getActionName());
                values.put("actionType", action.getActionType());

                entityManager.update(values, WhereBuilder.create("actionOriginalId", "=", actionEntrityInfo.getActionOriginalId()).and("serialNo", "=", serialNo));
            }
        }
    }

}


