package com.ubtechinc.nets.phonerobotcommunite;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageLite;
import com.ubtechinc.alpha.AlphaMessageHeaderOuterClass;
import com.ubtechinc.alpha.AlphaMessageOuterClass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author：tanghongyu
 * @date：5/16/2017 4:15 PM
 * @modifier：tanghongyu
 * @modify_date：5/16/2017 4:15 PM
 * [A brief description]
 * version
 */

public class ProtoBufferDispose{
    public static final String TAG = "ProtoBufferDispose";
    public static byte[] getPackMData(GeneratedMessageLite data) {
        if (data != null) {
            return data.toByteArray();
        } else {
            return null;
        }
//        byte[] content = data.toByteArray();
//        // 组包
//        PacketData packetData = new PacketData(4);
//        // 包标志
//        packetData.putShort(StaticValue.SOCKET_FLAG);
//        // 长度
//        packetData.putInt(content.length + 4);
//        // 通信版本号
//        packetData.putShort(StaticValue.SOCKET_VERSION);
//        // 辅助信息
//        packetData.putShort((short) 0);
//        // 消息内容
//        packetData.putBytes(content);
//        return packetData.getBuffer();
    }

    private static void printProtoBuffBody(GeneratedMessageLite protoBuff) {
        if (protoBuff != null) {
            Field[] fields = protoBuff.getClass().getDeclaredFields();
            if (fields != null) {
                for(Field field: fields) {
                    field.setAccessible(true);
                    Class clz = field.getType();
                    try {
                        Object object = field.get(protoBuff);
//                        Log.d(TAG,protoBuff.getClass().getName()+"---"+field.getName()+":"+field.get(protoBuff).toString()+"");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public static GeneratedMessageLite unPackData(Class targetClass,byte[] data) {
        try {
            Method parseFromMethod = targetClass.getDeclaredMethod("parseFrom",byte[].class);
            parseFromMethod.setAccessible(true);
            Object object = parseFromMethod.invoke(null,data);
//            printProtoBuffBody((GeneratedMessageLite)object);
            return (GeneratedMessageLite)object;


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
//        try {
//            return AlphaMessageOuterClass.AlphaMessage.parseFrom(data);
//        } catch(InvalidProtocolBufferException e) {
//            return null;
//        }
//        Header mHeader = new Header();
//        try {
//            for (int i = 0; i < data.length; i++) {
//                if (mHeader.setData(data[i])) {
//                    Class clazz = GeneratedMessageLite.class;
//                    ByteArrayInputStream bais = new ByteArrayInputStream(mHeader.getMsg());
//                    Method m2 = clazz.getMethod("parseFrom", InputStream.class);
//                    return (GeneratedMessageLite) m2.invoke(null, bais);
//
//                }
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    public static AlphaMessageOuterClass.AlphaMessage buildAlphaMessage(int cmdId,String version,GeneratedMessageLite requestBody,long sendSerial, long responseSerial) {
//        printProtoBuffBody(requestBody);
        AlphaMessageHeaderOuterClass.AlphaMessageHeader.Builder headerBuilder = AlphaMessageHeaderOuterClass.AlphaMessageHeader.newBuilder();
        headerBuilder.setCommandId(cmdId);
        headerBuilder.setVersionCode(version);
        headerBuilder.setSendSerial(sendSerial);
        headerBuilder.setResponseSerial(responseSerial);



        AlphaMessageOuterClass.AlphaMessage.Builder builder = AlphaMessageOuterClass.AlphaMessage.newBuilder();
        builder .setHeader(headerBuilder.build());
        if(requestBody != null) {
            ByteString body = ByteString.copyFrom(ProtoBufferDispose.getPackMData(requestBody));
            builder.setBodyData(body);
        }

        return builder.build();
    }
}
