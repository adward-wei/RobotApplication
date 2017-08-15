package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

public class PhotoList implements Serializable,Comparable<PhotoList>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2870791792134072493L;
	private String dateType;
	private List<ImageDetail> imageDetailList  ;
	
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public List<ImageDetail> getImageDetailList() {
		return imageDetailList;
	}
	public void setImageDetailList(List<ImageDetail> imageDetailList) {
		this.imageDetailList = imageDetailList;
	}
	@Override
	public int compareTo(PhotoList another) {
			int thisDate = Integer.valueOf(this.dateType);
			int compDate = Integer.valueOf(another.dateType);
			if( thisDate>compDate){
				Log.i("BookComment", "thisdate="+thisDate+";comparedate ="+compDate);
				return 1;
			}
		return -1;
	}
	
}
