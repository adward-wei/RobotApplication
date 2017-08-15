package com.ubtechinc.alpha2ctrlapp.data.robot;

/**
 * [排序基类]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2015-2-5 上午9:34:46
 * 
 **/

public class SortBaseModel {

	private String name; // 显示的数据
	private String sortLetters; // 显示数据拼音的首字母

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
