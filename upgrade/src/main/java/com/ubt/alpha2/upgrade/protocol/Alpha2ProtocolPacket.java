package com.ubt.alpha2.upgrade.protocol;

import android.os.Environment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

enum PROTOCOL_STATE {
	HEADER1, HEADER2, LENGHT, SESSION_ID, INDEX, CMD, PARAM, CHECKSUM, END
}

public class Alpha2ProtocolPacket {

	// 字头（2B）
	private byte[] mHeader;
	// 长度（1B）
	private byte mLen;
	// SessionID（1B）
	private byte mID;
	// 包序号(1B)
	private byte mIndex;
	// 命令(1B)
	private byte mCmd;
	// check sum (1B)
	private byte mCheckSum;
	// end(1B)
	private byte mEnd;
	// param(nB)
	private byte[] mParam;

	// 命令参数长度
	private int mParamLen;

	private PROTOCOL_STATE mState;
	private ByteBuffer mBufferDecode;

	/**
	 * 构造函数
	 */
	public Alpha2ProtocolPacket() {
		mState = PROTOCOL_STATE.HEADER1;
		mBufferDecode = ByteBuffer.allocate(1024);
		mBufferDecode.clear();
	}

	/**
	 * 获取解码之后的数据
	 */
	public byte[] getRawData() {
		int nBytes = mBufferDecode.position();
		byte[] rawData = new byte[nBytes];

		// rawData = mBuffer.array().clone();
		for (int i = 0; i < nBytes; i++) {
			rawData[i] = mBufferDecode.get(i);
		}

		return rawData;
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 */
	public void setRawData(byte[] data) {
		int nPos = 0;

		// 字头
		mHeader = new byte[2];
		System.arraycopy(data, nPos, mHeader, 0, 2);
		nPos += 2;
		// 长度
		mLen = data[nPos++];
		// SessionID（1B
		mID = data[nPos++];
		// 包序号(1B)
		mIndex = data[nPos++];
		// 命令(1B)
		mCmd = data[nPos++];
		// param(nB)
		if ((mLen - 7) > 0) {
			mParam = new byte[mLen - 7];
			System.arraycopy(data, nPos, mParam, 0, mLen - 7);
			nPos += mLen - 7;
			mParamLen = mLen - 7;
		}
		// check sum (1B)
		mCheckSum = data[nPos++];

		// end
		mEnd = data[nPos++];
	}

	/**
	 * 保存并解码数据
	 */
	public boolean setData_(byte data) {
		boolean bRet = false;

		switch (mState) {
		case HEADER1:
			if (data != (byte) 0xF8)
				break;

			mBufferDecode.clear();
			mBufferDecode.put(data);
			mState = PROTOCOL_STATE.HEADER2;
			break;

		case HEADER2:
			if (data != (byte) 0x8F) {
				mState = PROTOCOL_STATE.HEADER1;
				break;
			}

			mBufferDecode.put(data);
			mState = PROTOCOL_STATE.LENGHT;
			break;

		case LENGHT:
			mLen = data;
			mBufferDecode.put(data);
			mState = PROTOCOL_STATE.SESSION_ID;
			break;

		case SESSION_ID:
			mID = data;
			mBufferDecode.put(data);
			mState = PROTOCOL_STATE.INDEX;
			break;

		case INDEX:
			mIndex = data;
			mBufferDecode.put(data);
			mState = PROTOCOL_STATE.CMD;
			break;

		case CMD:
			mCmd = data;
			mBufferDecode.put(data);
			mState = PROTOCOL_STATE.PARAM;
			mParamLen = mLen - 7;
			// 先保留不提交 正常为不捕获异常
			try {
				mParam = new byte[mParamLen];
			} catch (Exception e) {
				e.printStackTrace();
				mState = PROTOCOL_STATE.HEADER1;
				String sdPath = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				try {
					FileOutputStream out = new FileOutputStream(sdPath
							+ "/crash/cmd.txt");
					//byte[] contentInBytes = e.toString().getBytes();
					 
					try {
						out.write(mCmd);
						out.flush();
						out.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			}
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
				break;
			}

			mBufferDecode.put(data);
			mState = PROTOCOL_STATE.END;
			break;

		case END:
			if (data != (byte) 0xED) {
				mState = PROTOCOL_STATE.HEADER1;
				break;
			}

			mBufferDecode.put(data);
			mState = PROTOCOL_STATE.HEADER1;
			bRet = true;

			int j = 0;
			for (int i = 0; i < mLen - 7; i++) {
				mParam[j++] = mBufferDecode.get(6 + i);
			}
			break;
		}

		return bRet;
	}

	/**
	 * 计算CHECKSUM
	 */
	public byte getCheckSum(byte[] data, int nStart, int nEnd) {
		byte nCheckSum = 0;

		for (int i = nStart; i <= nEnd; i++) {
			nCheckSum += data[i];
		}

		return nCheckSum;
	}

	/**
	 * 打包数据
	 */
	public byte[] packetData() {
		// |字头(2B) 长度(1B) ID(1B) index(1B) 命令(1B)
		short nTotalLen = (byte) (2 + 1 + 1 + 1 + 1
		// checksum(1B)
				+ 1 + 1);

		if (mParamLen == 0)
			nTotalLen += 1;
		else
			nTotalLen += mParamLen;

		byte[] result = new byte[nTotalLen];

		int i = 0;
		// 字头
		result[i++] = (byte) 0xF8;
		result[i++] = (byte) 0x8F;
		// 长度
		result[i++] = (byte) (nTotalLen - 1);
		// ID
		result[i++] = mID;
		// index
		result[i++] = mIndex;
		// 命令
		result[i++] = mCmd;
		// 参数
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

		// 结束符
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

	public byte getmID() {
		return mID;
	}

	public void setmID(byte mID) {
		this.mID = mID;
	}

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

	public byte getmIndex() {
		return mIndex;
	}

	public void setmIndex(byte mIndex) {
		this.mIndex = mIndex;
	}

}
