package com.ubtechinc.zh_chat.utils;

/**
 * 常量值
 * 
 * @author chenlin
 * 
 */
public class StaticValue {
	/**
	 * Socket服务端口
	 */
	public final static int SERVER_PORT = 6100;
	/**
	 * 舵机处理Socket端口
	 */
	public final static int MOTOR_SOCKET_PORT = 6102;

	/**
	 * ACTION FILE 存储路径
	 */
	// public static final String ACTION_PATH = "/mnt/external1/actions/";
	public static String ACTION_PATH = "/mnt/shell/emulated/0/actions/";
	/**
	 * PHOTO FILE 存储路径
	 */
	public static String PHOTO_PATH = "/mnt/shell/emulated/0/photos/";

	public static String ALPHA_UBTIFLYTEK_TTS_ACTION_EVENT = "";
	/**
	 * SOCKET通讯标志
	 */
	public static final short SOCKET_FLAG = 0x1234;
	/**
	 * SOCKET协议版本号
	 */
	public static final short SOCKET_VERSION = 0x0001;

	/**
	 * 胸部广播消息
	 */
	public static final String CHEST_ACTION = "com.ubtechinc.services.chest";

	/**
	 * 头部广播消息
	 */
	public static final String HEADER_ACTION = "com.ubtechinc.services.header";
	/** 接收讯飞语音 语音初始化完成 **/
	public static final String SPEECHINIT_ACTION = "com.ubtechinc.services.speechclientinit";
	/** 接收讯飞语音 语音命令广播 **/
	public static final String SPEECHCMD_ACTION = "com.ubtechinc.services.speechcmd";

