package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;

import java.util.Map;

/**
 * @ClassName ActionRepository
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 动作数据仓库
 * @modifier
 * @modify_time
 */

public class ActionRepository implements IActionDataSource {

    private static ActionRepository INSTANCE = null;
    private ActionLocalDataSource actionLocalDataSource;
    private ActionRemoteDataSource actionRemoteDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, ActionInfo> mCachedTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private ActionRepository(@NonNull ActionLocalDataSource actionLocalDataSource,
                             @NonNull ActionRemoteDataSource actionRemoteDataSource) {
        this.actionRemoteDataSource = actionRemoteDataSource;
        this.actionLocalDataSource = actionLocalDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param actionLocalDataSource the backend data source
     * @param actionRemoteDataSource  the device storage data source
     * @return the {@link ActionRepository} instance
     */
    public static ActionRepository getInstance(@NonNull ActionLocalDataSource actionLocalDataSource,
                                               @NonNull ActionRemoteDataSource actionRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ActionRepository(actionLocalDataSource, actionRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }




    @Override
    public void getLastActionList(int page, int pageSize, @NonNull LoadActionCallback callback) {
        actionRemoteDataSource.getLastActionList(page, pageSize, callback);
    }

    @Override
    public void searchAction(String actionName, int actionSonType, int page, int pageSize, @NonNull LoadActionCallback callback) {
        actionRemoteDataSource.searchAction(actionName, actionSonType, page, pageSize, callback);
    }

    @Override
    public void getActionList(int actionSonType, int page, int pageSize, @NonNull LoadActionCallback callback) {
        actionRemoteDataSource.getActionList( actionSonType, page, pageSize, callback);
    }

    @Override
    public void getActionDetail(int actionId, @NonNull LoadActionDetailCallback callback) {
        actionRemoteDataSource.getActionDetail(actionId, callback);
    }

    @Override
    public void praiseAction( String praiseObjectId, @NonNull PraiseActionCallback callback) {
        actionRemoteDataSource.praiseAction( praiseObjectId,callback );
    }

    @Override
    public void getShareUrl(String code, String type, @NonNull LoadShareUrlCallback callback) {
        actionRemoteDataSource.getShareUrl(code, type, callback);
    }
}
