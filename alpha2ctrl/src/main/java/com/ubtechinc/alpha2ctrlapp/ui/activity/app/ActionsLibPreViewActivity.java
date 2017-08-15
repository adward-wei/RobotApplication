package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.orhanobut.logger.Logger;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.data.Injection;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionRepository;
import com.ubtechinc.alpha2ctrlapp.data.shop.CommentReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.shop.IActionDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.ICommentDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.CollectReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.user.ICollectDataSource;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionFileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionDetail;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.CommentInfo;
import com.ubtechinc.alpha2ctrlapp.events.ActionDownloadStatusChangeEvent;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.third.IWeiXinListener;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.CommentListAdapter;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.alpha2ctrlapp.widget.popWindow.SharePopuWindow;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * @ClassName ActionsLibPreViewActivity
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 动作详情预览页
 * @modifier
 * @modify_time
 */
public class ActionsLibPreViewActivity extends BaseContactFragememt implements IUiListener, IWeiXinListener {


    private VideoView vvw_main;
    private Timer update_vvw_main;
    private int vvw_main_time;
    private ImageView img_pause_or_continue;
    private ActionDetail mAction;
    private TextView txt_current_time;
    private TextView txt_totle_time;
    private SeekBar sbr_produce;
    private ImageView img_loading;


    private TextView txt_action_name;
    private TextView txt_action_time;
    private TextView txt_action_disc;
    private ImageView img_type;

    private ListView commentListView;
    private List<CommentInfo> commentList = new ArrayList<CommentInfo>();

