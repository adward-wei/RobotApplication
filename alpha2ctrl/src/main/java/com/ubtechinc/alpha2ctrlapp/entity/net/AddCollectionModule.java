package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName AddCollectModule
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 添加收藏
 * @modifier
 * @modify_time
 */
@Keep
public class AddCollectionModule {
    @Url("alpha2-web/collect/add")
    @Keep
    public class Request {
        //收藏的关联id
        private int collectRelationId;
        //收藏的类型
        private int collectType;

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
