package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.RecommenedPageInfo;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName GetRecommendModule
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 获取推荐信息
 * @modifier
 * @modify_time
 */
@Keep
public class GetRecommendModule {
    @Url("alpha2-web/recommend/find")
    @Keep
    public class Request {

        private String systemLanguage;

        public String getSystemLanguage() {
            return systemLanguage;
        }

        public void setSystemLanguage(String systemLanguage) {
            this.systemLanguage = systemLanguage;
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
        private List<RecommenedPageInfo> result;

        public List<RecommenedPageInfo> getResult() {
            return result;
        }

        public void setResult(List<RecommenedPageInfo> result) {
            this.result = result;
        }
    }

}
