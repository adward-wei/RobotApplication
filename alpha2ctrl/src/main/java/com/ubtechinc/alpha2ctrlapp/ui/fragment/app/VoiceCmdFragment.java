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

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * <p>
 * interface.
 */
public class VoiceCmdFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int type;
    private ListView mListView;
    private ListView mConnectListView;
    private View mView;
    private VoiceAdapter mVoiceAdapter;
    private List<String> mList = new ArrayList<>();
    private List<String> mConnectList = new ArrayList<>();
    private VoiceAdapter mConnectAdapter;
    private LinearLayout llConnect;
    private View HeadView;

    public VoiceCmdFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static VoiceCmdFragment newInstance(int type) {
        VoiceCmdFragment fragment = new VoiceCmdFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt(ARG_COLUMN_COUNT);
            getData(type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_voice_cmditem_list, container, false);
        initView();
        return mView;
    }

    private void initView() {
        HeadView = LayoutInflater.from(getContext()).inflate(R.layout.head_voice_cmd_layout, null);
        llConnect = (LinearLayout) mView.findViewById(R.id.ll_connect_view);
        mListView = (ListView) mView.findViewById(R.id.cmd_list_view);
        mVoiceAdapter = new VoiceAdapter(getContext(), mList);
        mListView.setAdapter(mVoiceAdapter);
        if (type == 2) {
            mListView.addHeaderView(HeadView);
//            llConnect.setVisibility(View.GONE);
//            mConnectListView = (ListView) mView.findViewById(R.id.cmd_list_connect_view);
//            mConnectAdapter = new VoiceAdapter(getContext(), mConnectList);
//            mConnectListView.setAdapter(mConnectAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getData(int type) {
        mList.clear();
        String[] array = getCmdList(getContext(), type);
        for (int i = 0; i < array.length; i++) {
            mList.add(array[i]);
        }
    }

    /**
     * 获取命令
     *
     * @param context
     * @param type
     * @return
     */
    public  String[] getCmdList(Context context, int type) {
        //   List<String> list = new ArrayList<>();
        String[] array = null;
        switch (type) {
            case 1:
                array = context.getResources().getStringArray(R.array.voice_cmd_greet);
//                list.add(context.getString(R.string.main_voice_cmd_greet_one));
//                list.add(context.getString(R.string.main_voice_cmd_greet_two));
//                list.add(context.getString(R.string.main_voice_cmd_greet_three));
//                list.add(context.getString(R.string.main_voice_cmd_greet_four));
//                list.add(context.getString(R.string.main_voice_cmd_greet_five));
//                list.add(context.getString(R.string.main_voice_cmd_greet_six));
//                list.add(context.getString(R.string.main_voice_cmd_greet_seven));
//                list.add(context.getString(R.string.main_voice_cmd_greet_eight));
//                list.add(context.getString(R.string.main_voice_cmd_greet_nine));
//                list.add(context.getString(R.string.main_voice_cmd_greet_ten));
//                list.add(context.getString(R.string.main_voice_cmd_greet_eleven));
//                list.add(context.getString(R.string.main_voice_cmd_greet_twelve));
                break;

            case 2:
                array = context.getResources().getStringArray(R.array.voice_cmd_cmd);
//                list.add(context.getString(R.string.main_voice_cmd_four));
//                list.add(context.getString(R.string.main_voice_cmd_five));
//                list.add(context.getString(R.string.main_voice_cmd_six));
//                list.add(context.getString(R.string.main_voice_cmd_eight));
//                list.add(context.getString(R.string.main_voice_cmd_nine));
//                list.add(context.getString(R.string.main_voice_cmd_ten));
//                list.add(context.getString(R.string.main_voice_cmd_eleven));
//                list.add(context.getString(R.string.main_voice_cmd_twelve));

                break;
            case 3:
                array = context.getResources().getStringArray(R.array.voice_cmd_chat);
//                list.add(context.getString(R.string.main_voice_cmd_chat_one));
//                list.add(context.getString(R.string.main_voice_cmd_chat_two));
//                list.add(context.getString(R.string.main_voice_cmd_chat_three));
//                list.add(context.getString(R.string.main_voice_cmd_chat_four));
//                list.add(context.getString(R.string.main_voice_cmd_chat_five));
//                list.add(context.getString(R.string.main_voice_cmd_chat_six));
//                list.add(context.getString(R.string.main_voice_cmd_chat_seven));
//                list.add(context.getString(R.string.main_voice_cmd_chat_eight));
//                list.add(context.getString(R.string.main_voice_cmd_chat_nine));
//                list.add(context.getString(R.string.main_voice_cmd_chat_ten));
//                list.add(context.getString(R.string.main_voice_cmd_chat_eleven));
//                list.add(context.getString(R.string.main_voice_cmd_chat_twelve));
//
//                list.add(context.getString(R.string.main_voice_cmd_chat_thirteen));
//                list.add(context.getString(R.string.main_voice_cmd_chat_fourteen));
//                list.add(context.getString(R.string.main_voice_cmd_chat_fiveteen));
//                list.add(context.getString(R.string.main_voice_cmd_chat_sixteen));
//                list.add(context.getString(R.string.main_voice_cmd_chat_seventeen));
//                list.add(context.getString(R.string.main_voice_cmd_chat_eightteen));
//                list.add(context.getString(R.string.main_voice_cmd_chat_nineteen));
//                list.add(context.getString(R.string.main_voice_cmd_chat_twenty));
//                list.add(context.getString(R.string.main_voice_cmd_chat_twenty_one));
//                list.add(context.getString(R.string.main_voice_cmd_chat_twenty_two));
//                list.add(context.getString(R.string.main_voice_cmd_chat_twenty_three));
//                list.add(context.getString(R.string.main_voice_cmd_chat_twenty_four));
//
//                list.add(context.getString(R.string.main_voice_cmd_chat_twenty_five));
//                list.add(context.getString(R.string.main_voice_cmd_chat_twenty_six));
//                list.add(context.getString(R.string.main_voice_cmd_chat_twenty_seven));
//                list.add(context.getString(R.string.main_voice_cmd_chat_twenty_eight));
//                list.add(context.getString(R.string.main_voice_cmd_chat_twenty_nine));
                break;

            case 4:
                array = context.getResources().getStringArray(R.array.voice_cmd_action);
//                list.add(context.getString(R.string.main_voice_cmd_action_one));
//                list.add(context.getString(R.string.main_voice_cmd_action_two));
//                list.add(context.getString(R.string.main_voice_cmd_action_three));
//                list.add(context.getString(R.string.main_voice_cmd_action_four));
//                list.add(context.getString(R.string.main_voice_cmd_action_five));
//                list.add(context.getString(R.string.main_voice_cmd_action_six));
//                list.add(context.getString(R.string.main_voice_cmd_action_seven));
//                list.add(context.getString(R.string.main_voice_cmd_action_eight));
//                list.add(context.getString(R.string.main_voice_cmd_action_nine));
//                list.add(context.getString(R.string.main_voice_cmd_action_ten));
//                list.add(context.getString(R.string.main_voice_cmd_action_eleven));
//                list.add(context.getString(R.string.main_voice_cmd_action_twelve));
//
//                list.add(context.getString(R.string.main_voice_cmd_action_thirteen));
//                list.add(context.getString(R.string.main_voice_cmd_action_fourteen));
//                list.add(context.getString(R.string.main_voice_cmd_action_fiveteen));
//                list.add(context.getString(R.string.main_voice_cmd_action_sixteen));
//                list.add(context.getString(R.string.main_voice_cmd_action_seventeen));
//                list.add(context.getString(R.string.main_voice_cmd_action_eightteen));
//                list.add(context.getString(R.string.main_voice_cmd_action_nineteen));
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty));
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty_one));
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty_two));
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty_three));
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty_four));
//
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty_five));
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty_six));
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty_seven));
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty_eight));
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty_nine));
//
//                list.add(context.getString(R.string.main_voice_cmd_action_twenty_ten));
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty));
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty_one));
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty_two));
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty_three));
//
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty_four));
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty_five));
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty_six));
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty_seven));
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty_eight));
//
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty_nine));
//                list.add(context.getString(R.string.main_voice_cmd_action_thirty_ten));
//                list.add(context.getString(R.string.main_voice_cmd_action_forty_one));
//                list.add(context.getString(R.string.main_voice_cmd_action_forty_two));
//                list.add(context.getString(R.string.main_voice_cmd_action_forty_three));

                break;

            case 5:
                array = context.getResources().getStringArray(R.array.voice_cmd_perform);
