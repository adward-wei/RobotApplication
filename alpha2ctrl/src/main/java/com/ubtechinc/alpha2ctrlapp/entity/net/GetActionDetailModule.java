package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionDetail;
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
public class GetActionDetailModule {
    @Url("alpha2-web/action/detailById")
    @Keep
    public class Request {
        private int actionId ;

        public int getActionId() {
            return actionId;
        }

        public void setActionId(int actionId) {
            this.actionId = actionId;
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
        ActionDetail result;

        public ActionDetail getResult() {
            return result;
        }

        public void setResult(ActionDetail result) {
            this.result = result;
        }
    }

}
