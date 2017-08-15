package com.ubtechinc.alpha.speech.ttser;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;

import com.nuance.dragon.toolkit.audio.AudioChunk;
import com.nuance.dragon.toolkit.audio.AudioSource;
import com.nuance.dragon.toolkit.audio.AudioType;
import com.nuance.dragon.toolkit.audio.TtsMarker;
import com.nuance.dragon.toolkit.audio.TtsMarkerListener;
import com.nuance.dragon.toolkit.audio.sinks.PlayerSink;
import com.nuance.dragon.toolkit.audio.sinks.SpeakerPlayerSink;
import com.nuance.dragon.toolkit.file.FileManager;
import com.nuance.dragon.toolkit.util.json.JSONObject;
import com.nuance.dragon.toolkit.vocalizer.Vocalizer;
import com.nuance.dragon.toolkit.vocalizer.VocalizerConfig;
import com.nuance.dragon.toolkit.vocalizer.VocalizerVoice;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.StringUtils;

import org.json.JSONException;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.ubtechinc.alpha.speech.nuance.NuanceConstants.DEFAULT_TTS_LANGUEGE;
import static com.ubtechinc.alpha.speech.nuance.NuanceConstants.DEFAULT_TTS_PITCH;
import static com.ubtechinc.alpha.speech.nuance.NuanceConstants.DEFAULT_TTS_SPEED;
import static com.ubtechinc.alpha.speech.nuance.NuanceConstants.DEFAULT_TTS_VOICE;
import static com.ubtechinc.alpha.speech.nuance.NuanceConstants.DEFAULT_TTS_VOLUME;
import static com.ubtechinc.alpha.speech.nuance.NuanceConstants.ENU_VOICES;
import static com.ubtechinc.alpha.speech.nuance.NuanceConstants.KOK_VOICES;
import static com.ubtechinc.alpha.speech.nuance.NuanceConstants.RUR_VOICES;
import static com.ubtechinc.alpha.speech.nuance.NuanceConstants.SUPPORT_LANGS;


