package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtech.iflytekmix.R;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.zh_chat.nets.business.AddRecord;
import com.ubtechinc.zh_chat.nets.business.FindJoke;
import com.ubtechinc.zh_chat.nets.module.FindJokeModule;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;

import java.util.List;

/**
 * <pre>
 *   @author: Logic
 *   @email : 2091843903@qq.com
 *   @time  : 2017/3/24
 *   @desc  : 讲笑话
 * </pre>
 */

public class JokeBusiness extends BaseBusiness {
    public JokeBusiness(Context cxt) {
        super(cxt);
    }

    @Override
    public void start(final UBTSemanticRootProxy handle) {
        FindJoke.instance().requestFindJoke("CN", "1", new ResponseListener<FindJokeModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                LogUtils.e(e.getMessage());
                handle.start_TTS(mContext.getString(R.string.network_timeout), false);
            }

            @Override
            public void onSuccess(FindJokeModule.Response response) {
                if (response.success){
                    List<FindJokeModule.Result> results = response.data.result;
                    if (results != null) {
                        FindJokeModule.Result joke = results.get(0);
                        handle.start_TTS(mContext.getString(R.string.robot_ok_tip), false);
                        handle.start_TTS(joke.jokeContext, false);
                        AddRecord.instance().requestAddRecord(Type.JOKE.getValue(), joke.jokeContext, null, mContext.getString(R.string.robot_ok_tip),
                                getSpeechResult());
                    }
                }
            }
        });
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {

    }
}