    private TextView txt_send_comment;
    private EditText edt_comment;
    private TextView txt_praise;
    private ImageView img_praise, btn_collect;
    private ImageView btn_download;
    private int actionId;
    private CommentListAdapter commentAdpter;
    private boolean hasCollected = false;
    private LinearLayout shareLay;
    private SharePopuWindow shareDialog;
    private boolean isPause = false;
    private boolean isPraise = false;
    private ImageButton no_video_image;
    private boolean noVedio = false;
    private ImageView btn_play;
    private int mActionDownloadStatus = BusinessConstants.APP_STATE_INIT;// -1不可以下载 0 可以下载，1下载完成2 下载失败
    private TextView btn_downloadTextView;
    private View headView;
    private int page = 1;
    private boolean hasAddGetMore = false;
    private final int COUNT = 15;
    private View footerView;
    private LinearLayout btn_download_lay, praseLay, collectionLay;
    private RelativeLayout lay_ctrl;
    private View view_touch;
    private ImageView thumImage;
    private LinearLayout commentLay;
    private ActionRepository actionRepository;
    private boolean isDownLoadingAction = false;
    @Override
    public View onCreateFragmentView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_actions_lib_pre_view, container, false);
    }

    @Override
    public void initView() {
        if (mHandler == null)
            mHandler = new MHandler();
        actionRepository = Injection.provideActionRepository(mApplication);
        footerView =  LayoutInflater.from(mActivity).inflate(R.layout.get_more_layout, null, false);
        actionId = getBundle().getInt("actionid");
        commentListView = (ListView) mContentView.findViewById(R.id.lst_comment);
        commentAdpter = new CommentListAdapter(mActivity, commentList);
        headView = LayoutInflater.from(mActivity).inflate(R.layout.action_detail_header, null, false);
        commentListView.addHeaderView(headView);
        commentListView.setAdapter(commentAdpter);
        lay_ctrl = (RelativeLayout) mContentView.findViewById(R.id.lay_ctrl);
        shareLay = (LinearLayout) headView.findViewById(R.id.shareLay);
        btn_download = (ImageView) headView.findViewById(R.id.btn_download);
        btn_collect = (ImageView) headView.findViewById(R.id.btn_collect);
        commentLay = (LinearLayout) mContentView.findViewById(R.id.lay_send_comment);
        txt_send_comment = (TextView) mContentView.findViewById(R.id.txt_send_comment);
        edt_comment = (EditText) mContentView.findViewById(R.id.edt_comment);
        txt_praise = (TextView) headView.findViewById(R.id.txt_praise);
        img_praise = (ImageView) headView.findViewById(R.id.img_praise);
        btn_download_lay = (LinearLayout) headView.findViewById(R.id.btn_download_lay);
        praseLay = (LinearLayout) headView.findViewById(R.id.praise_lay);
        collectionLay = (LinearLayout) headView.findViewById(R.id.collect_lay);
        view_touch = (View) mContentView.findViewById(R.id.view_touch);

        btn_downloadTextView = (TextView) headView.findViewById(R.id.btn_downloadTextView);
        img_type = (ImageView) headView.findViewById(R.id.img_type);
        txt_action_name = (TextView) headView.findViewById(R.id.txt_action_name);
        txt_action_time = (TextView) headView.findViewById(R.id.txt_action_time);
        txt_action_disc = (TextView) headView.findViewById(R.id.txt_action_disc);

        thumImage = (ImageView) mContentView.findViewById(R.id.thum_image);

        // ---------------------------
        img_loading = (ImageView) mContentView.findViewById(R.id.img_loading);
        txt_current_time = (TextView) mContentView.findViewById(R.id.txt_current_time);
        txt_totle_time = (TextView) mContentView.findViewById(R.id.txt_totle_time);
        sbr_produce = (SeekBar) mContentView.findViewById(R.id.sbr_produce);
        vvw_main = (VideoView) mContentView.findViewById(R.id.vvw_main);
        img_pause_or_continue = (ImageView) mContentView.findViewById(R.id.img_pause_or_continue);
        no_video_image = (ImageButton) mContentView.findViewById(R.id.video_image);

        shareLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mAction != null) {
                    getActionUrl();
                }
            }
        });
        vvw_main.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ToastUtils.showShortToast(  "不能播放");
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
                no_video_image.setVisibility(View.VISIBLE);
                thumImage.setVisibility(View.GONE);
                img_pause_or_continue.setClickable(false);
                btn_play.setVisibility(View.GONE);
                noVedio = true;
                sbr_produce.setClickable(false);
                sbr_produce.setEnabled(false);
                return true;
            }
        });
        vvw_main.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                img_pause_or_continue
                        .setBackgroundResource(R.drawable.play_playing);
                isPause = false;
                no_video_image.setVisibility(View.GONE);
                thumImage.setVisibility(View.VISIBLE);
                btn_play.setVisibility(View.VISIBLE);
            }
        });

        view_touch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (lay_ctrl.getVisibility() != View.VISIBLE) {
                    showProgress();
                }
            }
        });
        vvw_main.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {
                // TODO Auto-generated method stub
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
                no_video_image.setVisibility(View.GONE);
                thumImage.setVisibility(View.GONE);
                // Toast.makeText(ActionsLibPreViewActivity.this, "开始播放视频", 500)
                // .show();
                // Toast.makeText(ActionsLibPreViewActivity.this,
                // "时长" + vvw_main.getDuration(), 500).show();
                txt_totle_time.setText("/" + TimeUtils.getMMTime(vvw_main.getDuration()));
                vvw_main_time = vvw_main.getDuration();
                if (update_vvw_main != null)
                    update_vvw_main.cancel();
                img_pause_or_continue
                        .setBackgroundResource(R.drawable.play_pause);
                update_vvw_main = new Timer();
                update_vvw_main.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = NetWorkConstant.DO_UPDATE_VIDEO_TIME;
                        msg.obj = vvw_main.getCurrentPosition();
                        mHandler.sendMessage(msg);

                    }
                }, 1000, 1000);
            }
        });

        img_pause_or_continue.setOnClickListener(new OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View arg0) {
                if (!noVedio && mAction != null && mAction.getActionVideoPath() != null) {
//					lay_ctrl.setVisibility(View.VISIBLE);
                    showProgress();
                    // TODO Auto-generated method stub
                    if (vvw_main.isPlaying()) {
                        vvw_main.pause();
                        isPause = true;
                        img_pause_or_continue
                                .setBackgroundResource(R.drawable.play_playing);

                    } else {
                        if (isPause) {
                            vvw_main.start();
                            isPause = false;
                            no_video_image.setVisibility(View.GONE);
                            thumImage.setVisibility(View.GONE);

                        } else {
                            Animation operatingAnim = AnimationUtils.loadAnimation(mMainPageActivity, R.anim.turn_around_anim);
                            operatingAnim.setInterpolator(new LinearInterpolator());
                            img_loading.setVisibility(View.VISIBLE);
                            btn_play.setVisibility(View.GONE);
                            img_loading.startAnimation(operatingAnim);
                            Uri path = Uri.parse(mAction.getActionVideoPath());
                            vvw_main.setVideoURI(path);
                            MediaController ctor = new MediaController(
                                    mMainPageActivity);
                            ctor.setBackgroundColor(ActionsLibPreViewActivity.this
                                    .getResources().getColor(R.color.actions_lib_video_crl));
                            ctor.setAnchorView(mMainPageActivity.findViewById(R.id.lay_ctrl));
                            ctor.setVisibility(View.GONE);
                            vvw_main.setMediaController(ctor);
                            vvw_main.start();

                        }
                        img_pause_or_continue
                                .setBackgroundResource(R.drawable.play_pause);
                    }


                }
            }

        });
        txt_send_comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                addComment();
            }
        });
        edt_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub

                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //do something;
                    slidingKeyBroad();
                    addComment();
