package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.io.Serializable;
/**
 * @ClassName ImageModel
 * @date 6/27/2017
 * @author tanghongyu
 * @Description 机器人拍照的图片实体
 * @modifier
 * @modify_time
 */
public class ImageModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9094409249516044759L;
	private String image_original_url;
	private String image_thumbnail_url;
	private String image_upload_time;
	private String image_id;
	private String robot_seq;
	public String getImage_original_url() {
		return image_original_url;
	}
	public void setImage_original_url(String image_original_url) {
		this.image_original_url = image_original_url;
	}
	public String getImage_thumbnail_url() {
		return image_thumbnail_url;
	}
	public void setImage_thumbnail_url(String image_thumbnail_url) {
		this.image_thumbnail_url = image_thumbnail_url;
	}
	public String getImage_upload_time() {
		return image_upload_time;
	}
	public void setImage_upload_time(String image_upload_time) {
		this.image_upload_time = image_upload_time;
	}
	public String getImage_id() {
		return image_id;
	}
	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}

	public String getRobot_seq() {
		return robot_seq;
	}

	public void setRobot_seq(String robot_seq) {
		this.robot_seq = robot_seq;
	}
}
