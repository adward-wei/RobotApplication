package com.ubtechinc.alpha.affairdispatch;

/**
 * @desc : tts/动作类事务
 * @author: wzt
 * @time : 2017/5/18
 * @modifier:
 * @modify_time:
 */
public abstract class BaseAffair extends AbstractAffair {


    public BaseAffair() {
        super();
        mID = (int) (Math.random() * 1000000 );
    }



    @Override
    public void start() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void stop() {
    }

}
