package com.ubtechinc.alpha2ctrlapp.entity.request;

public class FeedBackRequest extends CommonRequest{
	private String appVersion;
	private String feedbackInfo;
	private String feedbackUser;
	private String robotHardVersion;
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getFeedbackInfo() {
		return feedbackInfo;
	}
	public void setFeedbackInfo(String feedbackInfo) {
		this.feedbackInfo = feedbackInfo;
	}
	public String getFeedbackUser() {
		return feedbackUser;
	}
	public void setFeedbackUser(String feedbackUser) {
		this.feedbackUser = feedbackUser;
	}
	public String getRobotHardVersion() {
		return robotHardVersion;
	}
	public void setRobotHardVersion(String robotHardVersion) {
		this.robotHardVersion = robotHardVersion;
	}
	
}
