package com.ubtechinc.alpha2ctrlapp.entity.net;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName GetActionListModule
 * @date 6/7/2017
 * @author tanghongyu
 * @Description 获取最新的动作列表
 * @modifier
 * @modify_time
 */
public class GetLastActionListModule {
    @Url("alpha2-web/action/last")
    public class Request {

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

        List<ActionInfo> records;

        public List<ActionInfo> getRecords() {
            return records;
        }

        public void setRecords(List<ActionInfo> records) {
            this.records = records;
        }
    }
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
