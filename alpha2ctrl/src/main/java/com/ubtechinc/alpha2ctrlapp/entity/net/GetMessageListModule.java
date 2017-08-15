package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName GetMessageListModule
 * @date 6/8/2017
 * @author tanghongyu
 * @Description 获取消息列表
 * @modifier
 * @modify_time
 */
@Keep
public class GetMessageListModule {
    @Url("alpha2-web/notice/find")
    @Keep
    public class Request {
        //当前页的最大id（第二次刷新是传）
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        List<NoticeMessage> records;

        public List<NoticeMessage> getRecords() {
            return records;
        }

        public void setRecords(List<NoticeMessage> records) {
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
