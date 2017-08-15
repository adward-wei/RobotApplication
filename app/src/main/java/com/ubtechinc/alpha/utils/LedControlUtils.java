package com.ubtechinc.alpha.utils;

import com.ubtechinc.alpha.sdk.led.Led;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.sdk.led.LedEffect;
import com.ubtechinc.alpha.serverlibutil.aidl.LedInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/27
 * @modifier:
 * @modify_time:
 */

public final class LedControlUtils {

    public static List<LedInfo> getLedInfos(){
        List<LedInfo> infos = new ArrayList<>(5);
        infos.add(createChestInfo());
        infos.add(createEyeInfo());
        infos.add(createHeadInfo());
        return infos;
    }

    public static LedInfo createEyeInfo(){
        LedInfo eye = new LedInfo();
        eye.setLedType(Led.EYE);

        eye.addColor(LedColor.BLACK);
        eye.addColor(LedColor.YELLOW);
        eye.addColor(LedColor.WHITE);
        eye.addColor(LedColor.RED);
        eye.addColor(LedColor.GREEN);
        eye.addColor(LedColor.BLUE);
        eye.addColor(LedColor.MAGENTA);
        eye.addColor(LedColor.CYAN);

        eye.addEffect(LedEffect.LIGHT);
        eye.addEffect(LedEffect.BLINK);
        eye.addEffect(LedEffect.MARQUEE);
        eye.addEffect(LedEffect.FLASH);
        return eye;
    }

    public static LedInfo createHeadInfo(){
        LedInfo head = new LedInfo();
        head.setLedType(Led.HEAD);

        head.addColor(LedColor.BLACK);
        head.addColor(LedColor.YELLOW);
        head.addColor(LedColor.WHITE);
        head.addColor(LedColor.RED);
        head.addColor(LedColor.GREEN);
        head.addColor(LedColor.BLUE);
        head.addColor(LedColor.MAGENTA);
        head.addColor(LedColor.CYAN);

        head.addEffect(LedEffect.LIGHT);
        head.addEffect(LedEffect.BREATH);
        head.addEffect(LedEffect.MARQUEE);
        head.addEffect(LedEffect.FLASH);
        return head;
    }

    public static LedInfo createChestInfo(){
        LedInfo chest = new LedInfo();
        chest.setLedType(Led.CHEST);
        chest.addColor(LedColor.BLACK);
        chest.addColor(LedColor.WHITE);
        chest.addEffect(LedEffect.LIGHT);
        return chest;
    }
}
