package com.ubtechinc.alpha.serverlibutil.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import com.ubtechinc.alpha.sdk.led.Led;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.sdk.led.LedEffect;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @desc : 灯控信息类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/24
 * @modifier:
 * @modify_time:
 */
public final class LedInfo implements Parcelable {
    private static final String SPLIT = ",";
    private Led led;
    private Set<LedColor> colors = new TreeSet<LedColor>();
    private Set<LedEffect> effects = new TreeSet<LedEffect>();

    public LedInfo(){}

    public LedInfo(Parcel source) {
        readFromParcel(source);
    }

    public final void addColor(LedColor color) {
        if (color != null)
            colors.add(color);
    }

    public final void addEffect(LedEffect effect) {
        if (effect != null)
            effects.add(effect);
    }

    public final Led getLedType() {
        return led;
    }

    public final LedColor[] getSupportColors() {
        LedColor[] colors = new LedColor[this.colors.size()];
        return this.colors.toArray(colors);
    }

    public final LedEffect[] getSupportModes() {
        LedEffect[] effects = new LedEffect[this.effects.size()];
        return this.effects.toArray(effects);
    }

    public final void setLedType(Led led) {
        this.led = led;
    }

    protected final void readFromParcel(Parcel source) {
        led = Led.valueOf(source.readInt());
        String colorStr = source.readString();
        String[] colors = colorStr.split(SPLIT);
        for (int i = 0; i < colors.length -1; i++){
            LedColor ledColor = LedColor.valueOf(colors[i]);
            if (ledColor != null) this.colors.add(ledColor);
        }
        String effectStr = source.readString();
        String[] effects = effectStr.split(SPLIT);
        for (int i = 0; i < effects.length -1; i++){
            LedEffect ledEffect = LedEffect.valueOf(effects[i]);
            if (ledEffect != null) this.effects.add(ledEffect);
        }
    }

    public static final Creator<LedInfo> CREATOR = new Creator<LedInfo>() {
        @Override
        public LedInfo createFromParcel(Parcel source) {
            LedInfo ledInfo = new LedInfo();
            ledInfo.led = Led.valueOf(source.readInt());
            String colorStr = source.readString();
            String[] colors = colorStr.split(SPLIT);
            for (int i = 0; i < colors.length-1; i++){
                ledInfo.colors.add(LedColor.valueOf(colors[i]));
            }
            String effectStr = source.readString();
            String[] effects = effectStr.split(SPLIT);
            for (int i = 0; i < effects.length-1; i++){
                ledInfo.effects.add(LedEffect.valueOf(effects[i]));
            }
            return ledInfo;
        }

        @Override
        public LedInfo[] newArray(int size) {
            return new LedInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(led.value);
        StringBuffer sb = new StringBuffer();
        Iterator<LedColor> iter  = colors.iterator();
        while (iter.hasNext()){
            sb.append(iter.next().name()+SPLIT);
        }
        dest.writeString(sb.toString());
        sb = new StringBuffer();
        Iterator<LedEffect> iterator = effects.iterator();
        while (iterator.hasNext()){
            sb.append(iterator.next().name()+SPLIT);
        }
        dest.writeString(sb.toString());
    }

    @Override
    public String toString() {
        return "LedInfo [" + "led:" + led + "," + "colors:" + colors + ",effects:" + effects + "]";
    }
}
