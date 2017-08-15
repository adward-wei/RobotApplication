package com.ubtechinc.nets.im.annotation;

import com.ubtechinc.alpha.im.msghandler.IMsgHandler;
import com.ubtechinc.alpha.im.msghandler.NullHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/5/25.
 */

@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImMsgRelation {
    int requestCmdId() default 0;
    int responseCmdId() default 0;
    Class msgRequestClass() default Object.class;
    Class<? extends IMsgHandler> msgHandleClass() default NullHandler.class;
}
