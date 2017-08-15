package com.ubtechinc.alpha.upload.log;

//import com.ubtechinc.alpha2ctrlapp.network.common.HttpPost;
//import com.ubtechinc.service.mainservice.AlphaMainSeviceImpl;
//import com.ubtechinc.utils.uploadlog.Interface.UploadResultListener;

/**
 * 将log文件上传到七牛服务器
 *
 * @author wangzhengtian
 * @Date 2017-02-28   需要新接口上传七牛服务器
 */

public class FileUpload2QiNiuImpl {
//    private final static String TAG = "FileUpload2QiNiuImpl";
//
//    private final static String PICTURE_ADDRESS="http://video.ubtrobot.com";
//
//    private final static String KEY = "Alpha2Log";
//
//    private final static int UPLOAD_LOGFILE_TYPE = 1;
//
//    private static FileUpload2QiNiuImpl sFileUpload2QiNiuImpl;
//
//    private Context mContext;
//
//    private UploadManager uploadManager;
//    private long uploadLastTimePoint;
//    private long uploadLastOffset;
//    private long uploadFileLength;
//    private String uploadFilePath;
//
//    private UploadResultListener mUploadResultListener;
//
//    private ReentrantLock mLock;
//    private boolean workingNow = false;
//
//    private FileUpload2QiNiuImpl(Context context) {
//        mContext = context;
//        mLock = new ReentrantLock();
//    }
//
//    public static FileUpload2QiNiuImpl getInstance(Context context) {
//        if(sFileUpload2QiNiuImpl == null) {
//            sFileUpload2QiNiuImpl = new FileUpload2QiNiuImpl(context);
//        }
//        return sFileUpload2QiNiuImpl;
//    }
//
//    public void uploadFile2QINIU(String filePath, UploadResultListener listener){
//        if(TextUtils.isEmpty(filePath)) {
//            listener.onUploadResult(UploadResultCode.ERROR_PARAMETER);
//            return;
//        }
//        if(isWorkingNow()) {
//            listener.onUploadResult(UploadResultCode.ERROR_OCCUPY);
//            return;
//        } else {
//            setWorkingNow(true);
//        }
//
//        mUploadResultListener = listener;
//        this.uploadFilePath = filePath;
//        //从业务服务器获取上传凭证
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String result = sendJsonByPost(UPLOAD_LOGFILE_TYPE);
//                JSONTokener jsonParser = new JSONTokener(result);
//                try {
//                    JSONObject messageJson = (JSONObject) jsonParser.nextValue();
//                    messageJson.getString("models");
//                    upload(messageJson.getString("models"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    setWorkingNow(false);
//                    mUploadResultListener.onUploadResult(UploadResultCode.ERROR_FAIL);
//                }
//            }
//        }).start();
//    }
//
//    private void upload(String uploadToken) {
//        if (this.uploadManager == null) {
//            this.uploadManager = new UploadManager();
//        }
//        File uploadFile = new File(this.uploadFilePath);
///*        UploadOptions uploadOptions = new UploadOptions(null, null, false,
//                new UpProgressHandler() {
//                    @Override
//                    public void progress(String key, double percent) {
//                        updateStatus(percent);
//                    }
//                }, null);*/
//        UploadOptions uploadOptions = new UploadOptions(null, null, false,
//                                null, null);
//
//        final long startTime = System.currentTimeMillis();
//        final long fileLength = uploadFile.length();
//        this.uploadFileLength = fileLength;
//        this.uploadLastTimePoint = startTime;
//        this.uploadLastOffset = 0;
//        writeLog("0 %   0 KB/s  "+Tools.formatSize(fileLength));
//        writeLog( "...");
//
//        //key相当于服务器上的路径
//        String key = createKey();
//        this.uploadManager.put(uploadFile, key, uploadToken,
//                new UpCompletionHandler() {
//                    @Override
//                    public void complete(String key, ResponseInfo respInfo,
//                                         JSONObject jsonData) {
//
//                        long lastMillis = System.currentTimeMillis()
//                                - startTime;
//
//                        Log.d(TAG, "upload time = " + lastMillis);
//                        if (respInfo.isOK()) {
//                            setWorkingNow(false);
//                            mUploadResultListener.onUploadResult(UploadResultCode.SUCCESS);
//
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    deleteLogFileFromRobot();
//                                }
//                            }).start();
//
///*
//                            try {
//                                String fileKey = jsonData.getString("key");
//                                String fileHash = jsonData.getString("hash");
//                                writeLog("File Size: "
//                                        + Tools.formatSize(uploadFileLength));
//                                writeLog("File Key: " + fileKey);
//                                writeLog("File Hash: " + fileHash);
//                                writeLog("Last Time: "
//                                        + Tools.formatMilliSeconds(lastMillis));
//                                writeLog("Average Speed: "
//                                        + Tools.formatSpeed(fileLength,
//                                        lastMillis));
//                                writeLog("X-Reqid: " + respInfo.reqId);
//                                writeLog("X-Via: " + respInfo.xvia);
//                                writeLog("jsonData   "+jsonData.toString());
//                                Log.d("test","jsonData   "+jsonData.toString());
//                                String QINIU_IMAGEURL = PICTURE_ADDRESS+"/"+jsonData.get("key").toString();
//                                if(sendTo != null && !sendTo.equals("")) {
//                                    sendPhotoUrl(QINIU_IMAGEURL);
//                                }
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
////                                        sendJsonByPost(NOTIFICATION_SERVER_UPLOAD_RESULT);
//                                    }
//                                }).start();
//                                //Brian DELETE THE PICTURE FROM ROBOT DEVICE BEGINNING
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        deletePictureFromRobot();
//                                    }
//                                }).start();
//
//                                //Brian DELETE THE PICTURE FORM ROBOT DEVICE ENDING
//                                writeLog("--------------end------------------");
//                            } catch (JSONException e) {
//                                if (jsonData != null) {
//                                    writeLog(jsonData.toString());
//                                }
//                                writeLog("------------exception--------------------");
//                            }*/
//                        } else {
//                            setWorkingNow(false);
//                            mUploadResultListener.onUploadResult(UploadResultCode.ERROR_FAIL);
//                            writeLog(respInfo.toString());
//                            if (jsonData != null) {
//                                writeLog(jsonData.toString());
//                            }
//                            writeLog("--------------------------------");
//                        }
//                    }
//
//                }, uploadOptions);
//
//
//
//    }
//
//    private void updateStatus(final double percentage) {
//        long now = System.currentTimeMillis();
//        long deltaTime = now - uploadLastTimePoint;
//        long currentOffset = (long) (percentage * uploadFileLength);
//        long deltaSize = currentOffset - uploadLastOffset;
//        if (deltaTime <= 100) {
//            return;
//        }
//
//        final String speed = Tools.formatSpeed(deltaSize, deltaTime);
//        // update
//        uploadLastTimePoint = now;
//        uploadLastOffset = currentOffset;
//        int progress = (int) (percentage * 100);
//        Log.d(TAG,"progress  "+progress +"speed   "+speed);
//
//    }
//
//    private void writeLog(final String msg) {
//        Log.d(TAG, msg);
//        Log.d(TAG,"\r\n");
//    }
//
//    private void deleteLogFileFromRobot(){
//        Log.d(TAG,"delete LogFile "+this.uploadFilePath);
//        File deleteFile = new File(this.uploadFilePath);
//        deleteFile.delete();
//    }
//    public static String sendJsonByPost(int type ) {
//        String result = "";
//        String _url="";
//
//        if(type==1) {
//            _url = HttpPost.getWebServiceAdderss()+"system/getQiniuToken";
//        }else {
//            _url = HttpPost.getWebServiceAdderss()+"system/addRobotImage";
//        }
//        Log.d(TAG,"URL ADDRESS   "+_url +"type    "+type);
//        try {
//            URL url = new URL(_url);
//            HttpURLConnection conn = (HttpURLConnection) url
//                    .openConnection();
//            conn.setConnectTimeout(HttpPost.HTTP_TIMEOUT);
//            conn.setReadTimeout(HttpPost.HTTP_TIMEOUT);
//            conn.setRequestProperty("Content-Type",
//                    "application/json;charset=UTF-8");
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            PrintWriter out = new PrintWriter(new OutputStreamWriter(
//                    conn.getOutputStream(), "UTF-8"));
//            //   String _params = JsonUtils.getInstance().getJson(request);
//            String _params=null;
//            if(type==1) {
//                _params = "{\n" +
//                        "    \"appType\": \"1\",\n" +
//                        "    \"serviceVersion\": \"1.0.0.2\",\n" +
//                        "    \"requestKey\": \"3D71F1D887E4998537A0CF52BA867777\",\n" +
//                        "    \"requestTime\": \"20151215162519\",\n" +
//                        "    \"systemLanguage\": \"CN\"\n" +
//                        "}";
//            }else {
///*                _params = "{\n" +
//                        "    \"serialNumber\": \""+ AlphaMainSeviceImpl.serialNumber+"\",\n" +
//                        "    \"userId\": \""+ Alpha2XmppServices.robotHolder+"\",\n" +
//                        "    \"imageUrl\": \""+QINIU_IMAGEURL+"\",\n" +
//                        "    \"appType\": \"1\",\n" +
//                        "    \"serviceVersion\": \"1.0.0.2\",\n" +
//                        "    \"requestKey\": \"3D71F1D887E4998537A0CF52BA867777\",\n" +
//                        "    \"requestTime\": \"20151215162519\",\n" +
//                        "    \"systemLanguage\": \"CN\"\n" +
//                        "}";
//*/
//            }
//            Log.i("Alexa_Picture", "!!请求消息" + _params);
//            out.print(_params);
//            out.flush();
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream(),
//                            "UTF-8"));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//            out.close();
//            in.close();
//
//        } catch (Exception e) {
//            e.toString();
//        }
//        Log.i("Alexa_Picture", "!!!响应消息"+result);
//        return result;
//    }
//
//    private String createKey() {
//        String fileName = "";
//        int index = uploadFilePath.lastIndexOf("/");
//        if(index > 0 && index < (uploadFilePath.length() - 1)) {
//            fileName = uploadFilePath.substring(index);
//        } else {
//            fileName = uploadFilePath;
//        }
//        String key = KEY + "/" + AlphaMainSeviceImpl.serialNumber + fileName;
//
//        LogUtil.d(TAG, "log fileName=" + fileName);
//        LogUtil.d(TAG, "log key=" + key);
//        return key;
//    }
//
//    private void setWorkingNow(boolean workingNow) {
//        mLock.lock();
//        this.workingNow = workingNow;
//        mLock.unlock();
//    }
//
//    private boolean isWorkingNow() {
//        boolean workingNow;
//        mLock.lock();
//        workingNow = this.workingNow;
//        mLock.unlock();
//        return workingNow;
//    }
}