	/** 发送蓝牙音频切换广播 **/
	public static final String AUDIODEV_ACTION = "com.ubtechinc.services.audiodev";
	/** 发送蓝牙联系人更新广播 **/
	public static final String UPLOADCONTACTS_ACTION = "com.ubtechinc.services.uploadcontactslexicon";
	/** 发送给讯飞语音 语音命令更新广播 **/
	public static final String UPLOADALPHACMD_ACTION = "com.ubtechinc.services.uploadalphacmdlexicon";
	/** 服务基本的广播消息处理 */
	public static final String SERVICE_BASE_ACTION = "com.ubtechinc.services.baseaction";
	/** 命令词更新完成广播 **/
	public static final String UPLOADALPHACMDOVER_ACTION = "com.ubtechinc.services.uploadoveralphacmdlexicon";
	/** 停止外部TTS **/
	public static final String STOPTTS_ACTION = "com.ubtechinc.services.stoptts";
	/** 事件标志 */
	public static final String BASE_ACTION_EVENT = "base_event";
	/** wifi 连接AP改变事件 */
	public static final String BASE_ACTION_KEY_WIFICONNECTCHANGE = "wifi_connect_change";
	/** 关闭舵机电源 */
	public static final String BASE_ACTION_KEY_CLOSEMOTORPOWER = "close_motor_power";
	/** 蓝牙spp连接广播 **/
	public static final String BLUETOOTH_CONNECED_ACTION = "com.ubtechinc.services.bluetoothclientconnected";
	/** 蓝牙SPP断开 **/
	public static final String BLUETOOTH_DISCONNECTED_ACTION = "com.ubtechinc.services.bluetoothclientdisconnected";
	/** 蓝牙数据广播 **/
	public static final String BLUETOOTH_SENDDATA_ACTION = "com.ubtechinc.services.bluetoothclientdata";
	/** 蓝牙打电话广播 **/
	public static final String ALPHA_PHONE_ACTION = "com.ubtechinc.services.phone";
	/** 传递号码 **/
	public static final String ALPHA_PHONE_NUM = "com.ubtechinc.services.phone.num";
	/** 更新胸部软件 **/
	public static final String ALPHA_UPDATE_CHESSOFTWARE = "com.ubtechinc.update.chess";
	/** 更新头部软件 **/
	public static final String ALPHA_UPDATE_HEADERSOFTWARE = "com.ubtechinc.udate.header";
	/** 蓝牙连接广播 **/
	public static final String ALPHA_BT_CONNECTION = "com.ubtechinc.services.bluetooth";
	/** 手势aciton **/
	public static final String ALPHA_GESTURE_ACTION = "come.ubt.alpha2.gesture";
	/** 手势方向 **/
	public static final String ALPHA_GESTURE_DIRECTION = "getstureDirection";
	/** 手势开启录音，相当于按钮触发 **/
	public static final String ALPHA_GESTURE_ACTION_REC = "come.ubt.alpha2.gesture.rec";
	/** 蓝牙闹钟事件 **/
	public static final String ALPHA_BT_ALARM = "com.ubt.alpha2.bt.alarm";
	/** 二维码扫描结果反馈 **/
	public static final String ALPHA_QR_CODE = "com.ubt.alpha2.qr_code";
	/** 停止二维码扫面 **/
	public static final String ALPHA_QR_CODE_CANCLE = "com.ubt.alpha2.qr_code.cancle";
	/** app管理广播 **/
	public static final String ALPHA_APP_MANAGE = "com.ubt.alpha2.app.manager";
	/** 声波通讯广播 **/
	public static final String ALPHA_DIGITAL_DECODE = "com.ubt.alpha2.digital.decode";
	// /////////////////Third party 第三方开始///////////////////////
	/** 执行动作表广播 **/
	public static final String ALPHA_THIRD_PARTY_PLAY_ACITON = "com.ubtechinc.services.thirdparty.playaction";
	/** 执行动作的名称 参数 **/
	public static final String ALPHA_THIRD_PARTY_ACTION_NAME = "TP_alphacmdName";
	/** 识别结果广播 **/
	public static final String ALPHA_THIRD_PARTY_SPEECH_RECOGNIZERRESULT = "com.ubtechinc.services.speech.thirdparty.recognizerresult";
	/** TTS广播 **/
	public static final String ALPHA_THIRD_PARTY_SPEECH_TTS = "com.ubtechinc.services.speech.thirdparty.tts";
	/** TTS 文本参数 **/
	public static final String ALPHA_THIRD_PARTY_SPEECH_TEXT = "com.ubtechinc.services.speech.thirdparty.text";
	/** 请求验证码 **/
	public static final String ALPHA_MSG_VERIFYCATION_CODE = "com.ubtechinc.services.verifycationcode";
	/** 提醒功能 **/
	public static final String ALPHA_MSG_DESKCLOCK_WAKEUP = "com.ubtechinc.services.deskclock.wakeup";
	/** 执行动作 **/
	public static final String ALPHA_MSG_PLAY_ACTION = "com.ubtechinc.services.action.play";
	/** 停止动作 **/
	public static final String ALPHA_MSG_STOP_ACTION = "com.ubtechinc.services.action.stop";
	/** cooee 返回 **/
	public static final String ALPHA_RECEIVE_COOEE = "AlphaCooeeServiceBussiness";
	/** 发送到framework执行cooee **/
	public static final String ALPHA_SEND_COOEE="CooeeServiceAlphaBussiness";
	/** cooee事件模拟广播 **/
	public static final String ALPHA_COOEE_DECODE = "com.ubt.alpha2.cooee.decode";
	/** TTS广播 **/
	public static final String ALPHA_TTS_HINT = "com.ubtechinc.robot.tts_hint_wakeup";
	/**
	 * wifi连接结果
	 */
	public static final String ALPHA_WIFI_RESULT="com.ubt.alpha2.wifiresult";
	/**
	 * 硬件测试广播
	 */
	public static final String ALPHA_HARDWARE="com.alpha2.hardware.test";
	/**
	 * 打开蓝牙请求响应
	 */
	public static final String ALPHA_MSG_BLUTETOOTH_OPEN = "com.ubtechinc.services.bluetooth.open";
	/** 开启声音 **/
	public static final String SYSTEM_SOUND_START = "android.intent.notify.SOUND_START";
	/** 暂停声音 **/
	public static final String SYSTEM_SOUND_PAUSE = "android.intent.notify.SOUND_PAUSE";
	/** 停止声音 **/
	public static final String SYSTEM_SOUND_STOP = "android.intent.notify.SOUND_STOP";
	/** 眼睛灯、耳朵灯操作 **/
	public static final String ALPHA_LED_ACTION = "com.ubtechinc.services.LED_ACTION";
	/** 恢复出厂设置 **/
	public static final String ALPHA_MASTER_CLEAR = "android.intent.action.MASTER_CLEAR";
	/** 设置RTC时间 **/
	public static final String ALPHA_SET_RTC_TIME = "com.ubtechinc.services.SET_RTC_TIME";
	/** 发送省电广播 **/
	public static final String ALPHA_SEND_POWER_SAVE = "com.ubtechinc.services.POWER_SAVE";
	/** 发送边充边玩广播 **/
	public static final String ALPHA_SET_CHARGE_PLAY = "com.ubtechinc.services.SET_CHARGE_PLAY";
	/** 查询边充边玩广播 **/
	public static final String ALPHA_QUERY_CHARGE_PLAY = "com.ubtechinc.services.QUERY_CHARGE_PLAY";
	/** Nuance离线语法命令 **/
	public static final String ALPHA_NUANCE_OFFLINE_CMD = "com.ubtechinc.services.NUANCE_OFFLINE_CMD";
	public static final String ALPHA_IFLY_OFFLINE_CMD="com.ubtechinc.services.IFLY_OFFLINE_CMD";
	/** 发送主服务关于TTS的广播 **/
	public static final String ALPHA_SERVICES_TTS = "com.ubtechinc.services.ABOUT_TTS";
	/** 发送主服务关于动作执行的广播 **/
	public static final String ALPHA_SERVICES_ACTION = "com.ubtechinc.services.ABOUT_ACTION";
	/** 是否正在tts播报 **/
	public static final String ALPHA_IS_TTS_ON = "alpha_is_tts_on";
	/** 是否正在执行动作 **/
	public static final String ALPHA_IS_ACTION_ON = "alpha_is_action_on";
	/** tts的内容 **/
	public static final String ALPHA_TTS_CONTENT = "alpha_tts_content";
	/** 动作的名称 **/
	public static final String ALPHA_ACTION_NAME = "alpha_action_name";
	/** 声音方向广播 **/
	public static final String ALPHA_SPEECH_DIRECTION = "com.ubtechinc.services.SPEECH_DIRECTION";
	/** 查询电量广播  **/
	public static final String ALPHA_QUERY_POWER = "com.ubtechinc.services.ALPHA_QUERY_POWER";
	/** 查询软件版本号广播  **/
	public static final String ALPHA_QUERY_SOFTWARE_VERSION = "com.ubtechinc.services.ALPHA_QUERY_SOFTWARE_VERSION";

