package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.VoiceBasicCmdFragment;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.VoiceCmdFragment;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.app.VoiceCmdFragmentAdapter;

import java.util.ArrayList;
import java.util.List;


public class VoiceCmdActivity extends BaseContactActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private VoiceCmdFragment greetFragment;
    private VoiceCmdFragment cmdFragment;
    private VoiceCmdFragment actionFragment;
    private VoiceCmdFragment chatFragment;
    private VoiceCmdFragment performFragment;
    private VoiceBasicCmdFragment mVoiceBasicCmdFragment;
    private VoiceCmdFragmentAdapter mVoiceCmdFragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_cmd);
         initView();
    }

    @Override
    public void onResume() {
        super.onResume();
     }




    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_FindFragment_title);
        mViewPager = (ViewPager) findViewById(R.id.vp_FindFragment_pager);
        title = (TextView) findViewById(R.id.authorize_title);
        title.setText("语音指令");
        greetFragment = VoiceCmdFragment.newInstance(1);
        cmdFragment = VoiceCmdFragment.newInstance(2);
        chatFragment = VoiceCmdFragment.newInstance(3);
        actionFragment = VoiceCmdFragment.newInstance(4);
        performFragment = VoiceCmdFragment.newInstance(5);
        mVoiceBasicCmdFragment = VoiceBasicCmdFragment.newInstance();
        mFragmentList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        mFragmentList.add(greetFragment);
        mFragmentList.add(mVoiceBasicCmdFragment);
        mFragmentList.add(cmdFragment);
        mFragmentList.add(chatFragment);
        mFragmentList.add(actionFragment);
        mFragmentList.add(performFragment);
        mTitleList.add(this.getString(R.string.main_voice_greet_cmd));
        mTitleList.add(this.getString(R.string.main_voice_basic_cmd));
        mTitleList.add(this.getString(R.string.main_voice_cmd));
        mTitleList.add(this.getString(R.string.main_voice_chat_cmd));
        mTitleList.add(this.getString(R.string.main_voice_action_cmd));
        mTitleList.add(this.getString(R.string.main_voice_perform_cmd));

        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(3)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(4)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(5)));

        mVoiceCmdFragmentAdapter = new VoiceCmdFragmentAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(mVoiceCmdFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager.setCurrentItem(1);
    }




}
