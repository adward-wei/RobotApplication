package com.ubtechinc.alpha2ctrlapp.util;

import com.ubtechinc.alpha2ctrlapp.data.robot.SortBaseModel;

import java.util.Comparator;

/**
 * [根据拼音来排列ListView里面的数据类]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2015-2-5 上午9:37:24
 * 
 **/

public class PinyinComparatorUtils implements Comparator<SortBaseModel> {

	public int compare(SortBaseModel o1, SortBaseModel o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
