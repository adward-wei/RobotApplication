package com.performance.ubt.sdkTest.ui;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.performance.ubt.sdkTest.R;
import com.ubtechinc.alpha.sdk.led.LedBright;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.sdk.led.LedRobotApi;
import com.ubtechinc.alpha.serverlibutil.aidl.LedInfo;
import com.ubtechinc.alpha.serverlibutil.interfaces.LedListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.LedOperationResultListener;

import java.util.List;

public class LedFragment extends BaseFragement implements View.OnClickListener {

    private static final String TAG = "ledTest";
    private Spinner spinner;
    private static int colorIndex = 1;
    final String colors[] = new String[]{"null", "红色", "绿色", "蓝色", "黄色", "品红", "青色", "白色", "黑色"};

    @Override
    protected void initView() {
        //初始化view
        initButton();
        LedRobotApi.get().initializ(mContext);

        //根据id获取对象
        spinner = mView.findViewById(R.id.select_color);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, colors);
        //设置显示的数据
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(1, true);

        //注册事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                colorIndex = position;
                Log.i(TAG, "colorIndex is " + colorIndex);
                TextView tv = (TextView) view;
                tv.setTextSize(16.0f);    //设置大小
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);   //设置居中
                Toast.makeText(mContext, "已选择" + spinner.getItemAtPosition(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(mContext, "没有改变的处理", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_led;
    }

    @Override
    protected void getDataFromServer() {
    }

    /**
     * 初始化button
     */
    private void initButton() {
        Button getLedListButton = (Button) mView.findViewById(R.id.getLedList);
        getLedListButton.setOnClickListener(this);
        Button turnOnEyeBlinkButton = (Button) mView.findViewById(R.id.turnOnEyeBlink);
        turnOnEyeBlinkButton.setOnClickListener(this);
        Button turnOffEyeBlinkButton = (Button) mView.findViewById(R.id.turnOffEyeBlink);
        turnOffEyeBlinkButton.setOnClickListener(this);
        Button turnOnEyeFlashButton = (Button) mView.findViewById(R.id.turnOnEyeFlash);
        turnOnEyeFlashButton.setOnClickListener(this);
        Button turnOffEyeFlashButton = (Button) mView.findViewById(R.id.turnOffEyeFlash);
        turnOffEyeFlashButton.setOnClickListener(this);
        Button turnOnEyeMarqueeButton = (Button) mView.findViewById(R.id.turnOnEyeMarquee);
        turnOnEyeMarqueeButton.setOnClickListener(this);
        Button turnOffEyeMarqueeButton = (Button) mView.findViewById(R.id.turnOffEyeMarquee);
        turnOffEyeMarqueeButton.setOnClickListener(this);
        Button turnOnHeadFlashButton = (Button) mView.findViewById(R.id.turnOnHeadFlash);
        turnOnHeadFlashButton.setOnClickListener(this);
        Button turnOffHeadFlashButton = (Button) mView.findViewById(R.id.turnOffHeadFlash);
        turnOffHeadFlashButton.setOnClickListener(this);
        Button turnOnHeadMarqueeButton = (Button) mView.findViewById(R.id.turnOnHeadMarquee);
        turnOnHeadMarqueeButton.setOnClickListener(this);
        Button turnOffHeadMarqueeButton = (Button) mView.findViewById(R.id.turnOffHeadMarquee);
        turnOffHeadMarqueeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.getLedList:
                getLedList();
                break;
            case R.id.turnOnEyeBlink:
                turnOnEyeBlink();
                break;
            case R.id.turnOffEyeBlink:
                turnOffEyeBlink();
                break;
            case R.id.turnOnEyeFlash:
                turnOnEyeFlash();
                break;
            case R.id.turnOffEyeFlash:
                turnOffEyeFlash();
                break;
            case R.id.turnOnEyeMarquee:
                turnOnEyeMarquee();
                break;
            case R.id.turnOffEyeMarquee:
                turnOffEyeMarquee();
                break;
            case R.id.turnOnHeadFlash:
                turnOnHeadFlash();
                break;
            case R.id.turnOffHeadFlash:
                turnOffHeadFlash();
                break;
            case R.id.turnOnHeadMarquee:
                turnOnHeadMarquee();
                break;
            case R.id.turnOffHeadMarquee:
                turnOffHeadMarquee();
                break;
            default:
                break;
        }

    }

    private void getLedList() {
        LedRobotApi.get().getLedList(new LedListResultListener() {
            @Override
            public void onGetLedList(int nOpId, int nErr, List<LedInfo> oArrLed) {
                Log.i(TAG, "getLedList return nOpId " + nOpId);
                for (LedInfo info : oArrLed) {
                    Log.i(TAG, "getLedId:" + info.getLedType());
                    for (int i = 0; i < info.getSupportColors().length; i++) {
                        Log.i(TAG, "getSupportColors:" + info.getSupportColors()[i]);
                    }
                    for (int i = 0; i < info.getSupportModes().length; i++) {
                        Log.i(TAG, "getSupportModes:" + info.getSupportModes()[i]);
                    }
                }
                Log.i(TAG, "getLedList return nErr " + nErr);
            }
        });
    }

    private void turnOnEyeBlink() {
        LedRobotApi.get().turnOnEyeBlink(new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOnEyeBlink nOpId " + nOpId);
                Log.i(TAG, "turnOnEyeBlink return " + nErr);
            }
        });
    }

    private void turnOffEyeBlink() {
        LedRobotApi.get().turnOffEyeBlink(new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffEyeBlink nOpId " + nOpId);
                Log.i(TAG, "turnOffEyeBlink return " + nErr);
            }
        });
    }

    private void turnOnEyeFlash() {
        LedRobotApi.get().turnOnEyeFlash(LedColor.valueOf(colorIndex), LedBright.FIVE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOnEyeFlash nOpId " + nOpId);
                Log.i(TAG, "turnOnEyeFlash return " + nErr);
            }
        });
    }

    private void turnOffEyeFlash() {
        LedRobotApi.get().turnOffEyeFlash(new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffEyeFlash nOpId " + nOpId);
                Log.i(TAG, "turnOffEyeFlash return " + nErr);
            }
        });
    }

    private void turnOnEyeMarquee() {
        LedRobotApi.get().turnOnEyeMarquee(LedColor.valueOf(colorIndex), LedBright.FIVE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOnEyeMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOnEyeMarquee return " + nErr);
            }
        });
    }

    private void turnOffEyeMarquee() {
        LedRobotApi.get().turnOffEyeMarquee(new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffEyeMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOffEyeMarquee return " + nErr);
            }
        });
    }


    private void turnOnHeadFlash() {
        LedRobotApi.get().turnOnHeadFlash(LedColor.valueOf(colorIndex), LedBright.FIVE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOnHeadFlash nOpId " + nOpId);
                Log.i(TAG, "turnOnHeadFlash return " + nErr);
            }
        });
    }

    private void turnOffHeadFlash() {
        LedRobotApi.get().turnOffHeadFlash(new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffHeadFlash nOpId " + nOpId);
                Log.i(TAG, "turnOffHeadFlash return " + nErr);
            }
        });
    }

    private void turnOnHeadMarquee() {
        LedRobotApi.get().turnOnHeadMarquee(LedColor.valueOf(colorIndex), LedBright.FIVE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOnHeadMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOnHeadMarquee return " + nErr);
            }
        });
    }

    private void turnOffHeadMarquee() {
        LedRobotApi.get().turnOffHeadMarquee(new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffHeadMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOffHeadMarquee nErr " + nErr);
            }
        });
    }
}
