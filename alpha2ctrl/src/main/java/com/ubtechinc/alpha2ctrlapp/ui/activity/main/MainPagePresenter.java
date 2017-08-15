package com.ubtechinc.alpha2ctrlapp.ui.activity.main;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.common.base.Preconditions;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.data.app.AppConfigResponsitory;
import com.ubtechinc.alpha2ctrlapp.data.user.ILoginDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.LoginAndOutReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.AppUpdatePackgeInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AlphaParam;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ChatInfoItem;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MainPagePresenter
 * @date 5/15/2017
 * @author tanghongyu
 * @Description
 * @modifier
 * @modify_time
 */
public class MainPagePresenter implements MainPageContract.Presenter {
    public int shopType = 0;
    public AppInfo currentAppInfo = new AppInfo();
    public List<AppInfo> updatemodels = new ArrayList<AppInfo>();
    public  List<AppUpdatePackgeInfo> appInfoList = new ArrayList<AppUpdatePackgeInfo>();
    public String[] appdrwableList, actiondrwableList;
    public List<ActionDownLoad> actionList = new ArrayList<ActionDownLoad>();
    public boolean hasEdit = true;
    public int pictureMsg = 0;
    public int msgCount = 0;
    public int otherMsg = 0;
    //	private BadgeView bv;// 消息空间
    public  List<ChatInfoItem> msgList = new ArrayList<ChatInfoItem>();
    public  boolean hasConnectedNewRobot = false;
    private long existTime = 0;
    private ImageView has_new_icon;
    public  boolean hasGetLocolList = true;
    private MediaPlayer mediaPlayer;
    private List<String> list = new ArrayList<String>();
    private String mCurrentSetLanguage = "";
    public  final String KEY_MESSAGE = "message";
    public  final String KEY_EXTRAS = "extras";
    public  boolean isForeground = false;
    public  boolean isFromChangeLan = false;
    /**
     * Alpha 的配置
     **/
    public AlphaParam alphaParam = null;
    public  String LastVersion;// 最新的版本号
    public  String versionPath; // 最新的版本下载地址
    private Alpha2Application mContext;
    private MainPageContract.View mView;
    private AppConfigResponsitory mNoticeMessageRepository;
    LoginAndOutReponsitory loginReponsitory;
    //数据库
    public MainPagePresenter(@NonNull Context mContext, @NonNull MainPageContract.View view, @NonNull AppConfigResponsitory noticeMessageRepository, @NonNull LoginAndOutReponsitory loginReponsitory) {
        Preconditions.checkNotNull(mContext);
        Preconditions.checkNotNull(view);
        Preconditions.checkNotNull(noticeMessageRepository);
        this.mContext = (Alpha2Application) mContext;
        this.mView = view;
        this.mNoticeMessageRepository = noticeMessageRepository;
        this.loginReponsitory = loginReponsitory;
    }

    @Override
    public void initData() {

    }



    @Override
    public void loginOut() {
        loginReponsitory.loginOut(new ILoginDataSource.LoginOutCallback() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(ThrowableWrapper e) {

            }


        });
    }

    @Override
    public void startPlayMp3(String filePath) {
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
    public void stopPlayMp3() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