//					return true;
                }
                return false;

            }
        });
        praseLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!isPraise)
                    praiseAction();
                else {
                    ToastUtils.showShortToast( R.string.shop_page_praised);
                }
            }
        });
        btn_download_lay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!RobotManagerService.getInstance().isConnectedRobot()) {
                    ToastUtils.showShortToast(  R.string.main_page_connect_alpha_tips);
                } else if (mActionDownloadStatus == -1) {
                    ToastUtils.showShortToast(  R.string.shop_page_disable_download);
                } else {
                    download();
                }
            }
        });
        collectionLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                doCollector();
            }
        });
        mMainPageActivity.mLeft_menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        btn_play = (ImageView) mContentView.findViewById(R.id.btn_play);
        btn_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!noVedio) {
//					lay_ctrl.setVisibility(View.VISIBLE);
                    showProgress();
                    if (vvw_main.isPlaying()) {
                        vvw_main.pause();
                        isPause = true;
                        img_pause_or_continue
                                .setBackgroundResource(R.drawable.play_playing);

                    } else {
                        if (isPause) {
                            vvw_main.start();
                            isPause = false;
                            no_video_image.setVisibility(View.GONE);
                            thumImage.setVisibility(View.GONE);

                        } else {
                            Animation operatingAnim = AnimationUtils.loadAnimation(mMainPageActivity, R.anim.turn_around_anim);
                            operatingAnim.setInterpolator(new LinearInterpolator());
                            img_loading.setVisibility(View.VISIBLE);
                            btn_play.setVisibility(View.GONE);
                            img_loading.startAnimation(operatingAnim);
                            Uri path = Uri.parse(mAction.getActionVideoPath());
                            vvw_main.setVideoURI(path);
                            MediaController ctor = new MediaController(
                                    mMainPageActivity);
                            ctor.setBackgroundColor(ActionsLibPreViewActivity.this
                                    .getResources().getColor(R.color.actions_lib_video_crl));
                            ctor.setAnchorView(mMainPageActivity.findViewById(R.id.lay_ctrl));
                            ctor.setVisibility(View.GONE);
                            vvw_main.setMediaController(ctor);
                            vvw_main.start();

                        }
                        img_pause_or_continue.setBackgroundResource(R.drawable.play_pause);
                    }


                }
            }
        });
        edt_comment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(edt_comment.getText().toString())) {
                    commentLay.setBackgroundResource(R.drawable.input_nomal_shape);
                    txt_send_comment.setTextColor(mActivity.getResources().getColor(R.color.text_color_t3));
                    txt_send_comment.setBackgroundResource(R.drawable.send_button_able);
                    txt_send_comment.setClickable(true);

                } else {
                    commentLay.setBackgroundResource(R.drawable.input_diable_shape);
                    txt_send_comment.setTextColor(mActivity.getResources().getColor(R.color.text_color_t4));
                    txt_send_comment.setBackgroundResource(R.drawable.send_button_disable);
                    txt_send_comment.setClickable(false);
                }
            }
        });
        footerView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                checkComment(page + 1);
            }
        });



        getActionDetail();
        EventBus.getDefault().register(this);
    }

    private void initUI() {
        Animation operatingAnim = AnimationUtils.loadAnimation(mMainPageActivity, R.anim.turn_around_anim);
        operatingAnim.setInterpolator(new LinearInterpolator());
        img_loading.setVisibility(View.VISIBLE);

        img_loading.startAnimation(operatingAnim);
        img_pause_or_continue.setClickable(false);
        sbr_produce.setClickable(false);
        sbr_produce.setEnabled(false);
        new GetBitMapThread().start();
        if (RobotManagerService.getInstance().isConnectedRobot()) {
            if (TextUtils.isEmpty(mAction.getActionOriginalId()))
                mActionDownloadStatus = MainPageActivity.dao.queryActionStatus(mAction.getActionName(), mAction.getActionPath());
            else {
                mActionDownloadStatus = MainPageActivity.dao.queryActionStatus(mAction.getActionOriginalId(), mAction.getActionPath());
            }
        } else {
            mActionDownloadStatus = BusinessConstants.APP_STATE_ERROR;

        }
        changeDownloadStatus(mActionDownloadStatus);
        if (mAction.getIsPraise() == 0) {
            img_praise.setBackgroundResource(R.drawable.action_lib_icon_praise);
            isPraise = false;
        } else {
            img_praise.setBackgroundResource(R.drawable.action_lib_icon_praise_green);
            isPraise = true;
        }
        txt_praise.setText(mAction.getActionPraiseTime() + "");
        switch (mAction.getActionType()) {
            case 1:
                img_type.setImageResource(R.drawable.icon_small_base);
                break;
            case 2:
                img_type.setImageResource(R.drawable.icon_small_dance);
                break;
            case 3:
                img_type.setImageResource(R.drawable.icon_small_story);
                break;
            default:
                img_type.setImageResource(R.drawable.icon_small_base);
                break;
        }
        if (mAction.getIsCollect() == 0) {
            btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collect);
            hasCollected = false;
        } else {
            btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collection_green);
            hasCollected = true;
        }

        txt_action_time.setText(Tools.getActionTime(mAction.getActionTime()));
        if (TextUtils.isEmpty(mAction.getActionLangName())) {
            txt_action_name.setText(mAction.getActionName());
        } else {
            txt_action_name.setText(mAction.getActionLangName());
        }
        if (TextUtils.isEmpty(mAction.getActionLangDesciber())) {

            if (!TextUtils.isEmpty(mAction.getActionDesciber()))
                txt_action_disc.setText(mAction.getActionDesciber());
            else {
                txt_action_disc.setText(mActivity.getText(R.string.shop_page_no_description));
            }

        } else {
            txt_action_disc.setText(mAction.getActionLangDesciber());
        }

    }

    private void getActionDetail() {

        actionRepository.getActionDetail(actionId, new IActionDataSource.LoadActionDetailCallback() {
            @Override
            public void onActionDetailLoaded(ActionDetail tasks) {
                if (tasks != null) {
                    mAction = tasks;
                    String videoPath = mAction.getActionVideoPath();
                    videoPath = videoPath.replaceAll(" ", "%20");
                    mAction.setActionVideoPath(videoPath);
                    initUI();
                    checkComment(1);
                }
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

                ToastUtils.showShortToast(e.getMessage());
                LoadingDialog.dissMiss();
            }
        });
    }

    private void doCollector() {
        if (mAction != null) {
            if (hasCollected) {
                cancleCollect();
            } else {
                addCollector();
            }
        }
    }

    private void addComment() {
        if (TextUtils.isEmpty(edt_comment.getText().toString())) {
            ToastUtils.showShortToast(  R.string.shop_page_content_no_empty);
        } else {

            LoadingDialog.getInstance(mActivity).show();
            CommentReponsitory.getInstance().addComment(actionId, edt_comment.getText().toString(), BusinessConstants.COMMENT_TYPE_COMMENT, 0, new ICommentDataSource.AddCommentCallback() {
                @Override
                public void onSuccess() {
                    ToastUtils.showShortToast(  R.string.shop_page_add_comment_success);
                    edt_comment.setText("");
                    commentList.clear();
                    checkComment(1);
                }

                @Override
                public void onFail(ThrowableWrapper e) {
                    ToastUtils.showShortToast(e.getMessage());
                }
            });
        }

    }

    private void addCollector() {
        if (mAction != null) {
            LoadingDialog.getInstance(mActivity).show();
            CollectReponsitory.get().doCollect(actionId, 1, new ICollectDataSource.CollectCallback() {
                @Override
                public void onSuccess() {
                    ToastUtils.showShortToast(  R.string.shop_page_collect_success);
                    btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collection_green);
                    hasCollected = true;
                }

                @Override
                public void onFail(ThrowableWrapper e) {
                    ToastUtils.showShortToast(  R.string.shop_page_collect_failed);
                    btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collect);
                    hasCollected = false;
                }
            });
        }

    }

    private void cancleCollect() {

        CollectReponsitory.get().cancelCollect(actionId, 1, new ICollectDataSource.CollectCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShortToast(  R.string.shop_page_cancel_collect_success);
                btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collect);
                hasCollected = false;
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                ToastUtils.showShortToast(  R.string.shop_page_cancel_collect_failed);
            }
        });

    }

    private static final String TAG = "ActionsLibPreViewActivi";
    public void checkComment(int page) {
        Logger.i( "查询评论");
        this.page = page;

        CommentReponsitory.getInstance().getCommentList(actionId, new ICommentDataSource.CommentDataCallback() {
            @Override
            public void onLoadADData(List<CommentInfo> loginResponses) {
                LoadingDialog.dissMiss();
                refreshCommentList(loginResponses);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
                LoadingDialog.dissMiss();
            }
        });
    }

    private void praiseAction() {


        actionRepository.praiseAction(String.valueOf(actionId), new IActionDataSource.PraiseActionCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShortToast(  R.string.shop_page_zan_success);
                int times = Integer.valueOf(txt_praise.getText().toString());
                txt_praise.setText(times + 1 + "");
                img_praise.setBackgroundResource(R.drawable.action_lib_icon_praise_green);
                isPraise = true;
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                ToastUtils.showShortToast(  R.string.shop_page_zan_failed);
                img_praise.setBackgroundResource(R.drawable.action_lib_icon_praise);
            }
        });



    }

    private void download() {
        if (mAction != null) {
            if (mActionDownloadStatus == BusinessConstants.APP_STATE_INSTALL_SUCCESS) {
                if (TextUtils.isEmpty(mAction.getActionOriginalId())) {
                    Alpha2Application.getInstance().setShopAction(true);
                    mMainPageActivity.onPlayAction(mAction.getActionName(), txt_action_name.getText().toString());
                } else {
                    Alpha2Application.getInstance().setShopAction(true);
                    mMainPageActivity.onPlayAction(mAction.getActionOriginalId(), txt_action_name.getText().toString());
                }

            } else if (mActionDownloadStatus == BusinessConstants.APP_STATE_INIT || mActionDownloadStatus == BusinessConstants.APP_STATE_DOWNLOAD_SUCCESS) {
                ActionFileEntrity bean = new ActionFileEntrity();
                bean.setActionFilePath(mAction.getActionPath());
                bean.setActionId(mAction.getActionId());
                bean.setActionName(mAction.getActionName());
                bean.setActionType(mAction.getActionType());
                if (TextUtils.isEmpty(mAction.getActionOriginalId())) {
                    bean.setActionOriginalId(mAction.getActionName());
                } else {
                    bean.setActionOriginalId(mAction.getActionOriginalId());
                }
                mMainPageActivity.downLoadAction(bean, null, mAction);
                isDownLoadingAction = true;
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_loading));
            }

        }

    }

    private void getActionUrl() {
        if (Constants.SHARE_ACTION_URL.equals("")) {
            LoadingDialog.getInstance(mActivity).show();
            actionRepository.getShareUrl("url", "share", new IActionDataSource.LoadShareUrlCallback() {
                @Override
                public void onShareUrlLoaded(String url) {
                    LoadingDialog.dissMiss();
                    Constants.SHARE_ACTION_URL = url;
                    showShareDialog();
                }

                @Override
                public void onDataNotAvailable(ThrowableWrapper e) {
                    LoadingDialog.dissMiss();
                    ToastUtils.showShortToast(e.getMessage());
                }
            });

        } else {
            showShareDialog();
        }

    }

    private void showShareDialog() {
        if (shareDialog == null)
            shareDialog = new SharePopuWindow(getActivity(), 1, mAction, this, this);
        shareDialog.show();
    }

    private class MHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg == null) {
                Logger.i( "handleMessage msg is null.");
                return;
            }
            switch (msg.what) {

                case MessageType.ALPHA_LOST_CONNECTED:

                    mMainPageActivity.isCurrentAlpha2MacLostConnection((String) msg.obj);
                    break;


                case NetWorkConstant.DO_UPDATE_VIDEO_TIME:
                    int time = (Integer) msg.obj;
                    if (time == 0) {
                        update_vvw_main.cancel();
                        img_pause_or_continue.setBackgroundResource(R.drawable.play_playing);
                    }
                    if (txt_totle_time.getText().toString().equals("/00:00")) {
                        txt_totle_time.setText("/" + TimeUtils.getMMTime(vvw_main_time));
                    }

                    txt_current_time.setText(TimeUtils.getMMTime(time));
                    sbr_produce.setProgress(time * 100 / vvw_main_time);
                    break;




                case NetWorkConstant.RESPONSE_COLLECT_REPEATE:
                    btn_collect.setBackgroundResource(R.drawable.action_lib_icon_collection_green);
                    hasCollected = true;
                    break;


                case -1010:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    img_loading.clearAnimation();
                    img_loading.setVisibility(View.INVISIBLE);
                    no_video_image.setVisibility(View.GONE);
                    thumImage.setVisibility(View.GONE);
                    Logger.i( "获取到图片");
                    if (null != bitmap) {
                        thumImage.setImageBitmap(bitmap);
                        thumImage.setVisibility(View.VISIBLE);
                    }else{
                        thumImage.setVisibility(View.GONE);
                    }

                    img_pause_or_continue.setClickable(true);
                    btn_play.setVisibility(View.VISIBLE);
                    sbr_produce.setClickable(true);
                    sbr_produce.setEnabled(true);
//                    if (bitmap != null) {
//                        Logger.i( "获取到图片");
//                        thumImage.setImageBitmap(bitmap);
//                        thumImage.setVisibility(View.VISIBLE);
//                        img_pause_or_continue.setClickable(true);
//                        btn_play.setVisibility(View.VISIBLE);
//                        sbr_produce.setClickable(true);
//                        sbr_produce.setEnabled(true);
////	        		showProgress();
//                        lay_ctrl.setVisibility(View.VISIBLE);
//                    } else {
//                        Logger.i( "获取到图片null");
//                        no_video_image.setVisibility(View.VISIBLE);
//                        img_pause_or_continue.setClickable(false);
//                        thumImage.setVisibility(View.GONE);
//                        btn_play.setVisibility(View.GONE);
//                        noVedio = true;
//                        sbr_produce.setClickable(false);
//                        sbr_produce.setEnabled(false);
//                        lay_ctrl.setVisibility(View.GONE);
//                    }
                    break;
                case -1011:
                    lay_ctrl.setVisibility(View.GONE);
                    break;

            }
        }
    }


    private void refreshCommentList(List<CommentInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            this.commentList.add(list.get(i));
        }
        // 当数据有过15条，且不是获取最新的15条的时候，出现加载更多
        if (list.size() >= COUNT && !hasAddGetMore) {
            commentListView.addFooterView(footerView);
            hasAddGetMore = true;
        } else if (list.size() < COUNT && hasAddGetMore) {
            commentListView.removeFooterView(footerView);
            hasAddGetMore = false;
        }
        commentAdpter.onNotifyDataSetChanged(commentList);

    }

    public class GetBitMapThread extends Thread {
        private boolean hasGet = false;

        @Override
        public void run() {
            while (!hasGet) {
                hasGet = true;
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                Bitmap bitmap = null;
                try {
                    media.setDataSource(mAction.getActionVideoPath(), new HashMap<String, String>());
                    Logger.i( "开始获取图片");
                    bitmap = media.getFrameAtTime();

                } catch (Exception e) {
                    Logger.i( "读图片异常");
                    if (e != null)
                        Logger.i( e.getMessage());
                }

                mHandler.obtainMessage(-1010, bitmap).sendToTarget();


            }

        }
    }

    ;

    @Override
    public void refreshDownLoadData() {

        if (mAction.getActionOriginalId() == null || TextUtils.isEmpty(mAction.getActionOriginalId()))
            mActionDownloadStatus = MainPageActivity.dao.queryActionStatus(mAction.getActionName(), mAction.getActionPath());
        else {
            mActionDownloadStatus = MainPageActivity.dao.queryActionStatus(mAction.getActionOriginalId(), mAction.getActionPath());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mMainPageActivity.mainTopView.setVisibility(View.GONE);
        mMainPageActivity.bottomLay.setVisibility(View.GONE);
        mMainPageActivity.currentFragment = this;
    }

    private void changeDownloadStatus(int status) {
        mActionDownloadStatus = status;
        switch (mActionDownloadStatus) {
            case BusinessConstants.APP_STATE_ERROR:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.disable_download));
                btn_downloadTextView.setText(R.string.shop_page_download);
                break;
            case BusinessConstants.APP_STATE_INIT:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.download_state));
                btn_downloadTextView.setText(R.string.shop_page_download);
                break;
            case BusinessConstants.APP_STATE_DOWNLOADING:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_loading));
