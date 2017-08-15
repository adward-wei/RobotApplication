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
public class DeleteNoticeModule {
    @Url("alpha2-web/notice/delete")
    public class Request {
        //消息的id
        private String noticeIds;

        public String getNoticeIds() {
            return noticeIds;
        }

        public void setNoticeIds(String noticeIds) {
            this.noticeIds = noticeIds;
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
