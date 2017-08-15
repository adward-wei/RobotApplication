package com.ubtechinc.alpha2ctrlapp.ui.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.BasicCmdModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * <p>
 * interface.
 */
public class VoiceBasicCmdFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int type;
    private ListView mListView;
    private ListView mConnectListView;
    private View mView;
    private VoiceBasicAdapter mVoiceAdapter;
    private List<BasicCmdModel> mList = new ArrayList<>();
    private List<String> mConnectList = new ArrayList<>();
    private VoiceAdapter mConnectAdapter;
    private LinearLayout llConnect;
    private View HeadView;

    public VoiceBasicCmdFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static VoiceBasicCmdFragment newInstance() {
        VoiceBasicCmdFragment fragment = new VoiceBasicCmdFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_voice_cmditem_list, container, false);
        initView();
        return mView;
    }

    private void initView() {
        getData();
        HeadView = LayoutInflater.from(getContext()).inflate(R.layout.head_voice_cmd_layout, null);
        llConnect = (LinearLayout) mView.findViewById(R.id.ll_connect_view);
        mListView = (ListView) mView.findViewById(R.id.cmd_list_view);
        mVoiceAdapter = new VoiceBasicAdapter(getContext(), mList);
        mListView.setAdapter(mVoiceAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getData() {
        mList.clear();
        mList = getBasicCmdData();

    }
    public  List<BasicCmdModel> getBasicCmdData() {
        List<BasicCmdModel> basicCmdModels = new ArrayList<>();
        BasicCmdModel basicCmdModel1 = new BasicCmdModel("你好阿尔法", "将机器人从休眠状态唤醒", R.drawable.ic_huanxing);
        basicCmdModels.add(basicCmdModel1);

        BasicCmdModel basicCmdModel2 = new BasicCmdModel("你好阿尔法", "停止机器人当前动作或语音播报", R.drawable.ic_daduan);
        basicCmdModels.add(basicCmdModel2);

        BasicCmdModel basicCmdModel3 = new BasicCmdModel("去休息吧", "让机器人处于待机状态，需唤醒使用", R.drawable.ic_xiumian);
        basicCmdModels.add(basicCmdModel3);

        BasicCmdModel basicCmdModel4 = new BasicCmdModel("你关机吧", "关闭机器人并切断电源，需手动开机", R.drawable.ic_guanji);
        basicCmdModels.add(basicCmdModel4);
        return basicCmdModels;
    }

}
