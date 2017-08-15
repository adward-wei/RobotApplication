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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import timber.log.Timber;

/**
 * @desc : 音乐帧
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class MusicFrame extends Frame implements Cloneable {

    private int musicFileNameLength;
    private byte[] musicFileNameBytes;
    private String musicFileName;

    @Override
    public final boolean analysis(ByteBufferList bbl, int length) {
        boolean ret = super.analysis(bbl, length);
        if (!ret) return false;

        byte[] bytes = new byte[4];
        bbl.get(bytes);
        musicFileNameLength = ConvertUtils.l_byte2Int(bytes);

        musicFileNameBytes = new byte[musicFileNameLength];
        bbl.get(musicFileNameBytes);
        try {
            musicFileName = new String(musicFileNameBytes, Constraints.CHARSET_NAME);
            musicFileName.trim();
            musicFileName = musicFileName.substring(musicFileName.lastIndexOf("\\") + 1);
        } catch (UnsupportedEncodingException e) {
            Timber.w(e.getMessage());
        }
        return true;
    }

    @Override
    protected byte[] getExtra() {
        int total = 4 + musicFileNameLength;
        ByteBuffer bb = ByteBuffer.allocate(total);
        bb.rewind();
        bb.limit(total);

        bb.put(ConvertUtils.l_int2Byte(musicFileNameLength));
        bb.put(musicFileNameBytes);

        bb.rewind();
        byte[] bytes = new byte[total];
        bb.get(bytes);
        return bytes;
    }

    MusicFrame() {}

    @Override
    public Frame clone() throws CloneNotSupportedException {
        MusicFrame newObj = (MusicFrame) super.clone();
        newObj.musicFileNameBytes = new byte[musicFileNameBytes.length];
        System.arraycopy(musicFileNameBytes,0, newObj.musicFileNameBytes, 0, musicFileNameBytes.length);
        newObj.musicFileName = musicFileName;
        return newObj;
    }

    //////////////getter/////////
    public String getMusicFileName() {
        return musicFileName;
    }
}
