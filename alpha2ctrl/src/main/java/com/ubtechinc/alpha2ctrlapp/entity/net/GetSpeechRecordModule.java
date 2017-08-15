package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RecordResultInfo;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName GetSpeechRecordModule
 * @date 6/28/2017
 * @author tanghongyu
 * @Description 获取语音记录
 * @modifier
 * @modify_time
 */
@Keep
public class GetSpeechRecordModule {
    @Url("alpha2-web/user/operate/getListByPage")
    @Keep
    public class Request {
        //应用名称
        private String userId;
        //应用包名（选填）
        private String robotId;
        //当前页
        private int page;
        //	每页大小
        private int pageSize;
        //标签id （中英互译时必传）
        private  String labelId;
        //	1：上一页（即下拉刷新获取上一页的数据） 0：下一页（即手机端向上滑动时获取下一页的数据）
        private int direction;
        //获取上一页时传当前列表记录中最大的id，获取下一页时传当前列表记录中最小的id
        private int id;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRobotId() {
            return robotId;
        }

        public void setRobotId(String robotId) {
            this.robotId = robotId;
        }

        public String getLabelId() {
            return labelId;
        }

        public void setLabelId(String labelId) {
            this.labelId = labelId;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
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

        List<RecordResultInfo> records;

        public List<RecordResultInfo> getRecords() {
            return records;
        }

        public void setRecords(List<RecordResultInfo> records) {
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
