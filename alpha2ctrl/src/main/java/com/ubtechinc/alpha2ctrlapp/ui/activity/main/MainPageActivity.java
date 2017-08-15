package com.ubtechinc.alpha2ctrlapp.ui.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.orhanobut.logger.Logger;
import com.ubt.alpha2sdk.aidlclient.RobotInfo;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha.CmSetRTCTimeData;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.constants.SocketCmdId;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotActionDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAppDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotInitDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotActionRepository;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAppRepository;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotInitRepository;
import com.ubtechinc.alpha2ctrlapp.data.user.ILoginDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.LoginAndOutReponsitory;
import com.ubtechinc.alpha2ctrlapp.database.DBDao;
import com.ubtechinc.alpha2ctrlapp.database.NoticeManager;
import com.ubtechinc.alpha2ctrlapp.entity.AppInstalledInfo;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionFileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AlphaParam;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ButtonConfig;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ChatInfoItem;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewActionInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionDetail;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.events.RobotControlEvent;
import com.ubtechinc.alpha2ctrlapp.events.RobotIMStateChangeEvent;
import com.ubtechinc.alpha2ctrlapp.presenter.ISpotifyLoginPresenter;
import com.ubtechinc.alpha2ctrlapp.presenter.SpotifyLoginPresenterImpl;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.third.AidlService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.AboutActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.AppSettingActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.ContactusActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.VoiceCmdActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.ConfigureRobotNetworkActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.MyDeviceActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.AuthorizationActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.JpushReceiveActivty;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.UserInfoActvity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.UserRecordActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.AppSettingConfigFragment;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.DeveloperFragement;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.robot.AppFragMent;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.AppDetailFragment;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.ShopFragement;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.user.MyFavorite;
import com.ubtechinc.alpha2ctrlapp.util.FileReceive;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.util.PictureAcceptThread;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.util.UlitStatuBar;
import com.ubtechinc.alpha2ctrlapp.widget.ConfirmFindPhoneDialog;
import com.ubtechinc.alpha2ctrlapp.widget.ConfirmFindPhoneDialog.OnPositiveClickLitstener;
import com.ubtechinc.alpha2ctrlapp.widget.MyBottomRelativeLayout;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.alpha2ctrlapp.widget.popWindow.GuidePopView;
import com.ubtechinc.alpha2ctrlapp.widget.popWindow.MenuPopuWindow;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.im.event.IMStateChange;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


/**
 * 主页
 */
public class MainPageActivity extends MainPageBaseActivity implements OnClickListener, OnPositiveClickLitstener, MyBottomRelativeLayout.setOnSlideListener {
    private String TAG = "MainPageActivity";

    private TextView btn_mypage, btn_shop;// app_name;
    public ImageView userHeader;
    private Integer currentPage;
    public int shopType = BusinessConstants.SHOP_TYPE_APP;
    //机器人操作页，包含动作表、相册、闹钟等
    private AppFragMent appFragMent;
    //菜单栏，根据执行的应用不同，显示不同菜单
    public MenuPopuWindow menuWinDow;
    //当前页
    public BaseContactFragememt currentFragment;
    //我的收藏
    public MyFavorite myfavoriteFrage;
    public AppInstalledInfo currentAppInfo = new AppInstalledInfo();
    public RelativeLayout mainTopView;
    public AppSettingConfigFragment appConfigSetting;
    //开发者选项页
    public DeveloperFragement developer;
    private ImageView menuSetButton;
    private ImageView menuBtnClose;
    private ImageView btncmd;
    public SlidingMenu mLeft_menu;
    public RoundImageView img_head;
    public TextView txt_nick_name_head;
    private RelativeLayout lay_setting, lay_my_favo, lay_my_robot, lay_about;
    private RelativeLayout lay_exsit;
    public List<AppInfo> updateApps = new ArrayList<AppInfo>();
    public static List<AppUpdate> appInfoList = new ArrayList<AppUpdate>();
    public String[] appdrwableList, actiondrwableList;
    ;
    public List<ActionDownLoad> actionList = new ArrayList<ActionDownLoad>();
    public boolean hasEdit = true;
    public MyBottomRelativeLayout bottomLay;
    public TextView btn_finishEdit;

    private ImageView ivStartRecord;//底部启动闲聊记录按钮
    private PictureAcceptThread pictureThread;
    private ImageView btn_msg;
    public int pictureMsg = 0;
    public int msgCount = 0;
    public int otherMsg = 0;
    //	private BadgeView bv;// 消息空间
    public static List<ChatInfoItem> msgList = new ArrayList<ChatInfoItem>();
    public static boolean hasConnectedNewRobot = false;
    private NoticeManager noticeManager;
    private FileReceive mFileReceiVe;
    private long existTime = 0;
    private ImageView has_new_icon;
    private boolean isPlay = false;
    //	public  static List<RobotApp> downAppList = new ArrayList<RobotApp>();
//	public  static List<ActionFileEntrity> downActionList = new ArrayList<ActionFileEntrity>();
//	public 	GetActionFileList LocalAction  = new GetActionFileList();
    private ImageView btn_mypage_line, btn_shop_line;
    //数据库
    public static DBDao dao;
    public static boolean hasGetLocolList = true;
    private MediaPlayer mediaPlayer;
    private List<String> list = new ArrayList<String>();
    private ConfirmFindPhoneDialog stopMusicDialog;
    private TextView deviceNameTips;
    private GuidePopView guidePopView;
    private String mCurrentSetLanguage = "";
    /**
     * 极光推送相关字段
     **/
    public static final String MESSAGE_RECEIVED_ACTION = "com.ubtechinc.alpha2ctrlapp.mMainPageActivity.custom.MESSAGE_RECEIVED_ACTION";
    public static final String GET_NOTICE_MESSAGE_ACTION = "com.ubtechinc.alpha2ctrlapp.mMainPageActivity.custom.GET_NOTICE_MESSAGE_ACTION";

    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    public static boolean isFromChangeLan = false;
    public static MainPageActivity instance;
    /**
     * Alpha 的配置
     **/
    public static AlphaParam alphaParam = null;
    public static String LastVersion;// 最新的版本号
    public static String versionPath; // 最新的版本下载地址
    //商店页
    private ShopFragement shopFragement;
    private AppDetailFragment appDetailFragment;
    private Button btnLoginSpotify;
    private RelativeLayout contactUsLay;
    private RelativeLayout rlMyPage;
    private RelativeLayout rlShopPage;
    /**
     * 手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
     */
    private float x1 = 0;
    private float x2 = 0;
    private float y1 = 0;
    private float y2 = 0;

