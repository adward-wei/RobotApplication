package com.ubtechinc.alpha2ctrlapp.ui.activity.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.ScreenUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.user.ILoginDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.IMessageDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.LoginAndOutReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.user.MessageRepository;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.entity.net.LoginModule;
import com.ubtechinc.alpha2ctrlapp.im.Phone2RobotMsgMgr;
import com.ubtechinc.alpha2ctrlapp.util.ApkTools;
import com.ubtechinc.alpha2ctrlapp.util.DataCleanManager;
import com.ubtechinc.alpha2ctrlapp.util.LauncherUtil;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.io.File;
import java.util.List;
import java.util.Locale;


/**
 * @ClassName WelcomePresenter
 * @date 4/8/2017
 * @author tanghongyu
 * @Description 欢迎页逻辑处理
 * @modifier
 * @modify_time
 */
public class WelcomePresenter implements WelcomeContract.Presenter {

    private static final String TAG = "WelcomePresenter";
    //是否需要重新登录
    private int mLoginState = BusinessConstants.LOGIN_STATE_NO_LOGIN;
    //手机当前语言
    public String language;
    //手机当前语言所在城市缩写
    public static String country;
    //APP运行次数
    private int mAppRunTimes;
    private int currentVercode;
    private int lastVercode;
    private Alpha2Application mContext;
    private WelcomeContract.View mView;
    private MessageRepository mNoticeMessageRepository;
    public WelcomePresenter(@NonNull Context mContext, @NonNull WelcomeContract.View view, @NonNull MessageRepository noticeMessageRepository) {
        Preconditions.checkNotNull(mContext);
        Preconditions.checkNotNull(view);
        Preconditions.checkNotNull(noticeMessageRepository);
        this.mContext = (Alpha2Application) mContext;
        this.mView = view;
        this.mNoticeMessageRepository = noticeMessageRepository;
    }

    @Override
    public void initData() {
        Locale locale = Locale.getDefault();
        country = locale.getCountry();
        mLoginState = Tools.needLogin(SPUtils.get().getString(PreferenceConstants.LOGIN_TIME));
        String dataBasedir = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/eim/";
        File f = new File(dataBasedir);
        if (f.exists())
            DataCleanManager.deleteGeneralFile(dataBasedir);
        mAppRunTimes =  SPUtils.get().getInt(PreferenceConstants.APP_RUN_TIMES, 0);

        currentVercode = ApkTools.getVersionCode(mContext);
        lastVercode = SPUtils.get().getInt(Constants.VERSION_CODE, 0);

        if (currentVercode > lastVercode) {
            //当前版本是最新版本则需要创建新的快捷方式
            createShortCut(true);
        } else {
            createShortCut(false);
        }


        initImageSize();
    }



    /**
     * 初始化图片最大宽度和高度
     */

    private void initImageSize() {
        int screenHeight = ScreenUtils.getScreenHeight();
        Resources resources = mContext.getResources();
        //获取底部操作栏
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //真实屏幕高度 = 屏幕高度 - 减去底部操作栏的高度
            screenHeight = screenHeight - resources.getDimensionPixelSize(resourceId);
        }

