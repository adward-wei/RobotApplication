package com.ubtechinc.nlu.iflytekmix.pojos;

/**
 * 网络语法pojo
 */
public class Slots {
	private String direction;
	private String target;
	private String name;
	private String source;
	private String content;

	private String steps;
	private Location location;
	private Datetime datetime;
	private boolean isNLPAvailable;// 判断意义结果有效与否

	private String repeat;//闹钟重复
	//动作
	private String left;
	private String right;

	private String mode;

	private String song;
	private String artist;

	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
	}

	public String getRepeat() {
		return repeat;
	}

	public boolean isNLPAvailable() {
		if (datetime != null) {
			isNLPAvailable = true;
		}
		return isNLPAvailable;
	}

	public Datetime getDatetime() {
		return datetime;
	}

	public Location getLocation() {
		return location;
	}

	public String getSteps() {
		return steps;
	}

	public String getArtist() {
		return artist;
	}

	public String getSong() {
		return song;
	}

	public String getDirection() {
		return direction;
	}

	public String getTarget() {
		return target;
	}

	public String getSource() {
		return source;
	}

	public String getContent() {
		return content;
	}

	public String getName() {
		return name;
	}

	public String getMode() {
		return mode;
	}

}
