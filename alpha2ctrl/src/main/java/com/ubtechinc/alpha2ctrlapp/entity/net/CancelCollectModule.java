package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName DeleteCollectModule
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 取消收藏
 * @modifier
 * @modify_time
 */
@Keep
public class CancelCollectModule {
    @Url("alpha2-web/collect/cancel")
    @Keep
    public class Request {
        //收藏的关联id
        private int collectRelationId;
        //收藏的类型
        private int collectType;
        private int collectUserId;
        public int getCollectRelationId() {
            return collectRelationId;
        }

        public void setCollectRelationId(int collectRelationId) {
            this.collectRelationId = collectRelationId;
        }

        public int getCollectType() {
            return collectType;
        }

        public void setCollectType(int collectType) {
            this.collectType = collectType;
        }

        public int getCollectUserId() {
            return collectUserId;
        }

        public void setCollectUserId(int collectUserId) {
            this.collectUserId = collectUserId;
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
