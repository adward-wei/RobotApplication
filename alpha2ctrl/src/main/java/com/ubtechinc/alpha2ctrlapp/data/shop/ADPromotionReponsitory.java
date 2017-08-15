package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.data.user.MessageRepository;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.RecommenedPageInfo;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;


/**
 * @ClassName AdvertisingPromotionReponsitory
 * @date 5/23/2017
 * @author tanghongyu
 * @Description 广告推广信息数据仓库
 * @modifier
 * @modify_time
 */
public class ADPromotionReponsitory implements IADDataSource {
    private static ADPromotionReponsitory INSTANCE = null;

    private final IADDataSource iadDataSource;

    public ADPromotionReponsitory(IADDataSource iadDataSource) {
        this.iadDataSource = iadDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link MessageRepository} instance
     */
    public static ADPromotionReponsitory getInstance(IADDataSource iadDataSource
                                                ) {
        if (INSTANCE == null) {
            INSTANCE = new ADPromotionReponsitory(iadDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }








    @Override
    public void getAdvertisingPromotion(String language, final @NonNull ADDataCallback callback) {
        iadDataSource.getAdvertisingPromotion(language, new ADDataCallback() {

            @Override
            public void onLoadADData(List<RecommenedPageInfo> loginResponses) {
                callback.onLoadADData(loginResponses);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }
        });
    }
}
