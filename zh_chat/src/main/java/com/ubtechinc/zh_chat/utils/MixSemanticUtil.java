package com.ubtechinc.zh_chat.utils;

import android.content.Context;

import com.ubtech.iflytekmix.R;
import com.ubtechinc.alpha.model.speech.SlotValue;
import com.ubtechinc.nlu.iflytekmix.ExcelSemantic;
import com.ubtechinc.nlu.iflytekmix.MixSemantic;
import com.ubtechinc.nlu.iflytekmix.NetSemantic;
import com.ubtechinc.nlu.iflytekmix.OfflineSemantic;
import com.ubtechinc.nlu.iflytekmix.pojos.Data;
import com.ubtechinc.nlu.iflytekmix.pojos.Result;
import com.ubtechinc.nlu.iflytekmix.pojos.Semantic;
import com.ubtechinc.nlu.iflytekmix.pojos.Slots;
import com.ubtechinc.zh_chat.business.BaseBusiness;
import com.ubtechinc.zh_chat.business.CameraBusiness;
import com.ubtechinc.zh_chat.business.ChatBusiness;
import com.ubtechinc.zh_chat.business.FunPlayBusiness;
import com.ubtechinc.zh_chat.business.FunctionBusiness;
import com.ubtechinc.zh_chat.business.JokeBusiness;
import com.ubtechinc.zh_chat.business.LocalRobotActionBusiness;
import com.ubtechinc.zh_chat.business.MusicBusiness;
import com.ubtechinc.zh_chat.business.RemindBusiness;
import com.ubtechinc.zh_chat.business.RobotActionBusiness;
import com.ubtechinc.zh_chat.business.TranslationBusiness;
import com.ubtechinc.zh_chat.business.WeatherBusiness;

import java.util.List;
import java.util.Random;

/**
 * @desc : 工具类，处理mixSemantic的二次语义
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/22
 * @modifier:
 * @modify_time:
 */

public final class MixSemanticUtil {

    public static BaseBusiness resolveBusiness(Context cxt, MixSemantic semantic){
        ChatBusiness business = new ChatBusiness(cxt);
        business.setSemantic(semantic);
        business.setAnswerText(semantic.getAnswerText());
        return business;
    }

    public static BaseBusiness resolveBusiness(Context cxt, OfflineSemantic semantic){
        ChatBusiness mChat = new ChatBusiness(cxt);
        mChat.setSemantic(semantic);
        @SlotValue.Type final int value = semantic.getSlot();
        if (value == SlotValue.NAME) {
            mChat.setAnswerText(mChat.getContext().getString(
                    R.string.QA_robot_name));

        } else if (value == SlotValue.AGE) {
            mChat.setAnswerText(mChat.getContext().getString(
                    R.string.QA_robot_age));

        } else if (value == SlotValue.WHERE) {
            mChat.setAnswerText(mChat.getContext().getString(
                    R.string.QA_robot_where));

        } else if (value == SlotValue.DANCE || value == SlotValue.STORY ||
                value == SlotValue.NOD || value == SlotValue.SHAKE ||
                value == SlotValue.WELCOME || value == SlotValue.RIGHTBECK ||
                value == SlotValue.LEFTBECK || value == SlotValue.STOOP ||
                value == SlotValue.LOOKUP || value == SlotValue.LEFTLEGLIFTS ||
                value == SlotValue.RIGHTLEGLIFTS || value == SlotValue.KONGFU ||
                value == SlotValue.RIGHTKICK || value == SlotValue.LEFTKICK ||
                value == SlotValue.HEADDROP || value == SlotValue.RIGHTHANDSUP ||
                value == SlotValue.LEFTHANDSUP || value == SlotValue.HANDSUP ||
                value == SlotValue.HANDSRAISE || value == SlotValue.ACTINGCUTE ||
                value == SlotValue.WINK || value == SlotValue.SMILE ||
                value == SlotValue.CROUCH || value == SlotValue.STANDUP ||
                value == SlotValue.DENY || value == SlotValue.APPLAUD ||
                value == SlotValue.BACKWALK || value == SlotValue.FRONTWALK ||
                value == SlotValue.FRONTLOOK || value == SlotValue.BACKLOOK ||
                value == SlotValue.RIGHTLOOK || value == SlotValue.LEFTLOOK ||
                value == SlotValue.RIGHTWALK || value == SlotValue.LEFTWALK ||
                value == SlotValue.RIGHTPUNCH || value == SlotValue.LEFTPUNCH ||
                value == SlotValue.RIGHTHEADUP || value == SlotValue.LEFTHEADUP ||
                value == SlotValue.RIGHTTURN || value == SlotValue.LEFTTURN ||
                value == SlotValue.SHAKEHANDS || value == SlotValue.BORING ||
                value == SlotValue.AGREE || value == SlotValue.SAD ||
                value == SlotValue.THINKING || value == SlotValue.HAPPY ||
                value == SlotValue.LAUGH || value == SlotValue.HEARTTIRED ||
                value == SlotValue.HAPPYNEWYEAR || value == SlotValue.HUM ||
                value == SlotValue.GRIEVANCE || value == SlotValue.PERFECT ||
                value == SlotValue.SHARP || value == SlotValue.ROCK || value == SlotValue.TFBOY)
        {
            RobotActionBusiness mAction = new RobotActionBusiness(cxt);
            mAction.setSemantic(semantic);
            mAction.setOfflineGrammar(true);
            mAction.setOperation(SlotValue.VALUES[value]
                    .replace("<","")
                    .replace(">","")
                    .toUpperCase());
            return mAction;
        } else if (value == SlotValue.GREET) {
            mChat = null;
        }else {
            mChat.setAnswerText(mChat.getContext().getString(
                    R.string.QA_robot_empty));

        }
        return mChat;
    }



