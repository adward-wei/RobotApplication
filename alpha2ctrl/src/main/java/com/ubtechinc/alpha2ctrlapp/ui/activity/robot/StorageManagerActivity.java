package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAppDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAppRepository;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AppPackageSimpleInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.StorageResponse;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.UnIntallApp;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.third.AidlService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.robot.RobotAppManagerAdapter;
import com.ubtechinc.alpha2ctrlapp.util.DataCleanManager;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.ConnectRobotDialog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.NormalLoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/*************************
 * @author liuhai
 * @date 2017/4/13
 * @Description Alpha机器人内存管理
 * @modify
 * @modify_time
 **************************/
public class StorageManagerActivity extends BaseContactActivity implements View.OnClickListener, CommonDiaglog.OnNegsitiveClick, CommonDiaglog.OnPositiveClick, AdapterView.OnItemClickListener {
    private static final String TAG = StorageManagerActivity.class.getSimpleName();
    private TextView btn_finish;
    public TextView mTitle;
    private LinearLayout llBack;
    private TextView tvUsedSize;
    private TextView tvFreeSize;
    private TextView tvAppTip;
    private ListView mListView;
    private LinearLayout llSelected;
    private ImageView ivSelected;

    private RelativeLayout rlBottom;
    private CardView mCardView;
    private RobotAppManagerAdapter mRobotAppManagerAdapter;
    private List<AppPackageSimpleInfo> mAppInfos = new ArrayList<>();
    private List<AppPackageSimpleInfo> mSelectedAppInfos = new ArrayList<>();
    private boolean isEdit = false; //是否处于编辑状态 true为编辑状态
    private Button btnUnInstall;
    //卸载对话框、卸载失败对话框
    private CommonDiaglog unInstallDialog;
    private ConnectRobotDialog mConnectRobotDialog;
    private NormalLoadingDialog mNormalLoadingDialog;

    //掉线广播
    private UnLineBroadcastReceiver mUnLineBroadcastReceiver;

