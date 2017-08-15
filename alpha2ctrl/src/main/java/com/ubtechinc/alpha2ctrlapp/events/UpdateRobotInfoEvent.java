package com.ubtechinc.alpha2ctrlapp.events;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
/*
* 机器人设置修改名称 头像后刷新UI事件
* */
public class UpdateRobotInfoEvent {

	private RobotInfo robotInfo;

	public UpdateRobotInfoEvent(RobotInfo robotInfo) {
		this.robotInfo = robotInfo;
	}

	public RobotInfo getRobotInfo() {
		return robotInfo;
	}

	public void setRobotInfo(RobotInfo robotInfo) {
		this.robotInfo = robotInfo;
	}
}
