package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wzt
 *
 */
public class TransferPhotoInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 相册传输的类型
	 */
	private int type;
	/**
	 *缩略图数量
	 */
	private int amount;
	/**
	 * 指定要加载大图的缩略图文件名或是upload的url
	 */
	private String path;
	/**
	 *指定要删除或删除成功的缩略图文件名集合
	 */
	private List<String> delPics=new ArrayList<String>();

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	

	public List<String> getDelPics() {
		return delPics;
	}
	public void setDelPics(List<String> delPics) {
		this.delPics = delPics;
	}
	
	

}