package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtech.iflytekmix.R;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;


/**
 * <pre>
 *   author: Logic
 *   email : 2091843903@qq.com
 *   time  : 2017/3/24
 *   desc  : 照相机
 * </pre>
 */
public class CameraBusiness extends BaseBusiness {

    public CameraBusiness(Context cxt) {
        super(cxt);
    }

    @Override
    public void start(UBTSemanticRootProxy handle) {
        handle.start_TTS(mContext.getString(R.string.camera_ok), false);
//        AddRecord.instance().requestAddRecord(Type.CAMERA.getValue(), mContext.getString(R.string.camera_ok), null, mContext.getString(R.string.camera_ok),
//                getSpeechResult());
        handle.start_Action(mContext.getString(R.string.takephoto_action_name));
//        AddRecord.instance().requestAddRecord(Type.CAMERA.getValue(), null, null, null,
//                getSpeechResult());
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {
    }
}
