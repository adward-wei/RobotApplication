package com.ubtechinc.alpha2ctrlapp.data.robot;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.net.DeleteRobotImageModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetRobotImageModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @ClassName RobotGalleryRepository
 * @date 6/27/2017
 * @author tanghongyu
 * @Description 机器人相册
 * @modifier
 * @modify_time
 */
public class RobotGalleryRepository {

    private static RobotGalleryRepository INSTANCE = null;

    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private RobotGalleryRepository() {

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RobotGalleryRepository} instance
     */
    public static RobotGalleryRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotGalleryRepository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    public void getGallery(String robotSerialNo, int page, int pageSize, @NonNull final IRobotGalleryDataSource.GetGalleryCallback callback) {
        GetRobotImageModule.Request request = new GetRobotImageModule().new Request();
        request.setRobotSeq(robotSerialNo);
        request.setPageSize(pageSize);
        request.setPage(page);
        HttpProxy.get().doGet(request, new ResponseListener<GetRobotImageModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetRobotImageModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onGallery(response.getData().getResult().getRecords());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }

    public void deleteImage(String imageIds, @NonNull final IRobotGalleryDataSource.DeleteImageCallback callback){
        DeleteRobotImageModule.Request request = new DeleteRobotImageModule().new Request();
        request.setImageIds(imageIds);
        HttpProxy.get().doPost(request, new ResponseListener<GetRobotImageModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(GetRobotImageModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess();
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }




}