	/** 机器人发的message命令 **/
	public static final String COMMAND_MESSAGE = "robot_command";
	/** message命令里携带的是照片url **/
	public static final String PHOTO_URL = "photoUrl";

	/** 用户状态的命令*/
	public static final String STATE = "State";

	// /////////////////Third party 第三方结束//////////////////////
	// /////////////////////////////////////////
	// 胸部命令
	/**
	 * 开机命令
	 */
	public static final byte CHEST_CMD_START = 0x01;
	/**
	 * 关机命令
	 */
	public static final byte CHEST_CMD_STOP = 0x02;
	/**
	 * 舵机数据
	 */
	public static final byte CHEST_CMD_SENDMOTOR = 0x03;
	/**
	 * 设置命令: 参数01 ：电量 （10 表示10%） 参数02 ：距离（单位厘米CM)（10~50cm）
	 */
	public static final byte CHEST_CMD_SETTING = 0x04;
	/**
	 * 设置单个舵机角度 参数：id号(1BYTE) 参数：角度(1BYTE)
	 */
	public static final byte CHES_CMD_MOTORANGLE = 0x05;
	/**
	 * 读取单个舵机角度 参数：ID号(1BYTE)
	 */
	public static final byte CHES_CMD_READANGLE = 0x06;
	/**
	 * 暂停执行
	 */
	public static final byte CHES_CMD_PAUSE = 0x07;
	/**
	 * 停止执行
	 */
	public static final byte CHES_CMD_STOP = 0x09;
	/**
	 * wifi连接（android->stm32） 参数1:0----wifi断开连接 1----wifi已连接
	 */
	public static final byte CHES_CMD_WIFISTATUS = 0x14;
	/**
	 * 跳舞（android->stm32） 参数1: 0------跳舞结束 1------跳舞开始
	 */
	public static final byte CHES_CMD_ACTION = 0x15;
	/** 蓝牙连接 1----bt连接 0----bt断开 **/
	public static final byte CHES_CMD_BTSTATUS = 0x16;
	/**
	 * 开关舵机电源 参数1: 0-------关闭舵机电源 1-------打开舵机电源
	 */
	public static final byte CHES_CMD_MOTOR_POWER = 0x19;
	/** 保存闹钟 **/
	public static final byte CHES_CMD_SAVE_ALARM = 0x1A;
	/** 闹钟结束 **/
	public static final byte CHES_CMD_END_ALARM = 0x1B;
	/** 时间校准 **/
	public static final byte CHES_CMD_AJUST_TIME = 0x1C;
	/** 读取时间 **/
	public static final byte CHES_CMD_READ_TIME = 0x1D;
	/** 读取闹钟 **/
	public static final byte CHES_CMD_READ_ALARM = 0x1E;
	/**
	 * 开始升级 参数:1---4,文件大小,单位字节.
	 */
	public static final byte CHES_CMD_START_UPDATE = 0x30;

