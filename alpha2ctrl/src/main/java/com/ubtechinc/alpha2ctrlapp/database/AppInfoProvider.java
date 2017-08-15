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
import android.text.TextUtils;

import com.google.common.collect.ImmutableMap;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.framework.db.EntityManager;
import com.ubtechinc.framework.db.EntityManagerFactory;
import com.ubtechinc.framework.db.sqlite.Selector;
import com.ubtechinc.framework.db.sqlite.WhereBuilder;

import java.util.List;
import java.util.Map;


/**
 * @author tanghongyu
 * @ClassName AppInfoProvider
 * @date 5/18/2017
 * @Description 机器人已安装的App信息数据库操作类
 * @modifier
 * @modify_time
 */
public class AppInfoProvider extends BaseProvider<RobotAppEntrity> {
    private static final String TAG = "AppInfoProvider";
    //使用volatile关键字保其可见性
    volatile private static AppInfoProvider INSTANCE = null;

    public static AppInfoProvider get() {
        try {
            if (INSTANCE == null) {
                //创建实例之前可能会有一些准备性的耗时工作
                Thread.sleep(300);
                synchronized (AppInfoProvider.class) {
                    if (INSTANCE == null) {//二次检查
                        INSTANCE = new AppInfoProvider();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }

    EntityManager<RobotAppEntrity> entityManager;

    @Override
    public EntityManager<RobotAppEntrity> entityManagerFactory() {
        entityManager = EntityManagerFactory.getInstance(Alpha2Application.getAlpha2(),
                EntityManagerHelper.DB_VERSION,
                EntityManagerHelper.DB_ACCOUNT,
                null, null)
                .getEntityManager(RobotAppEntrity.class,
                        EntityManagerHelper.DB_APP_LIST_TABLE);
        return entityManager;
    }

    @Override
    public List<RobotAppEntrity> getAllData() {

        return entityManager.findAll();
    }


    @Override
    public RobotAppEntrity getDataById(String id) {
        return entityManager.findById(id);
    }


    public void save(RobotAppEntrity info) {
        entityManager.save(info);
    }

    @Override
    public void saveOrUpdate(RobotAppEntrity info) {
        entityManager.saveOrUpdate(info);
    }

    @Override
    public void saveOrUpdateAll(List<RobotAppEntrity> dataList) {
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

    public void deleteByParam(Map<String , Object> paramMap) {
        WhereBuilder whereBuilder = null;
        if(paramMap != null) {
            int index = 0;
            for(Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if(index == 0) {
                    whereBuilder = WhereBuilder.create(entry.getKey(), "=", entry.getValue());
                    index++;
                }else {
                    whereBuilder.and(entry.getKey(), "=", entry.getValue());
                }

            }
        }
        entityManager.delete(whereBuilder);
    }

    public void updateValuesByParam(RobotAppEntrity robotAppEntrity,  Map<String,Object> paramMap, String ... columns) {

        WhereBuilder whereBuilder = null;
        if(paramMap != null) {
            int index = 0;
            for(Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if(index == 0) {
                    whereBuilder = WhereBuilder.create(entry.getKey(), "=", entry.getValue());
                    index++;
                }else {
                    whereBuilder.and(entry.getKey(), "=", entry.getValue());
                }

            }
        }
        entityManager.update(robotAppEntrity, whereBuilder, columns);
    }


    public void saveOrUpdateByPackageNameAndSerialNo(List<RobotAppEntrity> lists) {
        for (RobotAppEntrity app : lists) {

            Selector selector = Selector.create().where("packageName", "=", app.getPackageName()).and("serialNo", "=", app.getSerialNo());
            List<RobotAppEntrity> robotAppEntrities = entityManager.findAll(selector);
            if (ListUtils.isEmpty(robotAppEntrities)) {//本地没有该条数据
                Logger.d("run saveOrUpdate save result = " + entityManager.save(app));
            } else {
                ContentValues values = new ContentValues();
                values.put("packageName", app.getPackageName());
                values.put("name", app.getName());
                values.put("appId", app.getAppId());
                values.put("versionCode", app.getVersionCode());
                values.put("versionName", app.getVersionName());
                values.put("downloadState", app.getDownloadState());
                values.put("isButtonEvent", app.isButtonEvent());
                values.put("isSetting", app.isSetting());
                values.put("url", app.getUrl());
                values.put("isSystemApp", app.isSystemApp());
                values.put("downloadState", app.getDownloadState());
                entityManager.update(values, WhereBuilder.create("packageName", "=", app.getPackageName()).and("serialNo", "=", app.getSerialNo()));

            }
        }

    }

    public void updateAppIconAndName(List<AppInfo> infoList) {
        for (AppInfo info : infoList) {
            if (info != null) {
                Logger.d(TAG, "appName=" + info.getAppName() + " appIconPath==" + info.getAppIcon() + " appId==" + info.getAppId());

                ContentValues values = new ContentValues();
                if (!TextUtils.isEmpty(info.getAppIcon()))
                    values.put("appImagePath", info.getAppIcon());
                values.put("name", info.getAppName());
                entityManager.update(values, WhereBuilder.create("packageName", "=", info.getPackageName()));
            }

        }
    }

    public List<RobotAppEntrity>  findAppListByParam(Map<String , Object> paramMap) {
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

    public RobotAppEntrity findAppByParam(ImmutableMap<String , Object> paramMap) {
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



}


