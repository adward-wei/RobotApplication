package com.ubtechinc.alpha2ctrlapp.entity.business.app;

import java.util.List;

/**
 * [A brief description]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2015年9月16日 下午7:57:34
 * 
 **/

public class AppItemGroup {
	private String marginTop;

	public String getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(String marginTop) {
		this.marginTop = marginTop;
	}

	public List<AppConfigItem> getItem() {
		return item;
	}

	public void setItem(List<AppConfigItem> item) {
		this.item = item;
	}

	private List<AppConfigItem> item;

}
