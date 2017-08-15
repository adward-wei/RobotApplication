package com.ubtechinc.alpha2ctrlapp.entity.net;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName DeleteCollectModule
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 取消收藏
 * @modifier
 * @modify_time
 */
public class PraiseActionModule {
    @Url("alpha2-web/action/praise")
    public class Request {
        //1:aplha1 2：alpha2 3:pc端	nu
        private int appType;
        //	点赞的对象id
        private String praiseObjectId;
        //点赞类型(1：动作 2：应用)
        private int praiseType;

        public int getAppType() {
            return appType;
        }

        public void setAppType(int appType) {
            this.appType = appType;
        }

        public String getPraiseObjectId() {
            return praiseObjectId;
        }

        public void setPraiseObjectId(String praiseObjectId) {
            this.praiseObjectId = praiseObjectId;
        }

        public int getPraiseType() {
            return praiseType;
        }

        public void setPraiseType(int praiseType) {
            this.praiseType = praiseType;
        }
    }

    public class Response extends BaseResponse {

        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }
    public class Result {


    }
    public class Data {

    }

}