//			btn_download.startAnimation(DownloadActionAnimation.getDownloadActionAnimation(mActivity));
                btn_downloadTextView.setText(R.string.shop_page_downloading);
                break;
            case BusinessConstants.APP_STATE_DOWNLOAD_SUCCESS:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_installing));
//			btn_download.startAnimation(DownloadActionAnimation.getDownloadActionAnimation(mActivity));
                btn_downloadTextView.setText(R.string.shop_page_installeding);
                break;
            case BusinessConstants.APP_STATE_DOWNLOAD_FAIL:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_failed));
                btn_downloadTextView.setText(R.string.shop_page_download_falied);
                break;
            case BusinessConstants.APP_STATE_INSTALL_SUCCESS:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.play_playing));
                btn_downloadTextView.setText(R.string.shop_page_open);
                break;
            case BusinessConstants.APP_STATE_INSTALL_FAIL:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_failed));
                btn_downloadTextView.setText(R.string.shop_page_installed_failed);
                break;
            case BusinessConstants.APP_STATE_INSUFFCIENT_SPACE:
                btn_download.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.app_failed));
                ToastUtils.showShortToast(  R.string.news_storage_title);
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mMainPageActivity.mLeft_menu.setTouchModeAbove(mMainPageActivity.mLeft_menu.intTouchAbouve);
    }



    @Override
    public void onCancel() {
        // TODO Auto-generated method stub
        LoadingDialog.dissMiss();
    }

    @Override
    public void onComplete(Object arg0) {
        // TODO Auto-generated method stub
        LoadingDialog.dissMiss();
        ToastUtils.showShortToast(  R.string.shop_page_share_success);
    }

    @Override
    public void onError(UiError arg0) {
        // TODO Auto-generated method stub
        LoadingDialog.dissMiss();
        ToastUtils.showShortToast(  R.string.shop_page_share_error);

    }

    @Override
    public void noteWeixinNotInstalled() {
        // TODO Auto-generated method stub
        ToastUtils.showShortToast(  R.string.ui_weixin_not_install);
    }

    private void showProgress() {
        lay_ctrl.setVisibility(View.VISIBLE);

        if (mHandler.hasMessages(-1011))
            mHandler.removeMessages(-1011);
        mHandler.sendEmptyMessageDelayed(-1011, 5000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActionDownloadStatusChangeEvent event) {
        Logger.t(TAG).d( "onEvent ActionDownloadStatusChangeEvent state = %s" +event.getActionFileEntrity().toString() );
        ActionFileEntrity entrity = event.getActionFileEntrity();
        changeDownloadStatus(entrity.getDownloadState());
        if (entrity.getDownloadState() == 4 && isDownLoadingAction) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("actionName", entrity.getActionName());
            MobclickAgent.onEvent(mActivity, Constants.YOUMENT_SHOP_APP_DOWNLOAD_TIMES, map);
            isDownLoadingAction = false;
        }
    }


}


