/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.serverlibutil.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @date 2017/2/4
 * @author paul.zhang@ubtrobot.com
 * @Description TTS发音人信息实体
 * @modifier
 * @modify_time
 */

public class SpeechVoice implements Parcelable {
	// 发音人名称
	private String name = "";
	//发音人性别 0:男 1:女
	private int sex;
	//发音人是否成年 0:未成年 1:成年
	private int adult;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	private String language = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAdult() {
		return adult;
	}

	public void setAdult(int adult) {
		this.adult = adult;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(sex);
		dest.writeInt(adult);

	}

	public static final Creator<SpeechVoice> CREATOR = new Creator<SpeechVoice>() {

		@Override
		public SpeechVoice[] newArray(int size) {
			return new SpeechVoice[size];
		}

		@Override
		public SpeechVoice createFromParcel(Parcel source) {
			return new SpeechVoice(source);
		}
	};

	public SpeechVoice() {
	}

	private SpeechVoice(Parcel source) {
		readFromParcel(source);
	}

	public void readFromParcel(Parcel source) {
		name = source.readString();
		sex = source.readInt();
		adult = source.readInt();
	}

	@Override
	public String toString() {

		return "speechvoice[name:" + name + ";sex:" + sex + ";adult:" + adult + ";language:"+language+"]";
	}
}
