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

package com.ubtechinc.alpha.serial;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.cmds.chest.ChestCmds;

import java.nio.ByteBuffer;

import timber.log.Timber;

/**
 * @desc : Alpha2 串口通讯协议,格式：
 *         1B   HEADER1  头1
 *         1B   HEADER2  头2
 *         1B   LENGTH   长: 不包含结束码
 *         1B   SESSIONID SessionID
 *         1B   INDEX     包序号
 *         1B   CMD       命令码
 *         ?B   PARAM     参数
 *         1B   CHECKSUM  校验码：计算校验码时，不包含字头和结束码
 *         1B   ENDCODE   结束码
 *
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/17
 * @modifier:
 * @modify_time:
 */
public class Alpha2SerialProtocol {
    private static final String TAG = "Alpha2SerialProtocol";

    private static byte header1 = (byte) 0xF8;
    private static byte header2 = (byte) 0x8F;
    private static byte end = (byte) 0xED;
    /**
     * 包长度计算：字头(2B) + 长度(1B) + ID(1B)
     *           +INDEX(1B) + CMD(1B)
     *           +校验码(1B)
     *           +(参数长度==0? 1: 参数长度)
     *
     *  不包含结束码
     */
    private byte mLength;
    /**
     * SessionId
     */
    private byte mId;
    /**
     * 包序号
     */
    private byte mIndex;
    /**
     * 命令码
     */
    private byte mCmd;
    /**
     * 校验码：计算校验码时，不包含字头、校验码和结束码
     */
    private byte mCheckSum = -1;
    /**
     * 参数
     */
    private byte[] mParam;

    /**
     *  传入一个完成的串口协议包数据
     *
     * @param in 一个完整的有效的串口协议包
     * @throws InvalidPacketException
     */
    public Alpha2SerialProtocol(byte[] in) throws InvalidPacketException {
        if (checkBytes(in)){
            parseData(in);
            return;
        }
        throw new InvalidPacketException("无效的串口数据包");
    }

    public Alpha2SerialProtocol(byte id, byte index, byte cmd, byte[] param){
        this.mId = id;
        this.mIndex = index;
        this.mCmd = cmd;
        this.mParam = param;
        this.mLength = calPackectLength(param);
        //checkSum未计算
    }

    public ByteBuffer toByteBuffer(){
        //字头(2B) 长度(1B) ID(1B) index(1B) 命令(1B)
        short totalLen = (short)(2 + 1 + 1 + 1 + 1);
        if (mParam.length > 0){
            totalLen += mParam.length;
        }else {
            totalLen += 1;
        }
        ByteBuffer bb = ByteBuffer.allocate(totalLen);//可写
        bb.put(header1);
        bb.put(header2);
        bb.put((byte) (totalLen + 1));//包含校验码
        bb.put(mId);
        bb.put(mIndex);
        bb.put(mCmd);
        if (mParam.length == 0){
            bb.put((byte) 0);
        }else {
            bb.put(mParam);
        }

        bb.rewind();//标记为可读
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);//数据读完

        ByteBuffer newBB = ByteBuffer.allocate(bytes.length + 2);//可写
        newBB.limit(bytes.length + 2);
        newBB.put(bytes);
        newBB.put(generateCheckSum(bytes, 2, bytes.length));
        newBB.put(end);
        newBB.rewind();//标记为可读
        return newBB;
    }

    public byte getId(){
        return mId;
    }

    public byte getCmd() {
        return mCmd;
    }

    public byte[] getParam() {
        return mParam;
    }

    public byte getIndex() {
        return mIndex;
    }

    private static byte calPackectLength(byte[] param){
        byte totalLen = (byte) (2 + 1 + 1 + 1 + 1 + 1 + 1);
        if (param.length > 0){
            totalLen += param.length;
        }else {
            totalLen += 1;
        }
        return (byte) (totalLen - 1);
    }

    public byte[] toRawBytes(){
        ByteBuffer bb = toByteBuffer();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        return bytes;
    }

    private void parseData(byte[] in) {
        ByteBuffer buffer = ByteBuffer.allocate(in.length);
        buffer.put(in);
        buffer.rewind();

        buffer.get();
        buffer.get();//字头
        mLength = buffer.get();//包长
        mId = buffer.get();//id
        mIndex = buffer.get();//包序号
        mCmd = buffer.get();//命令码
        int paramLength = mLength - 7;
        if (paramLength > 0) {
            mParam = new byte[paramLength];
            buffer.get(mParam);
        }else {
            mParam = new byte[0];
        }

        mCheckSum = buffer.get();//校验码
        buffer.get();//结束码
    }

    private static boolean checkBytes(byte[] in) {
//        Timber.d("head1=%s,head2=%s,(length:%d,%d),(checksum=%d,%d),(end=%s)", ConvertUtils.byte2HexString(in[0]),
//                ConvertUtils.byte2HexString(in[1]),in[2],in.length-1,
//                in[in.length-2],generateCheckSum(in, 2, in.length - 2),
//                ConvertUtils.byte2HexString(in[in.length-1]));
        return in[0] == header1 &&
                in[1] == header2 &&
                in[2] == (in.length - 1) &&
                in[in.length - 2] == generateCheckSum(in, 2, in.length - 2) &&
                in[in.length - 1] == end;
    }

    private static byte generateCheckSum(byte[] in, int start, int end) {
        byte checkSum = 0;
        //校验码计算：不包含结束码和校验码本身
        for (int i = start; i < end; i++) {
            checkSum += in[i];
        }
        return checkSum;
    }

    /**
     * 从一个bbl中找出第一个有效的串口包
     *
     * @param bbl bbl可能包含不完整的串口包
     * @return
     */
    public static byte[] findValidPacket(ByteBufferList bbl){
        if (bbl == null || bbl.remaining() < 9)
            return null;

        byte[] headers = bbl.peekBytes(2);
        //找出有效字头位置
        while (header1 != headers[0] || header2 != headers[1]){
            ByteBuffer invalidBuffer = ByteBuffer.allocate(9);
            invalidBuffer.putShort((short)bbl.getShort());//消耗字头
            if (!bbl.hasRemaining() || bbl.remaining() < 9) {
                byte[] bytes = new byte[invalidBuffer.remaining()];
                invalidBuffer.get(bytes);
                Timber.e("invalid bytes: %s", ConvertUtils.bytes2HexString(bytes));
                return null;
            }
            headers = bbl.peekBytes(2);
        }

        byte[] temp = bbl.peekBytes(3);
        byte length = temp[2];
        if (length + 1 > bbl.remaining()){
            //不完成的packet, 直接返回空，等待串口输入,原bbl规整有效字头位置
            Timber.i("数据包不完整...");
            return null;
        }

        //查找一个可能的完整包
        temp = bbl.peekBytes(length + 1);//包含结束码
        if (!checkBytes(temp)){
            //校验, 发现是不完整的包，这些数据全部丢掉
            bbl.get(temp.length);
            Timber.e("invalid Packet:%s", ConvertUtils.bytes2HexString(temp));
            return null;
        }

        //找到一个完整的串口包
        return bbl.get(temp.length).getAllByteArray();
    }


    private int retryCount = 2;
    synchronized public boolean tryRetry(){
        retryCount--;
        return retryCount > 0 && mCmd != ChestCmds.CMD_SEND_DATA_TO_MOTOR;
    }

    @Override
    public String toString() {
        return String.format("Packet:[cmd=%02X,Id=%02X,index=%02X,retryCount=%d, raw=%s]",mCmd, mId, mIndex,retryCount, ConvertUtils.bytes2HexString(toRawBytes()));
    }
}
