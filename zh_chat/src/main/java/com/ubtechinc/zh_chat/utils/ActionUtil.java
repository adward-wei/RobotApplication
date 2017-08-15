package com.ubtechinc.zh_chat.utils;

import android.text.TextUtils;

import com.ubtechinc.alpha.serverlibutil.aidl.ActionInfo;
import com.ubtechinc.nlu.iflytekmix.pojos.Slots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import timber.log.Timber;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/1
 * @modifier:
 * @modify_time:
 */

public final class ActionUtil {

    public static String getOfflineActionName(String operation) {
        String cmd = "";
        switch (operation) {
            case "NOD":
                cmd = "点头";
                break;
            case "SHAKE":
                cmd = "摇头";
                break;
            case "WELCOME":
                cmd = "欢迎";
                break;
            case "RIGHTBECK":
                cmd = "挥右手";
                break;
            case "LEFTBECK":
                cmd = "挥左手";
                break;
            case "STOOP":
                cmd = "弯腰";
                break;
            case "LOOKUP":
                cmd = "抬头";
                break;
            case "LEFTLEGLIFTS":
                cmd = "左抬腿";
                break;
            case "RIGHTLEGLIFTS":
                cmd = "右抬腿";
                break;
            case "KONGFU":
                cmd = "打功夫";
                break;
            case "RIGHTKICK":
                cmd = "右踢腿";
                break;
            case "LEFTKICK":
                cmd = "左踢腿";
                break;

            case "HEADDROP":
                cmd = "低头";
                break;
            case "RIGHTHANDSUP":
                cmd = "举右手";
                break;
            case "LEFTHANDSUP":
                cmd = "举左手";
                break;
            case "HANDSUP":
                cmd = "举双手";
                break;
            case "HANDSRAISE"://无
                cmd = "抬手";
                break;
            case "ACTINGCUTE":
                cmd = "卖萌";
                break;
            case "WINK":
                cmd = "眨眼";
                break;
            case "SMILE":
                cmd = "笑";
                break;
            case "CROUCH":
                cmd = "蹲下";
                break;
            case "STANDUP":
                cmd = "蹲下站起";
                break;
            case "DENY":
                cmd = "否定";
                break;
            case "APPLAUD":
                cmd = "鼓掌";
                break;
            case "BACKWALK":
                cmd = "后退";
                break;
            case "FRONTWALK":
                cmd = "前进";
                break;
            case "FRONTLOOK":
                cmd = "头转正";
                break;
            case "BACKLOOK":
                cmd = "向后走";
                break;
            case "RIGHTLOOK":
                cmd = "向右转头";
                break;
            case "LEFTLOOK":
                cmd = "向左转头";
                break;
            case "RIGHTWALK":
                cmd = "向右走";
                break;
            case "LEFTWALK":
                cmd = "右击拳";
                break;
            case "RIGHTPUNCH":
                cmd = "左击拳";
                break;
            case "LEFTPUNCH":
                cmd = "握手";
                break;
            case "RIGHTHEADUP":
                cmd = "右抬头";
                break;
            case "LEFTHEADUP":
                cmd = "左抬头";
                break;
            case "RIGHTTURN":
                cmd = "右移";
                break;
            case "LEFTTURN":
                cmd = "左移";
                break;
            case "SHAKEHANDS":
                cmd = "握手";
                break;
            case "BORING":
                cmd = "无聊";
                break;
            case "AGREE":
                cmd = "赞同";
                break;
            case "SAD":
                cmd = "伤心";
                break;
            case "THINKING":
                cmd = "思考";
                break;
            case "HAPPY":
                cmd = "开心";
                break;
            case "LAUGH":
                cmd = "大笑";
                break;
            case "HEARTTIRED":
                cmd = "感觉身体被掏空";
                break;
            case "HAPPYNEWYEAR":
                cmd = "恭喜发财红包拿来";
                break;
            case "GRIEVANCE":
                cmd = "好委屈";
                break;
            case "PERFECT":
                cmd = "完美";
                break;
            case "SHARP":
                cmd = "你这么厉害咋不上天呢";
                break;
            case "ROCK":
                cmd = "一起摇摆";
                break;
            case "TFBOY":
                cmd = "左手右手慢动作";
                break;
            case "HUM":
                cmd = "哼";
                break;
            case "DANCE":
                cmd = getDanceName();
                break;
            case "STORY":
                cmd = getStoryName();
                break;
        }
        return  cmd;
    }

