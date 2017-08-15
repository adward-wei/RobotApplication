package com.ubtechinc.alpha.speech.nuance;

import com.nuance.dragon.toolkit.vocalizer.Vocalizer;
import com.nuance.dragon.toolkit.vocalizer.VocalizerVoice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @desc :Nuance常量
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */

public final class NuanceConstants {
    public static final VocalizerVoice[] ENU_VOICES = Vocalizer.Languages.UNITED_STATES_ENGLISH.voices;//美式英文发音人
    public static final VocalizerVoice[] RUR_VOICES = Vocalizer.Languages.RUSSIAN.voices;//俄语发音人
    public static final VocalizerVoice[] KOK_VOICES = Vocalizer.Languages.KOREAN.voices;//韩文发音人
    //英语，俄语，韩语
    public static final String[] SUPPORT_LANGS = {Vocalizer.Languages.UNITED_STATES_ENGLISH.name,
            Vocalizer.Languages.RUSSIAN.name,
            Vocalizer.Languages.KOREAN.name};
    public static final List<VocalizerVoice> SUPPORT_VOICES = new ArrayList<>();
    static {
        SUPPORT_VOICES.addAll(Arrays.asList(ENU_VOICES));
        SUPPORT_VOICES.addAll(Arrays.asList(RUR_VOICES));
        SUPPORT_VOICES.addAll(Arrays.asList(KOK_VOICES));
    }
    public static final String DEFAULT_TTS_VOICE = ENU_VOICES[2].name;
    public static final String DEFAULT_TTS_SPEED = "100";
    public static final String DEFAULT_TTS_VOLUME = "80";
    public static final String DEFAULT_TTS_LANGUEGE = SUPPORT_LANGS[0];
    public static final String DEFAULT_TTS_PITCH = "100";

}
