package com.ubtechinc.nets.im.annotation;

import com.ubtechinc.alpha.im.msghandler.IMJsonMsgHandler;
import com.ubtechinc.alpha.im.msghandler.NullJsonHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/5/25.
 */

@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImJsonMsgRelation {
    int requestCmdId() default 0;
    int responseCmdId() default 0;
    Class<? extends IMJsonMsgHandler> msgHandleClass() default NullJsonHandler.class;
}
