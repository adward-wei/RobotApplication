package com.ubtechinc.alpha2ctrlapp.entity.business.shop;
/**
 * @ClassName RecommenedPageInfo
 * @date 5/17/2017
 * @author tanghongyu
 * @Description 广告推荐信息
 * @modifier
 * @modify_time
 */
public class RecommenedPageInfo {
	private String recommendForwardType;
	private int  recommendId;
	private String recommendUrl;
	private int recommendOrder;
	private int recommendType;
	private String recommendImage;
	private int recommendStatus;
	private String systemLanguage;

	public String getRecommendForwardType() {
		return recommendForwardType;
	}

	public void setRecommendForwardType(String recommendForwardType) {
		this.recommendForwardType = recommendForwardType;
	}

	public int getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(int recommendId) {
		this.recommendId = recommendId;
	}

	public String getRecommendUrl() {
		return recommendUrl;
	}

	public void setRecommendUrl(String recommendUrl) {
		this.recommendUrl = recommendUrl;
	}

	public int getRecommendOrder() {
		return recommendOrder;
	}

	public void setRecommendOrder(int recommendOrder) {
		this.recommendOrder = recommendOrder;
	}

	public int getRecommendType() {
		return recommendType;
	}

	public void setRecommendType(int recommendType) {
		this.recommendType = recommendType;
	}

	public String getRecommendImage() {
		return recommendImage;
	}

	public void setRecommendImage(String recommendImage) {
		this.recommendImage = recommendImage;
	}

	public int getRecommendStatus() {
		return recommendStatus;
	}

	public void setRecommendStatus(int recommendStatus) {
		this.recommendStatus = recommendStatus;
	}

	public String getSystemLanguage() {
		return systemLanguage;
	}

	public void setSystemLanguage(String systemLanguage) {
		this.systemLanguage = systemLanguage;
	}

	@Override
	public String toString() {
		return "RecommenedPageInfo{" +
				"recommendForwardType='" + recommendForwardType + '\'' +
				", recommendId=" + recommendId +
				", recommendUrl='" + recommendUrl + '\'' +
				", recommendOrder=" + recommendOrder +
				", recommendType=" + recommendType +
				", recommendImage='" + recommendImage + '\'' +
				", recommendStatus=" + recommendStatus +
				", systemLanguage='" + systemLanguage + '\'' +
				'}';
	}
}