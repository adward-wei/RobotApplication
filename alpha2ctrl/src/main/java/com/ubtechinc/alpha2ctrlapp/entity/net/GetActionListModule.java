package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName GetActionListModule
 * @date 6/7/2017
 * @author tanghongyu
 * @Description 获取动作列表（动作搜索通用）
 * @modifier
 * @modify_time
 */
@Keep
public class GetActionListModule {
    @Url("alpha2-web/action/getListByPage")
    @Keep
    public class Request {
        //动作名称（非必填）
        private String actionName;
        //动作类型动作类型(1 舞蹈 2 故事 3 基本 4 儿歌 5 科普 0:全部) 非必填
        private int actionSonType ;
        //当前页
        private int page;
        //	每页大小
        private int pagesize;


        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPagesize() {
            return pagesize;
        }

        public void setPagesize(int pagesize) {
            this.pagesize = pagesize;
        }

        public String getActionName() {
            return actionName;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }

        public int getActionSonType() {
            return actionSonType;
        }

        public void setActionSonType(int actionSonType) {
            this.actionSonType = actionSonType;
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

        List<ActionInfo> records;

        public List<ActionInfo> getRecords() {
            return records;
        }

        public void setRecords(List<ActionInfo> records) {
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
