package com.ubtechinc.nlu.iflytekmix.pojos;

public class Result {
	//音乐的
	private String downloadUrl;
	private String singer;
	private String name;
	private String url;

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSinger() {
		return singer;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	//天气的
	private String date;
	private String city;
	private String wind;
	private String weather;
	private String tempRange;
	private String areaAddr;

	public String getAreaAddr() {
		return areaAddr;
	}

	public String getTempRange() {
		return tempRange;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCity() {
		return city;
	}

	public String getWind() {
		return wind;
	}

	public String getWeather() {
		return weather;
	}

	//诗
	private String author;
	private String title;
	private String content;
	private String appreciation;//诗介绍
	private String dynasty;//朝代

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getDynasty() {
		return dynasty;
	}
}
