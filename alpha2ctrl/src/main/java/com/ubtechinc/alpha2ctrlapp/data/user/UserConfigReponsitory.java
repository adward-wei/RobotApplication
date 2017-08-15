package com.ubtechinc.alpha2ctrlapp.data.user;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.ubtech.utilcode.utils.JsonUtils;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.UserInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.EditUserInfoRequest;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.entity.net.EditUserInfoModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.FeedbackModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetMessageListModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetRobotListModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.UserDataInitModule;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.http.Url;
import com.ubtechinc.nets.utils.JsonUtil;

import java.util.List;


/**
 * @ClassName UserConfigReponsitory
 * @date 6/8/2017
 * @author tanghongyu
 * @Description 用户配置信息(合并请求)
 * @modifier
 * @modify_time
 */
public class UserConfigReponsitory implements IUserConfigDataSource{
    private RobotInfo mConnectedRobot;
    private UserInfo mCurrentUser;
    //使用volatile关键字保其可见性
    volatile private static UserConfigReponsitory instance = null;
    private UserConfigReponsitory(){}

    public static UserConfigReponsitory get() {
        try {
            if(instance != null){//懒汉式

            }else{
                //创建实例之前可能会有一些准备性的耗时工作
                Thread.sleep(300);
                synchronized (UserConfigReponsitory.class) {
                    if(instance == null){//二次检查
                        instance = new UserConfigReponsitory();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public RobotInfo getConnectedRobot() {
        return mConnectedRobot;
    }

    public void setConnectedRobot(RobotInfo mConnectedRobot) {
        this.mConnectedRobot = mConnectedRobot;
    }

    public UserInfo getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(UserInfo mCurrentUser) {
        this.mCurrentUser = mCurrentUser;
    }

    public boolean isRebotConnected() {
        return  this.mConnectedRobot != null;
    }


    @Override
    public void getUserConfigData(final @NonNull LoadUserConfigCallback callback) {
        final UserDataInitModule.Request request = new UserDataInitModule().new Request();
        GetRobotListModule.Request robotRequset = new GetRobotListModule().new Request();
        GetMessageListModule.Request messageRequest = new GetMessageListModule().new Request();
        String robotUrl = GetRobotListModule.Request.class.getAnnotation(Url.class).value();
        String messageUrl = GetMessageListModule.Request.class.getAnnotation(Url.class).value();
        String robotRequsetStr = JsonUtil.object2Json(robotRequset);
        String messageRequestStr = JsonUtil.object2Json(messageRequest);

        request.setContentType(JsonUtils.array2Json(ImmutableList.of(BusinessConstants.CONTENT_TYPE,BusinessConstants.CONTENT_TYPE )));
        request.setUri(JsonUtils.array2Json(ImmutableList.of(robotUrl,messageUrl )));
        request.setHeaders("");
        request.setParameters(JsonUtils.array2Json(ImmutableList.of(robotRequsetStr,messageRequestStr )));
        request.setRequestMethod(JsonUtils.array2Json(ImmutableList.of(BusinessConstants.REQUEST_METHOD,BusinessConstants.REQUEST_METHOD )));

        HttpProxy.get().doPost(request, new ResponseListener<UserDataInitModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataAvailable(e);
            }

            @Override
            public void onSuccess(UserDataInitModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    List<UserDataInitModule.Result> results =  response.getData();
                    List<RobotInfo> robotList = null;
                    List<NoticeMessage> messageList = null;
                    int index = 0;
                    for(UserDataInitModule.Result result : results) {

                        if(index == 0) {
                            robotList = JsonUtils.jsonToBean(result.getResult(), GetRobotListModule.Response.class).getData().getResult();
                        }else {
                            messageList =  JsonUtils.jsonToBean(result.getResult(), GetMessageListModule.Response.class).getData().getResult().getRecords();
                        }
                        index++;

                    }

                    callback.onLoadUserConfigData(robotList,messageList);
                }else {
                    callback.onDataAvailable(ErrorParser.get().getThrowableWrapper());
                }


            }
        });
    }

    @Override
    public void modifyUserInfo(EditUserInfoRequest editUserInfoRequest, final @NonNull ModifyUserInfoCallback callback) {
        EditUserInfoModule.Request request = new EditUserInfoModule().new Request();
        BeanUtils.copyBean(editUserInfoRequest , request);
        HttpProxy.get().doPost(request, new ResponseListener<EditUserInfoModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(EditUserInfoModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess(response.getData().getResult());
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }



            }
        });
    }

    @Override
    public void feedback(String comment,final  @NonNull FeedbackCallback callback) {
        FeedbackModule.Request request = new FeedbackModule().new Request();
        request.setFeedbackInfo(comment);
        HttpProxy.get().doPost(request, new ResponseListener<FeedbackModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(FeedbackModule.Response response) {
                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess();
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }



            }
        });
    }


}
