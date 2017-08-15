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
public class BatchGetAppInfoModule {
    @Url("alpha2-web/app/findAppInfo")
    @Keep
    public class Request {
        //key之间已头号分割
        private String appKey;

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
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
    public class Data {
        private List<AppInfo> result;

        public List<AppInfo> getResult() {
            return result;
        }

        public void setResult(List<AppInfo> result) {
            this.result = result;
        }
    }

}
