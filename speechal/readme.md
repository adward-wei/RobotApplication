优必选阿尔法系列机器人语音抽象层
====================================

模块功能说明
==========
####一、优必选语音抽象接口
* IRecoder ：录音抽象层，alpha系列机器人默认采用麦克风阵列作为语音输入
* IWakeuper ：唤醒抽象层，中文可统一采用讯飞cae唤醒，英文nuance
* ITtser ： 语音合成抽象层，需由第三方语音引擎实现   
* ISpeechRecognizer ：语音识别抽象层，需第三方语音引擎实现 


####二、第三方语音模块接入说明：
* 实现方式可参考speechifytek, speechnuance模块


本地构建命令
-----------------

Working on this project? Here's some helpful Gradle tasks:

 * `makeJar` - make jar. 包含java代码的jar
 * `buildJar` - build jar. 包含android asset,lib的jar


作者
------
    logic.peng@ubtechinc.com

License
--------

   Copyright (c) 2008-2017 UBT Corporation.

    All rights reserved.  Redistribution,
    modification, and use in source and binary 
    forms are not permitted unless otherwise authorized by UBT.    