package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtechinc.zh_chat.nets.business.AddRecord;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;
import com.ubtechinc.zh_chat.ui.GetDataFromWeb;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *   author: Logic
 *   email : 2091843903@qq.com
 *   time  : 2017/3/24
 *   desc  : 翻译
 * </pre>
 */
public class TranslationBusiness extends BaseBusiness {
    private final String strChinesToEnglishTransHttp = "http://117.121.48.3:8887/translate?key=1111&from=zh&to=en&domain=spoken&content=";
    private final String strEnglistToChineseTransHttp = "http://117.121.48.3:8887/translate?key=1111&from=en&to=zh&domain=spoken&content=";
    private String voice;
    private CancelableGetDataFromWeb getDataFromWeb;

    public TranslationBusiness(Context cxt) {
        super(cxt);
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    @Override
    public String toString() {
        return getSlots().getTarget() + " " + getSlots().getSource() + " "
                + getSlots().getContent();
    }

    @Override
    public String getUrl() {
        StringBuilder strBuilder = new StringBuilder();

        if (getSlots().getTarget().equals("zh")) {
            strBuilder.append(strEnglistToChineseTransHttp);
            this.setVoice("xiaoyan");
        } else if (getSlots().getTarget().equals("en")) {
            strBuilder.append(strChinesToEnglishTransHttp);
            this.setVoice("catherine");
        }
        strBuilder
                .append(android.net.Uri.encode(getSlots().getContent(), "US-ASCII"));

        return strBuilder.toString();
    }

    @Override
    public void start(final UBTSemanticRootProxy handle) {
        if (getDataFromWeb != null){
            getDataFromWeb.isCancel = true;
        }
        getDataFromWeb = new CancelableGetDataFromWeb(handle);
        GetDataFromWeb.getJsonByGet(getUrl(), getDataFromWeb);
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {
        getDataFromWeb.isCancel = true;
    }

    private  class CancelableGetDataFromWeb  implements GetDataFromWeb.IJsonListener {
        private final WeakReference<UBTSemanticRootProxy> proxy;
        volatile  boolean isCancel;
        CancelableGetDataFromWeb(UBTSemanticRootProxy proxy){
            isCancel = false;
            this.proxy = new WeakReference<UBTSemanticRootProxy>(proxy);
        }

        @Override
        public void onGetJson(boolean isSuccess, String json, long request_code) {
            if (!isCancel) {
                UBTSemanticRootProxy proxy = this.proxy.get();
                if (proxy != null) {
                    String strTts = parseString(json);
                    proxy.start_TTS(strTts, false);
                    AddRecord.instance().requestAddRecord(Type.TRANSLATION.getValue(), strTts, getUrl(), null, getSpeechResult());
                }
            }
        }
    }

    private static String parseString(String json) {
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            return joResult.getString("translation");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
