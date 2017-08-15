package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.user.MyFavoriteInfo;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName GetCollectListModule
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 获取收藏列表
 * @modifier
 * @modify_time
 */
@Keep
public class GetCollectListModule {
    @Url("alpha2-web/collect/find")
    @Keep
    public class Request {
        private int appType ;

        public int getAppType() {
            return appType;
        }

        public void setAppType(int appType) {
            this.appType = appType;
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
        private  List<MyFavoriteInfo> result;

        public  List<MyFavoriteInfo> getResult() {
            return result;
        }

        public void setResult( List<MyFavoriteInfo> result) {
            this.result = result;
        }
    }

}