    private boolean isAllSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_manager);
        initView();
        IntentFilter intentFilter = new IntentFilter(AidlService.RECEIVE_ERROR_DATA);
        mUnLineBroadcastReceiver = new UnLineBroadcastReceiver();
        registerReceiver(mUnLineBroadcastReceiver, intentFilter);
        getRobotStorage();

    }

    /**
     * 初始化数据
     */
    private void initView() {
        mTitle = (TextView) findViewById(R.id.tv_title);
        mTitle.setTextSize(16);
        mTitle.setText(R.string.device_robot_storage_manager);
        btn_finish = (TextView) findViewById(R.id.tv_right);
        btn_finish.setText(R.string.device_robot_manager);
        btn_finish.setOnClickListener(this);
        btn_finish.setEnabled(false);
        btn_finish.setTextColor(this.getResources().getColor(R.color.text_color_disable));
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(this);
        btn_finish.setVisibility(View.VISIBLE);
        rlBottom = (RelativeLayout) findViewById(R.id.ll_bottom);
        mCardView = (CardView) findViewById(R.id.card_bottom);
        tvUsedSize = (TextView) findViewById(R.id.tv_storage_used_size);
        tvFreeSize = (TextView) findViewById(R.id.tv_storage_free_size);
        tvAppTip = (TextView) findViewById(R.id.tv_app_tip);

        mListView = (ListView) findViewById(R.id.app_list_view);
        mRobotAppManagerAdapter = new RobotAppManagerAdapter(this, mAppInfos);
        mListView.setAdapter(mRobotAppManagerAdapter);
        mListView.setOnItemClickListener(this);
        btnUnInstall = (Button) findViewById(R.id.bn_unstall);
        btnUnInstall.setOnClickListener(this);
        btnUnInstall.setEnabled(false);

        llSelected = (LinearLayout) findViewById(R.id.ll_selected);
        ivSelected = (ImageView) findViewById(R.id.iv_seleted_bottom);
        llSelected.setOnClickListener(this);

        initDialog();
    }

    /**
     * 初始化对话框
     */
    private void initDialog() {
        initUnStallDialog();
        //机器人掉线
        mConnectRobotDialog = ConnectRobotDialog.getInstance(this);
        mConnectRobotDialog.setCanceledOnTouchOutside(false);
        mConnectRobotDialog.setPositiveClick(new ConnectRobotDialog.OnPositiveClick() {
            @Override
            public void OnPositiveClick() {
                mConnectRobotDialog.dismiss();
                mConnectRobotDialog = null;
                Intent intent2 = new Intent(StorageManagerActivity.this, MyDeviceActivity.class);
                mContext.startActivity(intent2);
                ((Alpha2Application) StorageManagerActivity.this.getApplication()).removeActivity();
            }
        });

    }

    private void initUnStallDialog() {
        //确定删除框
        unInstallDialog = new CommonDiaglog(this, false);
        unInstallDialog.setMessase(this.getString(R.string.news_warning_describe));
        unInstallDialog.setButtonText(getString(R.string.common_btn_cancel), getString(R.string.common_btn_confirm));
        unInstallDialog.setNegsitiveClick(this);
        unInstallDialog.setPositiveClick(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!RobotManagerService.getInstance().isConnectedRobot()) {
            finish();
        }
    }



    /**
     * 刷新数据
     *
     * @param storageResponse
     */
    private void refreshView(StorageResponse storageResponse) {
        tvFreeSize.setText(DataCleanManager.getFormatSize(storageResponse.getAvailableInternalSize()) + "");
        tvUsedSize.setText(DataCleanManager.getFormatSize(storageResponse.getTotalInternalSize() - storageResponse.getAvailableInternalSize()) + "");
        mAppInfos = storageResponse.getAppList();
        Collections.sort(mAppInfos, new CompareList());
        if (null != mAppInfos && mAppInfos.size() > 0) {
            tvAppTip.setText(getString(R.string.app_list) + "  ( " + mAppInfos.size() + " )");
            btn_finish.setEnabled(true);
            btn_finish.setTextColor(this.getResources().getColor(R.color.text_color_t5));
            mRobotAppManagerAdapter.setAppInfos(mAppInfos);
        } else {
            mAppInfos.clear();
            mRobotAppManagerAdapter.setAppInfos(mAppInfos);
            tvAppTip.setText(getString(R.string.app_list) + "( " + 0 + " )");
            btn_finish.setEnabled(false);
            btn_finish.setTextColor(this.getResources().getColor(R.color.text_color_disable));
        }

    }

    public class CompareList implements Comparator {
        /*
            * int compare(AppEntrityStorageInfo o1, AppEntrityStorageInfo o2) 返回一个基本类型的整型，
            * 返回负数表示：o1 小于o2，
            * 返回0 表示：o1和o2相等，
            * 返回正数表示：o1大于o2。
            */

        @Override
        public int compare(Object obj1, Object obj2) {
            AppPackageSimpleInfo info1 = (AppPackageSimpleInfo) obj1;
            AppPackageSimpleInfo info2 = (AppPackageSimpleInfo) obj2;
            //按照学生的年龄进行升序排列
            if (info1.getAppSize() < info2.getAppSize()) {
                return 1;
            }
            if (info1.getAppSize() == info2.getAppSize()) {
                return 0;
            }
            return -1;
        }
    }


    /**
     * 获取机器人内存管理
     */
    private void getRobotStorage() {
        LoadingDialog.getInstance(mContext).show();
        RobotAppRepository.getInstance().getRobotStorage(new IRobotAppDataSource.GetRobotStorageCallback() {
            @Override
            public void onLoadRobotStorage(StorageResponse storageResponse) {
                Logger.d( "获取结果" + storageResponse.getAppList().size());
                refreshView(storageResponse);
                LoadingDialog.dissMiss();
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {

            }
        });

    }

    /**
     * 批量卸载应用
     */
    private void deleteRobotApps() {


        RobotAppRepository.getInstance().batchUninstall(mSelectedAppInfos, new IRobotAppDataSource.BatchUninstallAppCallback() {
            @Override
            public void onSuccess(List<UnIntallApp> unIntallApps) {
                if (null != mNormalLoadingDialog) {
                    mNormalLoadingDialog.dismiss();
                }
                dealUnInstallInfoResult(unIntallApps);
                Logger.d( "卸载结果" + unIntallApps.size());
                btn_finish.performClick();
                mSelectedAppInfos.clear();
                mRobotAppManagerAdapter.initData();
            }

            @Override
            public void onFail(ThrowableWrapper e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                if (!isEdit) {
                    btn_finish.setText(R.string.common_btn_cancel);
                    mCardView.setVisibility(View.VISIBLE);
                    isAllSelected = true;
                    llSelected.performClick();
                } else {
                    btn_finish.setText(R.string.device_robot_manager);
                    mRobotAppManagerAdapter.initData();
                    mCardView.setVisibility(View.GONE);

                }
                isEdit = !isEdit;
                mRobotAppManagerAdapter.setManager(isEdit);
                break;
            case R.id.bn_unstall:
                if (null != unInstallDialog) {
                    initUnStallDialog();
                    unInstallDialog.setMessase(this.getString(R.string.device_robot_common_dialog_first_content) + " " + mSelectedAppInfos.size() +
                            " " + this.getString(R.string.device_robot_common_dialog_last_content));
                    unInstallDialog.show();
                }
                break;

            case R.id.ll_selected:
                clickAllSelected();
                break;

            case R.id.ll_back:
                finish();
                break;
        }
    }

    /**
     * 点击底部全选按钮
     */
    private void clickAllSelected() {
        isAllSelected = !isAllSelected;
        mSelectedAppInfos.clear();
        for (int i = 0; i < mAppInfos.size(); i++) {
            if (isAllSelected) {
                mSelectedAppInfos.add(mAppInfos.get(i));
            }
            RobotAppManagerAdapter.getIsSelectedMap().put(i, isAllSelected);
            mRobotAppManagerAdapter.notifyDataSetChanged();
        }

        if (isAllSelected) {
            ivSelected.setImageResource(R.drawable.ic_press);
            btnUnInstall.setEnabled(true);
            btnUnInstall.setTextColor(StorageManagerActivity.this.getResources().getColor(R.color.white));
        } else {
            ivSelected.setImageResource(R.drawable.ic_unpress);
            btnUnInstall.setEnabled(false);
            btnUnInstall.setTextColor(StorageManagerActivity.this.getResources().getColor(R.color.text_color_t2));
        }
    }

    /**
     * 回调选择的App
     *
     * @param appInstalledInfo
     * @param statu            true:add false:remove
     */
    public void selectedApp(AppPackageSimpleInfo appInstalledInfo, boolean statu) {
        if (statu) {
            mSelectedAppInfos.add(appInstalledInfo);
        } else {
            mSelectedAppInfos.remove(appInstalledInfo);
        }
        if (mSelectedAppInfos.size() > 0) {
            btnUnInstall.setEnabled(true);
            btnUnInstall.setTextColor(this.getResources().getColor(R.color.white));
        } else {
            btnUnInstall.setEnabled(false);
            btnUnInstall.setTextColor(this.getResources().getColor(R.color.text_color_t2));
            isAllSelected = false;
            ivSelected.setImageResource(R.drawable.ic_unpress);
        }
        if (mSelectedAppInfos.size() == mRobotAppManagerAdapter.getAppInfos().size()) {
            isAllSelected = true;
            ivSelected.setImageResource(R.drawable.ic_press);
        } else {
            isAllSelected = false;
            ivSelected.setImageResource(R.drawable.ic_unpress);
        }


    }

    /**
     * Item点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isEdit) {
            mRobotAppManagerAdapter.setSelectedItem(position);
            selectedApp(mRobotAppManagerAdapter.getAppInfos().get(position), mRobotAppManagerAdapter.getIsSelectedMap().get(position));
        }
    }

    /**
     * 广播接收者机器人掉线通知
     *
     * @author weijiang204321
     */
    class UnLineBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(AidlService.AIDL_RECEIVE_ERROR_CODE).equals(context.getString(R.string.main_page_alpha2_offline)) && null != mConnectRobotDialog) {
                mConnectRobotDialog.show();
                if (null != mNormalLoadingDialog) {
                    mNormalLoadingDialog.dismiss();
                }

                if (null != unInstallDialog) {
                    unInstallDialog.dismiss();
                }
            }
        }

    }


    /**
     * 卸载对话框回调
     */
    @Override
    public void OnNegsitiveClick() {
        unInstallDialog.dismiss();
    }

    @Override
    public void OnPositiveClick() {
        deleteRobotApps();
        unInstallDialog.dismiss();
        showProgressDialog();
    }

    /**
     * 处理卸载返回结果
     *
     * @param unIntallAppList
     */
    private void dealUnInstallInfoResult(List<UnIntallApp> unIntallAppList) {
        boolean isDeleteSuccess = true;
        String unInstallFiledName = new String();
        for (UnIntallApp unIntallApp : unIntallAppList) {
            if (!unIntallApp.isUnInstallSuccess()) {
                isDeleteSuccess = false;
                unInstallFiledName = unIntallApp.getName();
                break;
            }
        }
        showUnInstallResult(isDeleteSuccess, unInstallFiledName);
    }


    /**
     * 卸载中或者卸载成功回调对话框
     *
     * @param isSuccess true则表示卸载成功 false表示失败
     */
    private void showUnInstallResult(boolean isSuccess, String unInstallFailedName) {
        if (isSuccess) {
            mNormalLoadingDialog = new NormalLoadingDialog(this);
            mNormalLoadingDialog.setImageView(R.drawable.uninstall_success);
            mNormalLoadingDialog.setTipContent(getString(R.string.app_uninstall_success));
            mNormalLoadingDialog.stopImageAnim();
            mNormalLoadingDialog.show();
            HandlerUtils.runUITask(new Runnable() {
                @Override
                public void run() {
                    if (null != mNormalLoadingDialog) {
                        mNormalLoadingDialog.dismiss();
                    }
                    getRobotStorage();
                }
            }, 1000);
        } else {
            unInstallFailed(unInstallFailedName);
        }
    }

    /**
     * 显示卸载失败对话框
     */
    private void unInstallFailed(String filedName) {
        if (null != unInstallDialog) {
            if (!TextUtils.isEmpty(filedName)) {
                unInstallDialog.setMessase(filedName + getString(R.string.app_uninstall_failed));
            } else {
                unInstallDialog.setMessase(getString(R.string.app_uninstall_failed));
            }
            unInstallDialog.setNegsitiveVisible(false);
            unInstallDialog.setCancelable(false);
            unInstallDialog.setPositiveClick(new CommonDiaglog.OnPositiveClick() {
                @Override
                public void OnPositiveClick() {
                    getRobotStorage();
                    unInstallDialog.dismiss();
                }
            });
            unInstallDialog.setButtonText("", this.getResources().getString(R.string.common_btn_confirm));
            unInstallDialog.show();
        }
    }

    /**
     * 加载中对话框
     */
    private void showProgressDialog() {
        mNormalLoadingDialog = new NormalLoadingDialog(this);
        mNormalLoadingDialog.setImageView(R.drawable.loading_progress);
        mNormalLoadingDialog.setTipContent(getString(R.string.device_robot_dialog_unstalling));
        mNormalLoadingDialog.startImageAnim();
        mNormalLoadingDialog.setCancelable(false);
        mNormalLoadingDialog.show();
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                if (null != mNormalLoadingDialog) {
                    mNormalLoadingDialog.dismiss();
                }
            }
        }, 6000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUnLineBroadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mConnectRobotDialog) {
            mConnectRobotDialog.dismiss();
        }
    }
}
