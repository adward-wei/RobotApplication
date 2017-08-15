package com.ubtechinc.alpha.network.module;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */
@Keep
public class UpdateControlModule {
    @Url("alpha2-web/robot/updateControl")
    @Keep
    public  static class Request{


        private String equipmentId ;
        private String controlUserId ;


        public String getEquipmentId() {
            return equipmentId;
        }

        public void setEquipmentId(String equipmentId) {
            this.equipmentId = equipmentId;
        }

        public String getControlUserId() {
            return controlUserId;
        }

        public void setControlUserId(String controlUserId) {
            this.controlUserId = controlUserId;
        }

    }

    @Keep
    public  class Response{
        public String resultCode;
        public String msg;
        @Override
        public String toString() {
            return "Result{" +
                    "resultCode='" + resultCode + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }


}
