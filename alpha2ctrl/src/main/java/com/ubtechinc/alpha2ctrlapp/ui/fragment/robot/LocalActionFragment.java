package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ant.country.CharacterParserUtil;
import com.ant.country.GetCountryNameSort;
import com.google.common.collect.Lists;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.base.CallbackListener;
import com.ubtechinc.alpha2ctrlapp.base.IRequestHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotActionDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotActionRepository;
import com.ubtechinc.alpha2ctrlapp.data.robot.SortBaseModel;
import com.ubtechinc.alpha2ctrlapp.entity.business.Alpha2Entrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewActionInfo;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.SearchFragment;
import com.ubtechinc.alpha2ctrlapp.util.CharacterParserUtils;
import com.ubtechinc.alpha2ctrlapp.util.PinyinComparatorUtils;
import com.ubtechinc.alpha2ctrlapp.widget.SideBar;
import com.ubtechinc.alpha2ctrlapp.widget.SideBar.OnTouchingLetterChangedListener;
import com.ubtechinc.alpha2ctrlapp.widget.SwitchActionTypeButton;
import com.ubtechinc.alpha2ctrlapp.widget.SwitchActionTypeButton.OnSwitchChangedTypeListener;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog.OnNegsitiveClick;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog.OnPositiveClick;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity.alphaParam;
import static com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity.dao;


/**
 * @author
 * @ClassName LocalActionFragment
 * @date 12/26/2016
 * @Description 本地动作表
 * @modifier tanghongyu
 * @modify_time 12/26/2016
 */
