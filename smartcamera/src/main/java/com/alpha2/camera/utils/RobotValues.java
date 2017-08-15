package com.alpha2.camera.utils;

public class RobotValues {
	/**
	 * 运行在机器人上标识
	 */
	public static boolean ROBOT_RUN = true;
	public static final int FACE_TRACK = 0;// 人脸跟随
	public static final int HAND_TRACK = 1;// 手势跟随
	public static final int HANDV_POSE = 2;// V手势拍照
	public static final int FACE_POSE = 3;// 人脸拍照

	public enum PoseState {
		IDLE, PLAYING, POSING
	}
}
