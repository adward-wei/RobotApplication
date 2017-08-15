package com.ubtechinc.alpha.behavior;

import android.content.Context;

import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.motor.MoveAllAbsAngleOp;

/**
 * @desc : 机器人站起来
 * @author: wzt
 * @time : 2017/5/25
 * @modifier: logic.peng 数据存在溢出，应该定义为int数组，
 * @modify_time:
 */

public class RobotStandup {
    private Context mContext;

    public RobotStandup(Context context) {
        mContext = context;
    }

    public void start() {
        int[] dataMsg = {120, 205,120,120,35,120,120, 63, 145, 135,120,120, 177,95,105,120,120,120, 250,120};
        MoveAllAbsAngleOp moveAllAbsAngleOp = new MoveAllAbsAngleOp(dataMsg, (short) 800);
        RobotOpsManager.get(mContext).executeOp(moveAllAbsAngleOp);
    }
}
