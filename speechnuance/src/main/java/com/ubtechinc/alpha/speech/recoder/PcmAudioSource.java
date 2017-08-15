package com.ubtechinc.alpha.speech.recoder;

import android.os.SystemClock;

import com.nuance.dragon.toolkit.audio.AudioChunk;
import com.nuance.dragon.toolkit.audio.AudioType;
import com.nuance.dragon.toolkit.audio.sources.SingleSinkSource;
import com.nuance.dragon.toolkit.util.internal.Checker;
import com.ubtech.utilcode.utils.thread.HandlerUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @desc : pcm AudioSource 用于适配nuance
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */

public class PcmAudioSource extends SingleSinkSource<AudioChunk>  {
    private final AudioType audioType;
    private final List<AudioChunk> buffer;
    private long startTimestamp;
    private volatile int totalRead;

    public PcmAudioSource(AudioType audioType) {
        Checker.checkArgForNull("audioType", audioType);
        Checker.checkArgForCondition("audioType", "a type supported by this player", this.isCodecSupported(audioType));
        this.audioType = audioType;
        this.buffer = new LinkedList<>();
    }

    @Override
    protected AudioChunk getAudioChunk() {
        return !this.buffer.isEmpty()?this.buffer.remove(0):null;
    }

    @Override
    public AudioType getAudioType() {
        return audioType;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public int getChunksAvailable() {
        return this.buffer.size();
    }

    public void writeAudio(byte[] data, int length){
        final short[] buf = convertByteArrayToShortArray(data, true);
        if (totalRead == 0){
            //（逻辑参考MicrophoneRecorderSourceBase）
            startTimestamp = SystemClock.uptimeMillis() - (long)audioType.getDuration(buf.length);
        }
        final long timestamp = startTimestamp + (long)audioType.getDuration(totalRead);
        totalRead += buf.length;
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                AudioChunk chunk = new AudioChunk(audioType, buf, timestamp);
                synchronized (PcmAudioSource.this) {
                    buffer.add(chunk);
                }
                notifyChunksAvailable();
            }
        });
    }

    boolean isCodecSupported(AudioType audioType) {
        return audioType.encoding == AudioType.Encoding.PCM_16;
    }

    //待验证
    private static short[] convertByteArrayToShortArray(byte[] data, boolean littleEndian) {
        final int length = data.length;
        short[] shorts = new short[length / 2];
        for (int i = 0; i < length - 1; i += 2) {
            if (littleEndian) {
                shorts[i / 2] = (short)((((data[i + 1] << 8) & 0xFF00) | (data[i] & 0xFF)));
            } else {
                shorts[i / 2] = (short)((((data[i] << 8) & 0xFF00) | (data[i + 1] & 0xFF)));
            }
        }
        return shorts;
    }

    public void clearChunks() {
        totalRead = 0;
        synchronized (this){
            buffer.clear();
        }
    }
}
