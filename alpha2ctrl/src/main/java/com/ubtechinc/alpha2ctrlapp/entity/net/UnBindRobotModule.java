package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName BindRobotModule
 * @date 6/7/2017
 * @author tanghongyu
 * @Description 解除绑定机器人
 @Url("relation/remove")
 * @modifier
 * @modify_time
 */
@Keep
public class UnBindRobotModule {
    @Url("alpha2-web/relation/remove")
    @Keep
    public class Request {
        //机器人序列号
        private String equipmentId;
        //绑定人id
        private int userId;

        public String getEquipmentId() {
            return equipmentId;
        }

        public void setEquipmentId(String equipmentId) {
            this.equipmentId = equipmentId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    @Keep
    public class Response extends BaseResponse {

        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    @Keep
    public class Result {


    }

    @Keep
    public class Data {

    }

}
