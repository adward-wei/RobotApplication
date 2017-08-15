package com.ubtechinc.alpha.utils;

import com.ubtechinc.alpha.entity.TranslationBean;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class TranslationUtil {
    private final String strChinesToEnglishTransHttp = "http://117.121.48.3:8887/translate?key=1111&from=zh&to=en&domain=spoken&content=";
    private final String strEnglistToChineseTransHttp = "http://117.121.48.3:8887/translate?key=1111&from=en&to=zh&domain=spoken&content=";

    public static void performTranslate(TranslationBean mTranslationBean){
        if(mTranslationBean.needTranslate.equals("0")){
            String language;
            String VoiceName;
            if (mTranslationBean.language.equals("en")) {
//                language = LanguageType.LAU_ENGLISH;
//                VoiceName = TTSVoiceName.UNITED_STATES_ENGLISH_SAMANTHA;
            } else {
//                language = LanguageType.LAU_CHINESE;
//                VoiceName =  "nannan";

            }
//            mAlpha2SpeechMainServiceUtil2.speechStartTTS(mTranslationBean.content, VoiceName, language, true);
//            if(mTranslationBean.actionId!=null){
//                playAction(mTranslationBean.actionId);
//            }

            SpeechServiceProxy.getInstance().speechStartTTS(mTranslationBean.getContent(),null);
            return ;
        }
        StringBuilder strBuilder = new StringBuilder();

        if (mTranslationBean.language.equals("en")) {
//            strBuilder.append(strChinesToEnglishTransHttp);
//            strBuilder.append(java.net.URLEncoder.encode(mTranslationBean.content));
        } else {
//            strBuilder.append(strEnglistToChineseTransHttp);
//            if (mTranslationBean.content.length() < 3 || mTranslationBean.content.equals("Yeah.")) {
//                return;
//            }
            strBuilder.append(android.net.Uri.encode(mTranslationBean.content, "US-ASCII"));

        }
        if(strBuilder.toString().contains("NLU_Result")) {
            return ;
        }

//        GetDataFromWeb.getJsonByGet(strBuilder.toString(), new GetDataFromWeb.IJsonListener() {
//            @Override
//            public void onGetJson(boolean isSuccess, String json,
//                                  long request_code) {
//                String strTts = JsonParser.parseString(json);
//                if(mTranslationBean.actionId!=null){
//                    playAction(mTranslationBean.actionId);
//                }
//                String language;
//                String VoiceName;
//
//                if (mTranslationBean.language.equals("en")) {
//                    language = LanguageType.LAU_ENGLISH;
//                    VoiceName = TTSVoiceName.UNITED_STATES_ENGLISH_SAMANTHA;
//
//                } else {
//                    language = LanguageType.LAU_CHINESE;
//                    VoiceName = "nannan";
//                }
//                mAlpha2SpeechMainServiceUtil2.speechStartTTS(strTts, VoiceName, language, true);
//            }
//
//        });
    }

}
