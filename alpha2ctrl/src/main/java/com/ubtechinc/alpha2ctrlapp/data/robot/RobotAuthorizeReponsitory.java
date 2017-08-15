package com.ubtechinc.alpha2ctrlapp.data.robot;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.ArrayMap;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.MD5Utils;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.AIDisConnectRobot;
import com.ubtechinc.alpha.AlConnectRobot;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.entity.net.BindRobotModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetBindHistoryListModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetRobotIMStatusModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetRobotListModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.UnBindRobotModule;
import com.ubtechinc.alpha2ctrlapp.im.Phone2RobotMsgMgr;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.im.IMErrorUtil;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName RobotAuthorizeReponsitory
 * @date 6/14/2017
 * @author tanghongyu
 * @Description 机器人绑定授权相关数据仓库
 * @modifier
 * @modify_time
 */
public class RobotAuthorizeReponsitory implements IRobotAuthorizeDataSource{
    private static final String TAG = "RobotAuthorizeReponsito";
    Handler mainHandler = new Handler(Looper.getMainLooper());
    private static RobotAuthorizeReponsitory INSTANCE = null;
    private ArrayMap<String, Integer> mRobotStateMap = new ArrayMap<>();
    public RobotInfo connectedRobot;
    private ArrayList<RobotInfo> mRobotList = new ArrayList<>();// 包含状态的机器人列表

    private RobotAuthorizeReponsitory() {
    }

    public static RobotAuthorizeReponsitory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotAuthorizeReponsitory();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void bindRobot(String robotNo, String userOnlyId, final @NonNull BindRobotCallback callback) {
        BindRobotModule.Request request = new BindRobotModule().new Request();
        request.setUserName(robotNo);
        request.setUserOnlyId(userOnlyId);
        HttpProxy.get().doPost(request, new ResponseListener<BindRobotModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(BindRobotModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess(response.getData().getResult().getMacAddress());
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }

    @Override
    public void unbindRobot(String equipmentId, int userId,final @NonNull UnBindRobotCallback callback) {
        UnBindRobotModule.Request request = new UnBindRobotModule().new Request();
        request.setEquipmentId(equipmentId);
        request.setUserId(userId);
        HttpProxy.get().doPost(request, new ResponseListener<UnBindRobotModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(UnBindRobotModule.Response response) {


                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess();
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }
            }


        });
    }

    @Override
    public void loadRobotList(final @NonNull LoadRobotListCallback callback) {
        final GetRobotListModule.Request request = new GetRobotListModule().new Request();
        HttpProxy.get().doGet(request ,new ResponseListener<GetRobotListModule.Response>() {

            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetRobotListModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onLoadRobotList(response.getData().getResult());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }

            }
        });
    }

    @Override
    public void loadHistoryRobotList(String userId,final @NonNull LoadRobotListCallback callback) {
        final GetBindHistoryListModule.Request request = new GetBindHistoryListModule().new Request();
        request.setUserId(userId);
        HttpProxy.get().doPost(request ,new ResponseListener<GetBindHistoryListModule.Response>() {

            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetBindHistoryListModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onLoadRobotList(response.getData().getResult());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }

            }
        });
    }

    public void connectRobot(String serialNo, @NonNull final IControlRobotCallback callback) {
        AlConnectRobot.AlConnectRobotRequest.Builder builder = AlConnectRobot.AlConnectRobotRequest.newBuilder();
        builder.setRobotId(serialNo).setUserId(Alpha2Application.getInstance().getUserId());
        AlConnectRobot.AlConnectRobotRequest request = builder.build();
        Phone2RobotMsgMgr.getInstance().sendData(IMCmdId.IM_CONNECT_ROBOT_REQUEST, "1", request, serialNo, new ICallback<AlphaMessageOuterClass.AlphaMessage>() {
            @Override
            public void onSuccess(AlphaMessageOuterClass.AlphaMessage alphaMessage) {
                Logger.d(TAG, "onSuccess---IM 模块 --控制机器人成功");

                if (alphaMessage != null && alphaMessage.getBodyData() != null) {
                    AlConnectRobot.AlConnectRobotResponse connectResponse = (AlConnectRobot.AlConnectRobotResponse) ProtoBufferDispose.unPackData(AlConnectRobot.AlConnectRobotResponse.class, alphaMessage.getBodyData().toByteArray());
                    if (connectResponse.getIsSuccess()) {
                        HandlerUtils.runUITask(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess();
                            }
                        });

                    } else {

                        HandlerUtils.runUITask(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(IMErrorUtil.handleException(0));
                            }
                        });

                    }
                }
            }

            @Override
            public void onError(final ThrowableWrapper e) {

                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
                Logger.d(TAG, "onError---IM 模块 --控制机器人失败 ", e);
            }


        });
    }

    /**
     * 断开与机器人的连接
     */
    public void disConnectRobot(String serialNo, @NonNull final IControlRobotCallback callback) {
        AIDisConnectRobot.AlDisConnectRobotRequest.Builder builder = AIDisConnectRobot.AlDisConnectRobotRequest.newBuilder();
        builder.setRobotId(serialNo).setUserId(Alpha2Application.getInstance().getUserId());
        AIDisConnectRobot.AlDisConnectRobotRequest request = builder.build();
        Phone2RobotMsgMgr.getInstance().sendData(IMCmdId.IM_DISCONNECT_ROBOT_REQUEST, "1", request, serialNo, new ICallback<AlphaMessageOuterClass.AlphaMessage>() {
            @Override
            public void onSuccess(AlphaMessageOuterClass.AlphaMessage alphaMessage) {
                Logger.d(TAG, "onSuccess---IM 模块 --断开与机器人的连接");
                if (alphaMessage != null && alphaMessage.getBodyData() != null) {
                    AIDisConnectRobot.AlDisConnectRobotResponse connectResponse = (AIDisConnectRobot.AlDisConnectRobotResponse) ProtoBufferDispose.unPackData(AIDisConnectRobot.AlDisConnectRobotResponse.class, alphaMessage.getBodyData().toByteArray());
                    if (connectResponse.getIsSuccess()) {
                        HandlerUtils.runUITask(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess();
                            }
                        });

                    } else {

                        HandlerUtils.runUITask(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(IMErrorUtil.handleException(0));
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(final ThrowableWrapper e) {

                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
                Logger.d(TAG, "onError---IM 模块 --断开与机器人的连接 ", e);

            }


        });
    }


    public void getRobotIMStatus(final List<String> robotSNList , final @NonNull GetRobotIMStateCallback callback) {

        Logger.i(TAG, "run getRobotIMStatus");
        long time = TimeUtils.getCurrentTimeInLong();
        String md5 = MD5Utils.md5("IM$SeCrET" + time, 32);
        GetRobotIMStatusModule.Request request = new GetRobotIMStatusModule().new Request();
        StringBuffer buffer = new StringBuffer();
        for (String sn : robotSNList) {
            buffer.append(sn);
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        request.setAccounts(buffer.toString());
        request.setSignature(md5);
        request.setTime(String.valueOf(time));
        request.setUserId(Alpha2Application.getAlpha2().getUserId());
        request.setChannel("A2");
        HttpProxy.get().doGet(request, new ResponseListener<GetRobotIMStatusModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetRobotIMStatusModule.Response response) {

                callback.onLoadRobotIMState(response.getReturnMap());

            }
        });
    }

}
