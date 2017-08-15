package com.performance.ubt.sdkTest.ui;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.performance.ubt.sdkTest.R;
import com.ubtechinc.alpha.sdk.motion.MotionRobotApi;
import com.ubtechinc.alpha.serverlibutil.aidl.ActionInfo;
import com.ubtechinc.alpha.serverlibutil.aidl.MotorAngle;
import com.ubtechinc.alpha.serverlibutil.aidl.MotorInfo;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorMoveAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorReadAngleListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorSetAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.StopActonResultListener;

import java.util.List;

public class MotionFragment extends BaseFragement implements View.OnClickListener {

    private static final String TAG = "MotionTest";
    private static final String[] ActionName = {"avatar_at_ease", "avatar_fist_bump", "act16"};
    private short single_time = 900;

    @Override
    protected void initView() {
        //初始化view
        initButton();
        MotionRobotApi.get().initializ(mContext);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_motion;
    }

    @Override
    protected void getDataFromServer() {
    }

    /**
     * 初始化button
     */
    private void initButton() {
        Button getActListButton = (Button) mView.findViewById(R.id.getActList);
        getActListButton.setOnClickListener(this);
        Button playActButton = (Button) mView.findViewById(R.id.playAct);
        playActButton.setOnClickListener(this);
        Button stopActButton = (Button) mView.findViewById(R.id.stopAct);
        stopActButton.setOnClickListener(this);
        Button getMotorListButton = (Button) mView.findViewById(R.id.getMotorList);
        getMotorListButton.setOnClickListener(this);
        Button moveToAbsoluteAngleButton = (Button) mView.findViewById(R.id.moveToAbsoluteAngle);
        moveToAbsoluteAngleButton.setOnClickListener(this);
        Button moveRefAngleButton = (Button) mView.findViewById(R.id.moveRefAngle);
        moveRefAngleButton.setOnClickListener(this);
        Button readAbsoluteAngleButton = (Button) mView.findViewById(R.id.readAbsoluteAngle);
        readAbsoluteAngleButton.setOnClickListener(this);
        Button setMotorAbsoluteAnglesButton = (Button) mView.findViewById(R.id.setMotorAbsoluteAngles);
        setMotorAbsoluteAnglesButton.setOnClickListener(this);
        Button setPowerSaveModeButton = (Button) mView.findViewById(R.id.setPowerSaveMode);
        setPowerSaveModeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.getActList:
                getActionList();
                break;
            case R.id.playAct:
                playAction();
                break;
            case R.id.stopAct:
                stopAction();
                break;
            case R.id.getMotorList:
                getMotorList();
                break;
            case R.id.moveToAbsoluteAngle:
                moveToAbsoluteAngle();
                break;
            case R.id.moveRefAngle:
                moveRefAngle();
                break;
            case R.id.readAbsoluteAngle:
                readAbsoluteAngle();
                break;
            case R.id.setMotorAbsoluteAngles:
                setMotorAbsoluteAngles();
                break;
            case R.id.setPowerSaveMode:
                setPowerSaveMode();
                break;
        }

    }

    private void getActionList() {
        MotionRobotApi.get().getActionList(new ActionListResultListener() {
            @Override
            public void onGetActionList(int nOpId, int nErr, List<ActionInfo> onArrAction) {
                Log.i(TAG, "The Motion OpId:" + nOpId);
                int i = 0;
                for (ActionInfo info : onArrAction) {
                    Log.i(TAG, "getName is:" + info.getName());
                    Log.i(TAG, "getId is:" + info.getId());
                    Log.i(TAG, "getType is:" + info.getType());
                    Log.i(TAG, "getTime is:" + info.getTime());
                    i++;
                }
                Log.i(TAG, "getActionList return " + nErr);
                Log.i(TAG, "The Test Total Action-Files is" + i);
            }
        });
    }

    /*
    * play action from actionname
    * */
    private void playAction() {
        MotionRobotApi.get().playAction("move_forward", new ActionResultListener() {
            @Override
            public void onPlayActionResult(int nOpId, int nErr) {
                Log.i(TAG, "The Action OpId:" + nOpId);
                Log.i(TAG, "The Action nErr:" + nErr);
            }
        });
        //       stopAction();
    }

    /*
    * to stop play action
    * */
    private void stopAction() {
        MotionRobotApi.get().stopAction(new StopActonResultListener() {
            @Override
            public void onStopActionResult(int nErr) {
                Log.i(TAG, "nErr" + nErr);
            }
        });
    }

    /*
    * Get Motion List
    * */
    private void getMotorList() {
        Log.i(TAG, "Motor Message!");
        MotionRobotApi.get().getMotorList(new MotorListResultListener() {
            @Override
            public void onGetMotorList(int nOpId, int nErr, List<MotorInfo> onArrMotorInfo) {
                Log.i(TAG, "Plew");
                for (MotorInfo minfo : onArrMotorInfo) {
                    Log.i(TAG, "The Motion ID:" + minfo.getId());
                    Log.i(TAG, "The Motion Description:" + minfo.describeContents());
                    Log.i(TAG, "The Motion LowerLimitAngle" + minfo.getLowerLimitAngle());
                    Log.i(TAG, "The Motion UpperLimitAngle" + minfo.getUpperLimitAngle());
                    Log.i(TAG, "The Motion Torque" + minfo.getTorque());
                }
                Log.i(TAG, "The Motor Err Info" + nErr);
            }
        });
    }

    /*
    * moveToAbsoluteAngle
    * */
    private void moveToAbsoluteAngle() {
        MotionRobotApi.get().moveToAbsoluteAngle(5, 60, single_time, new MotorMoveAngleResultListener() {
            @Override
            public void onMoveAngle(int nOpId, int nErr, int nRadian) {
                Log.i(TAG, "The Motion nOpId:" + nOpId);
                Log.i(TAG, "The Motion nRadian:" + nRadian);
                Log.i(TAG, "The Motion nErr:" + nErr);
            }
        });
    }

    /*
    * moveRefAngle，Test Everyone motor callback result and the correct result
    * */
    private void moveRefAngle() {
        MotionRobotApi.get().moveRefAngle(5, 60, single_time, new MotorMoveAngleResultListener() {
            @Override
            public void onMoveAngle(int nOpId, int nErr, int nRadian) {
                Log.i(TAG, "The Motion nOpId:" + nOpId);
                Log.i(TAG, "The Motion nRadian:" + nRadian);
                Log.i(TAG, "The Motion nErr:" + nErr);
            }
        });
    }

    /*
    * readAbsoluteAngle,Test current motor angle
    * when you change the motor angle,and call back the correct
    * */
    private void readAbsoluteAngle() {
        MotionRobotApi.get().readAbsoluteAngle(13, false, new MotorReadAngleListener() {
            @Override
            public void onReadMotorAngle(int nOpId, int nErr, int angle) {
                Log.i(TAG, "The Motion nOpId:" + nOpId);
                Log.i(TAG, "The Motion nRadian:" + angle);
                Log.i(TAG, "The Motion nErr:" + nErr);
            }
        });
    }

    /*
    * setMotorAbsoluteAngles: Test a group motors
    * */
    private void setMotorAbsoluteAngles() {
        MotorAngle[] mas = new MotorAngle[3];
        mas[0] = new MotorAngle();
        mas[1] = new MotorAngle();
        mas[2] = new MotorAngle();
        mas[0].setId(13);
        mas[0].setAngle(175);
        mas[1].setId(6);
        mas[1].setAngle(120);
        mas[2].setId(7);
        mas[2].setAngle(120);
        Log.i(TAG, "Start setMotorAbsoluteAngle!");
        MotionRobotApi.get().setMotorAbsoluteAngles(mas, single_time, new MotorSetAngleResultListener() {
            @Override
            public void onSetMotorAngles(int nOpId, int nErr) {
                Log.i(TAG, "The Motion nOpId:" + nOpId);
                Log.i(TAG, "The Motion nErr:" + nErr);
            }
        });
    }

    /*
    * setPowerSaveMode
    * */
    private void setPowerSaveMode() {
        MotionRobotApi.get().setPowerSaveMode(true);
    }
}
