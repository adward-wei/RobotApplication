/*
 UBTECH Android Client, Constants
 Copyright (c) 2014 UBTECH Tech Company Limited
 http://www.ubtechinc.net/
 */

package com.ubtechinc.alpha2ctrlapp.constants;

import android.media.AudioFormat;
import android.os.Environment;

import com.ubtech.utilcode.utils.SDCardUtils;

/**
 * @ClassName Constants
 * @date 4/6/2017
 * @author tanghongyu
 * @Description 数据库相关
 * @modifier
 * @modify_time
 */
public class Constants {

    public static final String VERSION_CODE = "version_code";

    public static String ssID;
    public static String WIFI_CAPA;
    public static final String WIFI_NAME = "wifi_name";
    public static final String WIFI_PSW = "wifi_psw";
    public static final String WIFI_CAPABILITIY = "wifi_capabilities";
    public static final String WIFI_PSW_VISIBLE = "wifi_psw_visible";

    public static short channels = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    public static int sampleRate = 48000;
    public static short bitsPerSample = 16;


    public static boolean hasShowDiag = false;


    //机器人是否自己所有
    public static boolean isOwner = false;
    public static boolean hasLoginOut = true;
    //总的存放路径
    public static String main_dir = SDCardUtils.getSDCardPath() + "/ubt/";
    //APK存放路径
    public static String apk_dir = main_dir + "apk/";
    public static String downloadbyWifi = "downloadbyWifi";
    //设备名称
    public static String deviceName = "";
    //图片最大宽度=屏幕宽度
    public static int appImageWidth;
    public static int appDetailImageHeight;
    public static int appRecomlImageHeight;
    //屏幕高度
    public static int deviceHeight;
    public static String alarmActivte = "";
    public static boolean hasStartService = false;
    //	 public static String  Alpha_sn ="";
    public final static String ADD_AUTHORIZE_ACTION = "addrobotfriend";
    public final static int GET_COUNTRY_REQUEST = 1000;
    public final static String ROBOTSN = "robotSn";
    public final static String PIC_RECEIVE_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/ubtech/image/";
    public final static String PIC_RECEIVE_TEMP = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/ubtech/temp/image/";
    public final static String PIC_RECEIVE_THUMB_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/ubtech/temp/thumb/";
    //业务数据库缓存目录
    public final static String DATABASE_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/ubtech/database/";
    public final static String PIC_RECEIVE_APP = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/ubtech/appinfo/";
    public final static String PIC_DCIM_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/DCIM/Camera/";
    public final static String CHANGE_LANGUAGE_APK_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/ubtech/plugin/changeLan.apk";
    public final static String LOG_PAHT = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/ubtech/log/";
    public final static String QQ_TOKEN = "qq_token";
    public static String SHARE_APP_URL = "";
    public static String SHARE_ACTION_URL = "";
    public static int THIRD_LOGIN_TYPE = 0;
    public final static String WEIXIN_TOKEN = "weixin_token";


    public final static String WEIBO_TOKEN = "weibo_token";
    public final static String ROBOT_MAC = "robot_mac"; // 需要配置的机器人信息
    public static String SYSTEM_LAN = "system_lan";
    public static Double geoLat = 0.0;//纬度
    public static Double geoLng = 0.0; //经度
    public static String language = "";
    public static final String GUIDE_PAGES = "guide_pages";
    public static final String app_smartcamera_packageName = "com.ubtech.smartcamera";
    public static final String app_smartcamera_name_ch = "乐拍";
    public static final String app_smartcamera_name_en = "smartcamera";
    public static final String app_alphatranslation_name_ch = "中英互译";
    public static final String app_alphatranslation_name_en = "alphatranslation";

    public static final String app_video_name_ch = "视频监控";
    public static final String app_video_name_en = "videosupervision";


    public static final String voice_compound_packgeName = "com.ubt.voicecompund";
    public static final String main_menu_guide = "menu_gude";
    public static final String app_use_guide = "app_use_guide";
    public static final String device_add_guide = "device_add_guide";
    public static final String device_invate_guide = "device_invate_guide";
    public static final String main_app_guide = "app_gude";
    public static final String bind_guide = "bind_gude";
    public static final String scan_guide = "scan_guide";
    public static final String APP_LAUNGUAGE = "app_language";
    public static final String LANGUAGE_PLUGIN_PACKAGE = "com.alpha2.changeslan";
    public static final String app_chat_name = "悠聊UChat";
    public static final String app_chat_music = "音乐";
    public static final String app_chat_poem = "诗词";
    public static final String app_chat_joke = "笑话";
    public static final String app_chat_baike = "百科";
    public static final String app_chat_count = "计算";
    public static final String app_chat_weather = "天气";
    public static final String app_chat_time = "时间";
    public static final String app_chat_story = "故事";
    public static final String app_chat_huil = "汇率";
    public static final String app_chat_clock = "闹钟";
    public static final String app_chat_action = "动作";
    public static final int intent_voice_compound_rquestcode = 1;
    public static final int intent_voice_compound_resultcode = 2;
    public static final int intent_voice_compound_resultcode_three = 3;
    public static final String intent_voice_compound_data_action_name = "action_info_name";
    public static final String intent_voice_compound_data_action_id = "action_info_id";
    public static final String intent_voice_compound_position_data = "action_position";

