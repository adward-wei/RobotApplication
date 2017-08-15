package com.ubtechinc.alpha2ctrlapp.data.robot;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nixiaoyan on 2016/7/6.
 */
public class RobotStatusInfo  implements Serializable ,Comparable<RobotStatusInfo>{

    // 语音引擎类型
    private String speechEngine;
    // 语音引擎运行状态
    private String speechEngineState;
    // 语音引擎详细运行细节
    private String speechEngineDetail;
    // 当前正在运行的第三方app
    private String appName;


    private String time; //收到返回信息的时间
    // 正在运行的识别引擎类型
    private String speechASREngine;
    // 文件存储的路径
    private String filePath;

    public String getSpeechEngine() {
        return speechEngine;
    }

    public void setSpeechEngine(String speechEngine) {
        this.speechEngine = speechEngine;
    }

    public String getSpeechEngineState() {
        return speechEngineState;
    }

    public void setSpeechEngineState(String speechEngineState) {
        this.speechEngineState = speechEngineState;
    }

    public String getSpeechEngineDetail() {
        return speechEngineDetail;
    }

    public void setSpeechEngineDetail(String speechEngineDetail) {
        this.speechEngineDetail = speechEngineDetail;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpeechASREngine() {
        return speechASREngine;
    }

    public void setSpeechASREngine(String speechASREngine) {
        this.speechASREngine = speechASREngine;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public int compareTo(RobotStatusInfo another) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date thisDate = df.parse(this.time);
            Date compDate = df.parse(another.time);
            if(thisDate.before(compDate))
                return 1;
        }catch (Exception e){

        }
        return -1;
    }

}
