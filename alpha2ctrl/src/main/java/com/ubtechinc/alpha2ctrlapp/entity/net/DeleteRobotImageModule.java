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
public class DeleteRobotImageModule {
    @Url("alpha2-web/image/deleteRobotImage")
    public class Request {
        //消息id以逗号分隔
        private String  imageIds;

        public String getImageIds() {
            return imageIds;
        }

        public void setImageIds(String imageIds) {
            this.imageIds = imageIds;
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
