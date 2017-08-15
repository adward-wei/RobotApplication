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

package com.ubt.alpha2.upgrade.serial;


import com.ubt.alpha2.upgrade.bean.SerialCommandBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @desc : ByteBuffer对象池
 *         1.管理一个static的reclaimed ByteBuffer队列
 *         2.BBL实例维护一个mBuffers队列
 *         3.提供接口读取mBuffers中字节
 *         4.按条件重用mBuffers中的ByteBuffer。
 * @@author: Logic
 * @email : pdlogic1987@gmail.com
 * @time : 2017/4/17
 * @modifier:
 * @modify_time:
 */
public class ByteBufferList {
    private static final byte HEADER1 = (byte) 0xF8;
    private static final byte HEADER2 = (byte) 0x8F;
    private static final byte END = (byte) 0xED;
    ArrayList<SerialCommandBean> serialCommandBeanList;
    Object writeLock = new Object();

    public ByteBufferList(){
        serialCommandBeanList = new ArrayList<>(12);
    }

    public void put(byte[] command){
        synchronized (writeLock){
            if(command == null)
                return;
            if(command.length<2)
                return;
            ArrayList<Integer> headIndexList = new ArrayList<>();
            for(int i=0;i<command.length;i++){
                if(command[i] == HEADER1 && command[i+1] == HEADER2) {
                    if (i <2) {
                        headIndexList.add(i);
                    }else if(END == command[i-1]){
                        headIndexList.add(i);
                    }
                }
            }

            //命令没有头部
            if(headIndexList.isEmpty()){
                return;
            }

            //只有一个头域
            if(headIndexList.size() == 1){
                int head = headIndexList.get(0);
                if(head != 0){
                    getCommandTail(head,command);
                }else{
                    getHeadOrCommand(head,command);
                }
            }


            //遍历头部数组
            for(int i=0;i<headIndexList.size();i++){
                if(i < headIndexList.size()-1){
                    int head1 = headIndexList.get(i);
                    int head2 = headIndexList.get(i+1);
                    if(head1 != 0){
                        getCommandTail(head1,command);
                    }
                    getCommand(head2,head1,command);
                }
                //最后一个头域时
                int head2 = i;


            }
        }
    }

    private void getCommandHead(){

    }

    private void getCommandTail(int headIndex,byte[] command){
        byte[] tail = new byte[headIndex];
        System.arraycopy(command,0,tail,0,headIndex);
        for(SerialCommandBean bean:serialCommandBeanList){
            if(bean.hasTail){
                continue;
            }
            if(bean.currentLength+headIndex == bean.length){
                bean.currentLength = bean.length;
                bean.buffer.put(tail);
            }
        }
    }

    private void getCommand(int head2,int head1,byte[] command){
        byte[] fullCommand = new byte[head2-head1];
        System.arraycopy(command,head1,fullCommand,0,head2-head1);
        //校验长度
        if(fullCommand[2]+1 == head2-head1) {
            SerialCommandBean bean = new SerialCommandBean();
            bean.currentLength = bean.length = fullCommand[2];
            bean.hasHeader = true;
            bean.hasTail = true;
        }
    }

    private void getHeadOrCommand(int headIndex,byte[] command){
        SerialCommandBean bean = new SerialCommandBean();
        if(command.length == 2){
            bean.hasHeader = true;
            bean.hasTail = false;
            bean.currentLength = 2;
            bean.buffer.put(command);
        }
//        if(command.length>2)
    }

    public List<SerialCommandBean> findValidPacket(){
        List<SerialCommandBean> result = new ArrayList<>();
        if(serialCommandBeanList.isEmpty())
            return result;

        Iterator<SerialCommandBean> iterator = serialCommandBeanList.iterator();
        while(iterator.hasNext()){
            SerialCommandBean bean = iterator.next();
            if(bean.hasHeader && bean.hasTail){
                result.add(bean);
                iterator.remove();
            }
        }
        return serialCommandBeanList;
    }


}