/**
 * @desc : Nuance tts模块
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public final class NuanceTtser implements ITtser {
    private static final AudioType DEFAULT_AUDIO_TYPE = AudioType.PCM_22k;
    private static final int DEFAULT_FREQUENCIES= Vocalizer.Frequencies.FREQ_22KHZ;
    private static final String TAG = "NuanceTtser";
    private final Vocalizer mVocalizer;
    private SpeakerPlayerSink mPlayer;
    private boolean useBt = true;
    private ITTSListener mTTSListener;

    private String mVoiceName;
    private String mTtsSpeed;
    private String mTtsVolume;
    private String mTtsLanguage;
    private String mTtsPitch;

    private JSONObject mConfigJson;
    private volatile AtomicBoolean speeking = new AtomicBoolean(false);

    public NuanceTtser(Context context) {
        this.mVocalizer = Vocalizer.createVocalizer(new FileManager(context, ".jpg", "vocon", "vocon"));
        mPlayer = useBt? new SpeakerPlayerSink(AudioManager.STREAM_MUSIC, DEFAULT_AUDIO_TYPE)
                :new SpeakerPlayerSink(DEFAULT_AUDIO_TYPE);
        mConfigJson = new JSONObject();
        mPlayer.setTtsMarkerListener(new TtsMarkerListener() {
            @Override
            public void onTtsMarkerAvailable(TtsMarker ttsMarker) {

            }
        });
    }

    private final Vocalizer.TtsListener ttsListener = new Vocalizer.TtsListener() {

        @Override
        public void ttsGenerationStarted(String text, Object context, Vocalizer vocalizer) {
            LogUtils.d(TAG,"nuance 语音合成->generate开始...");
            if (mTTSListener != null) {
                mTTSListener.onTtsBegin();
            }
        }

        @Override
        public void ttsGenerationFinished(String text, Object context, Vocalizer vocalizer, boolean success) {
            LogUtils.d(TAG,"nuance 语音合成->generate完成...success = "+ success);
        }

        @Override
        public void ttsStreamingStarted(String text, Object context, Vocalizer vocalizer) {
            LogUtils.d(TAG,"nuance 语音合成->streaming开始...");
        }

        @Override
        public void ttsStreamingFinished(String text, Object context, Vocalizer vocalizer) {
            LogUtils.d(TAG,"nuance 语音合成->streaming结束...");
        }
    };

    final PlayerSink.Listener mPlayerSinkListener = new PlayerSink.Listener() {

        @Override
        public void onStopped(PlayerSink player) {
            LogUtils.i(TAG,"nuance 语音合成播放结束...");
            if (mTTSListener != null) {
                mTTSListener.onTtsCompleted(0);
            }
        }

        @Override
        public void onStarted(PlayerSink player) {
            LogUtils.d(TAG,"nuance 语音合成播放开始...");
        }
    };

    @Override
    public void init() {
        mVocalizer.enableVerboseAndroidLogging(true);
        setTtsLanguage(DEFAULT_TTS_LANGUEGE);
        setTtsVoicer(DEFAULT_TTS_VOICE);
        mConfigJson.tryPut("frequency", DEFAULT_FREQUENCIES);
        mConfigJson.tryPut("gender", 2);
        try {
            mVocalizer.load(VocalizerConfig.createFromJSON(mConfigJson), new Vocalizer.LoadListener() {
                @Override
                public void onLoaded(Vocalizer vocalizer, boolean b) {
                    LogUtils.d(TAG, "vocalizer load result ="+ b);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTtsSpeed(DEFAULT_TTS_SPEED);
        setTtsVolume(DEFAULT_TTS_VOLUME);
        setTtsPitch(DEFAULT_TTS_PITCH);
    }

    @Override
    public void startSpeaking(String text, ITTSListener listener) {
        this.mTTSListener = listener;
        if (!speeking.getAndSet(true)) {
            try {
                mPlayer.stopPlaying();
                mPlayer.disconnectAudioSource();
                AudioSource<AudioChunk> audioSource = mVocalizer.generateTts(text, ttsListener, null);
                mPlayer.connectAudioSource(audioSource);
                mPlayer.startPlaying(mPlayerSinkListener);
            } catch (Exception e) {
                e.printStackTrace();
                if (mTTSListener != null) mTTSListener.onTtsCompleted(-1);
            }
        }else {
            mVocalizer.cancelTts();
            mPlayer.stopPlaying();
            mPlayer.disconnectAudioSource();
            AudioSource<AudioChunk> audioSource = mVocalizer.generateTts(text, ttsListener, null);
            mPlayer.connectAudioSource(audioSource);
            mPlayer.startPlaying(mPlayerSinkListener);
        }
    }

    @Override
    public void stopSpeaking() {
        speeking.set(false);
        if (mVocalizer != null) {
            mVocalizer.cancelTts();
        }
        if (mPlayer != null)
            mPlayer.stopPlaying();
    }

    @Override
    public boolean isSpeaking() {
        return speeking.get();
    }

    @Override
    public boolean destroy() {
        if (mVocalizer != null) {
            mVocalizer.release();
        }
        if (mPlayer != null) {
            mPlayer.disconnectAudioSource();
            mPlayer.stopPlaying();
            mPlayer = null;
        }
        return true;
    }

    @Override
    public void setTtsSpeed(String speed) {
        if (TextUtils.isEmpty(speed) || speed.equals(mTtsSpeed) || !StringUtils.isNumeric(speed)) return;
        mTtsSpeed = speed;
        mVocalizer.setTtsSpeed(Integer.valueOf(speed));
    }

    @Override
    public String getTtsSpeed() {
        return mTtsSpeed;
    }

    @Override
    public void setTtsPitch(String pitch) {
        if (TextUtils.isEmpty(pitch) || pitch.equals(mTtsPitch) || !StringUtils.isNumeric(pitch)) return;
        mTtsPitch = pitch;
        mVocalizer.setTtsPitch(Integer.valueOf(pitch));
    }

    @Override
    public String getTtsPitch() {
        return mTtsPitch;
    }

    @Override
    public void setTtsVolume(String volume) {
        if (TextUtils.isEmpty(volume) || volume.equals(mTtsVolume) || !StringUtils.isNumeric(volume)) return;
        mTtsVolume = volume;
        mVocalizer.setTtsVolume(Integer.valueOf(volume));
    }

    @Override
    public String getTtsVolume() {
        return mTtsVolume;
    }

    @Override
    public void setTtsLanguage(String language) {
        if (TextUtils.isEmpty(language) || language.equals(mTtsLanguage) || !isVocalizerLanguageValid(language)) return;
        this.mTtsLanguage = language;
        mConfigJson.tryPut("language", language);
    }

    private boolean isVocalizerLanguageValid(String language) {
        for (int i = 0; i < SUPPORT_LANGS.length; i++){
            if (SUPPORT_LANGS[i].equals(language)) return true;
        }
        return false;
    }

    @Override
    public String getTtsLanguage() {
        return mTtsLanguage;
    }

    @Override
    public void setTtsVoicer(String voicer) {
        if (TextUtils.isEmpty(voicer) || voicer.equals(mVoiceName) || !isVocalizerVoicerValid(voicer)) return;
        this.mVoiceName = voicer;
        mConfigJson.tryPut("voice", mVoiceName);
    }

    private boolean isVocalizerVoicerValid(String voice) {
        if (mTtsLanguage == null) return true;
        if (mTtsLanguage.equals(SUPPORT_LANGS[0])){
            for (VocalizerVoice v:ENU_VOICES) {
                if (v.name.equals(voice)) return true;
            }
        }else if (mTtsLanguage.equals(SUPPORT_LANGS[1])){
            for (VocalizerVoice v:RUR_VOICES) {
                if (v.name.equals(voice)) return true;
            }
        }else if (mTtsLanguage.equals(SUPPORT_LANGS[2])){
            for (VocalizerVoice v:KOK_VOICES) {
                if (v.name.equals(voice)) return true;
            }
        }
        return false;
    }

    @Override
    public String getTtsVoicer() {
        return mVoiceName;
    }
}
