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

import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.framework.db.EntityManager;
import com.ubtechinc.framework.db.EntityManagerFactory;
import com.ubtechinc.framework.db.sqlite.Selector;
import com.ubtechinc.framework.db.sqlite.WhereBuilder;

import java.util.ArrayList;
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
public class RobotInfoProvider extends BaseProvider<DBRobotInfo> {
    private static final String TAG = "RobotInfoProvider";
    //使用volatile关键字保其可见性
    volatile private static RobotInfoProvider INSTANCE = null;


    public static RobotInfoProvider get() {
        try {
            if (INSTANCE == null) {
                //创建实例之前可能会有一些准备性的耗时工作
                Thread.sleep(300);
                synchronized (RobotInfoProvider.class) {
                    if (INSTANCE == null) {//二次检查
                        INSTANCE = new RobotInfoProvider();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }

    EntityManager<DBRobotInfo> entityManager;

    @Override
    public EntityManager<DBRobotInfo> entityManagerFactory() {
        entityManager = EntityManagerFactory.getInstance(Alpha2Application.getAlpha2(),
                EntityManagerHelper.DB_VERSION,
                EntityManagerHelper.DB_ACCOUNT,
                null, null)
                .getEntityManager(DBRobotInfo.class,
                        EntityManagerHelper.DB_ROBOT_LIST_TABLE);
        return entityManager;
    }

    @Override
    public List<DBRobotInfo> getAllData() {

        return entityManager.findAll();
    }


    @Override
    public DBRobotInfo getDataById(String id) {
        return entityManager.findById(id);
    }


    public void save(DBRobotInfo info) {
        entityManager.save(info);
    }
    public void saveAll(List<DBRobotInfo> robots) {
        entityManager.saveAll(robots);
    }
    @Override
    public void saveOrUpdate(DBRobotInfo info) {
        entityManager.saveOrUpdate(info);
    }

    @Override
    public void saveOrUpdateAll(List<DBRobotInfo> dataList) {
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


    public void update(DBRobotInfo action, String... updateColumnNames) {
        entityManager.update(action, updateColumnNames);
    }

    public void deleteRobotByParam(Map<String , Object> paramMap) {

        WhereBuilder whereBuilder = null;
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
        entityManager.delete(whereBuilder);
    }

    public List<RobotInfo> findRobotListByParam( Map<String , Object> paramMap) {



        Selector selector = null;
        if(paramMap != null) {
            int index = 0;
            for(Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if(index == 0) {
                    selector =  Selector.create().where(entry.getKey(), "=", entry.getValue());
                    index++;
                }else {
                    selector.and(entry.getKey(), "=", entry.getValue());
                }

            }
        }

        List<DBRobotInfo> dbRobotInfos = entityManager.findAll(selector);
        List<RobotInfo> robotInfoModels = new ArrayList<>();
        for (DBRobotInfo DBRobotInfo : dbRobotInfos) {
            RobotInfo robotInfo = new RobotInfo();
            BeanUtils.copyBean(DBRobotInfo, robotInfo);
            robotInfoModels.add(robotInfo);
        }
        return robotInfoModels;
    }
    public void saveOrUpdateByRelationIdAndEquitmentId(List<RobotInfo> lists) {



        for (RobotInfo robot : lists) {
            Selector selector = Selector.create().where("relationId", "=",robot.getRelationId()).and("equipmentId", "=", robot.getEquipmentId());
            DBRobotInfo robotData = entityManager.findFirst(selector);
            if(robotData == null) {
                robotData = new DBRobotInfo();
                BeanUtils.copyBean(robot, robotData);
                entityManager.save(robotData);
            }else {
                ContentValues values = new ContentValues();
                values.put("userId", robot.getUserId());
                values.put("userName", robot.getUserName());
                values.put("relationStatus", robot.getRelationStatus());
                values.put("relationId", robot.getRelationId());
                values.put("equipmentId", robot.getEquipmentId());
                values.put("upUserId", robot.getUpUserId());
                values.put("equipmentUserId", robot.getEquipmentUserId());
                values.put("controlUserId", robot.getControlUserId());
                values.put("macAddress", robot.getMacAddress());
                values.put("controlUserImage", robot.getControlUserImage());
                values.put("userOtherName", robot.getUserOtherName());
                values.put("status", robot.getStatus());
                values.put("controlUserName", robot.getControlUserName());
                values.put("userImage", robot.getUserImage());
                values.put("upUserName", robot.getUpUserName());

                entityManager.update(values, WhereBuilder.create("relationId", "=",robot.getRelationId()).and("equipmentId", "=", robot.getEquipmentId()));
            }
        }
    }



}