    public static BaseBusiness resolveBusiness(Context cxt, NetSemantic semantic){
        final String operation = semantic.getOperation();
        final String service = semantic.getService();
        if (service.equals("chat") || service.equals("calc")
                || service.equals("baike")
                || service.equals("datetime")) {// 聊天内容
            ChatBusiness mChat = new ChatBusiness(cxt);
            mChat.setSemantic(semantic);
            mChat.setAnswerText(semantic.getAnswerText());
            return mChat;
        } else if (service.equals("weather")) { // 天气--取时间，地址，详细内容
            return parseWeather(cxt, semantic);
        } else if (service.equals("robotAction")) {// 机器人动作
            return parseRobotAcitonBusiness(cxt, semantic);
        } else if (service.equals("translation")) {// 翻译
            return parseTranslation(cxt, semantic);
        } else if (service.equals("funPlay")) {// 笑话、故事 //2016-03-10
            // 讯飞又修改东西了 只有笑话部分
            return parseFunPlay(cxt, semantic);
        } else if (service.equals("qiubai")) {// 笑话
            return parseQiubai(cxt, semantic);
        } else if (service.equals("music")) {// 音乐
            return parseMusic(cxt, semantic);
        } else if (service.equals("camera")) {// 照相机
            CameraBusiness mCamera = new CameraBusiness(cxt);
            mCamera.setSemantic(semantic);
            mCamera.setOperation(operation);
            return mCamera;
        } else if (service.equals("scheduleX")) {// 日程
            return parseRemind(cxt, semantic);
        } else if ("poetry".equals(service)){//诗
            return parsePoetry(cxt, semantic);
        }else {//websearch 缺少
            return  noAnswerChatBusiness(cxt);
        }
    }

    private static BaseBusiness parsePoetry(Context cxt, NetSemantic semantic) {
        ChatBusiness chat = new ChatBusiness(cxt);
        Data mData = semantic.getData();
        List<Result> mResult = mData != null ? mData.getResult() : null;
        int size = mResult != null ? mResult.size() : 0;
        if (size != 0) {
            Result result = mResult.get(0);
            String text = result.getTitle() + ", " + result.getDynasty() + ", " + result.getAuthor() + ", " + result.getContent();
            chat.setAnswerText(text);
            chat.setSemantic(semantic);
            return chat;
        }
        return null;
    }