    public static final String app_tranlate_ch_true = "现在你可以对机器人说<font color='#9a9a9a'>中文</font>,机器人将翻译成<font color='#9a9a9a'>英文</font>";
    public static final String app_tranlate_en_true = "Now speak <font color='#9a9a9a'>Chinese</font> to the robot, and words will be translated into <font color='#9a9a9a'>English</font>";
    public static final String app_tranlate_ch_false = "现在你可以对机器人说<font color='#9a9a9a'>英文</font>,机器人将翻译成<font color='#9a9a9a'>中文</font>.";
    public static final String app_tranlate_en_false = "Now speak <font color='#9a9a9a'>English</font> to the robot, and words will be translated into <font color='#9a9a9a'>Chinese</font>.";
    public static final String record_none = "对阿尔法说：“<font color='#6CA8FF'>你好，阿尔法</font>”开启和机器人的对话吧！对话的记录将会显示在这里";


    // yuyong
    public final static int BindingRequestCode = 1001;
    public final static String Result_text = "Result_text";
    public final static String Result_other_way = "__Result_other_way__";
    /**
     * 字节数
     */
    private static short blockAlign = (short) (channels * bitsPerSample / 8);
    /**
     * 每秒播放速度音频数据buff的大小
     */
    public static int avgBytesPerSec = blockAlign * 48;


    /**
     * 主服务视频监控应用APP KEY
     */
    public static final String MAIN_SERVICE_SURVEILLANCE_APP_KEY = "BE2BC2AA3A0F6B084C1E6BAB66ED26F6";

    /**
     * 主服务监控应用名称
     */
    public static final String MAIN_SERVICE_SURVEILLANCE_NAME = "视频监控";

    /**
     * 主服务监控应用包名
     */
    public static final String MAIN_SERVICE_SURVEILLANCE_PACKAGE_NAME = "com.alpha2.videoSupervision";

    /**
     * 声网App ID
     */
    public static final String APP_ID = "bd63e74c3e2d4d428fd2fc7a0d2f741c";

    /**
     * 没有查询到电量的时候，查询间隔
     */
    public static final int POWER_REQUEST_INTERVAL_WITHOUT_OBTAINED = 2000;//2s

    /**
     * 查询电量的间隔时间
     */
    public static final int POWER_REQUEST_INTERVAL = 3 * 60 * 1000;//3分钟

    /**
     * 第一次使用视频监控提示KEY
     */
    public static final String SURVEILLANCE_FIRST_USE = "surveillance_first_use";

    /**
     * 第一次进入我的应用界面
     */
    public static final String APP_PAGE_FIRST_USE = "app_page_first_use";
    /**
     * 支持视频监控的主服务最低版本
     */
    public static final String MIN_SERVICE_VERSION_SUPPORT_SURVEILLANCE = "1.1.3.3";

    /**
     * 商店应用下载事件ID
     */
    public static final String YOUMENT_SHOP_APP_DOWNLOAD_TIMES = "shop_app_dowonload_times";

    /**
     * 商店动作下载事件ID
     */
    public static final String YOUMENT_SHOP_ACTION_DOWNLOAD_TIMES = "shop_action_download_times";
    /**
     * 我的页面动作列表播放次数事件ID
     */
    public static final String YOUMENT_MY_ACTION_PLAY_TIMES = "my_action_play_times";

    /**
     * 我的页面应用启动次数事件ID
     */
    public static final String YOUMENT_MY_APP_START_TIMES = "my_app_start_times";


    /**
     * 账号注册次数事件ID
     */
    public static final String YOUMENT_APP_REGISTER_TIMES = "app_register_times";


    /**
     * 版本名称:第一次启动进行设置，这里为了埋几百年的坑，弄的一个临时的静态变量，请求的时候，加上去
     */
    public static String VERSION_NAME = "V1.0";

}
