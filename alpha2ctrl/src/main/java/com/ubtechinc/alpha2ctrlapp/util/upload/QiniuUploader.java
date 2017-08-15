package com.ubtechinc.alpha2ctrlapp.util.upload;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetTokenModule;
import com.ubtechinc.alpha2ctrlapp.util.UnitUtils;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * @desc : 七牛上传工具
 * @author: wzt
 * @time : 2017/6/21
 * @modifier:
 * @modify_time:
 */

public class QiniuUploader implements IUploader {
    private static final String TAG = "QiniuUploader";
    //    private final static String PICTURE_ADDRESS="http://video.ubtrobot.com";
    private final static String KEY_USER_PHOTO = "user/photos";
    private final static String KEY_HEAD_IMAGE = "user/headimage";
    private final static String KEY_ROBOT_HEAD = "robot/headimage";
    private static QiniuUploader sQiniuUploader;

    private long uploadLastTimePoint;
    private long uploadLastOffset;
    private long uploadFileLength;
    private String uploadFilePath;


    private UploadManager uploadManager;

    private QiniuUploader() {
    }

    public static QiniuUploader get() {
        if (sQiniuUploader == null) {
            synchronized (QiniuUploader.class) {
                if (sQiniuUploader == null)
                    sQiniuUploader = new QiniuUploader();
            }
        }

        return sQiniuUploader;
    }


    @Override
    public void upload(final UploadCBHandler uploadCBHandler, final IUploadResultListener listener) {
        if (TextUtils.isEmpty(uploadCBHandler.filePath)) {
            return;
        }
        this.uploadFilePath = uploadCBHandler.filePath;
        //从业务服务器获取上传凭证
        getQiniuToken(uploadCBHandler, listener);
    }

    private void getQiniuToken(final UploadCBHandler uploadCBHandler, final IUploadResultListener listener) {
        GetTokenModule.Request request = new GetTokenModule.Request();
        request.setFileType(uploadCBHandler.type);
        HttpProxy.get().doGet(request, new ResponseListener<GetTokenModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                listener.onUploadFail(e.getMessage(), uploadCBHandler);
                Logger.t(TAG).w("requestGetToken  error");
            }

            @Override
            public void onSuccess(GetTokenModule.Response response) {
                String qiniuToken = response.data.result.token;

                upload(response.data.result.domain, qiniuToken, uploadCBHandler, listener);
            }
        });
    }

    private void upload(final String domain, String uploadToken, final UploadCBHandler uploadCBHandler, final IUploadResultListener listener) {
        if (this.uploadManager == null) {
            this.uploadManager = new UploadManager();
        }

        File uploadFile = new File(this.uploadFilePath);
        UploadOptions uploadOptions = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        updateStatus(percent);
                    }
                }, null);
        final long startTime = System.currentTimeMillis();
        final long fileLength = uploadFile.length();
        this.uploadFileLength = fileLength;
        this.uploadLastTimePoint = startTime;
        this.uploadLastOffset = 0;
        writeLog("0 %   0 KB/s  " + UnitUtils.formatSize(fileLength));
        writeLog("...");

        //key相当于服务器上的路径
        String key = createKey(uploadCBHandler.type);
        this.uploadManager.put(uploadFile, key, uploadToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo respInfo,
                                         JSONObject jsonData) {

                        long lastMillis = System.currentTimeMillis()
                                - startTime;
                        if (respInfo.isOK()) {
                            try {
                                String fileKey = jsonData.getString("key");
                                String fileHash = jsonData.getString("hash");
                                writeLog("File Size: "
                                        + UnitUtils.formatSize(uploadFileLength));
                                writeLog("File Key: " + fileKey);
                                writeLog("File Hash: " + fileHash);
                                writeLog("Last Time: "
                                        + UnitUtils.formatMilliSeconds(lastMillis));
                                writeLog("Average Speed: "
                                        + UnitUtils.formatSpeed(fileLength,
                                        lastMillis));
                                writeLog("X-Reqid: " + respInfo.reqId);
                                writeLog("X-Via: " + respInfo.xvia);
                                writeLog("jsonData   " + jsonData.toString());

                                String qiniu_url = domain + jsonData.get("key").toString();

                                listener.onUploadSuccess(qiniu_url, uploadCBHandler);
                            } catch (JSONException e) {
                                if (jsonData != null) {
                                    writeLog(jsonData.toString());
                                }
                                writeLog("------------exception--------------------");
                            }

                        } else {
                            listener.onUploadFail(respInfo.toString(), uploadCBHandler);
                        }


                    }

                }, uploadOptions);

    }

    private void updateStatus(final double percentage) {
        long now = System.currentTimeMillis();
        long deltaTime = now - uploadLastTimePoint;
        long currentOffset = (long) (percentage * uploadFileLength);
        long deltaSize = currentOffset - uploadLastOffset;
        if (deltaTime <= 100) {
            return;
        }

        final String speed = UnitUtils.formatSpeed(deltaSize, deltaTime);
        // update
        uploadLastTimePoint = now;
        uploadLastOffset = currentOffset;
        int progress = (int) (percentage * 100);
        Logger.t(TAG).d("progress  " + progress + "speed   " + speed);

    }

    private void writeLog(final String msg) {
        Logger.t(TAG).d(msg);
        Logger.t(TAG).d("\r\n");
    }

    private String createKey(int type) {
        String fileName = "";
        int index = uploadFilePath.lastIndexOf("/");
        if (index > 0 && index < (uploadFilePath.length() - 1)) {
            fileName = uploadFilePath.substring(index);
        } else {
            fileName = uploadFilePath;
        }
        String key = "";
        if (UploadType.TYPE_USER_PHOTO == type) {
            key = KEY_USER_PHOTO + "/" + fileName;
        } else if (UploadType.TYPE_ROBOT_HEAD_IMAGE == type) {
            key = KEY_ROBOT_HEAD + "/" + fileName;
        }else if (UploadType.TYPE_HEAD_IMAGE == type) {
            key = KEY_HEAD_IMAGE + "/" + fileName;
        }
        Logger.t(TAG).d("log fileName=" + fileName);
        Logger.t(TAG).d("log key=" + key);
        return key;
    }

}
