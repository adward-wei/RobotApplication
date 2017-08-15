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

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.ops.action.event.MotionEvent;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * @desc : UBX流程图
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public  class UbxFlow implements IByteProcessor {
    public static final String UBX_FILE_HEADER = "ubx-alpha";
    public static final int	UBX_FILE_VERSION = 2;
    private int totalLength;
    private String header;
    private byte[] headerBytes = new byte[9];
    private int version;
    private int pagesTotalLength;
    private ArrayList<Page> pages = new ArrayList<>();
    private int eventsTotalLength;
    private ArrayList<MotionEvent> events = new ArrayList<>();

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        totalLength = ConvertUtils.l_byte2Int(bytes);
        if (totalLength != length)
            return false;

        bytes = new byte[9];
        bbl.get(bytes);
        try {
            header = new String(bytes, Constraints.CHARSET_NAME);
            System.arraycopy(bytes, 0, headerBytes, 0, 9);
            if (!UBX_FILE_HEADER.equals(header))
                return false;
        } catch (UnsupportedEncodingException e) {
            Timber.d(e.getMessage());
            return false;
        }

        bytes = new byte[4];
        bbl.get(bytes);
        version = ConvertUtils.l_byte2Int(bytes);
        if (version > UBX_FILE_VERSION)
            return false;

        //解析页数据
        Timber.d("analysis page...");
        bytes = new byte[4];
        bbl.get(bytes);
        pagesTotalLength = ConvertUtils.l_byte2Int(bytes);
        if (pagesTotalLength > 0){
            bbl.get(4);
            bbl.get(bytes);
            int pagesCount = ConvertUtils.l_byte2Int(bytes);
            for (int i = 0; i < pagesCount; i++){
                Page page = new Page();
                bbl.get(bytes);
                int pageLength = ConvertUtils.l_byte2Int(bytes);
                ByteBufferList pageBBL = bbl.get(pageLength);
                boolean ret = page.analysis(pageBBL, pageLength);
                if (!ret) return false;
                pages.add(page);
            }
        }

        //解析事件
        Timber.d("analysis event...");
        bbl.get(bytes);
        eventsTotalLength = ConvertUtils.l_byte2Int(bytes);
        if (eventsTotalLength > 0){
            bbl.get(4);
            bbl.get(bytes);
            int eventsCount = ConvertUtils.l_byte2Int(bytes);
            for (int i = 0; i < eventsCount; i++){
                bbl.get(bytes);
                int eventType = ConvertUtils.l_byte2Int(bytes);
                bbl.get(bytes);
                int eventLength = ConvertUtils.l_byte2Int(bytes);
                if (eventType == MotionEvent.TYPE_INFRARE || eventType == MotionEvent.TYPE_SONAR) {
                    MotionEvent event = new MotionEvent(eventType);
                    ByteBufferList eventBBL = bbl.get(eventLength);
                    boolean ret = event.analysis(eventBBL, eventLength);
                    if (!ret) return false;
                    events.add(event);
                }else {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public byte[] toBytes(){
        ByteBuffer bb = ByteBufferList.obtain(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(totalLength));
        bb.put(headerBytes);
        bb.put(ConvertUtils.l_int2Byte(version));
        bb.put(ConvertUtils.l_int2Byte(pagesTotalLength));
        bb.put(ConvertUtils.l_int2Byte(pagesTotalLength));
        bb.put(ConvertUtils.l_int2Byte(pages.size()));

        int sum = 0;
        for (int i= 0; i < pages.size(); i++){
            bb.put(pages.get(i).toBytes());
            sum += pages.get(i).toBytes().length;
        }
        Timber.d("page:%d = %d?", pagesTotalLength, sum);

        bb.put(ConvertUtils.l_int2Byte(eventsTotalLength));
        bb.put(ConvertUtils.l_int2Byte(eventsTotalLength));
        bb.put(ConvertUtils.l_int2Byte(events.size()));

        sum = 0;
        for (int i = 0; i < events.size(); i++){
            bb.put(events.get(i).toBytes());
            sum += events.get(i).toBytes().length;
        }
        Timber.d("event:%d = %d?", pagesTotalLength, sum);

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }

    ////////////getter////////
    public ArrayList<Page> getPages() {
        return pages;
    }

    public ArrayList<MotionEvent> getEvents() {
        return events;
    }

}
