package com.ubtechinc.nets.im.business;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 机器人绑定的业务
 * Created by Liudongyang on 2017/4/18.
 */

public class RobotBindBusiness {
    private Context context;

    public RobotBindBusiness(Context context){
        this.context = context;
    }

    public void doRobotUnBind(){
//        sendHintBroadcastTTS(EmulatedTTSPlayer.ALEXA_DEREGISTERED);
//        LedManagerService.getLedManagerServiceInstance(context).deregisterLedEffect();
//        notifyAlphabindState(UbtMessageConstants.UNBIND);
//        saveRobotControl("");
    }



    public void doRobotBind(String bindUserId){
        saveRobotControl(bindUserId);
    }

    private void sendHintBroadcastTTS(int value){
//        org.json.JSONObject ttsPlayer= new org.json.JSONObject();
//        try {
//            ttsPlayer.put(EmulatedTTSPlayer.JSON_TYPE,EmulatedTTSPlayer.TYPE_ALEXA);
//            ttsPlayer.put(EmulatedTTSPlayer.JSON_VALUE, value);
//        }catch(JSONException E){
//
//        }
//        InternalEvent internalEvent1 = new InternalEvent();
//        internalEvent1.setCommandID(EventBusCmdId.EVENTBUS_INTERNAL_SYSTEM_TTS_PLAYER);
//        internalEvent1.setmObject(ttsPlayer.toString());
//        EventBus.getDefault().post(internalEvent1);
    }


    private void notifyAlphabindState(String unBindmsg){
//        Intent intent = new Intent(StaticValue.ALEXA_ROBOT_BINDSTATE);
//        intent.putExtra(StaticValue.ACTION_ALEXA_BINDSTATE, unBindmsg);
//        context.sendBroadcast(intent);
    }

    private void saveRobotControl(String info){
        SharedPreferences preferences = context.getSharedPreferences("RobotControl",
                Context.MODE_MULTI_PROCESS| Context.MODE_WORLD_WRITEABLE| Context.MODE_WORLD_READABLE);
        preferences.edit().putString("ControlName",info).commit();
    }
}
