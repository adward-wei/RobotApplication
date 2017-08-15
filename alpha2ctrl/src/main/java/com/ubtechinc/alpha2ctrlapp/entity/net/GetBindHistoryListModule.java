package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName GetActionListModule
 * @date 6/7/2017
 * @author tanghongyu
 * @Description 获取机器人列表
 * @modifier
 * @modify_time
 */
@Keep
public class GetBindHistoryListModule {
    @Url("alpha2-web/relation/getHistory")
    @Keep
    public class Request {
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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
        private List<RobotInfo> result;

        public List<RobotInfo> getResult() {
            return result;
        }

        public void setResult(List<RobotInfo> result) {
            this.result = result;
        }
    }

}