    public static String getNetActionName(String operation, Slots slots) {
        String cmd = "";
        if (slots != null) {
            if (operation.equals("WALK")) {
                if (slots.getDirection().equals("front")) {
                    cmd = "前进";
                } else if (slots.getDirection().equals("++")) {
                    cmd = "后退";
                } else if (slots.getDirection().equals("left")) {
                    cmd = "左移";
                } else if (slots.getDirection().equals("right")) {
                    cmd = "右移";
                }
            } else if (operation.equals("LOOK")) {
                String direction = slots.getDirection();
                if (direction != null) {
                    if (direction.equals("left")) {
                        cmd = "向左转头";
                    } else if (direction.equals("right")) {
                        cmd = "向右转头";
                    } else if (direction.equals("front")) {
                        cmd = "头转正";
                    }
                }
                // cmd = operation + slots.getTarget();
            } else if (operation.equals("TURN")) {
                // operation = operation + slots.getDirection();
                if (slots.getDirection().equals("left")) {
                    cmd = "左转";
                } else if (slots.getDirection().equals("right")) {
                    cmd = "右转";
                }
            } else if (operation.equals("HEADUP")) {
                String direction = slots.getDirection();
                if (direction != null) {
                    if (direction.equals("left")) {
                        cmd = "左抬头";
                    } else if (direction.equals("right")) {
                        cmd = "右抬头";
                    }
                }
            } else if (operation.equals("BECK")) {
                String left = slots.getLeft();
                String right = slots.getRight();
                if (left != null && !left.equals("")) {
                    cmd = "挥左手";
                } else if (right != null && !right.equals("")) {
                    cmd = "挥右手";
                }
            } else if (operation.equals("HANDSRAISE")) {
                String left = slots.getLeft();
                String right = slots.getRight();
                if (left != null && !left.equals("")) {
                    cmd = "抬左手";
                } else if (right != null && !right.equals("")) {
                    cmd = "抬右手";
                } else {
                    cmd = "抬双手";
                }
            } else if (operation.equals("HANDSUP")) {
                String left = slots.getLeft();
                String right = slots.getRight();
                if (left != null && !left.equals("")) {
                    cmd = "举左手";
                } else if (right != null && !right.equals("")) {
                    cmd = "举右手";
                } else {
                    cmd = "举双手";
                }
            }

            else if (operation.equals("PUNCH")) {
                String direction = slots.getDirection();
                if (direction != null) {
                    if (direction.equals("left")) {
                        cmd = "左击拳";
                    } else if (direction.equals("right")) {
                        cmd = "右击拳";
                    }
                }
            }else if (operation.equals("LEGLIFTS")) {
                if(slots.getLeft() != null){
                    cmd = "左抬腿";
                }else if(slots.getRight() != null){
                    cmd = "右抬腿";
                }else{
                    cmd = "抬腿";//动作表无该动作
                }
            }else if (operation.equals("EXPRESS")) {
                if(slots.getMode().equals("thinking")){
                    cmd = "思考";
                }else if(slots.getMode().equals("boring")){
                    cmd = "无聊";
                }else if(slots.getMode().equals("wink")){
                    cmd = "眨眼";
                }else if(slots.getMode().equals("smile")){
                    cmd = "笑";
                }else if(slots.getMode().equals("deny")){
                    cmd = "否定";
                }else if(slots.getMode().equals("sad")){
                    cmd = "伤心";
                }else if(slots.getMode().equals("happy")){//该动作未测试
                    cmd = "开心";
                }else if(slots.getMode().equals("agree")){//该动作未测试
                    cmd = "赞同";
                }
                else{
                    cmd = "卖萌";
                }
            }
        } else {
            if (operation.equals("NOD")) {
                cmd = "点头";
            } else if (operation.equals("REST")) {
                cmd = "休息";
            } else if (operation.equals("CIRCLE")) {
                cmd = "转圈";
            } else if (operation.equals("SHAKE")) {
                cmd = "摇头";
            } else if (operation.equals("INTRODUCE")) {
                cmd = "自我介绍";
            } else if (operation.equals("STANDUP")) {
                cmd = "蹲下站起";
            } else if (operation.equals("WELCOME")) {
                cmd = "欢迎";
            } else if (operation.equals("WAVE")) {
                cmd = "招手";
            } else if (operation.equals("STOP")) {
                cmd = "停止";
            } else if (operation.equals("STOOP")) {
                cmd = "弯腰";
            } else if (operation.equals("LOOKUP")) {
                cmd = "抬头";
            } else if (operation.equals("KICK")) {
                cmd = "踢腿";
            } else if (operation.equals("HEADDROP")) {
                cmd = "低头";
            } else if (operation.equals("HANDSUP")) {
                cmd = "举手";
            } else if (operation.equals("HANDSRAISE")) {
                cmd = "抬手";
            } else if (operation.equals("CROUCH")) {
                cmd = "蹲下";
            } else if (operation.equals("FOOTBALL")) {
                cmd = "踢足球";
            } else if (operation.equals("DANCE")) {
                cmd = getDanceName();
            } else if (operation.equals("CRAWLUP")) {
                cmd = "坐下爬起";
            } else if (operation.equals("SITDOWN")) {
                cmd = "坐下";
            }  else if (operation.equals("KUNFU")) {
                cmd = "打功夫";
            }else if(operation.equals("APPLAUD")){
                cmd = "鼓掌";
            }else if(operation.equals("SHAKEHANDS")){
                cmd = "握手";
            }else {
                cmd = operation;
            }
        }
        return cmd;
    }

