package com.ubtechinc.alpha2ctrlapp.entity.business.robot;


/**
 * @ClassName UserRecordInfo
 * @date 6/28/2017
 * @author tanghongyu
 * @Description 机器人闲聊记录
 * @modifier
 * @modify_time
 */
public class UserRecordInfo {
    private String userWord;
    private String robotTranlate;
    private String date;

    public UserRecordInfo(String userWord, String robotTranlate, String date) {
        this.userWord = userWord;
        this.robotTranlate = robotTranlate;
        this.date = date;
    }

    public String getUserWord() {
        return userWord;
    }

    public void setUserWord(String userWord) {
        this.userWord = userWord;
    }

    public String getRobotTranlate() {
        return robotTranlate;
    }

    public void setRobotTranlate(String robotTranlate) {
        this.robotTranlate = robotTranlate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "UserRecordInfo{" +
                "userWord='" + userWord + '\'' +
                ", robotTranlate='" + robotTranlate + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