	/**
	 * 0x31:升级数据包 参数:1-2,数据包page 参数:3-66,共64字节数据,不到64字节用00填满
	 */
	public static final byte CHES_CMD_UPDATE_PAGE = 0X31;

	/**
	 * 0x32:结束升级
	 */
	public static final byte CHES_CMD_UPDATE_END = 0x32;
	/** 读胸部版本 **/
	public static final byte CHEST_READ_VERSION = 0x33;
	/** 设置所有舵机---仿真用 **/
	public static final byte CHEST_SET_ALL_ANGLE = 0x34;
	/** 协议0x35,控制使能边充边玩功能 **/
	public static final byte CHEST_CHANGE_PLAY = 0x35;
	/** 协议0x36,写序列号到机器eeprom **/
	public static final byte CHEST_WRITE_SID_EEPROM = 0x36;
	/** 协议0x37,从机器eeprom读序列号 **/
	public static final byte CHEST_READ_SID_EEPROM = 0x37;
	/** 蹲下 **/
	public static final byte CHEST_SQUAT = (byte) 0x38;
	/** 省电模式 **/
	public static final byte CHEST_POWER_SAVE = (byte) 0x40;

	/** 电量 **/
	public static final byte CHEST_SEND_POWER = (byte) 0x80;
	/**
	 * 有障碍物
	 */
	public static final byte CHES_SEND_OBSTACLE = (byte) 0x81;
	/**
	 * 舵机角度 参数1： ID(1BYTE) 参数2：角度（1BYTE）
	 */
	public static final byte CHES_SEND_ANGLEINFO = (byte) 0x82;
	/**
	 * 关机命令
	 */
	public static final byte CHES_SEND_SHUTDWON = (byte) 0x83;

