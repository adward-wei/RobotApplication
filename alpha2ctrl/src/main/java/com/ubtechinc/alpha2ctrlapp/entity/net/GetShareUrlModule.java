package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.nets.http.Url;

/**
 * @ClassName GetActionDetailModule
 * @date 6/7/2017
 * @author tanghongyu
 * @Description 获取动作详情
 * @modifier
 * @modify_time
 */
@Keep
public class GetShareUrlModule {
    @Url("alpha2-web/system/share")
    @Keep
    public class Request {
        private String  code ;
        private String  type ;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
        String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

}
