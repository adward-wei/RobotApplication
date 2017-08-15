package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

/**
 * @author：liuhai
 * @date：2017/4/15 9:33
 * @modifier：liuhai
 * @modify_date：2017/4/15 9:33
 * [A brief description]
 * version
 */

public class TranlateInfo {
    private String userWord;
    private String robotTranlate;

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

    @Override
    public String toString() {
        return "TranlateInfo{" +
                "userWord='" + userWord + '\'' +
                ", robotTranlate='" + robotTranlate + '\'' +
                '}';
    }
}