	/** 闹钟触发 **/
	public static final byte CHES_SEND_ALARM = (byte) 0x87;
	/**
	 * 手部触摸
	 */
	public static final byte CHEST_TOUCH_BOARD = (byte)0x89;
	/** 电源插入或拔出 **/
	public static final byte CHES_DC_STATE = (byte) 0x8A;
	/**
	 * 舵机温度高,需要休息
	 */
	public static final byte CHES_SEND_TEMPBEYOND = (byte) 0x92;
	/**
	 * 数据发送到头部（stm32->android）
	 */
	public static final byte CHES_SEND_TRANSFORM = (byte) 0x96;
	/**
	 * 跌倒 参数1:0-------前跌 1-------后跌
	 */
	public static final byte CHES_SEND_FALLDOWN = (byte) 0x97;

	// //////////////////////////////////////////////
	// 头部命令
	/**
	 * 耳朵灯
	 */
	public static final byte LED_EAR = 0x01;
	/**
	 * 眼睛灯
	 */
	public static final byte LED_EYE = 0x02;
	/**
	 * 嘴灯
	 */
	public static final byte LED_MOUTH = 0x03;
	/**
	 * 设置红外距离（0~15cm）
	 */
	public static final byte INFRARED_SETTING = 0x04;
	/**
	 * 开机命令
	 * */
	public static final byte STARTUP_CMD = 0x05;
	/**
	 * 暂停执行
	 */
	public static final byte HEAD_PAUSE_CMD = 0x06;
	/**
	 * 继续执行
	 */
	public static final byte HEAD_CONTINUE_CMD = 0x07;
	/**
	 * 停止执行
	 */
	public static final byte STOP_CMD = 0x08;
	/**
	 * 跳舞（android->stm32） 参数1: 0------跳舞结束 1------跳舞开始
	 */
	public static final byte HEAD_ACTION = 0x15;
	/**
	 * 关机命令
	 */
	public static final byte HEAD_SHUTDOWN = 0x16;
	/**
	 * 功放控制
	 */
	public static final byte SOUND_CONTROL = 0x19;
	/**
	 * 蓝牙打开命令
	 */
	public static final byte HEAD_BLUETOOTH_OPEN =0x20;
	/**
	 * 蓝牙打开命令
	 */
	public static final byte HEAD_CONTROL_BYPASS =0x27; 
	/**
	 * 开始升级 参数:1---4,文件大小,单位字节.
	 */
	public static final byte HEAD_CMD_START_UPDATE = 0x30;

	/**
	 * 0x31:升级数据包 参数:1-2,数据包page 参数:3-66,共64字节数据,不到64字节用00填满
	 */
	public static final byte HEAD_CMD_UPDATE_PAGE = 0X31;

	/**
	 * 0x32:结束升级
	 */
	public static final byte HEAD_CMD_UPDATE_END = 0x32;

	/**
	 * 有障碍物
	 */
	public static final byte HEADER_SEND_OBSTACLE = (byte) 0x80;
	/**
	 * 按键
	 */
	public static final byte HEADER_SEND_KEY = (byte) 0x81;
	/**
	 * 数据发到胸部（stm32->android）
	 */
	public static final byte HEADER_SEND_TRANSFORM = (byte) 0x96;

	/** 降噪功能 **/
	public static final byte HEADER_SEND_NOISE = 0X27;
	/** 控制功放 **/
	public static final byte HEADER_SEND_AMP = 0X19;
	/** 读头部版本 **/
	public static final byte HEADER_READ_VERSION = 0x33;
	/** 系统更新 **/
	public static final byte HEADER_SYSTEM_REBOOT = 0X21;
	/** 高温报警 **/
	public static final byte HEADER_HIGHT_TEMP = (byte) 0x84;
	/**
	 * 声音检测
	 */
	public static final byte HEADER_SOUND_DIRECTION=(byte) 0x82;
	/**
	 * 跌到检测
	 */
	public static final byte HEADER_FALL_DERECTION=(byte) 0x83;

	
}