    private static WeatherBusiness parseWeather(Context cxt, NetSemantic netSemantic){
        WeatherBusiness mWeather = new WeatherBusiness(cxt);
        mWeather.setSemantic(netSemantic);
        Slots mSlots = netSemantic.getSemantic().getSlots();
        mWeather.setSlots(mSlots);

        String date = mSlots.getDatetime().getDate();
        String city = mSlots.getLocation().getCity();
        if ("CURRENT_CITY".equals(city)) {
            return mWeather;
        }

        Data mData = netSemantic.getData();
        List<Result> result = mData != null ? mData.getResult() : null;
        int index = -1;
        for (int i = 0; i < (result != null ? result.size() : 0); i++) {
            String rsDate = result.get(i).getDate();
            // 判断对应的天气时间,CURRENT_DAY表示今天的时间
            if (rsDate.equals(date) || "CURRENT_DAY".equals(date)) {
                index = i;
                break;
            }
        }
        if (index != -1) {// 查询到对应的天气
            String city1 = result.get(index).getCity();
            String wind = result.get(index).getWind();
            String weather = result.get(index).getWeather();
            String tempRange = result.get(index).getTempRange();
            mWeather.setCity(city1);
            try {
                if ("CURRENT_DAY".equals(date)) {
                    mWeather.setDate2("今天");
                } else {
                    mWeather.setDate2(mSlots.getDatetime().getDateOrig());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mWeather.setTempRange(tempRange);
            mWeather.setWeather(weather);
            mWeather.setWind(wind);
        }
        return mWeather;
    }

    private static RobotActionBusiness parseRobotAcitonBusiness(Context cxt, NetSemantic netSemantic) {
        RobotActionBusiness mRobotAction = new RobotActionBusiness(cxt);
        mRobotAction.setSemantic(netSemantic);
        Slots mSlots = netSemantic.getSemantic().getSlots();
        mRobotAction.setSlots(mSlots);
        mRobotAction.setOperation(netSemantic.getOperation());
        return mRobotAction;
    }

    private static TranslationBusiness parseTranslation(Context cxt, NetSemantic netSemantic) {
        TranslationBusiness mTranslation = new TranslationBusiness(cxt);
        mTranslation.setSemantic(netSemantic);
        Semantic mSemantic = netSemantic.getSemantic();
        Slots mSlots = mSemantic != null ? mSemantic.getSlots() : null;
        mTranslation.setSlots(mSlots);
        return mTranslation;
    }

    private static FunPlayBusiness parseFunPlay(Context cxt, NetSemantic netSemantic) {
        FunPlayBusiness mFunPlay = new FunPlayBusiness(cxt);
        mFunPlay.setSemantic(netSemantic);
        Data mData = netSemantic.getData();
        List<Result> mResult = mData != null ? mData.getResult() : null;
        if ((mResult != null ? mResult.size() : 0) != 0) {
            int index = new Random(System.currentTimeMillis()).nextInt(mResult.size());
            mFunPlay.setName(mResult.get(index).getName());
            mFunPlay.setUrl(mResult.get(index).getUrl());
        }
        return mFunPlay;
    }

    //走公司笑话服务器
    private static JokeBusiness parseQiubai(Context cxt, NetSemantic semantic) {
        JokeBusiness jb =  new JokeBusiness(cxt);
        jb.setSemantic(semantic);
        return jb;
    }

    private static MusicBusiness parseMusic(Context cxt, NetSemantic netSemantic) {
        MusicBusiness mMusic = new MusicBusiness(cxt);
        mMusic.setSemantic(netSemantic);
        Data mData = netSemantic.getData();
        List<Result> mResult = mData != null ? mData.getResult() : null;
        int size = mResult != null ? mResult.size() : 0;
        if (size != 0) {
            Result result = mResult.get(new Random(System.currentTimeMillis()).nextInt(size));
            mMusic.setUrl(result.getDownloadUrl());
            mMusic.setSinger(result.getSinger());
            mMusic.setName(result.getName());
        }
        return mMusic;
    }

    private static BaseBusiness parseRemind(Context cxt, NetSemantic netSemantic) {
        RemindBusiness mRemid = new RemindBusiness(cxt);
        mRemid.setSemantic(netSemantic);
        Semantic mSemantic = netSemantic.getSemantic();
        Slots mSlots = mSemantic != null ? mSemantic.getSlots() : null;
        mRemid.setSlots(mSlots);
        mRemid.setOperation(netSemantic.getOperation());
        // 将text 改为 侧重点内容
        mRemid.setContent(mSlots != null ? mSlots.getContent() : null);
        return mRemid;
    }

    //没有找到结果的随机返回
    public static ChatBusiness noAnswerChatBusiness(Context cxt) {
        ChatBusiness mChat = new ChatBusiness(cxt);
        String[] answer = cxt.getResources().getStringArray(
                R.array.no_answer_text);
        int index =  new Random(System.currentTimeMillis()).nextInt(answer.length);
        mChat.setAnswerText(answer[index]);
        return mChat;
    }

    public static BaseBusiness resolveBusiness(Context cxt, ExcelSemantic excelSemantic){
        switch (excelSemantic.getType()) {
            case ExcelSemantic.TYPE_CHAT:
                return parseChatBusiness(cxt, excelSemantic);
            case ExcelSemantic.TYPE_ROBOT_ACTION:
                return parseRobotAcitonBusiness(cxt, excelSemantic);
            case ExcelSemantic.TYPE_FUNCTION:
                return parseLocalFunctionBusiness(cxt, excelSemantic);
            default:
                return noAnswerChatBusiness(cxt);
        }
    }

    private static ChatBusiness parseChatBusiness(Context cxt, ExcelSemantic semantic)  {
        ChatBusiness mChat = new ChatBusiness(cxt);
        mChat.setSemantic(semantic);
        if (semantic.getAnswers().size() > 0) {
            mChat.setAnswerText(semantic.getAnswers()
                    .get(new Random(System.currentTimeMillis())
                            .nextInt(semantic.getAnswers().size())));
        }else {
            String[] answer = cxt.getResources().getStringArray(
                    R.array.no_answer_text);
            int index =  new Random(System.currentTimeMillis()).nextInt(answer.length);
            mChat.setAnswerText(answer[index]);
        }
        return mChat;
    }

    private static LocalRobotActionBusiness parseRobotAcitonBusiness(Context cxt, ExcelSemantic semantic)  {
        LocalRobotActionBusiness mRobotAction = new LocalRobotActionBusiness(cxt);
        mRobotAction.setSemantic(semantic);
        mRobotAction.setOperation(semantic.getOperation());
        mRobotAction.setSlot(semantic.getSlot());
        mRobotAction.setAnswerList(semantic.getAnswers());
        return mRobotAction;
    }

    private static FunctionBusiness parseLocalFunctionBusiness(Context cxt, ExcelSemantic semantic)  {
        FunctionBusiness mRobotAction = new FunctionBusiness(cxt);
        mRobotAction.setSemantic(semantic);
        mRobotAction.setOperation(semantic.getOperation());
        return mRobotAction;
    }
}
