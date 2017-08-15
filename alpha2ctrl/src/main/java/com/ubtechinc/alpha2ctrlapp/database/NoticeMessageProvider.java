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
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.framework.db.EntityManager;
import com.ubtechinc.framework.db.EntityManagerFactory;
import com.ubtechinc.framework.db.sqlite.Selector;
import com.ubtechinc.framework.db.sqlite.WhereBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ubtechinc.framework.db.sqlite.WhereBuilder.create;


/**
 * @author tanghongyu
 * @ClassName AppInfoProvider
 * @date 5/18/2017
 * @Description 通知消息数据库操作类
 * @modifier
 * @modify_time
 */
public class NoticeMessageProvider extends BaseProvider<NoticeMessageInfo> {
    private static final String TAG = "NoticeMessageProvider";
    //使用volatile关键字保其可见性
    volatile private static NoticeMessageProvider INSTANCE = null;


    public static NoticeMessageProvider get() {
        try {
            if (INSTANCE == null) {
                //创建实例之前可能会有一些准备性的耗时工作
                Thread.sleep(300);
                synchronized (NoticeMessageProvider.class) {
                    if (INSTANCE == null) {//二次检查
                        INSTANCE = new NoticeMessageProvider();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }

    EntityManager<NoticeMessageInfo> entityManager;

    @Override
    public EntityManager<NoticeMessageInfo> entityManagerFactory() {
        entityManager = EntityManagerFactory.getInstance(Alpha2Application.getAlpha2(),
                EntityManagerHelper.DB_VERSION,
                EntityManagerHelper.DB_ACCOUNT,
                null, null)
                .getEntityManager(NoticeMessageInfo.class,
                        EntityManagerHelper.DB_NOTICE_MESSAGE_TABLE);
        return entityManager;
    }

    @Override
    public List<NoticeMessageInfo> getAllData() {

        return entityManager.findAll();
    }


    @Override
    public NoticeMessageInfo getDataById(String id) {
        return entityManager.findById(id);
    }


    public void save(NoticeMessageInfo info) {
        entityManager.save(info);
    }

    public void saveAll(List<NoticeMessage> noticeMessageInfos) {
        List<NoticeMessageInfo> dataList = Lists.newArrayList();
        for(NoticeMessage noticeMessage : noticeMessageInfos) {
            NoticeMessageInfo data = new NoticeMessageInfo();
            BeanUtils.copyBean(noticeMessage, data);
            dataList.add(data);
        }
        entityManager.saveAll(dataList);
    }

    @Override
    public void saveOrUpdate(NoticeMessageInfo info) {
        entityManager.saveOrUpdate(info);
    }

    @Override
    public void saveOrUpdateAll(List<NoticeMessageInfo> dataList) {
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


    public void update(NoticeMessageInfo action, String... updateColumnNames) {
        entityManager.update(action, updateColumnNames);
    }


    public List<NoticeMessage> findNoticeListByParam(@NonNull  Map<String , Object> paramMap) {
        Selector selector = Selector.create();
        int index = 0;
        if(paramMap != null) {
            for(Map.Entry<String, Object> entry: paramMap.entrySet()) {
                if(index == 0)  {
                    selector.where(entry.getKey(), "=", entry.getValue());
                }else {
                    selector.and(entry.getKey(), "=", entry.getValue());
                }
                index++;
            }

        }
        List<NoticeMessageInfo> list =  entityManager.findAll(selector);
        List<NoticeMessage> robotInfoModels = new ArrayList<>();
        for (NoticeMessageInfo noticeMessageInfo : list) {
            NoticeMessage message = new NoticeMessage();
            BeanUtils.copyBean(noticeMessageInfo, message);
            robotInfoModels.add(message);
        }
        return robotInfoModels;
    }
    public NoticeMessageInfo findNoticeByParam(@NonNull Map<String , Object> paramMap) {
        Selector selector = Selector.create();
            int index = 0;
            for(Map.Entry<String, Object> entry: paramMap.entrySet()) {
                if(index == 0)  {
                    selector.where(entry.getKey(), "=", entry.getValue());
                }else {
                    selector.and(entry.getKey(), "=", entry.getValue());
                }
                index++;
            }

        return entityManager.findFirst(selector);
    }

    public int getNoticeListCountByParam(  Map<String , Object> paramMap) {

        return ListUtils.getSize(findNoticeListByParam(paramMap));
    }


    public void deleteNoticeByParam(  Map<String , Object> paramMap) {
        WhereBuilder whereBuilder = null;
        if(paramMap != null) {
            int index = 0;
            for(Map.Entry<String, Object> entry: paramMap.entrySet()) {
                if(index == 0)  {
                    whereBuilder = WhereBuilder.create(entry.getKey(), "=", entry.getValue());
                }else {
                    whereBuilder.and(entry.getKey(), "=", entry.getValue());
                }
                index++;
            }
        }
        entityManager.delete(whereBuilder);
    }

    public void updateParamByNoticeId( String noticeId, Map<String , Object> paramMap) {
        ContentValues contentValues = new ContentValues();
        for(Map.Entry<String, Object> entry: paramMap.entrySet()) {
            Object value = entry.getValue();
            if(value instanceof Integer) {
                contentValues.put(entry.getKey(), (Integer)entry.getValue());
            }else {
                contentValues.put(entry.getKey(), entry.getValue().toString());
            }


        }

        entityManager.update(contentValues, create("noticeId", "=",noticeId));

    }

}


