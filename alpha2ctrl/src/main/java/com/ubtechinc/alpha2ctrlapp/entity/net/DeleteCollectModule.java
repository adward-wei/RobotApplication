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
public class DeleteCollectModule {
    @Url("alpha2-web/collect/delete")
    @Keep
    public class Request {
        //收藏的id
        private int collectId;

        public int getCollectId() {
            return collectId;
        }

        public void setCollectId(int collectId) {
            this.collectId = collectId;
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
