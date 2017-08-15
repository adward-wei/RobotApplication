package com.ubtechinc.alpha2ctrlapp.entity.net;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName FeedbackModule
 * @date 7/27/2017
 * @author tanghongyu
 * @Description 用户反馈
 * @modifier
 * @modify_time
 */
public class FeedbackModule {
    @Url("alpha2-web/system/feedback")
    public class Request {
        private String feedbackInfo;

        public String getFeedbackInfo() {
            return feedbackInfo;
        }

        public void setFeedbackInfo(String feedbackInfo) {
            this.feedbackInfo = feedbackInfo;
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
