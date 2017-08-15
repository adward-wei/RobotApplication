package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotStatusInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.app.AlphaStastusAdapter;
import com.ubtechinc.alpha2ctrlapp.util.DataCleanManager;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.utils.JsonUtil;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CheckAlphaStatusActivity extends BaseContactActivity implements View.OnClickListener, CommonDiaglog.OnPositiveClick, CommonDiaglog.OnNegsitiveClick {

    private String TAG = "CheckAlphaStatusActivity";
    private RefreshListView lst_actions;
    private AlphaStastusAdapter adapter;
    private List<RobotStatusInfo> mRobotStatuslist = new ArrayList<RobotStatusInfo>();
    private boolean isonRefresh = false;
    public static final int  MSG_GET_FROM_LOCAL =-1;
    private boolean readEnd=false;
    private TextView btnEdit;
    private LinearLayout delelteLay; // 底部删除布局
    private CheckBox checkBoxSelect;
    private TextView  tvDelete;  // 底部删除按钮执行删除事件
    public boolean isEdit = false;
    public HashMap<String, Boolean> isSelected = new HashMap<String, Boolean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_alpha_status);
        this.mContext = this;
        initView();
    }

    public void initView() {
        title= (TextView) findViewById(R.id.authorize_title);
        title.setText(getString(R.string.abnormal_nav));
        lst_actions= (RefreshListView)findViewById(R.id.lst_actions);
        btnEdit  =(TextView)findViewById(R.id.btn_top_right);
        btnEdit.setVisibility(View.VISIBLE);
        btnEdit.setOnClickListener(this);
        btnEdit.setText(R.string.common_btn_edit);
        delelteLay = (LinearLayout)findViewById(R.id.ll_delete_lay);
        checkBoxSelect = (CheckBox)findViewById(R.id.choice_check_box);
        tvDelete =(TextView)findViewById(R.id.delelte_tv);
        adapter = new AlphaStastusAdapter(this, mRobotStatuslist);
        lst_actions.setAdapter(adapter);
        lst_actions.setOnItemClickListener(adapter);
        lst_actions.setonRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isonRefresh = true;
                getAlphaStastus(true);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!isEdit){
                    delelteLay.setVisibility(View.VISIBLE);
                    isEdit = true;
                    btnEdit.setText(R.string.common_btn_finish);
                }else{
                    delelteLay.setVisibility(View.GONE);
                    isEdit = false;
                    btnEdit.setText(R.string.common_btn_edit);
                }

            }
        });
        checkBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    for (int i = 0; i < mRobotStatuslist.size(); i++) {
                        isSelected.put(mRobotStatuslist.get(i).getFilePath(), true);
                    }
                }else{
                    for (int i = 0; i < mRobotStatuslist.size(); i++) {
                        isSelected.put(mRobotStatuslist.get(i).getFilePath(), false);
                    }
                }
                adapter.notifyDataSetChanged();
            }

        });
        tvDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                doDelete();
            }
        });
        readLog();
    }

    /**
     * 获取Alpha状态信息
     */
    private void getAlphaStastus(boolean isonRefresh){
        if(!isonRefresh){
            LoadingDialog.getInstance(mContext).show();
        }



//        RobotInitRepository.getInstance().getRobotInitParam(param, new ICallback<CmAlphaCommonParam.CmAlphaCommonParamResponse>() {
//            @Override
//            public void onSuccess(CmAlphaCommonParam.CmAlphaCommonParamResponse data) {
//                AlphaParam param = JsonUtils.jsonToBean(data.getParam(), AlphaParam.class);
//                RobotStatusInfo info = (RobotStatusInfo)msg.obj;
//                info.set
//                if(info!=null)
//                    mRobotStatuslist.add(info);
//                onRefreshData(false);
//            }
//
//            @Override
//            public void onError(int code) {
//
//            }
//
//
//        });

    }
    private void readLog(){

        getAlphaStastus(false);
        getAlphaLog();

    }

    /*
     从本地读取的日志
     */
    private   void  getAlphaLog() {

       final  List<RobotStatusInfo> list = new ArrayList<RobotStatusInfo>();
        String path = Constants.LOG_PAHT+ Alpha2Application.getRobotSerialNo()+"/";
       final  File fileDir = new File(path);
        readEnd =false;
        if (fileDir.exists()) {
            if(fileDir.isDirectory()){
                Thread  thread = new Thread(){
                    @Override
                    public void run (){
                        while(!readEnd){
                            for(File file :fileDir.listFiles()){
                                try {
                                    String res = "";
                                    FileInputStream fin = new FileInputStream(file.getAbsolutePath());
                                    int length = fin.available();
                                    byte[] buffer = new byte[length];
                                    fin.read(buffer);
                                    res = EncodingUtils.getString(buffer, "UTF-8");// //依Y.txt的编码类型选择合适的编码，如果不调整会乱码
                                    fin.close();// 关闭资源
                                    RobotStatusInfo param = JsonUtil.getObject(res, RobotStatusInfo.class);
                                    param.setFilePath(file.getAbsolutePath());
                                    if(param!=null){
                                        param.setTime(file.getName().split(".ubt")[0]);
                                        list.add(param);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            }
                            readEnd =true;
                            mHandler.obtainMessage(MSG_GET_FROM_LOCAL,list).sendToTarget();
                        }
                    }
                };
                thread.start();

            }

        }else{
            mHandler.obtainMessage(MSG_GET_FROM_LOCAL,list).sendToTarget();
        }


    }

    /**
     * 刷新界面数据
     */
    private void onRefreshData(boolean isFromLocal){

        if(isonRefresh){
            isonRefresh =false;
            lst_actions.onRefreshComplete();
        }
        if(!isFromLocal)
             LoadingDialog.dissMiss();
        isSelected.clear();
        for (int i = 0; i < mRobotStatuslist.size(); i++) {
            isSelected.put(mRobotStatuslist.get(i).getFilePath(), false);
            delelteLay.setVisibility(View.GONE);
            isEdit = false;
            btnEdit.setText(R.string.common_btn_edit);
        }
        Collections.sort(mRobotStatuslist);
        adapter.notifyDataSetChanged();
    }
    private void doDelete(){
        for(int i = 0; i< mRobotStatuslist.size(); i++){
            if(isSelected.get(mRobotStatuslist.get(i).getFilePath())){
                if(mRobotStatuslist.get(i).getFilePath()!=null){
                    if( DataCleanManager.deleteGeneralFile(mRobotStatuslist.get(i).getFilePath())){
//                        isSelected.remove(mRobotStatuslist.get(i).getFileSaveFolder());
                        mRobotStatuslist.remove(i);
                        i--;
                    }

                }
            }
        }
        checkBoxSelect.setChecked(false);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void OnNegsitiveClick() {

    }

    @Override
    public void OnPositiveClick() {

    }

    @Override
    public void onClick(View v) {

    }

    BaseHandler mHandler = new BaseHandler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MessageType.ALPHA_LOST_CONNECTED:
                    isCurrentAlpha2MacLostConnection((String) msg.obj);
                    break;

                case MSG_GET_FROM_LOCAL:
                    List<RobotStatusInfo>listObj = (List<RobotStatusInfo>)msg.obj;
                    mRobotStatuslist.addAll(listObj);
                    onRefreshData(true);
                    break;

            }
        }
    };

}
