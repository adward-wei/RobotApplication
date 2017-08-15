package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ImageModel;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName GetRobotImageModule
 * @date 6/27/2017
 * @author tanghongyu
 * @Description 获取机器人相册
 * @modifier
 * @modify_time
 */
@Keep
public class GetRobotImageModule {
    @Url("alpha2-web/image/findRobotImage")
    @Keep
    public class Request {
        private String robotSeq;
        private int pageSize;
        private int page;
        public String getRobotSeq() {
            return robotSeq;
        }

        public void setRobotSeq(String robotSeq) {
            this.robotSeq = robotSeq;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
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
        private List<ImageModel> records;

        public List<ImageModel> getRecords() {
            return records;
        }

        public void setRecords(List<ImageModel> records) {
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
