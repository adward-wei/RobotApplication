package com.ubtech.utilcode.utils.bt;

import java.nio.ByteBuffer;

import android.util.Log;

enum PROTOCOL_STATE {
    HEADER1, HEADER2, LENGHT,
    // SESSION_ID,
    // INDEX,
    CMD, PARAM, CHECKSUM, END
}

public class ProtocolPacket {

    private byte[] mHeader;
    private byte mLen;
    // private byte mID;
    // private byte mIndex;
    private byte mCmd;
    // check sum (1B)
    private byte mCheckSum;
    // end(1B)
    private byte mEnd;
    // param(nB)
    private byte[] mParam;

    private int mParamLen;

    private PROTOCOL_STATE mState;
    private ByteBuffer mBufferDecode;

    public ProtocolPacket() {
        mState = PROTOCOL_STATE.HEADER1;
        mBufferDecode = ByteBuffer.allocate(1024);
        mBufferDecode.clear();
    }

    public byte[] getRawData() {
        int nBytes = mBufferDecode.position();
        byte[] rawData = new byte[nBytes];

        // rawData = mBuffer.array().clone();
        for (int i = 0; i < nBytes; i++) {
            rawData[i] = mBufferDecode.get(i);
        }

        return rawData;
    }

    public void setRawData(byte[] data) {
        int nPos = 0;

        mHeader = new byte[2];
        System.arraycopy(data, nPos, mHeader, 0, 2);
        nPos += 2;
        mLen = data[nPos++];
        mCmd = data[nPos++];
        if ((mLen - 5) > 0) {
            mParam = new byte[mLen - 5];
            System.arraycopy(data, nPos, mParam, 0, mLen - 5);
            nPos += mLen - 5;
            mParamLen = mLen - 5;
        }
        mCheckSum = data[nPos++];

        mEnd = data[nPos++];
    }

    public boolean setData_(byte data) {
        boolean bRet = false;

        switch (mState) {
        case HEADER1:
            if (data != (byte) 0xFB) {
                Log.v("error_blue", "1");
                break;
            }

            mBufferDecode.clear();
            mBufferDecode.put(data);
            mState = PROTOCOL_STATE.HEADER2;
            break;

        case HEADER2:
            if (data != (byte) 0xBF) {
                mState = PROTOCOL_STATE.HEADER1;
                Log.v("error_blue", "2");
                break;
            }

            mBufferDecode.put(data);
            mState = PROTOCOL_STATE.LENGHT;
            break;

        case LENGHT:
            mLen = data;
            mBufferDecode.put(data);
            mState = PROTOCOL_STATE.CMD;
            break;

        // case SESSION_ID:
        // mID = data;
        // mBufferDecode.put(data);
        // mState = PROTOCOL_STATE.INDEX;
        // break;
        //
        // case INDEX:
        // mIndex = data;
        // mBufferDecode.put(data);
        // mState = PROTOCOL_STATE.CMD;
        // break;

        case CMD:
            mCmd = data;
            mBufferDecode.put(data);
            mState = PROTOCOL_STATE.PARAM;
            mParamLen = mLen - 5;

            mParam = new byte[mParamLen];
            break;

        case PARAM:
            mBufferDecode.put(data);
            mParamLen -= 1;
            if (mParamLen == 0) {
                mState = PROTOCOL_STATE.CHECKSUM;
            }
            break;

        case CHECKSUM:
            byte[] b = mBufferDecode.array();
            byte nCheckSum = getCheckSum(b, 2, mBufferDecode.position() - 1);
            if (nCheckSum != data) {
                mState = PROTOCOL_STATE.HEADER1;
                Log.v("error_blue", "3");
                break;
            }

            mBufferDecode.put(data);
            mState = PROTOCOL_STATE.END;
            break;

        case END:
            if (data != (byte) 0xED) {
                mState = PROTOCOL_STATE.HEADER1;
                Log.v("error_blue", "4");
                break;
            }

            mBufferDecode.put(data);
            mState = PROTOCOL_STATE.HEADER1;
            bRet = true;

            int j = 0;
            for (int i = 0; i < mLen - 5; i++) {
                mParam[j++] = mBufferDecode.get(4 + i);
            }
            // mParamLen = mParam.length;
            break;
        }

        return bRet;
    }

    public byte getCheckSum(byte[] data, int nStart, int nEnd) {
        byte nCheckSum = 0;

        for (int i = nStart; i <= nEnd; i++) {
            nCheckSum += data[i];
        }

        return nCheckSum;
    }

    public byte[] packetData() {
        short nTotalLen = (short) (2 + 1 + 1
        // checksum(1B)
        + 1 + 1);

        if (mParamLen == 0)
            nTotalLen += 1;
        else
            nTotalLen += mParamLen;

        byte[] result = new byte[nTotalLen];

        int i = 0;
        result[i++] = (byte) 0xFB;
        result[i++] = (byte) 0xBF;
        result[i++] = (byte) ((nTotalLen - 1) & 0xff);
        // ID
        // result[i++] = mID;
        // index
        // result[i++] = mIndex;
        result[i++] = mCmd;
        if (mParamLen == 0) {
            result[i++] = 0;
        } else {
            for (int n = 0; n < mParamLen; n++) {

                result[i++] = mParam[n];
            }
        }

        result[i++] = getCheckSum(result, 2, i);
        // int nCheckSumPos = i;
        // i++;

        result[i] = (byte) 0xED;

        return result;
    }

    public byte[] getmHeader() {
        return mHeader;
    }

    public void setmHeader(byte[] mHeader) {
        this.mHeader = mHeader;
    }

    public byte getmLen() {
        return mLen;
    }

    public void setmLen(byte mLen) {
        this.mLen = mLen;
    }

    // public byte getmID() {
    // return mID;
    // }
    // public void setmID(byte mID) {
    // this.mID = mID;
    // }
    public byte getmCmd() {
        return mCmd;
    }

    public void setmCmd(byte mCmd) {
        this.mCmd = mCmd;
    }

    public byte getmCheckSum() {
        return mCheckSum;
    }

    public void setmCheckSum(byte mCheckSum) {
        this.mCheckSum = mCheckSum;
    }

    public byte getmEnd() {
        return mEnd;
    }

    public void setmEnd(byte mEnd) {
        this.mEnd = mEnd;
    }

    public byte[] getmParam() {
        return mParam;
    }

    public void setmParam(byte[] mParam) {
        this.mParam = mParam;
    }

    public int getmParamLen() {
        return mParamLen;
    }

    public void setmParamLen(int mParamLen) {
        this.mParamLen = mParamLen;
    }

    // public byte getmIndex() {
    // return mIndex;
    // }
    //
    // public void setmIndex(byte mIndex) {
    // this.mIndex = mIndex;
    // }

}
