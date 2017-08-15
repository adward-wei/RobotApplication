package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.CommentInfo;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName LoginModule
 * @date 6/3/2017
 * @author tanghongyu
 * @Description 登录请求和返回处理
 * @modifier
 * @modify_time
 */
@Keep
public class GetCommentListModule {
    @Url("alpha2-web/comment/getListByPage")
    @Keep
    public class Request {
        //
        private int actionId;

        public int getActionId() {
            return actionId;
        }

        public void setActionId(int actionId) {
            this.actionId = actionId;
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

        List<CommentInfo> records;

        public List<CommentInfo> getRecords() {
            return records;
        }

        public void setRecords(List<CommentInfo> records) {
            this.records = records;
        }
    }

    @Keep
    public class Data {
        private Result result;

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }

}
