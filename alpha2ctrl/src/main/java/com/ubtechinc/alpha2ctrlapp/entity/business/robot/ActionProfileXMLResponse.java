package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.io.Serializable;

/**
 * [动作配置文件xml解析这样的字段direction_b时候会出现 null 改为 directionB]
 * 
 * @author devin.hu
 * @version 1.0
 * @date 2013-9-30
 * 
 **/
public class ActionProfileXMLResponse implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = -8167727836628076453L;
	private String directionA;
	private String directionB;
	private String directionC;
	private String directionD;
	private String directionDown;
	private String directionLeft;
	private String directionRight;
	private String directionUp;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "direction_left" + directionLeft + " direction_up" + directionUp
				+ " direction_right" + directionRight + " direction_down"
				+ directionDown + " direction_a" + directionD + " direction_b"
				+ directionB + " direction_c" + directionC + " direction_d"
				+ directionD + "";
	}

	public String getDirectionA() {
		return directionA;
	}

	public void setDirectionA(String directionA) {
		this.directionA = directionA;
	}

	public String getDirectionB() {
		return directionB;
	}

	public void setDirectionB(String directionB) {
		this.directionB = directionB;
	}

	public String getDirectionC() {
		return directionC;
	}

	public void setDirectionC(String directionC) {
		this.directionC = directionC;
	}

	public String getDirectionD() {
		return directionD;
	}

	public void setDirectionD(String directionD) {
		this.directionD = directionD;
	}

	public String getDirectionDown() {
		return directionDown;
	}

	public void setDirectionDown(String directionDown) {
		this.directionDown = directionDown;
	}

	public String getDirectionLeft() {
		return directionLeft;
	}

	public void setDirectionLeft(String directionLeft) {
		this.directionLeft = directionLeft;
	}

	public String getDirectionRight() {
		return directionRight;
	}

	public void setDirectionRight(String directionRight) {
		this.directionRight = directionRight;
	}

	public String getDirectionUp() {
		return directionUp;
	}

	public void setDirectionUp(String directionUp) {
		this.directionUp = directionUp;
	}

}
