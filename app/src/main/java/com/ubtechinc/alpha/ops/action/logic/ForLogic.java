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

package com.ubtechinc.alpha.ops.action.logic;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.ops.action.Constraints;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import timber.log.Timber;

/**
 * @desc : for逻辑model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class ForLogic implements ILogic {

    private int type;
    private int mInitalValuesLength;
    private byte[] mInitalValuesBytes = new byte[0];
    private int mInitValue;
    private int mStepValuesLength;
    private byte[] mStepValuesBytes = new byte[0];
    private int mStepValue;
    private int mEndValuesLength;
    private byte[] mEndValuesBytes = new byte[0];
    private int mEndValue;

    @Override
    public boolean analysis(ByteBufferList bbl, int length) {
        byte[] bytes = new byte[4];
        bbl.get(bytes);
        type = ConvertUtils.l_byte2Int(bytes);
        if (type != ILogic.FOR_LOGIC) return false;

        bbl.get(bytes);
        mInitalValuesLength = ConvertUtils.l_byte2Int(bytes);
        if (mInitalValuesLength > 0){
            mInitalValuesBytes = new byte[mInitalValuesLength];
            bbl.get(mInitalValuesBytes);
            try {
                String initalValues = new String(mInitalValuesBytes, Constraints.CHARSET_NAME);
                mInitValue = Integer.valueOf(initalValues);
            } catch (UnsupportedEncodingException e) {
                Timber.d(e.getMessage());
                return false;
            }catch (NumberFormatException e){
                Timber.d(e.getMessage());
                return false;
            }
        }

        bbl.get(bytes);
        mStepValuesLength = ConvertUtils.l_byte2Int(bytes);
        if (mStepValuesLength > 0){
            mStepValuesBytes = new byte[mStepValuesLength];
            bbl.get(mStepValuesBytes);
            try {
                String stepValues = new String(mStepValuesBytes, Constraints.CHARSET_NAME);
                mStepValue = Integer.valueOf(stepValues);
            } catch (UnsupportedEncodingException e) {
                Timber.d(e.getMessage());
                return false;
            }catch (NumberFormatException e){
                Timber.d(e.getMessage());
                return false;
            }
        }

        bbl.get(bytes);
        mEndValuesLength = ConvertUtils.l_byte2Int(bytes);
        if (mEndValuesLength > 0){
            mEndValuesBytes = new byte[mEndValuesLength];
            bbl.get(mEndValuesBytes);
            try {
                String endValues = new String(mEndValuesBytes, Constraints.CHARSET_NAME);
                mEndValue = Integer.valueOf(endValues);
            } catch (UnsupportedEncodingException e) {
                Timber.d(e.getMessage());
                return false;
            } catch (NumberFormatException e){
                Timber.d(e.getMessage());
                return false;
            }
        }

        return true;
    }

    @Override
    public byte[] toBytes() {
        int totalLength = 4 + 4 + mInitalValuesLength + 4 + mStepValuesLength + 4 + mEndValuesLength;
        ByteBuffer bb = ByteBuffer.allocate(totalLength);
        bb.rewind();
        bb.limit(totalLength);

        bb.put(ConvertUtils.l_int2Byte(type));
        bb.put(ConvertUtils.l_int2Byte(mInitalValuesLength));
        bb.put(mInitalValuesBytes);
        bb.put(ConvertUtils.l_int2Byte(mStepValuesLength));
        bb.put(mStepValuesBytes);
        bb.put(ConvertUtils.l_int2Byte(mEndValuesLength));
        bb.put(mEndValuesBytes);

        bb.rewind();
        byte[] bytes = new byte[totalLength];
        bb.get(bytes);
        return bytes;
    }

    ////////////////getter//////////

    public int getInitValue() {
        return mInitValue;
    }

    public int getStepValue() {
        return mStepValue;
    }

    public int getEndValue() {
        return mEndValue;
    }

    private int mCurrentValue;
    synchronized public int getCurrentValue(){
        if (mCurrentValue == 0) {
            mCurrentValue = mInitValue;
        }
        return mCurrentValue;
    }
    synchronized public void stepCurrentValue(){
        mCurrentValue += mStepValue;
    }
}
