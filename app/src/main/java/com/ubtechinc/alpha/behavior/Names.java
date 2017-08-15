package com.ubtechinc.alpha.behavior;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/10
 * @modifier:
 * @modify_time:
 */

public final class Names {
    public static final String HTTP_PREFFIX = "http://";
    public static final String FILE_PREFFIX = "file://";

    //element
    public static final String SET = "set";
    public static final String ACTION = "action";
    public static final String LED = "led";
    public static final String MUSIC = "music";
    public static final String TTS = "tts";

    //attri
    public static final String ORDER = "order";
    public static final String DELAY = "delay";
    public static final String REPEAT = "repeat";

    public static final String SPEED = "speed";
    public static final String TEXT = "text";
    public static final String VOICER = "voicer";
    public static final String MOOD = "mood";
    public static final String VOLUME = "volume";
    public static final String POSITION = "position";
    public static final String NUMBER = "number";
    public static final String ON_TIME = "ontime";
    public static final String RUN_TIME = "runtime";
    public static final String COLOR = "color";
    public static final String SECOND_COLOR = "secondColor";
    public static final String BRIGHT = "bright";
    public static final String EFFECT = "effect";
    public static final String URI = "uri";

    public static void main(String[] args){
        for (int i=0 ; i < 16; i++){
            for (int j=i; j < 16 ; j ++){
                System.out.println("<xs:enumeration value=\""+ Integer.toHexString(i)+ Integer.toHexString(j)+"\"></xs:enumeration>");
            }
        }
    }
}
