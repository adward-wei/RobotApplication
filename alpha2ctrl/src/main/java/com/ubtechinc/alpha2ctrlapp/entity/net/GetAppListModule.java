package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
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
public class GetAppListModule {
    @Url("alpha2-web/app/findHost")
    @Keep
    public class Request {
        //应用名称
        private String appName;
        //应用状态·(固定值为3)
        private int appStatus = 3;
        //应用包名（选填）
        private String packageName;
        //当前页
        private int page;
        //	每页大小
        private int pagesize;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public int getAppStatus() {
            return appStatus;
        }

        public void setAppStatus(int appStatus) {
            this.appStatus = appStatus;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

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

        List<AppInfo> records;

        public List<AppInfo> getRecords() {
            return records;
        }

        public void setRecords(List<AppInfo> records) {
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