    public static String getLocalActionName(String operation, String slot) {
        String cmd = "";
        if (operation.equals("WALK")) {
            if(TextUtils.isEmpty(slot)){
                cmd = "前进";
            }else if (slot.equals("front")) {
                cmd = "前进";
            } else if (slot.equals("++")) {
                cmd = "后退";
            } else if (slot.equals("left")) {
                cmd = "左移";
            } else if (slot.equals("right")) {
                cmd = "右移";
            }
        } else if (operation.equals("LOOK")) {
            if ("left".equals(slot)) {
                cmd = "向左转头";
            } else if ("right".equals(slot)) {
                cmd = "向右转头";
            } else if ("front".equals(slot)) {
                cmd = "头转正";
            }
        } else if (operation.equals("TURN")) {
            if ("left".equals(slot)) {
                cmd = "左转";
            } else if ("right".equals(slot)) {
                cmd = "右转";
            }
        } else if (operation.equals("HEADUP")) {
            if ("left".equals(slot)) {
                cmd = "左抬头";
            } else if ("right".equals(slot)) {
                cmd = "右抬头";
            }
        } else if (operation.equals("BECK")) {
            if ("LEFT".equals(slot)) {
                cmd = "挥左手";
            } else if ("RIGHT".equals(slot)) {
                cmd = "挥右手";
            }
        } else if (operation.equals("HANDSRAISE")) {
            if ("LEFT".equals(slot)) {
                cmd = "抬左手";
            } else if ("RIGHT".equals(slot)) {
                cmd = "抬右手";
            } else {
                cmd = "抬双手";
            }

        } else if (operation.equals("HANDSUP")) {
            if ("LEFT".equals(slot)) {
                cmd = "举左手";
            } else if ("RIGHT".equals(slot)) {
                cmd = "举右手";
            } else {
                cmd = "举双手";
            }
        } else if (operation.equals("PUNCH")) {
            if("left".equals(slot)) {
                cmd = "左击拳";
            }else {
                cmd = "右击拳";
            }
        }else if (operation.equals("LEGLIFTS")) {
            if ("LEFT".equals(slot)) {
                cmd = "左抬腿";
            } else if ("RIGHT".equals(slot)) {
                cmd = "右抬腿";
            } else {
                cmd = "抬腿";
            }
        }else if (operation.equals("EXPRESS")) {
            if (TextUtils.isEmpty(slot)){
                cmd = "卖萌";
            } else if(slot.equals("thinking")){
                cmd = "思考";
            }else if(slot.equals("boring")){
                cmd = "无聊";
            }else if(slot.equals("wink")){
                cmd = "眨眼";
            }else if(slot.equals("smile")){
                cmd = "笑";
            }else if(slot.equals("deny")){
                cmd = "否定";
            }else if(slot.equals("sad")){
                cmd = "伤心";
            }else if(slot.equals("happy")){//该动作未测试
                cmd = "开心";
            }else if(slot.equals("agree")){//该动作未测试
                cmd = "赞同";
            }
        }else if (operation.equals("KICK")) {
            if ("LEFT".equals(slot)) {
                cmd = "左踢腿";
            }else if ("RIGHT".equals(slot)) {
                cmd = "右踢腿";
            }else {
                cmd = "踢腿";
            }
        }else if (operation.equals("NOD")) {
            cmd = "点头";
        } else if (operation.equals("REST")) {
            cmd = "休息";
        } else if (operation.equals("CIRCLE")) {
            cmd = "转圈";
        } else if (operation.equals("SHAKE")) {
            cmd = "摇头";
        } else if (operation.equals("INTRODUCE")) {
            cmd = "自我介绍";
        } else if (operation.equals("STANDUP")) {
            cmd = "蹲下站起";
        } else if (operation.equals("WELCOME")) {
            cmd = "欢迎";
        } else if (operation.equals("WAVE")) {
            cmd = "招手";
        } else if (operation.equals("STOP")) {
            cmd = "停止";
        } else if (operation.equals("STOOP")) {
            cmd = "弯腰";
        } else if (operation.equals("LOOKUP")) {
            cmd = "抬头";
        }  else if (operation.equals("HEADDROP")) {
            cmd = "低头";
        } else if (operation.equals("HANDSUP")) {
            cmd = "举手";
        } else if (operation.equals("HANDSRAISE")) {
            cmd = "抬手";
        } else if (operation.equals("CROUCH")) {
            cmd = "蹲下";
        } else if (operation.equals("FOOTBALL")) {
            cmd = "踢足球";
        } else if (operation.equals("DANCE")) {
            cmd = getDanceName();
        } else if (operation.equals("CRAWLUP")) {
            cmd = "坐下爬起";
        } else if (operation.equals("SITDOWN")) {
            cmd = "坐下";
        }  else if (operation.equals("KUNFU")) {
            cmd = "打功夫";
        }else if(operation.equals("APPLAUD")){
            cmd = "鼓掌";
        }else if(operation.equals("SHAKEHANDS")){
            cmd = "握手";
        }else if(operation.equals("LAUGH")){
            cmd = "大笑";
        }else if(operation.equals("HEARTTIRED")){
            cmd = "感觉身体被掏空";
        }else if(operation.equals("HAPPYNEWYEAR")){
            cmd = "恭喜发财红包拿来";
        }else if(operation.equals("GRIEVANCE")){
            cmd = "好委屈";
        }else if(operation.equals("HUM")){
            cmd = "哼";
        }else if(operation.equals("SHARP")){
            cmd = "你这么厉害咋不上天呢";
        }else if(operation.equals("PERFECT")){
            cmd = "完美";
        }else if(operation.equals("ROCK")){
            cmd = "一起摇摆";
        }else if(operation.equals("TFBOY")){
            cmd = "左手右手慢动作";
        }else if (operation.equals("STORY")) {
            cmd = getStoryName();
        }
        else {
            cmd = operation;
        }
        return cmd;
    }

