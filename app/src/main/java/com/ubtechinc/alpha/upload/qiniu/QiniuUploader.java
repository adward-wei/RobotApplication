package com.ubtechinc.alpha.upload.qiniu;

import android.text.TextUtils;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.network.business.GetToken;
import com.ubtechinc.alpha.network.module.GetTokenModule;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.upload.IUploadResultListener;
import com.ubtechinc.alpha.upload.IUploader;
import com.ubtechinc.alpha.upload.UploadCBHandler;
import com.ubtechinc.alpha.upload.UploadType;
import com.ubtechinc.alpha.utils.UnitUtils;
import com.ubtechinc.nets.ResponseListener;
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
    private final static String KEY_PHOTO = "robot/photos";
    private final static String KEY_LOG = "Alpha2Log";
    private static QiniuUploader sQiniuUploader;

    private long uploadLastTimePoint;
    private long uploadLastOffset;
    private long uploadFileLength;
    private String uploadFilePath;



    private UploadManager uploadManager;

    private QiniuUploader() {}

    public static QiniuUploader get() {
        if(sQiniuUploader == null) {
            synchronized (QiniuUploader.class) {
                if(sQiniuUploader == null)
                    sQiniuUploader = new QiniuUploader();
            }
        }

        return sQiniuUploader;
    }


    @Override
    public void upload(final UploadCBHandler uploadCBHandler, final IUploadResultListener listener) {
        if(TextUtils.isEmpty(uploadCBHandler.filePath)) {
            return;
        }
        this.uploadFilePath = uploadCBHandler.filePath;
        //从业务服务器获取上传凭证
        ThreadPool.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                getQiniuToken(uploadCBHandler, listener);
            }
        });
    }

    private void getQiniuToken(final UploadCBHandler uploadCBHandler, final IUploadResultListener listener) {

        GetToken.getInstance().requestGetToken(uploadCBHandler.type, new ResponseListener<GetTokenModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                LogUtils.d(TAG, "requestGetToken  error : " + e.getMessage());
            }

            @Override
            public void onSuccess(GetTokenModule.Response response) {
                String qiniuToken = response.data.result.token;
                upload(response.data.result.domain,qiniuToken, uploadCBHandler, listener);
            }
        });
    }

    private void upload(final String domain,String uploadToken, final UploadCBHandler uploadCBHandler, final IUploadResultListener listener) {
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
        writeLog("0 %   0 KB/s  "+ UnitUtils.formatSize(fileLength));
        writeLog( "...");

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
                                writeLog("jsonData   "+jsonData.toString());

                                String qiniu_url = domain + jsonData.get("key").toString();
                                LogUtils.d(TAG, "onUploadSuccess   qiniu_url: " + qiniu_url);
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
        Log.d(TAG,"progress  "+progress +"speed   "+speed);

    }

    private void writeLog(final String msg) {
        Log.d(TAG, msg);
        Log.d(TAG,"\r\n");
    }

    private String createKey(int type) {
        String fileName = "";
        int index = uploadFilePath.lastIndexOf("/");
        if(index > 0 && index < (uploadFilePath.length() - 1)) {
            fileName = uploadFilePath.substring(index);
        } else {
            fileName = uploadFilePath;
        }
        String key = "";
        if(UploadType.TYPE_PHOTO == type) {
            key = KEY_PHOTO + "/" + RobotState.get().getSid() + fileName;
        } else if(UploadType.TYPE_LOG == type) {
            key = KEY_LOG + "/" + RobotState.get().getSid() + fileName;
        }
        LogUtils.d("log fileName=" + fileName);
        LogUtils.d("log key=" + key);
        return key;
    }

}
