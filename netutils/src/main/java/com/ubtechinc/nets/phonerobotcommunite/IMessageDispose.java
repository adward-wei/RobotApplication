package com.ubtechinc.nets.phonerobotcommunite;

import com.google.protobuf.GeneratedMessageLite;

/**
 * @author：tanghongyu
 * @date：5/16/2017 4:13 PM
 * @modifier：tanghongyu
 * @modify_date：5/16/2017 4:13 PM
 * [A brief description]
 * version
 */

public interface IMessageDispose {

    byte[] getPackMData(GeneratedMessageLite data);
    GeneratedMessageLite unPackData(GeneratedMessageLite generatedMessageLite, byte[] data);
}