    public static String getDanceName(){
        String name = "";
        if(mAlphaDanceList.size() >0){
            name =  mAlphaDanceList.get(new Random(System.currentTimeMillis()).nextInt(mAlphaDanceList.size()));
        }
        return name;
    }

    public static String getStoryName(){
        String name = "";
        if(mAlphaStoryList.size() >0){
            name =  mAlphaStoryList.get(new Random(System.currentTimeMillis()).nextInt(mAlphaStoryList.size()));
        }
        return name;
    }

    private static ArrayList<String> mAlphaActionList = new ArrayList<>();
    private static  ArrayList<String> mAlphaStoryList = new ArrayList<>();
    private static  ArrayList<String> mAlphaDanceList = new ArrayList<>();

    public static void setActionList(ArrayList<String> action, ArrayList<String> dances,
                                     ArrayList<String> story){
        mAlphaActionList.addAll(action);
        mAlphaDanceList.addAll(dances);
        mAlphaStoryList.addAll(story);
    }

    private static List<String> mAction2SecondList = new ArrayList<String>();
    private static List<String> mAction4SecondList = new ArrayList<String>();
    private static List<String> mAction8SecondList = new ArrayList<String>();
    private static List<String> mAction13SecondList = new ArrayList<String>();

    /**
     * @param
     * @returnp
     * @throws
     * @Description 初始化动作表
     */
    public static void initActionList(ArrayList<ArrayList<String>> list) {
        if (list != null) {
            for (ArrayList<String> item : list) {
                Timber.d("init action list...");
                if (item.get(1) != null && item.get(2) != null) {
                    if ("1".equals(item.get(1))) {
                        String actionName = item.get(2);
                        if (actionName.contains("ACT")) {
                            int time = getActionTime(actionName);
                            switch (time) {
                                case 2:
                                    mAction2SecondList.add(actionName);
                                    break;
                                case 4:
                                    mAction4SecondList.add(actionName);
                                    break;
                                case 8:
                                    mAction8SecondList.add(actionName);
                                    break;
                                case 13:
                                    mAction13SecondList.add(actionName);
                                    break;
                                default:
                                    mAlphaActionList.add(actionName);
                                    break;
                            }
                        } else {
                            mAlphaActionList.add(actionName);
                        }
                    } else if ("2".equals(item.get(1))) {
                        mAlphaDanceList.add(item.get(2));
                    } else if ("3".equals(item.get(1))) {
                        mAlphaStoryList.add(item.get(2));
                    }
                }
            }
            setActionList(mAlphaActionList, mAlphaDanceList, mAlphaStoryList);
        }
    }

