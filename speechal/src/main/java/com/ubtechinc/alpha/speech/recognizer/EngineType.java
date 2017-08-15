package com.ubtechinc.alpha.speech.recognizer;

/**
 * @desc : 引擎类型
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/8/4
 * @modifier:
 * @modify_time:
 */

public enum EngineType {
    LOCAL,//本地语法
    CLOUD;//云端语法

    @Override
    public String toString() {
        switch (this){
            case LOCAL:
                return "本地语法识别";
            case CLOUD:
                return "云端语法识别";
        }
        return "";
    }
}
