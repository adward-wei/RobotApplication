#!/bin/sh
javah -o SerialPortFile.h -jni -classpath ../build/intermediates/classes/debug/ com.ubtechinc.alpha.jni.SerialPortFile
javah -o head_led.h -jni -classpath ../build/intermediates/classes/debug/ com.ubtechinc.alpha.jni.LedControl
javah -o head_key_mgr.h -jni -classpath ../build/intermediates/classes/debug/ com.ubtechinc.alpha.jni.headkey.HeadKeyMgr