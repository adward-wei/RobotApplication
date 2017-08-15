package com.ubtechinc.alpha.sdk.led;import android.content.Context;import com.ubtechinc.alpha.sdk.SdkConstants;import com.ubtechinc.alpha.serverlibutil.interfaces.LedListResultListener;import com.ubtechinc.alpha.serverlibutil.interfaces.LedOperationResultListener;import com.ubtechinc.alpha.serverlibutil.service.LedControlServiceUtil;/** * @author logic.peng * @Data 2017/4/17 * @modifier: logic.peng * @modifier-time:2017/7/20 */public class LedRobotApi {    public LedControlServiceUtil ledControlServiceUtil;    private static volatile LedRobotApi mLedRobotApi;    private LedRobotApi() {    }    public static LedRobotApi get() {        if (mLedRobotApi == null) {            synchronized (LedRobotApi.class) {                if (mLedRobotApi == null) {                    mLedRobotApi = new LedRobotApi();                }            }        }        return mLedRobotApi;    }    public synchronized LedRobotApi initializ(Context context) {        if (ledControlServiceUtil == null) {            ledControlServiceUtil = new LedControlServiceUtil(context);        }        return get();    }    public synchronized void destroy() {        ledControlServiceUtil = null;    }    public int getLedList(LedListResultListener listener){        if (ledControlServiceUtil == null || listener == null){            return SdkConstants.ErrorCode.RESULT_FAIL;        }        return ledControlServiceUtil.getLedList(listener);    }    public int turnOnEyeLed(LedColor color, LedOperationResultListener listener){        int nState;        if (ledControlServiceUtil == null || listener == null) {            nState = SdkConstants.ErrorCode.RESULT_FAIL;            return nState;        }        return ledControlServiceUtil.turnOnEye(color, listener);    }    public void turnOffEyeLed(LedOperationResultListener listener){        if (listener == null || ledControlServiceUtil == null)            return;        ledControlServiceUtil.turnOffEye(listener);    }    public int turnOnEyeBlink(LedOperationResultListener listener) {        int nState;        if (ledControlServiceUtil == null || listener == null) {            nState = SdkConstants.ErrorCode.RESULT_FAIL;            return nState;        }        return ledControlServiceUtil.turnOnEyeBlink(listener);    }    public void turnOffEyeBlink(LedOperationResultListener listener) {        if (listener == null || ledControlServiceUtil == null)            return;        ledControlServiceUtil.turnOffEyeBlink(listener);    }    public int turnOnEyeFlash(LedColor color, LedBright bright, LedOperationResultListener listener) {        int nState;        if (ledControlServiceUtil == null || listener == null) {            nState = SdkConstants.ErrorCode.RESULT_FAIL;            return nState;        }        return ledControlServiceUtil.turnOnEyeFlash(color, bright, listener);    }    public void turnOffEyeFlash(LedOperationResultListener listener) {        if (listener == null || ledControlServiceUtil == null)            return;        ledControlServiceUtil.turnOffEyeFlash(listener);    }    public int turnOnEyeMarquee(LedColor color, LedBright bright, LedOperationResultListener listener) {        int nState;        if (ledControlServiceUtil == null || listener == null) {            nState = SdkConstants.ErrorCode.RESULT_FAIL;            return nState;        }        return ledControlServiceUtil.turnOnEyeMarquee(color, bright, listener);    }    public void turnOffEyeMarquee(LedOperationResultListener listener) {        if (listener == null || ledControlServiceUtil == null)            return;        ledControlServiceUtil.turnOffEyeMarquee(listener);    }    public int turnOnHeadLed(LedColor color, LedBright bright, final LedOperationResultListener listener){        int nState;        if (ledControlServiceUtil == null || listener == null) {            nState = SdkConstants.ErrorCode.RESULT_FAIL;            return nState;        }        return ledControlServiceUtil.turnOnHead(color, bright, listener);    }    public void turnOffHeadLed(final LedOperationResultListener listener){        if (listener == null || ledControlServiceUtil == null)            return;        ledControlServiceUtil.turnOffHead(listener);    }    public int turnOnHeadFlash(LedColor color, LedBright bright, LedOperationResultListener listener) {        int nState;        if (ledControlServiceUtil == null || listener == null) {            nState = SdkConstants.ErrorCode.RESULT_FAIL;            return nState;        }        return ledControlServiceUtil.turnOnHeadFlash(color, bright, listener);    }    public void turnOffHeadFlash(LedOperationResultListener listener) {        ledControlServiceUtil.turnOffHeadFlash(listener);    }    public int turnOnHeadMarquee(LedColor color, LedBright bright, LedOperationResultListener listener) {        int nState;        if (ledControlServiceUtil == null || listener == null) {            nState = SdkConstants.ErrorCode.RESULT_FAIL;            return nState;        }        return ledControlServiceUtil.turnOnHeadMarquee(color, bright, listener);    }    public void turnOffHeadMarquee(LedOperationResultListener listener) {        if (listener == null || ledControlServiceUtil == null)            return;        ledControlServiceUtil.turnOffHeadMarquee(listener);    }    public int turnOnHeadBreath(LedColor color, LedBright bright, LedOperationResultListener listener) {        int nState;        if (ledControlServiceUtil == null || listener == null) {            nState = SdkConstants.ErrorCode.RESULT_FAIL;            return nState;        }        return ledControlServiceUtil.turnOnHeadBreath(color, bright, listener);    }    public void turnOffHeadBreath(LedOperationResultListener listener) {        if (listener == null || ledControlServiceUtil == null)            return;        ledControlServiceUtil.turnOffHeadBreath(listener);    }    public int turnOnChestLed(LedOperationResultListener listener){        if (ledControlServiceUtil == null || listener == null) {            return SdkConstants.ErrorCode.RESULT_FAIL;        }        return ledControlServiceUtil.turnOnChestLed(listener);    }    public void turnOffChestLed(LedOperationResultListener listener){        if (ledControlServiceUtil == null || listener == null) {             return;        }        ledControlServiceUtil.turnOffChestLed(listener);    }}