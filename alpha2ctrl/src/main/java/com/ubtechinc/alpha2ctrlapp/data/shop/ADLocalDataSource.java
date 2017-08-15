package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;


/**
 * @author：tanghongyu
 * @date：4/7/2017 3:36 PM
 * @modifier：tanghongyu
 * @modify_date：4/7/2017 3:36 PM
 * [A brief description]
 * version
 */

public class ADLocalDataSource implements IADDataSource {

    private static ADLocalDataSource INSTANCE;
    Context context;
    private ADLocalDataSource(@NonNull Context context) {
       this.context = context;
    }
    public static ADLocalDataSource getInstance(@NonNull Context context) {
        Preconditions.checkNotNull(context);
        if (INSTANCE == null) {
            INSTANCE = new ADLocalDataSource(context);
        }
        return INSTANCE;
    }







    @Override
    public void getAdvertisingPromotion(String language, @NonNull ADDataCallback callback) {

    }
}
