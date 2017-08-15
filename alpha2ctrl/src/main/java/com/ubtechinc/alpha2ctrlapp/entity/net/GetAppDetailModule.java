package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.AppDetail;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName LoginModule
 * @date 6/3/2017
 * @author tanghongyu
 * @Description 获取App详情
 * @modifier
 * @modify_time
 */
@Keep
public class GetAppDetailModule {
    @Url("alpha2-web/app/findLinkApp")
    @Keep
    public class Request {
        private int appId ;

        public int getAppId() {
            return appId;
        }

        public void setAppId(int appId) {
            this.appId = appId;
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
        List<AppDetail> result;

        public List<AppDetail> getResult() {
            return result;
        }

        public void setResult(List<AppDetail> result) {
            this.result = result;
        }
    }

}
