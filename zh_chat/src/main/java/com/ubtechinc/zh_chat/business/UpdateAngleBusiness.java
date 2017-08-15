package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;

/**
 * <pre>
 *   author: Logic
 *   email : 2091843903@qq.com
 *   time  : 2017/3/27
 *   desc  : 调整角度
 * </pre>
 */

public class UpdateAngleBusiness extends BaseBusiness {

    private byte angle;

    public void setAngle(byte angle) {
        this.angle = angle;
    }

    public UpdateAngleBusiness(Context cxt) {
        super(cxt);
    }

    @Override
    public void start(UBTSemanticRootProxy handle) {
//        int angleINT = angle;
//        int angleHight = (angleINT << 8);
//        if (angleINT < 0) {
//            angleINT = 256 + angleINT;
//        }
//        handle.stat_FreeAngle((byte) 19, angleINT, (short) 500);
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {

    }
}
