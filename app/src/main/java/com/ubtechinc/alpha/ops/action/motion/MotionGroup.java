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

package com.ubtechinc.alpha.ops.action.motion;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.ops.action.Constraints;
import com.ubtechinc.alpha.ops.action.IByteProcessor;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * @desc : 组
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class MotionGroup implements IByteProcessor, Cloneable {
    private final @IMotionLayer.Type int type;//不参与序列

    private int mLength;
    private int id;
    private int startTime;
    private int endTime;
    private byte[] descBytes = new byte[Constraints.DESC_SIZE];

    private ArrayList<Frame> frames = new ArrayList<>();

    public MotionGroup(@IMotionLayer.Type int type) {
        this.type = type;
    }

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        mLength = ConvertUtils.l_byte2Int(bytes);
        if (mLength != length) return false;

        bbl.get(bytes);
        id = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        startTime = ConvertUtils.l_byte2Int(bytes);

        bbl.get(bytes);
        endTime = ConvertUtils.l_byte2Int(bytes);

        descBytes = new byte[Constraints.DESC_SIZE];
        bbl.get(descBytes);

        bbl.get(bytes);
        int frameCount = ConvertUtils.l_byte2Int(bytes);
        boolean ret;
        for (int i= 0; i < frameCount; i ++){
            bbl.get(bytes);
            int frameLength = ConvertUtils.l_byte2Int(bytes);
            ByteBufferList frameBBL = bbl.get(frameLength);

            Frame frame = null;
            if (type == IMotionLayer.TYPE_MOTOR){
                frame = new MotorFrame();
                ret = frame.analysis(frameBBL, frameLength);
                if (!ret) return false;
            }else if (type == IMotionLayer.TYPE_EYE || type == IMotionLayer.TYPE_EAR || type == IMotionLayer.TYPE_MOUTH){
                frame = new LedFrame(type);
                ret = frame.analysis(frameBBL, frameLength);
                if (!ret) return false;
            }else if (type == IMotionLayer.TYPE_MUSIC){
                frame = new MusicFrame();
                ret = frame.analysis(frameBBL, frameLength);
                if (!ret) return false;
            }

            frames.add(frame);
        }

        return true;
    }

    @Override
    public byte[] toBytes() {
        int totalLength = 4 + mLength;
        ByteBuffer bb = ByteBufferList.obtain(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(mLength));
        bb.put(ConvertUtils.l_int2Byte(id));
        bb.put(ConvertUtils.l_int2Byte(startTime));
        bb.put(ConvertUtils.l_int2Byte(endTime));
        bb.put(descBytes);

        bb.put(ConvertUtils.l_int2Byte(frames.size()));
        for (int i=0; i < frames.size(); i ++){
            bb.put(frames.get(i).toBytes());
        }

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        ByteBufferList.reclaim(bb);
        return bytes;
    }

    ///////////////getter/////////////
    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public ArrayList<Frame> getFrames() {
        return frames;
    }

    @Override
    public MotionGroup clone() throws CloneNotSupportedException {
        MotionGroup obj = (MotionGroup) super.clone();
        obj.descBytes = new byte[descBytes.length];
        System.arraycopy(descBytes, 0, obj.descBytes, 0, descBytes.length);
        obj.frames = (ArrayList<Frame>) frames.clone();
        return obj;
    }
}
