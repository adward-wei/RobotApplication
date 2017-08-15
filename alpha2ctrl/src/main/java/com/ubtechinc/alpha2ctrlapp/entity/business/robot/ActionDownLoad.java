package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;

/**
 * @ClassName ActionDownLoad
 * @date 5/18/2017
 * @author tanghongyu
 * @Description 动作下载实体
 * @modifier
 * @modify_time
 */
public class ActionDownLoad extends ActionInfo {
	
	private static final long serialVersionUID = -1919651522220124621L;
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int stutus) {
		this.status = stutus;
	}
	

}
