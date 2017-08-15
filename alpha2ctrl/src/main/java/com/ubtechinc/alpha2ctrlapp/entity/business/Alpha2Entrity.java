/*
 Launch Android Client, Alpha2
 Copyright (c) 2014 LAUNCH Tech Company Limited
 http:www.cnlaunch.com
 */

package com.ubtechinc.alpha2ctrlapp.entity.business;


import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionProfileXMLResponse;

import java.io.Serializable;
import java.util.List;

/**
 * [alpha2 实体类]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2014-10-15
 * 
 **/

public class Alpha2Entrity implements Serializable {
	/** 名称 **/
	private String name;
	/** 动作列表 **/
	private List<String> mActionFileList;
	/** 动作配置表 **/
	private ActionProfileXMLResponse actionBean;
	/** 动作列表是否为null 默认为True**/
	private boolean isHasAction = true;

	public boolean isHasAction() {
		return isHasAction;
	}

	public void setHasAction(boolean isHasAction) {
		this.isHasAction = isHasAction;
	}

	public ActionProfileXMLResponse getActionBean() {
		return actionBean;
	}

	public void setActionBean(ActionProfileXMLResponse actionBean) {
		this.actionBean = actionBean;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getmActionFileList() {
		return mActionFileList;
	}

	public void setmActionFileList(List<String> mActionFileList) {
		this.mActionFileList = mActionFileList;
	}
}
