package com.ubtechinc.zh_chat.business;

import android.content.Context;

/**
 * <pre>
 *   author: Logic
 *   email : 2091843903@qq.com
 *   time  : 2017/3/24
 *   desc  : 音乐
 * </pre>
 */

public class MusicBusiness extends PlayBusiness {
    public MusicBusiness(Context cxt) {
        super(cxt);
    }

    @Override
    protected Type getType() {
        return Type.MUSIC;
    }

    private String singer;

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
