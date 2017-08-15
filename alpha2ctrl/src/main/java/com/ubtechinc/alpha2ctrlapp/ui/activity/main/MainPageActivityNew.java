//package com.ubtechinc.alpha2ctrlapp.ui.activity.main;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Message;
//import android.support.v4.content.ContextCompat;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.ubt.alpha2sdk.aidlclient.DBRobotInfo;
//import com.ubtechinc.alpha2ctrlapp.R;
//import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
//import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
//import com.ubtechinc.alpha2ctrlapp.constants.Constants;
//import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
//import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
//import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
//import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
//import com.ubtechinc.alpha2ctrlapp.constants.SocketCmdId;
//import com.ubtechinc.alpha2ctrlapp.data.AppConfigResponsitory;
//import com.ubtechinc.alpha2ctrlapp.data.user.LoginOutResponsitory;
//import com.ubtechinc.alpha2ctrlapp.entity.AppInfo;
//import com.ubtechinc.alpha2ctrlapp.entity.AppInstalledInfo;
//import com.ubtechinc.alpha2ctrlapp.entity.CheckAppUpdate;
//import com.ubtechinc.alpha2ctrlapp.entity.local.ActionDownLoad;
//import com.ubtechinc.alpha2ctrlapp.entity.local.ButtonConfig;
//import com.ubtechinc.alpha2ctrlapp.entity.local.ChatInfoItem;
//import com.ubtechinc.alpha2ctrlapp.entity.request.LoginOutRequest;
//import com.ubtechinc.alpha2ctrlapp.entity.request.RefreshDownload;
//import com.ubtechinc.alpha2ctrlapp.entity.response.ActionDetail;
//import com.ubtechinc.alpha2ctrlapp.presenter.ISpotifyLoginPresenter;
//import com.ubtechinc.alpha2ctrlapp.presenter.SpotifyLoginPresenterImpl;
//import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
//import com.ubtechinc.alpha2ctrlapp.third.AidlService;
//import com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.app.AboutActivity;
//import com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.app.AppSettingActivity;
//import com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.app.ContactusAcitivty;
//import com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.app.VoiceCmdActivity;
//import com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.robot.ChooseModelActivity;
//import com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.user.AuthorizationActivity;
//import com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.user.JpushReceiveActivty;
//import com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.user.MyDeviceActivity;
//import com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.user.UserInfoActvity;
//import com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.user.UserRecordActivity;
//import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.AppSettingConfigFragment;
//import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.DeveloperFragement;
//import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
//import com.ubtechinc.alpha2ctrlapp.ui.fragment.robot.AppFragMent;
//import com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.AppDetailFragment;
//import com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.ShopFragement;
//import com.ubtechinc.alpha2ctrlapp.ui.fragment.user.MyFavorite;
//import com.ubtechinc.alpha2ctrlapp.util.FileReceive;
//import com.ubtechinc.alpha2ctrlapp.util.NToast;
//import com.ubtechinc.alpha2ctrlapp.util.PictureAcceptThread;
//import com.ubtechinc.alpha2ctrlapp.util.PrefsUtils;
//import com.ubtechinc.alpha2ctrlapp.util.StringUtils;
//import com.ubtechinc.alpha2ctrlapp.util.Tools;
//import com.ubtechinc.alpha2ctrlapp.util.UlitStatuBar;
//import com.ubtechinc.alpha2ctrlapp.widget.MyBottomRelativeLayout;
//import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;
//import com.ubtechinc.alpha2ctrlapp.widget.dialog.ConfirmFindPhoneDialog;
//import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
//import com.ubtechinc.alpha2ctrlapp.widget.popWindow.GuidePopView;
//import com.ubtechinc.alpha2ctrlapp.widget.popWindow.MenuPopuWindow;
//import com.ubtechinc.alpha2ctrlapp.widget.slidemenu.SlidingMenu;
//import com.ubtechinc.service.model.ActionFileEntrity;
//import com.ubtechinc.service.model.TransferAppData;
//import com.ubtechinc.service.protocols.AlphaParam;
//import com.ubtechinc.service.protocols.FindMobilePhoneData;
//import com.ubtechinc.service.protocols.GetNewActionList;
//import com.ubtechinc.service.protocols.GetNewActionListRsp;
//import com.ubtechinc.service.protocols.PlayActionFile;
//import com.ubtechinc.service.protocols.SetRTCTimeData;
//import com.umeng.analytics.MobclickAgent;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.TimeZone;
//
//import static com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.main.MainPagePresenter.dao;
//import static com.ubtechinc.alpha2ctrlapp.ui.mMainPageActivity.main.MainPagePresenter.database;
//
//
///**
// * 主页
// */
//public class MainPageActivityNew extends MainPageBaseActivity implements OnClickListener, ConfirmFindPhoneDialog.OnPositiveClickLitstener, MyBottomRelativeLayout.setOnSlideListener, MainPageContract.View {
//    private String TAG = "MainPageActivity";
//
//    private String[] alpha2MacList = new String[1];
//    private TextView btn_mypage, btn_shop;// app_name;
//    public ImageView userHeader;
//    private Integer currentPage;
//    //机器人操作页，包含动作表、相册、闹钟等
//    private AppFragMent appFragMent;
//    //菜单栏，根据执行的应用不同，显示不同菜单
//    public MenuPopuWindow menuWinDow;
//    //当前页
//    public BaseContactFragememt currentFragment;
//    //我的收藏
//    public MyFavorite myfavoriteFrage;
//    public AppInstalledInfo currentAppInfo = new AppInstalledInfo();
//    public RelativeLayout mainTopView;
//    public AppSettingConfigFragment appConfigSetting;
//    //开发者选项页
//    public DeveloperFragement developer;
//    private ImageView menuSetButton;
//    private ImageView menuBtnClose;
//    private ImageView btncmd;
//    public SlidingMenu mLeft_menu;
//    public RoundImageView img_head;
//    public TextView txt_nick_name_head;
//    private RelativeLayout lay_setting, lay_my_favo, lay_my_robot, lay_about;
//    private RelativeLayout lay_exsit;
//    public List<AppInfo> updatemodels = new ArrayList<AppInfo>();
//    public static List<CheckAppUpdate> appInfoList = new ArrayList<CheckAppUpdate>();
//    public String[] appdrwableList, actiondrwableList;
//    ;
//    public List<ActionDownLoad> actionList = new ArrayList<ActionDownLoad>();
//    public boolean hasEdit = true;
//    public MyBottomRelativeLayout bottomLay;
//    public ImageView bottomView;
//    public TextView btn_finishEdit;
//
//    private ImageView ivBottomLeft;
//    private ImageView ivStartRecord;//底部启动闲聊记录按钮
//    private ImageView ivBottomRight;
//    private ImageView ivLeft;
//    private ImageView ivRight;
//    private PictureAcceptThread pictureThread;
//    private ImageView btn_msg;
//    public int pictureMsg = 0;
//    public int msgCount = 0;
//    public int otherMsg = 0;
//    //	private BadgeView bv;// 消息空间
//    public static List<ChatInfoItem> msgList = new ArrayList<ChatInfoItem>();
//    public static boolean hasConnectedNewRobot = false;
//
//    private FileReceive mFileReceiVe;
//    private long existTime = 0;
//    private ImageView has_new_icon;
//    private boolean isPlay = false;
//    private ImageView btn_mypage_line, btn_shop_line;
//
//
//    private List<String> list = new ArrayList<String>();
//    private ConfirmFindPhoneDialog stopMusicDialog;
//    private TextView deviceNameTips;
//    private GuidePopView guidePopView;
//    private String mCurrentSetLanguage = "";
//    public static final String GET_NOTICE_MESSAGE_ACTION = "com.ubtechinc.alpha2ctrlapp.mMainPageActivity.custom.GET_NOTICE_MESSAGE_ACTION";
//
//    public static final String KEY_TITLE = "title";
//    public static final String KEY_MESSAGE = "message";
//    public static final String KEY_EXTRAS = "extras";
//    public static boolean isForeground = false;
//    public static boolean isFromChangeLan = false;
//    /**
//     * Alpha 的配置
//     **/
//    public AlphaParam alphaParam = null;
//    //商店页
//    private ShopFragement shopFragement;
//    private AppDetailFragment appDetailFragment;
//    private Button btnLoginSpotify;
//    private RelativeLayout contactUsLay;
//    private RelativeLayout rlMyPage;
//    private RelativeLayout rlShopPage;
//
//    /**
//     * 往左边滑动的最小距离，大于这个值时显示悬浮按钮
//     */
//    private static final int MIN_SCROLL_LEGTH = 40;
//    private MainPagePresenter mMainPagePresenter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_common_fragment_container);
//        UlitStatuBar.assistActivity(this, findViewById(R.id.layout_fragment_contanier));
//        initFragment( savedInstanceState);
//        btn_mypage = (TextView) findViewById(R.id.btn_my_page);
//        btn_shop = (TextView) findViewById(R.id.btn_shop);
//        btn_mypage_line = (ImageView) findViewById(R.id.btn_my_page_line);
//        btn_shop_line = (ImageView) findViewById(R.id.btn_shop_line);
//        userHeader = (ImageView) findViewById(R.id.user_header);
//        rlMyPage = (RelativeLayout) findViewById(R.id.rl_my_page);
//        rlShopPage = (RelativeLayout) findViewById(R.id.rl_shop_page);
//        btn_msg = (ImageView) findViewById(R.id.btn_msg);
//        btncmd = (ImageView) findViewById(R.id.btn_cmd);
//        ivStartRecord = (ImageView) findViewById(R.id.iv_startrecord);
//        ivStartRecord.setOnClickListener(this);
//        rlMyPage.setOnClickListener(this);
//        rlShopPage.setOnClickListener(this);
//        userHeader.setOnClickListener(this);
//        btncmd.setOnClickListener(this);
//        btn_msg.setOnClickListener(this);
//        currentPage = R.id.rl_my_page;
//        mainTopView = (RelativeLayout) findViewById(R.id.main_page_top_view);
//        mainTopView.setVisibility(View.VISIBLE);
//        initLeft();
//        btn_finishEdit = (TextView) findViewById(R.id.btn_finish);
//        bottomLay = (MyBottomRelativeLayout) findViewById(R.id.bottom_status);
//        bottomLay.setmSetOnSlideListener(this);
//        btn_finishEdit.setOnClickListener(this);
//
//        mMainPagePresenter = new MainPagePresenter(mActivity, this, AppConfigResponsitory.get(), LoginOutResponsitory.get());
//        //引导页
//        guidePopView = new GuidePopView(this, !RobotManagerService.getInstance(mActivity).isConnectedRobot());
//
//    }
//
//    private void initFragment(Bundle savedInstanceState) {
//        appFragMent = new AppFragMent();
//        appConfigSetting = new AppSettingConfigFragment();
//        developer = new DeveloperFragement();
//        currentFragment = appFragMent;
//        myfavoriteFrage = new MyFavorite();
//        shopFragement = new ShopFragement();
//        appDetailFragment = new AppDetailFragment();
//        if (savedInstanceState == null)
//            onChangeFragment(appFragMent, false, null);
//    }
//
//    @Override
//    public void onTopToBottom() {
//
//    }
//
//
//    /**
//     * 初始化左边侧边栏相关信息
//     *
//     * @param
//     */
//    private void initLeft() {
//        mLeft_menu = new SlidingMenu(this);
//        mLeft_menu.setMenuIcon(userHeader);
//        mLeft_menu.setMode(SlidingMenu.LEFT);
//        mLeft_menu.setShadowWidth(10);
//        mLeft_menu.setShadowDrawable(R.drawable.shadow);
//        mLeft_menu.setBehindOffset(250);
//        mLeft_menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        mLeft_menu.setMenu(R.layout.layout_left_page);
//        lay_setting = (RelativeLayout) mLeft_menu
//                .findViewById(R.id.lay_setting);
//        lay_setting.setOnClickListener(this);
//        lay_my_favo = (RelativeLayout) mLeft_menu
//                .findViewById(R.id.lay_my_favo);
//        lay_my_favo.setOnClickListener(this);
//        lay_my_robot = (RelativeLayout) mLeft_menu
//                .findViewById(R.id.lay_my_robot);
//        lay_my_robot.setOnClickListener(this);
//        has_new_icon = (ImageView) mLeft_menu.findViewById(R.id.has_new_icon);
//        lay_about = (RelativeLayout) mLeft_menu.findViewById(R.id.lay_about);
//        deviceNameTips = (TextView) mLeft_menu.findViewById(R.id.devices_name);
//        mLeft_menu.setV(has_new_icon);
//        lay_about.setOnClickListener(this);
//        lay_exsit = (RelativeLayout) mLeft_menu.findViewById(R.id.lay_exsit);
//        lay_exsit.setOnClickListener(this);
//        View root = mLeft_menu.getRootView();
//        img_head = (RoundImageView) root.findViewById(R.id.img_head);
//        txt_nick_name_head = (TextView) root.findViewById(R.id.txt_nick_name_head);
//        img_head.setOnClickListener(this);
//        contactUsLay = (RelativeLayout) mLeft_menu.findViewById(R.id.lay_contact_us);
//        contactUsLay.setOnClickListener(this);
//    }
//
//    public final BaseHandler mHandler = new BaseHandler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MessageType.CHECK_SPOTIFY_LOGIN:
//                    SpotifyLoginPresenterImpl.getInstance().checkLoginState(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT);
//                    break;
//                case MessageType.ALPHA_GET_MP3://播放音频
//                    String filePath = (String) msg.obj;
//                    mMainPagePresenter.startPlayMp3(filePath);
//                    if (stopMusicDialog == null)
//                        stopMusicDialog = new ConfirmFindPhoneDialog(MainPageActivityNew.this);
//                    stopMusicDialog.show();
//                    stopMusicDialog.setPositiveClick(MainPageActivityNew.this);
//                    break;
//                case MessageType.ALPHA_STOP_MP3:// 停止播放音频
//                    mMainPagePresenter.stopPlayMp3();
//                    FindMobilePhoneData findData = new FindMobilePhoneData();
//                    findData.setSendByClient(true);
//                    sendRequest(findData, SocketCmdId.ALPHA_MSG_FIND_MOBILEPHONE, alpha2MacList); // 告诉机器人已经找到手机
//                    break;
//                case SocketCmdId.ALPHA_MSG_RSP_FIND_MOBILEPHONE:
//                    if (list.size() > 0) {
//                        mHandler.obtainMessage(MessageType.ALPHA_GET_MP3, list.get(0)).sendToTarget(); //已经查到mp3 文件就直接播放
//                    } else
//                        chekMp3File(); // 查找mp3文件
//                    break;
//                case MessageType.ALPHA_HAS_TAKE_PIC: {//Alpha拍照
//                    getLocalUnReadNoticeMessage();
//
//                }
//                break;
//                case SocketCmdId.ALPHA_MSG_RSP_TRANSFER_BIG_PHOTO://请求大图
//                    getLocalUnReadNoticeMessage();
//                    break;
//                case MessageType.ALPHA_GET_PIC_FAILED://获取图片失败
//                    break;
//                case MessageType.ALPHA_CHECK_LIST_DIALOG:
//                    if (LoadingDialog.mDia != null && !LoadingDialog.mDia.isShowing()) {
//                        LoadingDialog.mDia.show();
//                    } else if (LoadingDialog.mDia == null) {
//                        LoadingDialog.getInstance(mActivity).show();
//                    }
//                    break;
//                case MessageType.ALPHA_MSG_RSP_GET_APP_BUTTONEVENT:
//                    if (!currentFragment.getClass().getName().equals(AppFragMent.class.getName())) {
//                        dismissLoadingDialog();
//                        ButtonConfig ss = (ButtonConfig) msg.obj;
//                        if (ss.getModels().size() > 0 || currentAppInfo.getApkInfo().isSetting()) {
//                            showMenuDiag(ss, currentFragment,
//                                    currentAppInfo.getApkInfo().isSetting());
//                        }
//                    }
//
//                    break;
//                case MessageType.ALPHA_START_APP_RESPONSE:
//                    if (!currentFragment.getClass().getName().equals(AppFragMent.class.getName())) {
//                        refreshBottom(true);
//                    }
//
//                    break;
//                case 0:
//                    TransferAppData entrity = (TransferAppData) msg.obj;
//                    sendRequest(entrity,
//                            MessageType.ALPHA_MSG_GET_APP_BUTTONEVENT,
//                            new String[]{Alpha2Application.currentAlpha2Mac});
//                    break;
//
//                case SocketCmdId.ALPHA_MSG_RSP_GET_ACTIONLIST:
//                    GetNewActionListRsp newActionList = (GetNewActionListRsp) msg.obj;
//                    dao.insertActionList(newActionList.getActionList(), database);
//                    currentFragment.refreshDownLoadData();
//                    break;
//
//
//            }
//        }
//    };
//
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == currentPage) {
//            return;
//        } else {
//            switch (v.getId()) {
//                case R.id.user_header:
//                    mLeft_menu.showMenu();
//                    break;
//                case R.id.rl_my_page://我的机器人
//                    changeType(true);
//                    break;
//                case R.id.rl_shop_page://商店
//                    changeType(false);
//                    break;
//                case R.id.iv_startrecord:
//                    startActivity(new Intent(this, UserRecordActivity.class));
//                    overridePendingTransition(R.anim.activity_open, 0);
//                    break;
//
//                case R.id.btn_setting://菜单里的设置
//                    if (currentFragment != null) {
//                        onChangeFragment(appConfigSetting, false, null);
//                        currentFragment = appConfigSetting;
//                        dismissMenuPop();
//                    }
//                    break;
//                case R.id.btn_spotify_login:
//                    showLoadingDialog();
//                    mHandler.sendEmptyMessageDelayed(MessageType.CHECK_SPOTIFY_LOGIN, 5 * 1000);//英语闲未完全启动时不能响应，故延时三秒发送检测spotify 登录
//                    break;
//                case R.id.lay_setting://系统设置
//                    Intent settingIntent = new Intent(MainPageActivityNew.this, AppSettingActivity.class);
//                    startActivity(settingIntent);
//                    overridePendingTransition(R.anim.push_left_in, 0);
//                    mLeft_menu.toggle();
//                    break;
//                case R.id.lay_my_favo://我的收藏
//                    if (currentFragment != null) {
//                        onChangeFragment(myfavoriteFrage, false, null);
//                        currentFragment = myfavoriteFrage;
//                        dismissMenuPop();
//                        mLeft_menu.toggle();
//                    }
//                    break;
//                case R.id.lay_my_robot://我的设备
//                    goMainMode(false);
//                    mLeft_menu.toggle();
//                    break;
//                case R.id.btn_finish:
//                    //设置完个人信息，更新我的主页表
//                    hasEdit = true;
//                    btn_finishEdit.setVisibility(View.GONE);
//                    if (Tools.isShowBottom(alphaParam, mActivity)) {
//                        bottomLay.setVisibility(View.VISIBLE);
//                    } else {
//                        bottomLay.setVisibility(View.GONE);
//                    }
//                    AppFragMent fra = (AppFragMent) currentFragment;
//                    fra.appInfoAdapter.notifyDataSetChanged();
//                    break;
//                case R.id.lay_about://关于
//                    Intent aboutIntent = new Intent(MainPageActivityNew.this, AboutActivity.class);
//                    startActivity(aboutIntent);
//                    overridePendingTransition(R.anim.push_left_in, 0);
//                    mLeft_menu.toggle();
//                    break;
//                case R.id.lay_contact_us:
//                    Intent contactIntent = new Intent(MainPageActivityNew.this, ContactusAcitivty.class);
//                    startActivity(contactIntent);
//                    overridePendingTransition(R.anim.push_left_in, 0);
//                    mLeft_menu.toggle();
//                    break;
//                case R.id.lay_exsit://退出登录
//                    // if(!Alpha2Application.currentAlpha2Mac.equals("")){
//                    doLoginOut();
//                    // }
//                    break;
//                case R.id.img_head://头像
//                    Intent intent2 = new Intent(MainPageActivityNew.this, UserInfoActvity.class);
//                    startActivity(intent2);
//                    overridePendingTransition(R.anim.push_left_in, 0);
//                    mLeft_menu.toggle();
//                    break;
////                case R.id.no_robot://没有设备时，跳转到我的设备页面，连接设备
////                    goMainMode(true);
////                    break;
//                case R.id.btn_msg://我的消息
//                    if (!Tools.isFastClick(2000)) {
//                        Intent intent = new Intent(MainPageActivityNew.this, JpushReceiveActivty.class);
//                        MainPageActivityNew.this.startActivity(intent);
//                        overridePendingTransition(R.anim.push_left_in, 0);
//                        msgList.clear();
//                        msgCount = 0;
//                        pictureMsg = 0;
//                        otherMsg = 0;
//                    }
//                    break;
//                case R.id.btn_cmd:
//                    if (!Tools.isFastClick(2000)) {
//                        Intent intent1 = new Intent(MainPageActivityNew.this, VoiceCmdActivity.class);
//                        MainPageActivityNew.this.startActivity(intent1);
//                        overridePendingTransition(R.anim.push_left_in, 0);
//                    }
//                    break;
//                case R.id.btn_close: // 趣声菜单栏的关闭按钮
//                    dismissMenuPop();
//                    AppInstalledInfo apkInfo = null;
//                    if (alphaParam != null) {
//
//                        if (alphaParam.getsServiceLanguage().equalsIgnoreCase("zh_cn")) {
//                            apkInfo = dao.queryApk(database, BusinessConstants.PACKAGENAME_CH_CHAT);
//                        } else {
//                            apkInfo = dao.queryApk(database, BusinessConstants.PACKAGENAME_EN_CHAT);
//                        }
//                    }
//                    if (apkInfo != null) {
//                        startNewApp(apkInfo);
//                    } else {
//                        sendRequest(currentAppInfo.getApkInfo(), MessageType.ALPHA_STOP_APP_REQUEST, new String[]{Alpha2Application.currentAlpha2Mac});
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//        }
//
//    }
//
//    /**
//     * 显示菜单栏
//     */
//    @Override
//    public  void showMenuPop() {
//        if (menuWinDow != null && !menuWinDow.isShowing())
//            menuWinDow.show();
//    }
//
//    /**
//     * 消失菜单栏
//     */
//    @Override
//    public  void dismissMenuPop() {
//        if (menuWinDow != null && menuWinDow.isShowing())
//            menuWinDow.dismiss();
//    }
//
//    /**
//     * 销毁菜单栏
//     */
//    @Override
//    public void destoryMenuPop() {
//        if (menuWinDow != null) {
//            dismissMenuPop();
//            menuWinDow = null;
//        }
//    }
//
//    @Override
//    public boolean isMenuPopShow() {
//        return menuWinDow != null && menuWinDow.isShowing();
//    }
//
//
//    /**
//     * 刷新底部菜单界面
//     *
//     * @param appBitmap
//     * @param appName
//     * @param appPackge
//     */
//    public void changeCurrentApp(Bitmap appBitmap, String appName,
//                                 String appPackge) {
//        if (Tools.isShowBottom(alphaParam, mActivity)) {
//            bottomLay.setVisibility(View.VISIBLE);
//        } else {
//            bottomLay.setVisibility(View.GONE);
//        }
//
//
//
//    }
//
//    /**
//     * 跳转我的设备列表
//     */
//    @Override
//    public  void goMainMode(boolean fromBottom) {
//        Intent intent = new Intent(this, MyDeviceActivity.class);
//        intent.putExtra("isFirst", false);
//        startActivity(intent);
//        if (!fromBottom)
//            overridePendingTransition(R.anim.push_left_in, 0);
//        else
//            overridePendingTransition(R.anim.menu_enter, 0);
//    }
//
//    public void showMenuDiag(ButtonConfig ss, BaseContactFragememt fragment,
//                             boolean hasSetting) {
//        if (currentAppInfo != null && currentAppInfo.getApkInfo().getPackageName().equals(BusinessConstants.PACKAGENAME_ALPHA_TRANSLATION)) {
//            return;
//        }
//        if (menuWinDow == null) {
//            menuWinDow = new MenuPopuWindow(this, fragment);
//        }
//        try {
//            menuWinDow.refresh(ss, currentAppInfo.getApkInfo().getPackageName(), currentFragment, hasSetting);
//            menuSetButton = (ImageView) menuWinDow.findViewById(R.id.btn_setting);
//            menuBtnClose = (ImageView) menuWinDow.findViewById(R.id.btn_close);
//            btnLoginSpotify = (Button) menuWinDow.findViewById(R.id.btn_spotify_login);
//            menuBtnClose.setOnClickListener(this);
//            menuSetButton.setOnClickListener(this);
//            btnLoginSpotify.setOnClickListener(this);
//            showMenuPop();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void showLoadingDialog() {
//
//    }
//
//    @Override
//    public void dismissLoadingDialog() {
//
//    }
//
//    @Override
//    protected void onPause() {
//        isForeground = false;
//        super.onPause();
//
//    }
//
//    @Override
//    public void onResume() {
//        isForeground = true;
//        super.onResume();
//        Alpha2Application.getInstance().removeActivity();
//        if (PrefsUtils.loadPrefBoolean(mActivity,PreferenceConstants.IS_FROM_THRID_AUTH, false)) {//是否来自第三方验证
//            Intent sendIntent = new Intent(this, AuthorizationActivity.class);
//            String appId = SPUtils.get().getString(PreferenceConstants.THRID_APP_ID);
//            String appKey = SPUtils.get().getString(PreferenceConstants.THRID_APP_KEY);
//            sendIntent.putExtra(AidlService.AIDL_DATA_APPID, appId);
//            sendIntent.putExtra(AidlService.AIDL_DATA_APPKEY, appKey);
//            startActivity(sendIntent);
//            PrefsUtils.loadPrefBoolean(mActivity,PreferenceConstants.IS_FROM_THRID_AUTH, false);
//        } else if (PrefsUtils.loadPrefBoolean(mActivity,PreferenceConstants.IS_FROM_THRID_CONFIG_NETWORK, false)) {//是否来自第三方配置网络
//            Intent intent = new Intent();
//            DBRobotInfo robot = (DBRobotInfo) SPUtils.get().getObject(DBRobotInfo.class);
//            intent.setClass(mActivity, ChooseModelActivity.class);
//            intent.putExtra("isFromDevice", true);
//            intent.putExtra(Constants.ROBOTSN, robot.getEquipmentId());
//            intent.putExtra(Constants.CONNECT_NET_ROBOT_JID, Tools.getJidByName(robot.getEquipmentUserId() + ""));
//            if (!StringUtils.isEmpty(robot.getMacAddress())) {
//                intent.putExtra(Constants.ROBOT_MAC, robot.getMacAddress());
//            }
//            PrefsUtils.savePrefBoolean(mActivity,PreferenceConstants.IS_FROM_THRID_CONFIG_NETWORK, false);
//        } else if (PrefsUtils.loadPrefBoolean(mActivity,PreferenceConstants.IS_FROM_THRID_DOWNLOAD_APP, false)) {//是否来自第三方下载机器人App
//            Bundle bundle = new Bundle();
//            bundle.putInt(IntentConstants.DATA_APP_ID, Integer.valueOf(SPUtils.get().getString(PreferenceConstants.THRID_APP_ID)));
//            bundle.putBoolean(IntentConstants.DATA_IS_DOWNLOAD, true);
//            onChangeFragment(appDetailFragment, false, bundle);
//            PrefsUtils.savePrefBoolean(mActivity,PreferenceConstants.IS_FROM_THRID_DOWNLOAD_APP, false);
//        } else if (RobotManagerService.getInstance(mActivity).isConnectedRobot() && !PrefsUtils.loadPrefBoolean(mActivity,Constants.VERSION_CODE + Constants.main_menu_guide, false)) {
//            guidePopView.refresh(false);
//            guidePopView.show();
//            PrefsUtils.savePrefBoolean(mActivity,Constants.VERSION_CODE + Constants.main_menu_guide, true);
//        }
//
//        mFileReceiVe = FileReceive.getInstance(mActivity, noticeManager, mHandler);
//        if (RobotManagerService.getInstance(mActivity).isConnectedRobot()) {
//            deviceNameTips.setText(R.string.main_page_connect_alpha_tips);
//        } else {
//            deviceNameTips.setText(Constants.deviceName);
//        }
//
//        if (hasConnectedNewRobot && !currentFragment.getClass().getName().equals(AppFragMent.class.getName())) {
//            currentPage = R.id.rl_my_page;
//            onChangeFragment(appFragMent, false, null);
//            rlShopPage.setBackgroundResource(R.drawable.main_page_title_right_gray_bg);
//            rlMyPage.setBackgroundResource(R.drawable.main_page_title_left_bg);
//            btn_shop.setTextColor(mActivity.getResources().getColor(
//                    R.color.text_color_t5));
//            btn_mypage.setTextColor(mActivity.getResources().getColor(
//                    R.color.white));
//        }
//        if (isFromChangeLan) {
//            isFromChangeLan = false;
//        }
//        txt_nick_name_head.setText(SPUtils.get().getString(PreferenceConstants.USER_NAME));
//        mCurrentSetLanguage = SPUtils.get().getString(Constants.APP_LAUNGUAGE).split(",")[0];
//        if (mCurrentSetLanguage.equals("en") || !isZh()) {
//            btncmd.setVisibility(View.GONE);
//        } else if (mCurrentSetLanguage.equals("zh_cn") || (TextUtils.isEmpty(mCurrentSetLanguage) && isZh())) {
//            btncmd.setVisibility(View.VISIBLE);
//        }
//
//        if (!Tools.isCh(mActivity)) {
//            bottomLay.setVisibility(View.GONE);
//        }
//
//        LoadImage.LoadHeader(this, 0, img_head, SPUtils.get().getString(PreferenceConstants.USER_IMAGE));
//        getLocalUnReadNoticeMessage();
//    }
//
//
//    /**
//     * 是否中文
//     *
//     * @return
//     */
//    private boolean isZh() {
//        Locale locale = getResources().getConfiguration().locale;
//        String language = locale.getLanguage();
//        if (language.endsWith("zh"))
//            return true;
//        else
//            return false;
//    }
//
//    @Override
//    public void onDestroy() {
//        destoryMenuPop();
//        if (pictureThread != null) {
//            pictureThread.stopProcess();
//            pictureThread = null;
//        }
//        unRegisterBroadcastReceiver();
//        mMainPagePresenter.stopPlayMp3();
//        if (!Constants.hasLoginOut && !isFromChangeLan)
//            Alpha2Application.getInstance().exit(true);
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);// must store the new intent unless getIntent() will
//        // return the old one
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == event.KEYCODE_BACK) {
//            String currentName = currentFragment.getClass().getName();
//            if (currentName.equals(AppFragMent.class.getName())) {
//                if (!hasEdit) {
//                    hasEdit = true;
//                    btn_finishEdit.setVisibility(View.GONE);
//                    if (Tools.isShowBottom(alphaParam, mActivity)) {
//                        bottomLay.setVisibility(View.VISIBLE);
//                    } else {
//                        bottomLay.setVisibility(View.GONE);
//                    }
//                    AppFragMent fra = (AppFragMent) currentFragment;
//                    fra.onRefreshAfterEdit();
//                    return false;
//                } else {
//                    exist();
//                    return false;
//                }
//
//            }
//            if (currentName.equals(shopFragement.getClass().getName())) {
//                Logger.d("nxy", "主界面返回 ");
//                exist();
//                return false;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    public void showAppGuide() {
//        if (!RobotManagerService.getInstance(mActivity).isConnectedRobot() && !PrefsUtils.loadPrefBoolean(mActivity,Constants.VERSION_CODE + Constants.main_app_guide, false)) {
//            guidePopView.refresh(true);
//            guidePopView.show();
//            PrefsUtils.savePrefBoolean(mActivity,Constants.VERSION_CODE + Constants.main_app_guide, true);
//        }
//    }
//
//
//
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(GET_NOTICE_MESSAGE_ACTION)) {
//                getLocalUnReadNoticeMessage();
//                dismissLoadingDialog();
//            } else if (action.equals(AidlService.ACTION_SEND_DATA_TO_SERVICE)) {// 将第三方发送的数据，发送给主服务，并且反馈错误码
//                String errorStr = null;
//                if (RobotManagerService.getInstance(mActivity).isConnectedRobot()) {
//                    errorStr = mActivity.getResources().getString(R.string.main_page_connect_alpha_tips);
//                } else {
//                    String keyId = intent.getStringExtra(AidlService.AIDL_KEYID_FROM_THIRD);
//                    String data = intent.getStringExtra(AidlService.AIDL_SEND_DATA_FROM_THIRD);
//                    try {
//                        errorStr = ConstactsTools.createThirdNotificationIQ(keyId, data, Alpha2Application.getRobotSerialNo(), context);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                /**
//                 * 有错误就反馈错误码
//                 */
//                if (!TextUtils.isEmpty(errorStr)) {
//                    Intent sendIntent = new Intent();
//                    sendIntent.setAction(AidlService.RECEIVE_ERROR_DATA);
//                    sendIntent.putExtra(AidlService.AIDL_RECEIVE_ERROR_CODE, errorStr);
//                    sendBroadcast(sendIntent);
//                }
//            }
//
//            if (RobotManagerService.SEND_ROBOT_IS_AWAY.equals(action)) {
//                if (!Constants.hasLoginOut) {
//                    String currentName = currentFragment.getClass().getName();
//                    if (currentName.equals(AppFragMent.class.getName())) {
//                        AppFragMent f = (AppFragMent) getFragmentManager().findFragmentByTag(AppFragMent.class.getName());
//                        if (f != null)
//                            f.refreshView();
//                    }
//                    /**
//                     * 提示第三方机器人掉线
//                     */
//                    {
//                        Intent sendIntent = new Intent();
//                        sendIntent.setAction(AidlService.RECEIVE_ERROR_DATA);
//                        sendIntent.putExtra(AidlService.AIDL_RECEIVE_ERROR_CODE, context.getString(R.string.main_page_alpha2_offline));
//                        sendBroadcast(sendIntent);
//                    }
//                    destoryMenuPop();
//                }
//            } else if (action.equals(RobotManagerService.SEND_STATR_ROBOT_SUCCESS)) {
//                sendTime();
//            }
//
//        }
//    };
//
//
//    /**
//     * 机器人连接上之后设置时间
//     */
//    private void sendTime() {
//        String date = Tools.getTime("yyyy-MM-dd-HH-mm-ss");
//        SetRTCTimeData setData = new SetRTCTimeData();
//        String[] dateStr = date.split("-");
//        setData.setYear(Integer.valueOf(dateStr[0]));
//        setData.setMonth(Integer.valueOf(dateStr[1]));
//        setData.setDay(Integer.valueOf(dateStr[2]));
//        setData.setHour(Integer.valueOf(dateStr[3]));
//        setData.setMinute(Integer.valueOf(dateStr[4]));
//        setData.setSecond(Integer.valueOf(dateStr[5]));
//        Calendar c = Calendar.getInstance();
//        int week = Integer.valueOf(c.get(Calendar.DAY_OF_WEEK));
//        setData.setWeek(week);
//        TimeZone tz = TimeZone.getDefault();
//        setData.setTimeZone(tz.getID());//时区
//        sendRequest(setData, MessageType.ALPHA_SET_TIME, new String[]{Alpha2Application.currentAlpha2Mac});
//    }
//
//    /**
//     * 注册广播
//     * 1.监听好友上下线状态
//     * 2.推送相关信息
//     * 3.Alpha控制相关
//     */
//    public void registerBroadcastReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(RobotManagerService.SEND_ROBOT_IS_AWAY);
//        filter.addAction(GET_NOTICE_MESSAGE_ACTION);
//
//        /**接收第三方的数据并发给服务端**/
//        filter.addAction(RobotManagerService.SEND_STATR_ROBOT_SUCCESS);
//        filter.addAction(AidlService.ACTION_SEND_DATA_TO_SERVICE);
//        filter.addAction(AidlService.ACTION_SEND_TO_VERIFY);
//        registerReceiver(receiver, filter);
//
//    }
//
//    public void unRegisterBroadcastReceiver() {
//        // TODO Auto-generated method stub
//        if (receiver != null) {
//
//            try {
//                this.unregisterReceiver(receiver);
//            } catch (Exception e) {
//
//            }
//        }
//        receiver = null;
//    }
//
//
//    private void exist() {
//        if ((System.currentTimeMillis() - existTime) > 2000) {
//            ToastUtils.showShortToast(  R.string.main_page_exist_tip);
//            existTime = System.currentTimeMillis();
//        } else {
//            startHeartBeat();
//            Alpha2Application.getAlpha2().exit(true);
//        }
//    }
//
//    /**
//     * @param
//     * @return void
//     * @Description 本地获取未读消息
//     */
//    private void getLocalUnReadNoticeMessage() {
//        if (PrefsUtils.loadPrefBoolean(mActivity,PreferenceConstants.IS_OPEN_MSG_TIPS, true)) {//是否打开消息推送
//            msgCount = noticeManager.getUnReadNoticeCount();
//            if (msgCount > 0) {
//                btn_msg.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.message__new_icon));
//            } else {
//                btn_msg.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.message_icon));
//            }
//        } else {
//            btn_msg.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.message_icon));
//        }
//    }
//
//    private void doLoginOut() {
//        LoginOutRequest request = new LoginOutRequest();
//        request.setToken(SPUtils.get().getString(
//                PreferenceConstants.TOKEN));
//        UserAction action = UserAction.getInstance(mActivity, null);
//        action.setParamerObj(request);
//        action.doRequest(NetWorkConstant.REQUEST_LOGIN_OUT, "");
//    }
//
//
//    public void startApp(AppInstalledInfo appInfo) {
//        appInfo = dao.queryApk(database, appInfo.getApkInfo().getPackageName());
//        if (currentAppInfo != null && currentAppInfo.getApkInfo() != null) {
//            if (currentAppInfo.getApkInfo().getPackageName().equals(appInfo.getApkInfo().getPackageName())) {
//                if ((currentAppInfo.getApkInfo().isButtonEvent()
//                        || currentAppInfo.getApkInfo().isSetting()
//                        || currentAppInfo.getApkInfo().getPackageName().equals(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT))) {
//                    if (menuWinDow != null && !menuWinDow.isShowing()) {
//                        menuWinDow.show();
//                    } else {
////								getButton(true);
//                    }
//
//                }
//            } else {
//                startNewApp(appInfo);
//            }
//
//        } else {
//            startNewApp(appInfo);
//        }
//    }
//
//    private void startNewApp(AppInstalledInfo appInfo) {
//
//        if (currentAppInfo.getApkInfo() != null) {
//            // 停止当前的App
//            if (currentAppInfo.getApkInfo().getPackageName().equals(BusinessConstants.PACKAGENAME_TEMP_ALARM)) {
//                sendRequest(null, MessageType.ALPHA_STOPACTION_REQUEST, new String[]{Alpha2Application.currentAlpha2Mac});
//            } else {
//                sendRequest(currentAppInfo.getApkInfo(), MessageType.ALPHA_STOP_APP_REQUEST, new String[]{Alpha2Application.currentAlpha2Mac});
//            }
//            if (menuWinDow != null) {
//                menuWinDow = null;
//            }
//        }
//        currentAppInfo = MainPageActivityNew.dao.queryApk(MainPageActivityNew.database, appInfo.getApkInfo().getPackageName());
//        LoadingDialog.getInstance(mActivity).show();
//        sendRequest(currentAppInfo.getApkInfo(), MessageType.ALPHA_START_APP_REQUEST, new String[]{Alpha2Application.currentAlpha2Mac});
//
//    }
//
//    /**
//     * 设置并刷新底部菜单
//     *
//     * @needDelay 是否需要延时
//     */
//    public void refreshBottom(boolean needDelay) {
//        if (currentAppInfo == null || currentAppInfo.getApkInfo() == null) {
//            LoadingDialog.dissMiss();
//        } else {
//            getMenuDiag(needDelay);
//        }
//
//
//    }
//
//    /**
//     * 获取的菜单
//     *
//     * @param deLay 是否需要延时
//     */
//    public void getMenuDiag(boolean deLay) {
//        if (currentAppInfo != null && currentAppInfo.getApkInfo() != null && currentAppInfo.getApkInfo().isButtonEvent()) {
//
//            final TransferAppData entrity = new TransferAppData();
//            entrity.setDatas("en".getBytes());
//            entrity.setPackageName(currentAppInfo.getApkInfo().getPackageName());
//            if (!deLay) {
//                sendRequest(entrity,
//                        MessageType.ALPHA_MSG_GET_APP_BUTTONEVENT,
//                        new String[]{Alpha2Application.currentAlpha2Mac});
//            } else {
//                Message msg = new Message();
//                msg.what = 0;
//                msg.obj = entrity;
//                mHandler.sendMessageDelayed(msg, 1000);
//            }
//
//        } else {
//            if ((currentAppInfo.getApkInfo() != null && currentAppInfo.getApkInfo().isSetting())
//                    || currentAppInfo.getApkInfo().getPackageName().equals(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT)) {
//                showMenuDiag(null, currentFragment, currentAppInfo.getApkInfo().isSetting());
//            }
//            LoadingDialog.dissMiss();
//        }
//    }
//
//    /**
//     * [执行动作]
//     *
//     * @author zengdengyi
//     */
//    public void onPlayAction(String id, String mFileName) {
//        if (currentAppInfo.getApkInfo() != null) {
//            // 停止当前的App
//            if (!currentAppInfo.getApkInfo().getPackageName().equals(
//                    BusinessConstants.PACKAGENAME_TEMP_ALARM)) {
//                sendRequest(currentAppInfo.getApkInfo(),
//                        MessageType.ALPHA_STOP_APP_REQUEST,
//                        new String[]{Alpha2Application.currentAlpha2Mac});
//                if (menuWinDow != null) {
//                    menuWinDow = null;
//                }
//            }
//        } else {
//            RobotApp info = new RobotApp();
//            info.setName(mFileName);
//            info.setPackageName(BusinessConstants.PACKAGENAME_TEMP_ALARM);
//            info.setAppId(id);
//            currentAppInfo.setApkInfo(info);
//        }
//
//        // TODO Auto-generated method stub
//        PlayActionFile playfile = new PlayActionFile();
//        playfile.setFilename(id);
//        sendRequest(playfile, MessageType.ALPHA_PLAYACTION_REQUEST, alpha2MacList);
//        changePlay(false, mFileName);
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("actionName", mFileName);
//        MobclickAgent.onEvent(mActivity, Constants.YOUMENT_MY_ACTION_PLAY_TIMES, map);
//    }
//
//    public void changePlay(boolean isStop, String mFileName) {
//        if (isStop) {
//            isPlay = false;
//        } else {
//            isPlay = true;
//        }
//    }
//
//    /**
//     * 添加下载的app 到下载的app 列表中，再发送命令进行下载
//     *
//     * @param info      要下载的app 信息
//     * @param imagePath app的图标
//     * @param appId     app 的id
//     */
//    public void downLoadApp(RobotApp info, String imagePath, int versionCode, int appId) {
//        if (info.getPackageName().equals(BusinessConstants.PACKAGENAME_CH_CHAT) && !TextUtils.isEmpty(Alpha2Application.getInstance().getChatIconPath())) {
//            imagePath = Alpha2Application.getInstance().getChatIconPath();
//        }
//        info.setDownloadState(BusinessConstants.APP_STATE_INIT);
//        dao.updateAppIcon(database, info.getPackageName(), info.getName(), imagePath, versionCode + "");
//        sendRequest(info, MessageType.ALPHA_MSG_UPDATE_PACKAGES, new String[]{Alpha2Application.currentAlpha2Mac});
//        RefreshDownload refreshDownload = new RefreshDownload();// 每次点击下载一次更新一次下载次数
//        refreshDownload.setAppId(appId);
//        UserAction.getInstance(this, null).setParamerObj(refreshDownload);
//        UserAction.getInstance(this, null).doRequest(NetWorkConstant.REQUEST_REFRESG_DOWNLOAD, "");
//
////			}
//
//    }
//
//    /**
//     * 添加下载的action 到下载的action 列表中，再发送命令进行下载
//     *
//     * @param info 要下载的action 信息
//     */
//    public void downLoadAction(ActionFileEntrity info, ActionDownLoad downloadObj, ActionDetail detailObj) {
//        info.setDownloadState(1);
//        MainPageActivityNew.dao.updateActionDownLoadState(MainPageActivityNew.database, info, downloadObj, detailObj);
//        sendRequest(info, MessageType.ALPHA_MSG_ACTIONFILE_DOWNLOAD, new String[]{Alpha2Application.currentAlpha2Mac});
//
////			}
//
//    }
//
//    /**
//     * [停止动作]
//     *
//     * @author zengdengyi
//     */
//    public void onStopAction() {
//        // TODO Auto-generated method stub
//        sendRequest(null, MessageType.ALPHA_STOPACTION_REQUEST, alpha2MacList);
//        changePlay(true, "");
//    }
//
//    /**
//     * @return
//     * @Description 获取动作列表
//     */
//    public void onGetActionList() {
//
//        GetNewActionList request = new GetNewActionList();
//        request.setActionType(0);
//        request.setLanguageType(Constants.SYSTEM_LAN);
//        sendRequest(request, SocketCmdId.ALPHA_MSG_GET_ACTIONLIST, new String[]{Alpha2Application.currentAlpha2Mac});
//        hasGetLocolList = false;
//    }
//
//
//    private void chekMp3File() {
//        //是否有外部存储设备
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            new Thread(new Runnable() {
//                String[] ext = {".mp3"};
//                File file = Environment.getExternalStorageDirectory();
//
//                public void run() {
//                    search(file, ext); // 取第一个查到的MP3文件
//                    if (!TextUtils.isEmpty(list.get(0)))
//                        mHandler.obtainMessage(MessageType.ALPHA_GET_MP3, list.get(0)).sendToTarget();
//                }
//            }).start();
//
//        }
//
//    }
//
//    // 搜索音乐文件
//    private void search(File file, String[] ext) {// 搜索音乐文件
//
//        if (file != null) {
//            if (file.isDirectory()) {
//                File[] listFile = file.listFiles();
//                if (listFile != null) {
//                    for (int i = 0; i < listFile.length; i++) {
//                        search(listFile[i], ext);
//                    }
//                }
//            } else {
//                String filename = file.getAbsolutePath();
//                for (int i = 0; i < ext.length; i++) {
//                    if (filename.endsWith(ext[i])) {
//                        list.add(filename);
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//
//    @Override
//    public void OnPositiveClick() {
//        mHandler.sendEmptyMessage(MessageType.ALPHA_STOP_MP3);//
//    }
//
//    /**
//     * 商城动作和应用的切换
//     *
//     * @param isApp true 切换到应用，false 切换到动作
//     */
//    public void changeType(boolean isApp) {
//        if (isApp) {
//            currentPage = R.id.rl_my_page;
//            onChangeFragment(appFragMent, false, null);
//            rlShopPage.setBackgroundResource(R.drawable.main_page_title_right_gray_bg);
//            rlMyPage.setBackgroundResource(R.drawable.main_page_title_left_bg);
//            btn_shop.setTextColor(  ContextCompat.getColor(mActivity, R.color.text_color_t5));
//
//            btn_mypage.setTextColor(  ContextCompat.getColor(mActivity, R.color.white));
//          ;
//            if (Tools.isShowBottom(alphaParam, mActivity)) {
//                bottomLay.setVisibility(View.VISIBLE);
//            } else {
//                bottomLay.setVisibility(View.GONE);
//            }
//        } else {
//            if (!hasEdit) {
//                hasEdit = true;
//                btn_finishEdit.setVisibility(View.GONE);
//                AppFragMent fra = (AppFragMent) currentFragment;
//                fra.appInfoAdapter.notifyDataSetChanged();
//
//            }
//            currentPage = R.id.rl_shop_page;
//            onChangeFragment(shopFragement, false, null);
//            rlShopPage.setBackgroundResource(R.drawable.main_page_title_right_bg);
//            rlMyPage.setBackgroundResource(R.drawable.main_page_title_left_gray_bg);
//            btn_shop.setTextColor(mActivity.getResources().getColor(
//                    R.color.white));
//            btn_mypage.setTextColor(mActivity.getResources().getColor(
//                    R.color.text_color_t5));
//            bottomLay.setVisibility(View.GONE);
//        }
//
//
//    }
//
//
//    @Override
//    public void onBottomToTop() {
//        startActivity(new Intent(this, UserRecordActivity.class));
//        overridePendingTransition(R.anim.activity_open, 0);
//    }
//
//
//}
