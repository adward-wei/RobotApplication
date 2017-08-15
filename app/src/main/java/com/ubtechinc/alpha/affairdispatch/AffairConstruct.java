package com.ubtechinc.alpha.affairdispatch;

import com.ubtechinc.alpha.affairdispatch.constants.AffairState;
import com.ubtechinc.alpha.affairdispatch.constants.AffairType;

/**
 * @desc : 构造事务
 * @author: wzt
 * @time : 2017/5/18
 * @modifier: logic.peng 增加回调方式,bob.xu:把tts事件和action事件分开，action与tts可以同时执行
 * @modify_time:
 */

public class AffairConstruct {

    public static void processAffair(BaseAffair affair) {
        if (affair != null) {
            BaseAffairManager eventManager = null;
            if (affair.getType() == AffairType.EVENT_TYPE_TTS) {
                eventManager = TtsAffairManager.getInstance();
            } else if (affair.getType() == AffairType.EVENT_TYPE_ACTION) {
                eventManager = ActionAffairManager.getInstance();
            } else {
                throw new RuntimeException("当前不支持其他类型");
            }
            affair.setState(AffairState.STATE_PREPARE);
            affair.setNeedCheck(true);
            eventManager.processAffair(affair);
        }
    }
}