//                list.add(context.getString(R.string.main_voice_cmd_perform_one));
//                list.add(context.getString(R.string.main_voice_cmd_perform_two));
//                list.add(context.getString(R.string.main_voice_cmd_perform_three));
//                list.add(context.getString(R.string.main_voice_cmd_perform_four));
//                list.add(context.getString(R.string.main_voice_cmd_perform_five));
//                list.add(context.getString(R.string.main_voice_cmd_perform_six));
//                list.add(context.getString(R.string.main_voice_cmd_perform_seven));
//                list.add(context.getString(R.string.main_voice_cmd_perform_eight));
//                list.add(context.getString(R.string.main_voice_cmd_perform_nine));

                break;

            case 6:
                array = context.getResources().getStringArray(R.array.voice_cmd_cmd);
//                list.add(context.getString(R.string.main_voice_cmd_four));
//                list.add(context.getString(R.string.main_voice_cmd_five));
//                list.add(context.getString(R.string.main_voice_cmd_six));
//                list.add(context.getString(R.string.main_voice_cmd_eight));
//                list.add(context.getString(R.string.main_voice_cmd_nine));
//                list.add(context.getString(R.string.main_voice_cmd_ten));
//                list.add(context.getString(R.string.main_voice_cmd_eleven));
//                list.add(context.getString(R.string.main_voice_cmd_twelve));
                break;
        }

        return array;
    }
}
