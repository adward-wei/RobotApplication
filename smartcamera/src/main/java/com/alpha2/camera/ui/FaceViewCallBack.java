package com.alpha2.camera.ui;

import java.nio.ByteBuffer;

/**
 * Created by ubt on 2016/9/5.
 */
public interface FaceViewCallBack {

        public void onRobotAngle(int type,int x, int y);

        public void onTTS(int result);

        public void onDetectFace(ByteBuffer data, int width, int height);
}