    /**
     * 往左边滑动的最小距离，大于这个值时显示悬浮按钮
     */
    private static final int MIN_SCROLL_LEGTH = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_fragment);
        UlitStatuBar.assistActivity(this, findViewById(R.id.layout_fragment_contanier));
        registerBroadcastReceiver();
        instance = this;
        this.mContext = this;
        dao = DBDao.getInstance(this);
        appFragMent = new AppFragMent();
        appConfigSetting = new AppSettingConfigFragment();
        developer = new DeveloperFragement();
        currentFragment = appFragMent;
        myfavoriteFrage = new MyFavorite();
        shopFragement = new ShopFragement();
        appDetailFragment = new AppDetailFragment();
        if (savedInstanceState == null)
            onChangeFragment(appFragMent, false, null);
        btn_mypage = (TextView) findViewById(R.id.btn_my_page);
        btn_shop = (TextView) findViewById(R.id.btn_shop);
        btn_mypage_line = (ImageView) findViewById(R.id.btn_my_page_line);
        btn_shop_line = (ImageView) findViewById(R.id.btn_shop_line);
        userHeader = (ImageView) findViewById(R.id.user_header);
        rlMyPage = (RelativeLayout) findViewById(R.id.rl_my_page);
        rlShopPage = (RelativeLayout) findViewById(R.id.rl_shop_page);

        btn_msg = (ImageView) findViewById(R.id.btn_msg);
        btn_msg.setOnClickListener(this);
        btncmd = (ImageView) findViewById(R.id.btn_cmd);
        btncmd.setOnClickListener(this);

        ivStartRecord = (ImageView) findViewById(R.id.iv_startrecord);
        ivStartRecord.setOnClickListener(this);

        rlMyPage.setOnClickListener(this);
        rlShopPage.setOnClickListener(this);

        userHeader.setOnClickListener(this);
        currentPage = R.id.rl_my_page;
        mainTopView = (RelativeLayout) findViewById(R.id.main_page_top_view);
        mainTopView.setVisibility(View.VISIBLE);
        /********左边侧边栏配置开始*****/
        mLeft_menu = new SlidingMenu(this);
        mLeft_menu.setMenuIcon(userHeader);
        mLeft_menu.setMode(SlidingMenu.LEFT);
        mLeft_menu.setShadowWidth(10);
        mLeft_menu.setShadowDrawable(R.drawable.shadow);
        mLeft_menu.setBehindOffset(250);
        mLeft_menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        initLeft();
        /********左边侧边栏配置结束*****/

        btn_finishEdit = (TextView) findViewById(R.id.btn_finish);
        bottomLay = (MyBottomRelativeLayout) findViewById(R.id.bottom_status);
        bottomLay.setmSetOnSlideListener(this);
        btn_finishEdit.setOnClickListener(this);
        noticeManager = NoticeManager.getInstance();
        //引导页
        guidePopView = new GuidePopView(this, RobotManagerService.getInstance().isConnectedRobot());
        EventBus.getDefault().register(this);
    }


    /**
     * 初始化左边侧边栏相关信息
     *
     * @param
     */
    private void initLeft() {
        mLeft_menu.setMenu(R.layout.layout_left_page);
        lay_setting = (RelativeLayout) mLeft_menu
                .findViewById(R.id.lay_setting);
        lay_setting.setOnClickListener(this);
        lay_my_favo = (RelativeLayout) mLeft_menu
                .findViewById(R.id.lay_my_favo);
        lay_my_favo.setOnClickListener(this);
        lay_my_robot = (RelativeLayout) mLeft_menu
                .findViewById(R.id.lay_my_robot);
        lay_my_robot.setOnClickListener(this);
        has_new_icon = (ImageView) mLeft_menu.findViewById(R.id.has_new_icon);
        lay_about = (RelativeLayout) mLeft_menu.findViewById(R.id.lay_about);
        deviceNameTips = (TextView) mLeft_menu.findViewById(R.id.devices_name);
        mLeft_menu.setV(has_new_icon);
        lay_about.setOnClickListener(this);
        lay_exsit = (RelativeLayout) mLeft_menu.findViewById(R.id.lay_exsit);
        lay_exsit.setOnClickListener(this);
        View root = mLeft_menu.getRootView();
        img_head = (RoundImageView) root.findViewById(R.id.img_head);
        txt_nick_name_head = (TextView) root.findViewById(R.id.txt_nick_name_head);
        img_head.setOnClickListener(this);
        contactUsLay = (RelativeLayout) mLeft_menu.findViewById(R.id.lay_contact_us);
        contactUsLay.setOnClickListener(this);
    }

    public final BaseHandler mHandler = new BaseHandler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageType.CHECK_SPOTIFY_LOGIN:
                    SpotifyLoginPresenterImpl.getInstance().checkLoginState(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT);
                    break;
                case MessageType.ALPHA_GET_MP3://播放音频
                    String filePath = (String) msg.obj;
                    startPlayMp3(filePath);
                    if (stopMusicDialog == null)
                        stopMusicDialog = new ConfirmFindPhoneDialog(MainPageActivity.this);
                    stopMusicDialog.show();
                    stopMusicDialog.setPositiveClick(MainPageActivity.this);
                    break;
                case MessageType.ALPHA_STOP_MP3:// 停止播放音频
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
//                    FindMobilePhoneData findData = new FindMobilePhoneData();
//                    findData.setSendByClient(true);
//                    sendRequest(findData, SocketCmdId.ALPHA_MSG_FIND_MOBILEPHONE); // 告诉机器人已经找到手机
                    break;
                case SocketCmdId.ALPHA_MSG_RSP_FIND_MOBILEPHONE:
                    if (list.size() > 0) {
                        mHandler.obtainMessage(MessageType.ALPHA_GET_MP3, list.get(0)).sendToTarget(); //已经查到mp3 文件就直接播放
                    } else
                        chekMp3File(); // 查找mp3文件
                    break;
                case MessageType.ALPHA_HAS_TAKE_PIC: {//Alpha拍照
                    getLocalUnReadNoticeMessage();
//				if (msg.obj != null)
//					sendReivePicRsp((TransferPhotoInfo) msg.obj);

                }
                break;
                case SocketCmdId.ALPHA_MSG_RSP_TRANSFER_BIG_PHOTO://请求大图
                    getLocalUnReadNoticeMessage();
                    break;
                case MessageType.ALPHA_GET_PIC_FAILED://获取图片失败
                    break;
                case MessageType.ALPHA_CHECK_LIST_DIALOG:
                    if (LoadingDialog.mDia != null && !LoadingDialog.mDia.isShowing()) {
                        LoadingDialog.mDia.show();
                    } else if (LoadingDialog.mDia == null) {
                        LoadingDialog.getInstance(mContext).show();
                    }
                    break;


                case 0:
                    getAppButtonConfig();
                    break;


            }
        }
    };


    public void noApp() {
        currentAppInfo = new AppInstalledInfo();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == currentPage) {

            return;
        } else {
            switch (v.getId()) {
                case R.id.user_header:
                    mLeft_menu.showMenu();
                    break;
                case R.id.rl_my_page://我的机器人
                    changeType(true);
                    break;
                case R.id.rl_shop_page://商店
                    changeType(false);
                    break;
                case R.id.iv_startrecord:
                    startActivity(new Intent(this, UserRecordActivity.class));
                    overridePendingTransition(R.anim.activity_open, 0);
                    break;

                case R.id.btn_setting://菜单里的设置
                    if (currentFragment != null) {
                        onChangeFragment(appConfigSetting, false, null);
                        currentFragment = appConfigSetting;
                        dismissMenuPop();
                    }
                    break;
                case R.id.btn_spotify_login:
                    LoadingDialog.getInstance(mContext).show();
                    mHandler.sendEmptyMessageDelayed(MessageType.CHECK_SPOTIFY_LOGIN, 5 * 1000);//英语闲未完全启动时不能响应，故延时三秒发送检测spotify 登录
                    break;
                case R.id.lay_setting://系统设置
                    Intent settingIntent = new Intent(MainPageActivity.this, AppSettingActivity.class);
                    startActivity(settingIntent);
                    overridePendingTransition(R.anim.push_left_in, 0);
                    mLeft_menu.toggle();
                    break;
                case R.id.lay_my_favo://我的收藏
                    if (currentFragment != null) {
                        onChangeFragment(myfavoriteFrage, false, null);
                        currentFragment = myfavoriteFrage;
                        dismissMenuPop();
                        mLeft_menu.toggle();
                    }
                    break;
                case R.id.lay_my_robot://我的设备
                    goMainMode(false);
                    mLeft_menu.toggle();
                    break;
                case R.id.btn_finish:
                    //设置完个人信息，更新我的主页表
                    hasEdit = true;
                    btn_finishEdit.setVisibility(View.GONE);
                    if (Tools.isShowBottom(alphaParam, mContext)) {
                        bottomLay.setVisibility(View.VISIBLE);
                    } else {
                        bottomLay.setVisibility(View.GONE);
                    }
                    AppFragMent fra = (AppFragMent) currentFragment;
                    fra.appInfoAdapter.notifyDataSetChanged();
                    break;
                case R.id.lay_about://关于
                    Intent aboutIntent = new Intent(MainPageActivity.this, AboutActivity.class);
                    startActivity(aboutIntent);
                    overridePendingTransition(R.anim.push_left_in, 0);
                    mLeft_menu.toggle();
                    break;
                case R.id.lay_contact_us:
                    Intent contactIntent = new Intent(MainPageActivity.this, ContactusActivity.class);
                    startActivity(contactIntent);
                    overridePendingTransition(R.anim.push_left_in, 0);
                    mLeft_menu.toggle();
                    break;
                case R.id.lay_exsit://退出登录
                    doLoginOut(false);
                    break;
                case R.id.img_head://头像
                    Intent intent2 = new Intent(MainPageActivity.this, UserInfoActvity.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.push_left_in, 0);
                    mLeft_menu.toggle();
                    break;
//                case R.id.no_robot://没有设备时，跳转到我的设备页面，连接设备
//                    goMainMode(true);
//                    break;
                case R.id.btn_msg://我的消息
                    if (!Tools.isFastClick(2000)) {
                        Intent intent = new Intent(MainPageActivity.this, JpushReceiveActivty.class);
                        MainPageActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, 0);
                        msgList.clear();
                        msgCount = 0;
                        pictureMsg = 0;
                        otherMsg = 0;
                    }
                    break;
                case R.id.btn_cmd:
                    if (!Tools.isFastClick(2000)) {
                        Intent intent1 = new Intent(MainPageActivity.this, VoiceCmdActivity.class);
                        MainPageActivity.this.startActivity(intent1);
                        overridePendingTransition(R.anim.push_left_in, 0);
                    }
                    break;
                case R.id.btn_close: // 趣声菜单栏的关闭按钮
                    dismissMenuPop();
                    AppInstalledInfo apkInfo = null;
                    if (alphaParam != null) {

                        if (alphaParam.getServiceLanguage().equalsIgnoreCase("zh_cn")) {
                            apkInfo = dao.queryApk(BusinessConstants.PACKAGENAME_CH_CHAT);
                        } else {
                            apkInfo = dao.queryApk(BusinessConstants.PACKAGENAME_EN_CHAT);
                        }
                    }
                    if (apkInfo != null) {
                        startNewApp(apkInfo);
                    } else {
                        RobotAppRepository.getInstance().startApp(currentAppInfo.getName(), currentAppInfo.getPackageName(), new IRobotAppDataSource.ControlAppCallback() {
                            @Override
                            public void onSuccess() {
                                if (!currentFragment.getClass().getName().equals(AppFragMent.class.getName())) {
                                    refreshBottom(true);
                                }
                            }

                            @Override
                            public void onFail(ThrowableWrapper e) {

                            }
                        });

                    }
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * 显示菜单栏
     */
    private void showMenuPop() {
        if (menuWinDow != null && !menuWinDow.isShowing())
            menuWinDow.show();
    }

    /**
     * 消失菜单栏
     */
    private void dismissMenuPop() {
        if (menuWinDow != null && menuWinDow.isShowing())
            menuWinDow.dismiss();
    }

    /**
     * 销毁菜单栏
     */
    private void destoryMenuPop() {
        if (menuWinDow != null) {
            dismissMenuPop();
            menuWinDow = null;
        }
    }

    private boolean isMenuPop() {
        return menuWinDow != null && menuWinDow.isShowing();
    }

    /**
     * 刷新底部菜单界面
     */
    public void changeCurrentApp() {
        if (Tools.isShowBottom(alphaParam, mContext)) {
            bottomLay.setVisibility(View.VISIBLE);
        } else {
            bottomLay.setVisibility(View.GONE);
        }


    }

    /**
     * 跳转我的设备列表
     */
    private void goMainMode(boolean fromBottom) {
        Intent intent = new Intent(this, MyDeviceActivity.class);
        intent.putExtra("isFirst", false);
        startActivity(intent);
        if (!fromBottom)
            overridePendingTransition(R.anim.push_left_in, 0);
        else
            overridePendingTransition(R.anim.menu_enter, 0);

    }

    public void showMenuDiag(ButtonConfig ss, BaseContactFragememt fragment,
                             boolean hasSetting) {
        if (currentAppInfo != null && currentAppInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_ALPHA_TRANSLATION)) {
            return;
        }
        if (menuWinDow == null) {
            menuWinDow = new MenuPopuWindow(this, fragment);
        }
        try {
            menuWinDow.refresh(ss, currentAppInfo.getPackageName(), currentFragment, hasSetting);
            menuSetButton = (ImageView) menuWinDow.findViewById(R.id.btn_setting);
            menuBtnClose = (ImageView) menuWinDow.findViewById(R.id.btn_close);
            btnLoginSpotify = (Button) menuWinDow.findViewById(R.id.btn_spotify_login);
            menuBtnClose.setOnClickListener(this);
            menuSetButton.setOnClickListener(this);
            btnLoginSpotify.setOnClickListener(this);
            showMenuPop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();

    }

    @Override
    public void onResume() {
        isForeground = true;
        super.onResume();
        Alpha2Application.getInstance().removeActivity();
        if (SPUtils.get().getBoolean(PreferenceConstants.IS_FROM_THRID_AUTH, false)) {//是否来自第三方验证
            Intent sendIntent = new Intent(this, AuthorizationActivity.class);
            String appId = SPUtils.get().getString(PreferenceConstants.THRID_APP_ID);
            String appKey = SPUtils.get().getString(PreferenceConstants.THRID_APP_KEY);
            sendIntent.putExtra(AidlService.AIDL_DATA_APPID, appId);
            sendIntent.putExtra(AidlService.AIDL_DATA_APPKEY, appKey);
            startActivity(sendIntent);
            SPUtils.get().put(PreferenceConstants.IS_FROM_THRID_AUTH, false);
        } else if (SPUtils.get().getBoolean(PreferenceConstants.IS_FROM_THRID_CONFIG_NETWORK, false)) {//是否来自第三方配置网络
            Intent intent = new Intent();
            RobotInfo robot = (RobotInfo) SPUtils.get().getObject(RobotInfo.class);
            intent.setClass(mContext, ConfigureRobotNetworkActivity.class);
            intent.putExtra("isFromDevice", true);
            intent.putExtra(Constants.ROBOTSN, robot.getEquipmentId());
            if (!StringUtils.isEmpty(robot.getMacAddress())) {
                intent.putExtra(Constants.ROBOT_MAC, robot.getMacAddress());
            }
            SPUtils.get().put(PreferenceConstants.IS_FROM_THRID_CONFIG_NETWORK, false);
        } else if (SPUtils.get().getBoolean(PreferenceConstants.IS_FROM_THRID_DOWNLOAD_APP, false)) {//是否来自第三方下载机器人App
            Bundle bundle = new Bundle();
            bundle.putInt(IntentConstants.DATA_APP_ID, Integer.valueOf(SPUtils.get().getString(PreferenceConstants.THRID_APP_ID)));
            bundle.putBoolean(IntentConstants.DATA_IS_DOWNLOAD, true);
            onChangeFragment(appDetailFragment, false, bundle);
            SPUtils.get().put(PreferenceConstants.IS_FROM_THRID_DOWNLOAD_APP, false);
        } else if (!RobotManagerService.getInstance().isConnectedRobot() && !SPUtils.get().getBoolean(Constants.VERSION_CODE + Constants.main_menu_guide, false)) {
            guidePopView.refresh(false);
            guidePopView.show();
            SPUtils.get().put(Constants.VERSION_CODE + Constants.main_menu_guide, true);
        }

        mFileReceiVe = FileReceive.getInstance(mContext, noticeManager, mHandler);
        if (RobotManagerService.getInstance().isConnectedRobot()) {
            deviceNameTips.setText(Constants.deviceName);

        } else {
            deviceNameTips.setText(R.string.main_page_connect_alpha_tips);

        }

        if (hasConnectedNewRobot && !currentFragment.getClass().getName().equals(AppFragMent.class.getName())) {
            currentPage = R.id.rl_my_page;
            onChangeFragment(appFragMent, false, null);
            rlShopPage.setBackgroundResource(R.drawable.main_page_title_right_gray_bg);
            rlMyPage.setBackgroundResource(R.drawable.main_page_title_left_bg);
            btn_shop.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_t5));
            btn_mypage.setTextColor(mContext.getResources().getColor(
                    R.color.white));
        }
        if (isFromChangeLan) {
            isFromChangeLan = false;
        }
        txt_nick_name_head.setText(SPUtils.get().getString(PreferenceConstants.USER_NAME));

        mCurrentSetLanguage = SPUtils.get().getString(Constants.APP_LAUNGUAGE);


        if (StringUtils.isEquals(mCurrentSetLanguage, BusinessConstants.APP_LANGUAGE_EN) || !Tools.isZh(mContext)) {
            btncmd.setVisibility(View.GONE);
        } else if (StringUtils.isEquals(mCurrentSetLanguage, BusinessConstants.APP_LANGUAGE_CN) || (TextUtils.isEmpty(mCurrentSetLanguage) && Tools.isZh(mContext))) {
            btncmd.setVisibility(View.VISIBLE);
        }

        if (RobotManagerService.getInstance().isConnectedRobot() && !Tools.isCh(mContext)) {
            bottomLay.setVisibility(View.GONE);
        }

        LoadImage.LoadHeader(this, 0, img_head, SPUtils.get().getString(PreferenceConstants.USER_IMAGE));
        getLocalUnReadNoticeMessage();
    }


    @Override
    public void onDestroy() {
        destoryMenuPop();
        if (pictureThread != null) {
            pictureThread.stopProcess();
            pictureThread = null;
        }
        EventBus.getDefault().unregister(this);
        unRegisterBroadcastReceiver();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop(); // 退出程序时停止播放
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (!Constants.hasLoginOut && !isFromChangeLan)
            Alpha2Application.getInstance().exit(true);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// must store the new intent unless getIntent() will
        // return the old one
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            String currentName = currentFragment.getClass().getName();
            if (currentName.equals(AppFragMent.class.getName())) {
                if (!hasEdit) {
                    hasEdit = true;
                    btn_finishEdit.setVisibility(View.GONE);
                    if (Tools.isShowBottom(alphaParam, mContext)) {
                        bottomLay.setVisibility(View.VISIBLE);
                    } else {
                        bottomLay.setVisibility(View.GONE);
                    }
                    AppFragMent fra = (AppFragMent) currentFragment;
                    fra.onRefreshAfterEdit();
                    return false;
                } else {
                    exist();
                    return false;
                }

            }
            if (currentName.equals(shopFragement.getClass().getName())) {
                Logger.d("nxy", "主界面返回 ");
                exist();
                return false;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void showAppGuide() {
        if (RobotManagerService.getInstance().isConnectedRobot() && !SPUtils.get().getBoolean(Constants.VERSION_CODE + Constants.main_app_guide, false)) {
            guidePopView.refresh(true);
            guidePopView.show();
            SPUtils.get().put(Constants.VERSION_CODE + Constants.main_app_guide, true);
        }
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MESSAGE_RECEIVED_ACTION)) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!TextUtils.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                if (!TextUtils.isEmpty(showMsg)) {
                    ToastUtils.showLongToast(showMsg + "");

                }
                LoadingDialog.dissMiss();
            } else if (action.equals(GET_NOTICE_MESSAGE_ACTION)) {
                getLocalUnReadNoticeMessage();
                LoadingDialog.dissMiss();
            } else if (action.equals(AidlService.ACTION_SEND_DATA_TO_SERVICE)) {// 将第三方发送的数据，发送给主服务，并且反馈错误码
                String errorStr = null;
                if (RobotManagerService.getInstance().isConnectedRobot()) {

                    String keyId = intent.getStringExtra(AidlService.AIDL_KEYID_FROM_THIRD);
                    String data = intent.getStringExtra(AidlService.AIDL_SEND_DATA_FROM_THIRD);

//                    try {
//                        errorStr = ConstactsTools.createThirdNotificationIQ(keyId, data, Alpha2Application.getRobotSerialNo(), context);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                } else {

                    errorStr = mContext.getResources().getString(R.string.main_page_connect_alpha_tips);
                }
                /**
                 * 有错误就反馈错误码
                 */
                if (!TextUtils.isEmpty(errorStr)) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(AidlService.RECEIVE_ERROR_DATA);
                    sendIntent.putExtra(AidlService.AIDL_RECEIVE_ERROR_CODE, errorStr);
                    sendBroadcast(sendIntent);
                }
            }


        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IMStateChange event) {
        if (!Constants.hasLoginOut && (event.getState() == IMStateChange.STATE_DISCONNECTED)) {
            String currentName = currentFragment.getClass().getName();
            if (currentName.equals(AppFragMent.class.getName())) {
                AppFragMent f = (AppFragMent) getFragmentManager().findFragmentByTag(AppFragMent.class.getName());
                if (f != null)
                    f.refreshView();
            }
            /**
             * 提示第三方机器人掉线
             */
            {
                Intent sendIntent = new Intent();
                sendIntent.setAction(AidlService.RECEIVE_ERROR_DATA);
                sendIntent.putExtra(AidlService.AIDL_RECEIVE_ERROR_CODE, getString(R.string.main_page_alpha2_offline));
                sendBroadcast(sendIntent);
            }
            destoryMenuPop();
        }

        if (event.getState() == IMStateChange.STATE_FORCE_OFFLINE) {
            doLoginOut(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RobotControlEvent event) {
        switch (event.getAction()) {
            case RobotControlEvent.CONNECT_SUCCESS:
                sendTime();
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RobotIMStateChangeEvent event) {
        if (!RobotManagerService.getInstance().isConnectedRobot()) {
            deviceNameTips.setText(R.string.main_page_connect_alpha_tips);
            String currentName = currentFragment.getClass().getName();
            if (currentName.equals(AppFragMent.class.getName())) {
                AppFragMent f = (AppFragMent) getFragmentManager().findFragmentByTag(AppFragMent.class.getName());
                if (f != null)
                    f.refreshView();
            }
        }
    }

    /**
     * 机器人连接上之后设置时间
     */
    private void sendTime() {
        String date = TimeUtils.getCurrentTimeInString(TimeUtils.DEFAULT_DATE_FORMAT2);
        CmSetRTCTimeData.CmSetRTCTimeDataRequest.Builder builder = CmSetRTCTimeData.CmSetRTCTimeDataRequest.newBuilder();
        String[] dateStr = date.split("-");
        builder.setYear(Integer.valueOf(dateStr[0]));
        builder.setMonth(Integer.valueOf(dateStr[1]));
        builder.setDay(Integer.valueOf(dateStr[2]));
        builder.setHour(Integer.valueOf(dateStr[3]));
        builder.setMinute(Integer.valueOf(dateStr[4]));
        builder.setSecond(Integer.valueOf(dateStr[5]));
        Calendar c = Calendar.getInstance();
        int week = Integer.valueOf(c.get(Calendar.DAY_OF_WEEK));
        builder.setWeek(week);
        TimeZone tz = TimeZone.getDefault();
        builder.setTimeZone(tz.getID());//时区
        RobotInitRepository.getInstance().setRTCTime(builder.build(), new IRobotInitDataSource.SetInitDataCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(ThrowableWrapper e) {

            }
        });


    }

    /**
     * 注册广播
     * 1.监听好友上下线状态
     * 2.推送相关信息
     * 3.Alpha控制相关
     */
    public void registerBroadcastReceiver() {
        // TODO Auto-generated method stub
        IntentFilter filter = new IntentFilter();
        /**极光推送相关开始**/
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        /**极光推送结束**/
        filter.addAction(GET_NOTICE_MESSAGE_ACTION);

        filter.addAction(AidlService.ACTION_SEND_DATA_TO_SERVICE);
        filter.addAction(AidlService.ACTION_SEND_TO_VERIFY);
        registerReceiver(receiver, filter);

    }

    public void unRegisterBroadcastReceiver() {
        // TODO Auto-generated method stub
        if (receiver != null) {

            try {
                this.unregisterReceiver(receiver);
            } catch (Exception e) {

            }
        }
        receiver = null;
    }


    private void exist() {
        if ((System.currentTimeMillis() - existTime) > 2000) {
            ToastUtils.showShortToast(R.string.main_page_exist_tip);
            existTime = System.currentTimeMillis();
        } else {
            Alpha2Application.getAlpha2().exit(true);
        }
    }

    /**
     * @param
     * @return void
     * @Description 本地获取未读消息
     */
    private void getLocalUnReadNoticeMessage() {
        if (SPUtils.get().getBoolean(PreferenceConstants.IS_OPEN_MSG_TIPS, true)) {//是否打开消息推送
            msgCount = noticeManager.getUnReadNoticeCount();
            if (msgCount > 0) {
                btn_msg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message__new_icon));
            } else {
                btn_msg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_icon));
            }
        } else {
            btn_msg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_icon));
        }
    }

    private void goMainActivity(boolean isForceOffline) {
        Intent intent = new Intent();

        SPUtils.get().put(PreferenceConstants.LOGIN_TIME, "");
        Constants.hasShowDiag = false;
        Constants.hasLoginOut = true;
        RobotManagerService.getInstance().clearConnectCacheData();
        intent.setClass(mContext, MainActivity.class);
        intent.putExtra(MainActivity.FORCE_OFFLINE, isForceOffline);
        Alpha2Application.getAlpha2().exit(false);
        startActivity(intent);

    }

    private void doLoginOut(boolean isForceOffline) {
        goMainActivity(isForceOffline);
        LoginAndOutReponsitory.getInstance().loginOut(new ILoginDataSource.LoginOutCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(ThrowableWrapper e) {
                //ToastUtils.showShortToast(e.getMessage());
            }


        });

    }


    public void startApp(AppInstalledInfo appInfo) {
        appInfo = dao.queryApk(appInfo.getPackageName());
        if (currentAppInfo != null) {
            if (currentAppInfo.getPackageName().equals(appInfo.getPackageName())) {
                if ((currentAppInfo.isButtonEvent()
                        || currentAppInfo.isSetting()
                        || currentAppInfo.getPackageName().equals(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT))) {
                    if (menuWinDow != null && !menuWinDow.isShowing()) {
                        menuWinDow.show();
                    } else {
//								getButton(true);
                    }

                }
            } else {
                startNewApp(appInfo);
            }

        } else {
            startNewApp(appInfo);
        }
    }

    private void startNewApp(AppInstalledInfo appInfo) {

        if (currentAppInfo != null) {
            // 停止当前的App
            if (currentAppInfo.getPackageName().equals(BusinessConstants.PACKAGENAME_TEMP_ALARM)) {

                RobotActionRepository.getInstance().stopPlayAction(new IRobotActionDataSource.ControlActionCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(ThrowableWrapper e) {

                    }
                });


            } else {
                RobotAppRepository.getInstance().stopApp(currentAppInfo.getName(), currentAppInfo.getPackageName(), new IRobotAppDataSource.ControlAppCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(ThrowableWrapper e) {

                    }
                });
            }
            if (menuWinDow != null) {
                menuWinDow = null;
            }
        }
        currentAppInfo = MainPageActivity.dao.queryApk(appInfo.getPackageName());
        LoadingDialog.getInstance(mContext).show();
        RobotAppRepository.getInstance().startApp(currentAppInfo.getName(), currentAppInfo.getPackageName(), new IRobotAppDataSource.ControlAppCallback() {
            @Override
            public void onSuccess() {
                if (!currentFragment.getClass().getName().equals(AppFragMent.class.getName())) {
                    refreshBottom(true);
                }
            }

            @Override
            public void onFail(ThrowableWrapper e) {

            }
        });

    }

    /**
     * 设置并刷新底部菜单
     *
     * @needDelay 是否需要延时
     */
    public void refreshBottom(boolean needDelay) {
        if (currentAppInfo == null || currentAppInfo == null) {
            LoadingDialog.dissMiss();
        } else {
            getMenuDiag(needDelay);
        }


    }

    /**
     * 获取的菜单
     *
     * @param deLay 是否需要延时
     */
    public void getMenuDiag(boolean deLay) {
        if (currentAppInfo != null && currentAppInfo != null && currentAppInfo.isButtonEvent()) {


            if (!deLay) {
                getAppButtonConfig();

            } else {
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessageDelayed(msg, 1000);
            }

        } else {
            if ((currentAppInfo != null && currentAppInfo.isSetting())
                    || currentAppInfo.getPackageName().equals(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT)) {
                showMenuDiag(null, currentFragment, currentAppInfo.isSetting());
            }
            LoadingDialog.dissMiss();
        }
    }

    private void getAppButtonConfig() {
        RobotAppRepository.getInstance().getAppButton(0, currentAppInfo.getPackageName(), "en".getBytes(), new IRobotAppDataSource.GetAppButtonConfigCallback() {
            @Override
            public void onLoadButtonConfig(ButtonConfig buttonConfig) {
                if (!currentFragment.getClass().getName().equals(AppFragMent.class.getName())) {
                    LoadingDialog.dissMiss();

                    if (buttonConfig.getModels().size() > 0 || currentAppInfo.isSetting()) {
                        showMenuDiag(buttonConfig, currentFragment,
                                currentAppInfo.isSetting());
                    }
                }
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });
    }

    /**
     * [执行动作]
     *
     * @author zengdengyi
     */
    public void onPlayAction(String id, String mFileName) {
        if (currentAppInfo != null) {
            // 停止当前的App
            if (!currentAppInfo.getPackageName().equals(
                    BusinessConstants.PACKAGENAME_TEMP_ALARM)) {

                RobotAppRepository.getInstance().stopApp(currentAppInfo.getName(), currentAppInfo.getPackageName(), new IRobotAppDataSource.ControlAppCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(ThrowableWrapper e) {

                    }
                });
                if (menuWinDow != null) {
                    menuWinDow = null;
                }
            }
        } else {
            AppInstalledInfo builder = new AppInstalledInfo();
            builder.setName(mFileName);
            builder.setPackageName(BusinessConstants.PACKAGENAME_TEMP_ALARM);
            builder.setAppId(id);
            currentAppInfo = builder;
        }

        RobotActionRepository.getInstance().playAction(id, new IRobotActionDataSource.ControlActionCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(ThrowableWrapper e) {

                    }
                }
        );

        changePlay(false, mFileName);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("actionName", mFileName);
        MobclickAgent.onEvent(mContext, Constants.YOUMENT_MY_ACTION_PLAY_TIMES, map);
    }

    //		private void addLocalAction(ActionFileEntrity enty){
