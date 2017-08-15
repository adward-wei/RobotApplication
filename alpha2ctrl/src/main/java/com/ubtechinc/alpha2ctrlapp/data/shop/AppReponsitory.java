package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.data.user.MessageRepository;

/**
 * @author：tanghongyu
 * @date：4/13/2017 5:22 PM
 * @modifier：tanghongyu
 * @modify_date：4/13/2017 5:22 PM
 * [A brief description]
 * version
 */

public class AppReponsitory implements IAppDataSource {
    private static AppReponsitory INSTANCE = null;

    private final IAppDataSource iAppDataSource;

    public AppReponsitory(IAppDataSource iAppDataSource) {
        this.iAppDataSource = iAppDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link MessageRepository} instance
     */
    public static AppReponsitory getInstance(IAppDataSource iAppDataSource                           ) {
        if (INSTANCE == null) {
            INSTANCE = new AppReponsitory(iAppDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void loadAppList(int page, int pageSize, @NonNull AppListDataCallback callback) {
        iAppDataSource.loadAppList(page, pageSize, callback);
    }

    @Override
    public void loadRecentAppList(int page, int pageSize, @NonNull AppListDataCallback callback) {
        iAppDataSource.loadRecentAppList(page, pageSize, callback);
    }

    @Override
    public void searchApp(String appName, int page, int pageSize, @NonNull AppListDataCallback callback) {
        iAppDataSource.searchApp(appName, page, pageSize, callback);
    }

    @Override
    public void getAppDetail(int appId, @NonNull LoadAppDetailDataCallback callback) {
        iAppDataSource.getAppDetail(appId, callback);
    }

    @Override
    public void batchGetAppInfo(String appKeyList, @NonNull AppListDataCallback callback) {
        iAppDataSource.batchGetAppInfo(appKeyList, callback);
    }


}
