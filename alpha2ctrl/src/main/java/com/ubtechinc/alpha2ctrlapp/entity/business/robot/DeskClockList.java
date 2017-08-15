package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.util.ArrayList;
import java.util.List;

/**
 * [提醒列表]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2015年10月15日 下午3:34:22
 * 
 **/

public class DeskClockList {
	/**
	 * wifi列表
	 */
	private List<DeskClock> list;

	public DeskClockList() {
		list = new ArrayList<DeskClock>();
	}

	public void addToList(DeskClock info) {
		list.add(info);
	}

	public List<DeskClock> getList() {
		return this.list;
	}

}