//			if(enty.getDownloadState()==4){
//				boolean has =false;
//				for(int i =0;i<LocalAction.getFileList().size();i++){
//					String  tempData = LocalAction.getFileList().get(i);
//					if(tempData.equals(enty.getActionName())){
//						has = true;
//						break;
//					}
//				}
//				if(!has){
//					LocalAction.getFileList().add(enty.getActionName());
//				}
//			}
//
//		}
    public void changePlay(boolean isStop, String mFileName) {
        if (isStop) {
            //   btn_menu.setBackgroundResource(R.drawable.menu_play_icon);
            isPlay = false;
        } else {
            // appIcon.setImageDrawable(context.getResources()
            //         .getDrawable(R.drawable.move_selected));
            //  app_name.setText(mFileName);
            // appIcon.setVisibility(View.VISIBLE);
            // app_name.setVisibility(View.VISIBLE);
            // btn_menu.setBackgroundResource(R.drawable.menu_stop_icon);
            isPlay = true;
        }
    }

    /**
     * 添加下载的app 到下载的app 列表中，再发送命令进行下载
     *
     * @param info      要下载的app 信息
     * @param imagePath app的图标
     * @param appId     app 的id
     */
    public void downLoadApp(RobotApp info, String imagePath, int versionCode) {

        if (info.getPackageName().equals(BusinessConstants.PACKAGENAME_CH_CHAT) && !TextUtils.isEmpty(Alpha2Application.getInstance().getChatIconPath())) {
            imagePath = Alpha2Application.getInstance().getChatIconPath();
        }
        info.setDownloadState(BusinessConstants.APP_STATE_INIT);

        dao.updateAppIcon(info.getPackageName(), info.getName(), imagePath, versionCode + "");
        RobotAppRepository.getInstance().updateApp(info, new IRobotAppDataSource.ControlAppCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShortToast("启动下载成功");
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });


    }

    /**
     * 添加下载的action 到下载的action 列表中，再发送命令进行下载
     */
    public void downLoadAction(ActionFileEntrity actionFileEntrity, ActionDownLoad downloadObj, ActionDetail detailObj) {
        actionFileEntrity.setDownloadState(1);
        MainPageActivity.dao.updateActionDownloadState(actionFileEntrity, downloadObj, detailObj);
        RobotActionRepository.getInstance().downloadAction(actionFileEntrity, new IRobotActionDataSource.ControlActionCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShortToast("启动下载成功");
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });

    }

    /**
     * [停止动作]
     *
     * @author zengdengyi
     */
    public void onStopAction() {
        // TODO Auto-generated method stub
//        sendRequest(null, MessageType.ALPHA_STOPACTION_REQUEST);

        RobotActionRepository.getInstance().stopPlayAction(new IRobotActionDataSource.ControlActionCallback() {
                                                               @Override
                                                               public void onSuccess() {

                                                               }

                                                               @Override
                                                               public void onFail(ThrowableWrapper e) {

                                                               }
                                                           }
        );
        changePlay(true, "");
    }

    /**
     * @return
     * @Description 获取动作列表
     */
    public void onGetActionList() {

        RobotActionRepository.getInstance().getActionList(Constants.SYSTEM_LAN, 0, new IRobotActionDataSource.GetActionListCallback() {
            @Override
            public void onLoadActionList(List<NewActionInfo> actionInfoList) {
                dao.insertActionList(actionInfoList);
                hasGetLocolList = true;
                currentFragment.refreshDownLoadData();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });
        hasGetLocolList = false;
    }


    private void chekMp3File() {
        //是否有外部存储设备
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            new Thread(new Runnable() {
                String[] ext = {".mp3"};
                File file = Environment.getExternalStorageDirectory();

                public void run() {
                    search(file, ext); // 取第一个查到的MP3文件
                    if (!TextUtils.isEmpty(list.get(0)))
                        mHandler.obtainMessage(MessageType.ALPHA_GET_MP3, list.get(0)).sendToTarget();
                }
            }).start();

        }

    }

    // 搜索音乐文件
    private void search(File file, String[] ext) {// 搜索音乐文件

        if (file != null) {
            if (file.isDirectory()) {
                File[] listFile = file.listFiles();
                if (listFile != null) {
                    for (int i = 0; i < listFile.length; i++) {
                        search(listFile[i], ext);
                    }
                }
            } else {
                String filename = file.getAbsolutePath();
                for (int i = 0; i < ext.length; i++) {
                    if (filename.endsWith(ext[i])) {
                        list.add(filename);
                        break;
                    }
                }
            }
        }
    }

    private void startPlayMp3(String filePath) {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        else if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);// 单曲循环
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void OnPositiveClick() {
        // TODO Auto-generated method stub
        mHandler.sendEmptyMessage(MessageType.ALPHA_STOP_MP3);//
    }

    /**
     * 商城动作和应用的切换
     *
     * @param isApp true 切换到我的机器人，false 切换到商店
     */
    public void changeType(boolean isApp) {
        if (isApp) {
            currentPage = R.id.rl_my_page;
            onChangeFragment(appFragMent, false, null);

            rlShopPage.setBackgroundResource(R.drawable.main_page_title_right_gray_bg);
            rlMyPage.setBackgroundResource(R.drawable.main_page_title_left_bg);
            btn_shop.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_t5));
            btn_mypage.setTextColor(mContext.getResources().getColor(
                    R.color.white));
            if (Tools.isShowBottom(alphaParam, mContext)) {
                bottomLay.setVisibility(View.VISIBLE);
            } else {
                bottomLay.setVisibility(View.GONE);
            }
        } else {
            if (!hasEdit) {
                hasEdit = true;
                btn_finishEdit.setVisibility(View.GONE);

                AppFragMent fra = (AppFragMent) currentFragment;
                fra.appInfoAdapter.notifyDataSetChanged();

            }
            currentPage = R.id.rl_shop_page;

            ShopFragement.shopType = shopType;
            onChangeFragment(shopFragement, false, null);

            rlShopPage.setBackgroundResource(R.drawable.main_page_title_right_bg);
            rlMyPage.setBackgroundResource(R.drawable.main_page_title_left_gray_bg);
            btn_shop.setTextColor(mContext.getResources().getColor(
                    R.color.white));
            btn_mypage.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_t5));
            bottomLay.setVisibility(View.GONE);
        }


    }


    @Override
    public void onBottomToTop() {
        startActivity(new Intent(this, UserRecordActivity.class));
        overridePendingTransition(R.anim.activity_open, 0);
    }

    @Override
    public void onTopToBottom() {

    }
}
