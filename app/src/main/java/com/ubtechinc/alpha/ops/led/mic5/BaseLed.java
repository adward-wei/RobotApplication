package com.ubtechinc.alpha.ops.led.mic5;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public abstract class BaseLed {
    protected int priority;
    protected LedType ledType;

    BaseLed(int priority,LedType ledType){
        this.priority=priority;
        this.ledType =ledType;
    }

    abstract void operationLed();
}