public class LocalActionFragment extends BaseContactFragememt implements IRequestHandler, OnPositiveClick, OnNegsitiveClick,
        OnSwitchChangedTypeListener, LocalActionAdpter.ActionRunListener {

    private final static int SERVER_PORT = 6100;

    private ListView listView;
    private LocalActionAdpter localActionAdapter;
    private List<SortBaseModel> mActionList = new ArrayList<SortBaseModel>();

    private ImageView updateProcess;
    private AnimationDrawable animationDrawable;
    private String[] alpha2MacList = new String[1];
    private TextView alphaName_tv;

    // private String name;
    private Alpha2Entrity mAlpha2 = null;

    private String TAG = "LocalActionFragment";


    private BaseHandler mHandler;

    private PinyinComparatorUtils pinyinComparator;

    private CharacterParserUtils characterParser;

    private SideBar sideBar;

    private TextView dialog;
    private RelativeLayout bottomControlLay;

    private Button btn_stop, btn_reset, btn_diaoDian;
    public CommonDiaglog exsitdialog;
    private SwitchActionTypeButton switchButton;
    private ImageView btn_search;
    private int mActionType = 1;
    private List<NewActionInfo> actionInfo = new ArrayList<NewActionInfo>();
    private TextView goToDwonloadTv;
    private TextView btnBack;
    private GetCountryNameSort countryChangeUtil;

    private CharacterParserUtil characterParserUtil;
    private String reSetId = "";
    private boolean isActionRunning = false;
    private String runningActionId;
    private String runningActionName;

    @Override
    public View onCreateFragmentView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.local_movement, container, false);
    }

    public void initView() {
        mActionType = 1;
        countryChangeUtil = new GetCountryNameSort();
        characterParserUtil = new CharacterParserUtil();
        initValue();
        listView = (ListView) mContentView.findViewById(R.id.actionlistView);
        alphaName_tv = (TextView) mContentView.findViewById(R.id.alphaName_tv);
        bottomControlLay = (RelativeLayout) mContentView.findViewById(R.id.relativeLayout2);
        switchButton = (SwitchActionTypeButton) mContentView.findViewById(R.id.switch_button);
        switchButton.setChangedTypeListener(this);
        localActionAdapter = new LocalActionAdpter(mActivity, mActionList, this, actionInfo, new CallbackListener<NewActionInfo>() {
            @Override
            public void callback(final NewActionInfo newActionInfo) {
                RobotActionRepository.getInstance().deleteActionById(newActionInfo.getActionId(), new IRobotActionDataSource.ControlActionCallback() {
                    @Override
                    public void onSuccess() {
                        LoadingDialog.dissMiss();

                        ToastUtils.showShortToast(
                                R.string.local_action_uninstall_action_tips_success);
                        onRefleshAfterDelte(newActionInfo.getActionId());
                        mMainPageActivity.actionList.clear();// 让动作库界面重新刷新数据

                    }

                    @Override
                    public void onFail(ThrowableWrapper e) {
                        ToastUtils.showShortToast(
                                R.string.local_action_uninstall_action_tips_failed);
                    }
                });
            }
        });
        listView.setOnItemClickListener(localActionAdapter);
        listView.setOnItemLongClickListener(localActionAdapter);
        localActionAdapter.setActionRunListener(this);
        // alphaName_tv.setText(name);

        sideBar = (SideBar) mContentView.findViewById(R.id.sidrbar);
        dialog = (TextView) mContentView.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        btn_stop = (Button) mContentView.findViewById(R.id.btn_stop);
        btn_reset = (Button) mContentView.findViewById(R.id.btn_reset);
        btn_diaoDian = (Button) mContentView.findViewById(R.id.btn_diaoDian);
        listView.setAdapter(localActionAdapter);
        this.setSideBarListener();
        exsitdialog = new CommonDiaglog(getActivity(), true);
        exsitdialog.setMessase(mActivity
                .getString(R.string.qpps_actionklist_warning_describe));
        exsitdialog.setNegsitiveClick(this);
        exsitdialog.setPositiveClick(this);
        exsitdialog.setButtonText(
                mActivity.getString(R.string.common_btn_cancel),
                mActivity.getString(R.string.common_btn_confirm));
        exsitdialog.setTitle(mActivity
                .getString(R.string.apps_actionklist_warning_title));
        btn_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onStop(v);
            }
        });
        btn_search = (ImageView) mContentView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bundle = new Bundle();
                bundle.putBoolean(IntentConstants.DATA_IS_LOCAL_DATA, true);
                bundle.putInt(IntentConstants.DATA_SEARCH_TYPE, mActionType);
                replaceFragment(SearchFragment.class.getName(), bundle);
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onDefault(v);
            }
        });
        btn_diaoDian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                exsitdialog.show();
            }
        });
        goToDwonloadTv = (TextView) mContentView.findViewById(R.id.tips1);
        goToDwonloadTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mMainPageActivity.shopType = BusinessConstants.SHOP_TYPE_ACTION;
                mMainPageActivity.changeType(false);
            }
        });
        btnBack = (TextView) mContentView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onRequestDate(MessageType.ON_STOP_ACTION);
                mMainPageActivity.onBack(v);


            }
        });
    }

    public void initValue() {
        // 实例化汉字转拼音类
        characterParser = CharacterParserUtils.getInstance();
        pinyinComparator = new PinyinComparatorUtils();
        // name =
        // this.getIntent().getStringExtra(Alpha2ActivityIntent.ALPHANAME);
    }


    /**
     * 为ListView数据排序
     *
     * @param date
     * @return
     */
    private List<SortBaseModel> filledData(String[] date) {
        List<SortBaseModel> mSortList = new ArrayList<SortBaseModel>();

        for (int i = 0; i < date.length; i++) {
            SortBaseModel sortModel = new SortBaseModel();
            sortModel.setName(date[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    private long mlLastClickTime = SystemClock.uptimeMillis();

    /*****************
     * 事件监听 beging
     *************************/
    public void onStop(View view) {
        if (isActionRunning) {
            onRequestDate(MessageType.ON_STOP_ACTION);
            isActionRunning = false;
            if (!TextUtils.isEmpty(runningActionId)) {
                btn_stop.setText(mActivity.getString(R.string.local_action_list_start));
                Drawable dra = getResources().getDrawable(R.drawable.btn_start_action);
                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                btn_stop.setCompoundDrawables(null, dra, null, null);
            }
        } else {
            if (RobotManagerService.getInstance().isConnectedRobot()) {

                if (!TextUtils.isEmpty(runningActionId)) {
                    boolean bTimeOut = Math.abs(SystemClock.uptimeMillis()
                            - mlLastClickTime) > 250 ? true : false;
                    mlLastClickTime = SystemClock.uptimeMillis();
                    if (bTimeOut) {// 防止频繁发送命令
                        localActionAdapter.setmPlayActionFileName(runningActionId);
                        mMainPageActivity.onPlayAction(runningActionId, runningActionName);
                        isActionRunning = true;
                        btn_stop.setText(mActivity.getString(R.string.local_action_list_stop));
                        Drawable dra = getResources().getDrawable(R.drawable.btn_stop_action);
                        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                        btn_stop.setCompoundDrawables(null, dra, null, null);
                    }
                }
            }
        }
    }


    @Override
    public void actionRun(String actionId, String actionName) {
        this.runningActionId = actionId;
        this.runningActionName = actionName;
        btn_stop.setText(mActivity.getString(R.string.local_action_list_stop));
        Drawable dra = getResources().getDrawable(R.drawable.btn_stop_action);
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
        btn_stop.setCompoundDrawables(null, dra, null, null);
        isActionRunning = true;
    }

    /**
     * 执行起来的动作
     *
     * @param view
     * @author zengdengyi
     * @date 2015年7月28日 上午9:44:23
     */
    public void onDefault(View view) {
//        PlayActionFile playfile = new PlayActionFile();
        String mFileName = "";
        if (!TextUtils.isEmpty(reSetId)) {
            mFileName = reSetId;
        } else {
            if (alphaParam != null && alphaParam.getServiceLanguage() != null && BusinessConstants.ROBOT_SERVICE_LANGUAGE_CHINESE.equalsIgnoreCase(alphaParam.getServiceLanguage())) {
                mFileName = "蹲下站起";//中文执行
            } else {
                mFileName = "Squat and stand up"; // 英文执行
            }
        }
        RobotActionRepository.getInstance().playAction(mFileName, new IRobotActionDataSource.ControlActionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(ThrowableWrapper e) {

            }
        });



    }

    public void onPowerDown(View view) {
        final RobotActionRepository robotActionRepository = RobotActionRepository.getInstance();
        robotActionRepository.stopPlayAction(new IRobotActionDataSource.ControlActionCallback() {
            @Override
            public void onSuccess() {
                for (int i = 1; i < 21; i++) {
                    robotActionRepository.readMotorAngleAction(i, new IRobotActionDataSource.ControlActionCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail(ThrowableWrapper e) {

                        }
                    });
                }
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                Logger.t(TAG).d(e.getMessage());
            }
        });



//        for (int i = 1; i < 21; i++) {
//            ReadMotorAngle entrity = new ReadMotorAngle();
//            entrity.setId(i);
//            sendRequest(entrity, MessageType.ALPHA_GETMOTORANGLE_REQUEST,
//                    alpha2MacList);
//        }
    }

    public void setSideBarListener() {
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = localActionAdapter.getPositionForSection(s
                        .charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }

            }
        });
    }

    /****************** 事件监听 end ***************************/

    /**
     * [获取动作列表]
     *
     * @author zengdengyi
     */

    public void onGetActionList() {

        RobotActionRepository.getInstance().getActionList(Constants.SYSTEM_LAN, mActionType, new IRobotActionDataSource.GetActionListCallback() {
            @Override
            public void onLoadActionList(List<NewActionInfo> actionInfoList) {
                LoadingDialog.dissMiss();
                onRefleshActionListView(actionInfoList);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });




    }



    /**
     * [更新动作文件列表]
     *
     * @param actionList
     * @author zengdengyi
     */

    public void onRefleshActionListView(List<NewActionInfo> actionList) {
        if (ListUtils.isEmpty(actionList)) {
            return;
        }
        dao.insertActionList(actionList);// 将新增的动作插入数据库
        if (listView.getEmptyView() == null && null != mContentView) {
            View emptyView = mContentView.findViewById(android.R.id.empty);
            if (null != emptyView) {
                listView.setEmptyView(emptyView);
            }
        }
        getDataFromDb(false);

    }


    /**
     * [更新动作文件列表]
     *
     * @param actionId
     * @author zengdengyi
     */

    private void onRefleshAfterDelte(String actionId) {
//        String[] str = name.split("/");
//        name = str[str.length - 1].split(".ubx")[0];
        dao.deleteAction(actionId) ;
            getDataFromDb(false);


    }


    /**
     * [停止动作]
     *
     * @author zengdengyi
     */

    public void onStopAction() {
        // TODO Auto-generated method stub
        mMainPageActivity.onStopAction();

    }

    @Override
    public void onRequestDate(int code) {
        // TODO Auto-generated method stub
        switch (code) {
            case MessageType.ON_STOP_ACTION:
                onStopAction();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMainPageActivity.mainTopView.setVisibility(View.GONE);
        // mMainPageActivity.btn_swich_active.setVisibility(View.GONE);
        mMainPageActivity.bottomLay.setVisibility(View.GONE);
        mMainPageActivity.currentFragment = this;
        getDataFromDb(true);
        Alpha2Application.getInstance().setFromAction(true);

    }




    private class MovementHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg == null) {
                Logger.i( "handleMessage msg is null.");
                return;
            }
            switch (msg.what) {

                case MessageType.ALPHA_LOST_CONNECTED:
                    Logger.i( "handleMessage ALPHA_LOST_CONNECTED.");
                    // isReBack((String) msg.obj);
                    mMainPageActivity.isCurrentAlpha2MacLostConnection((String) msg.obj);
                    break;


            }
        }
    }




    @Override
    public void OnNegsitiveClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnPositiveClick() {
        // TODO Auto-generated method stub
        onPowerDown(null);
    }

    @Override
    public void onChangeActive(Integer index) {

        // TODO Auto-generated method stub
        switch (index) {
            case 0:// 基本动作
                mActionType = 1;
                break;
            case 1:// 寓言故事
                mActionType = 3;
                break;
            case 2: // 音乐舞蹈
                mActionType = 2;
                break;

            default:
                break;
        }
        getDataFromDb(false);
    }

    /**
     * 从数据库中查询已安装的动作
     *
     * @param isGetAction 是否查询机器人的数据
     */
    private void getDataFromDb(boolean isGetAction) {
        List<NewActionInfo> newSortList = Lists.newArrayList();
        List<NewActionInfo> typeList = dao.queryActionListByType( mActionType);
        if (isGetAction) {
            onGetActionList();
            if (typeList.size() == 0) {
                LoadingDialog.getInstance(mActivity).show();// 查出来没有数据就显示加载框，否则后台进行
            }
        }
        //刷新界面数据
        if (typeList.size() > 0) {
            mActionList.clear();
            for (int i = 0; i < typeList.size(); i++) {
                if (TextUtils.isEmpty(typeList.get(i).getActionName())) {
                    continue;
                }
                SortBaseModel sortModel = new SortBaseModel();
                sortModel.setName(typeList.get(i).getActionName());
                String countrySortKey = characterParserUtil.getSelling(sortModel.getName());
                String sortLetter = countryChangeUtil.getSortLetterBySortKey(countrySortKey);
                if (sortLetter == null) {
                    sortLetter = countryChangeUtil.getSortLetterBySortKey(sortModel.getName());
                }
                sortModel.setSortLetters(sortLetter);

                if (typeList.get(i).getActionName().equals("蹲下站起") || typeList.get(i).getActionName().equals("Squat and stand up")) {
                    reSetId = typeList.get(i).getActionId();

                }
                mActionList.add(sortModel);

            }
            Collections.sort(mActionList, pinyinComparator);
            for (int y = 0; y < mActionList.size(); y++) {
                for (int z = 0; z < typeList.size(); z++) {
                    if (mActionList.get(y).getName().equals(typeList.get(z).getActionName())) {
                        newSortList.add(y, typeList.get(z));
                    }
                }
            }
        } else {
            mActionList.clear();
        }
        localActionAdapter.onNotifyDataSetChanged(mActionList, newSortList);

    }

}