        Constants.appImageWidth = ScreenUtils.getScreenWidth();
        Constants.deviceHeight = screenHeight ;
        Constants.appDetailImageHeight = (int) (screenHeight * (3.0 / 4.0));
        Constants.appRecomlImageHeight = (int) (screenHeight * (1.0 / 3.0));

    }

    @Override
    public void doGo() {


            if (mAppRunTimes == 0 || currentVercode > lastVercode) {//第一次运行
                mView.skipToGuide();
                SPUtils.get().put( Constants.VERSION_CODE, currentVercode);
            } else {
                String countryCode = SPUtils.get().getString( PreferenceConstants.COUNTRY_NUM, BusinessConstants.DEFAULT_COUNTRY_CODE);
                if (StringUtils.isEmpty(countryCode) || countryCode.equals(BusinessConstants.DEFAULT_COUNTRY_CODE)) {//国家码为空需要重新登录
                    mView.skipMainToLogin( BusinessConstants.LOGIN_STATE_NO_LOGIN);
                } else {
                    String userAccount = SPUtils.get().getString(PreferenceConstants.USER_ACCOUNT);

                    String pwd = SPUtils.get().getString(PreferenceConstants.USER_PASSWORD);
                    if (!StringUtils.isEmpty(userAccount)  && mLoginState == BusinessConstants.LOGIN_STATE_LOGIN_EFFECTIVE) {//已经登录过，且时间有效
                        if (!StringUtils.isEmpty(pwd)) {//本地存有密码和用户名
                            String loginAccount;
                            if(Tools.isEmail(userAccount)) {
                                loginAccount = userAccount;
                            }else {
                                loginAccount = countryCode + userAccount;
                            }
                            LoginAndOutReponsitory.getInstance().loginAndLoadData(loginAccount, pwd, new ILoginDataSource.LoginDataCallback() {
                                @Override
                                public void onLoadLoginData(LoginModule.Response loginResponses) {
                                    Phone2RobotMsgMgr.getInstance().init();
                                    //登录成功，存用户名和密码到本地
                                    mContext.setUserId(loginResponses.getData().getResult().get(0).getUserId());
                                    mView.skipToMainPage();

                                }

                                @Override
                                public void onDataNotAvailable(ThrowableWrapper e) {
                                    ToastUtils.showShortToast(e.getMessage());
                                    mNoticeMessageRepository.getMessages(Alpha2Application.getAlpha2().getUserId(),new IMessageDataSource.LoadMessageCallback() {
                                        @Override
                                        public void onMessageLoaded(List<NoticeMessage> tasks) {
                                            Logger.d( "正常 获取息中心消息");
                                            mNoticeMessageRepository.deleteMessageByMessageType(Alpha2Application.getAlpha2().getUserId(),BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG);
                                            mNoticeMessageRepository.deleteMessageByMessageType(Alpha2Application.getAlpha2().getUserId(),BusinessConstants.NOTICE_TYPE_SYS_MSG);
                                            mNoticeMessageRepository.saveMessages(tasks);

                                            mView.skipMainToLogin( BusinessConstants.LOGIN_STATE_NO_LOGIN);


                                        }

                                        @Override
                                        public void onDataNotAvailable(ThrowableWrapper e) {

                                            mView.skipMainToLogin( BusinessConstants.LOGIN_STATE_NO_LOGIN);
                                        }
                                    });


                                }
                            });

                        } else {//本地没有用户名和密码，则需要重新登录


                            mView.skipMainToLogin( BusinessConstants.LOGIN_STATE_LOGIN_TIME_OUT);
                        }
                    } else {
                        mView.skipMainToLogin( mLoginState);
                    }
                }

            }




    }



    /**
     * 创建桌面快捷方式
     *
     * @param needDelete true 表示版本升级时删除原来的快捷方式
     */

    private void createShortCut(boolean needDelete) {

        if (LauncherUtil.isShortCutExist(mContext)) {
            if (needDelete) {
                delShortcut();
                if (hasShortcut(mContext)) {
                    // 快捷方式还未删除
                    Logger.i("快捷方式未删除");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                addShortcut();
            }

        } else {
            addShortcut();
        }

    }

    /**
     * 添加桌面快捷
     */
    private void addShortcut() {
        Intent intent = new Intent();
        intent.setClass(mContext, WelcomeActivity.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,  mContext.getString(R.string.app_name));
        shortcut.putExtra("duplicate", false);//不允许重复创建
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(mContext, R.drawable.alpha2_ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        mContext.sendBroadcast(shortcut);
        Logger.i("添加快捷方式");
    }

    /**
     * 删除旧版本的桌面快捷方式
     */
    private void delShortcut() {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,  mContext.getString(R.string.app_name));
        //注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
        String appClass = "com.ubtechinc.alpha2ctrlapp.mMainPageActivity.WelcomeActivity";
        ComponentName comp = new ComponentName("com.ubtechinc.alpha2ctrlapp", appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
        mContext.sendBroadcast(shortcut);
        Logger.i("删除快捷方式");
    }

    /**
     * 判断桌面是否有快捷方式
     *
     * @param cx
     * @return
     * @author xuweitao
     */
    private boolean hasShortcut(Context cx) {
        boolean result = false;
        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = cx.getPackageManager();
            title = pm.getApplicationLabel(pm.getApplicationInfo(cx.getPackageName(), PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }
        final String uriStr;
        if (android.os.Build.VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        final Uri CONTENT_URI = Uri.parse(uriStr);
        final Cursor c = cx.getContentResolver().query(CONTENT_URI, null, "title=?", new String[]{title}, null);
        if (c != null && c.getCount() > 0) {
            result = true;
        }
        return result;
    }

}
