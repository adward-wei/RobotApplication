package com.alpha2.camera;

import com.alpha2.camera.arcsoft.dam.DamEngine;
import com.alpha2.camera.utils.RobotValues;

public class DamEngineModel {
	
	private static int model = RobotValues.FACE_TRACK;

	public static synchronized int getModel() {
		return model;
	}

	public static synchronized void setModle(int index) {

		switch (index) {
		case RobotValues.FACE_TRACK://0人脸跟随
			DamEngine.setModelEnable(0);// 人脸
			break;
		case RobotValues.HAND_TRACK:
			DamEngine.setModelEnable((1 << 1));// 手势
			break;
		case RobotValues.HANDV_POSE:
			DamEngine.setModelEnable(1 << 5);// V手势
			break;
		case RobotValues.FACE_POSE://3人脸拍照
			DamEngine.setModelEnable(0);// 人脸
			break;
		default:
			break;
		}
		model = index;
	}
}