    public static void initActionList(List<ActionInfo> onArrAction) {
        if (onArrAction == null) return;
        for (int i = 0; i < onArrAction.size(); i++){
            ActionInfo action = onArrAction.get(i);
            if ("1".equals(action.getType())) {
                if (action.getName().contains("ACT")) {
                    int time = getActionTime(action.getName());
                    switch (time) {
                        case 2:
                            mAction2SecondList.add(action.getName());
                            break;
                        case 4:
                            mAction4SecondList.add(action.getName());
                            break;
                        case 8:
                            mAction8SecondList.add(action.getName());
                            break;
                        case 13:
                            mAction13SecondList.add(action.getName());
                            break;
                        default:
                            mAlphaActionList.add(action.getName());
                            break;
                    }
                } else {
                    mAlphaActionList.add(action.getName());
                }
            } else if("2".equals(action.getType())){
                mAlphaDanceList.add(action.getName());
            } else if ("3".equals(action.getType())){
                mAlphaStoryList.add(action.getName());
            }
        }
        setActionList(mAlphaActionList, mAlphaDanceList, mAlphaStoryList);
    }

    public static String getRandomAction(String text) {
        String actionName = "";
        int length = text.length();
        if (length < 13) {
            if (mAction2SecondList.size() > 0) {
                int number = new Random().nextInt(mAction2SecondList.size());
                number = number + 1;
                actionName = String.format("ACT2-%d", number);
            } else {
                return actionName;
            }
        } else if (length < 25) {
            if (mAction4SecondList.size() > 0) {
                int number = new Random().nextInt(mAction4SecondList.size());
                number = number + 1;
                actionName = String.format("ACT4-%d", number);
            } else {
                return actionName;
            }
        } else if (length < 45) {
            if (mAction8SecondList.size() > 0) {
                int number = new Random().nextInt(mAction8SecondList.size());
                number = number + 1;
                actionName = String.format("ACT8-%d", number);
            } else {
                return actionName;
            }
        } else {
            if (mAction13SecondList.size() > 0) {
                int number = new Random().nextInt(mAction13SecondList.size());
                number = number + 1;
                actionName = String.format("ACT13-%d", number);
            } else {
                return actionName;
            }
        }
        return actionName;
    }

    private static int getActionTime(String actionName) {
        int time = 0;
        String str[] = actionName.split("-");
        if (str.length > 1) {
            if (str[0].length() > 3) {
                time = Integer.parseInt(str[0].substring(3));
            }
        }
        return time;
    }

    public static String organizeActionList() {
        StringBuilder sb = new StringBuilder();

        for (String item : mAlphaActionList) {
            sb.append("1").append("##");
            sb.append(item).append("##");
        }
        for (String item : mAction2SecondList) {
            sb.append("1").append("##");
            sb.append(item).append("##");
        }
        for (String item : mAction4SecondList) {
            sb.append("1").append("##");
            sb.append(item).append("##");
        }
        for (String item : mAction8SecondList) {
            sb.append("1").append("##");
            sb.append(item).append("##");
        }
        for (String item : mAction13SecondList) {
            sb.append("1").append("##");
            sb.append(item).append("##");
        }
        for (String item : mAlphaDanceList) {
            sb.append("2").append("##");
            sb.append(item).append("##");
        }
        for (String item : mAlphaStoryList) {
            sb.append("3").append("##");
            sb.append(item).append("##");
        }

        if (sb.length() >= 2) {
            sb.delete(sb.length() - 2, sb.length());
        }

        return sb.toString();
    }

    public static String getActionNameByDefault(String text) {
        String actionName = "";
        if("大笑".equals(text)) {
            actionName = "大笑";
        } else if("感觉身体被掏空".equals(text)) {
            actionName = "感觉身体被掏空";
        } else if("恭喜发财，红包拿来".equals(text)) {
            actionName = "恭喜发财红包拿来";
        } else if("好委屈".equals(text)) {
            actionName = "好委屈";
        } else if("哼".equals(text)) {
            actionName = "哼";
        } else if("你这么厉害你咋不上天呢".equals(text)) {
            actionName = "你这么厉害你咋不上天呢";
        } else if("完美".equals(text)) {
            actionName = "完美";
        } else if("一起摇摆".equals(text)) {
            actionName = "一起摇摆";
        } else if("左手右手慢动作".equals(text)) {
            actionName = "左手右手慢动作";
        }
        return actionName;
    }
}
