package com.ubtechinc.alpha.ops.action.utils;

import com.ubtechinc.alpha.ops.action.Block;
import com.ubtechinc.alpha.ops.action.Port;

/**
 * @desc : 块工具类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/2
 * @modifier:
 * @modify_time:
 */

public final class BlockHelper {

    private BlockHelper(){
        throw new RuntimeException("");
    }

    public static int findPortIdByPortType(Block block, @Port.PortType int portType){
        int id = -1;
        for(Port port : block.getPorts()){
            if (port.getType() == portType){
                id = port.getId();
                break;
            }
        }
        return id;
    }

    public static int findPortIdByPortName(Block block, String name){
        int id = -1;
        for(Port info : block.getPorts()){
            short[] sTemp = info.getName();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i < sTemp.length; i++){
                if (sTemp[i] != 0){
                    sb.append((char)sTemp[i]);
                }
            }
            String s = sb.toString();
            s = s.trim();
            if (name.equals(s)){
                id = info.getId();
                break;
            }
        }
        return id;
    }

    public static Port findPortByPortId(Block block, int id){
        Port info = null;
        for(Port i : block.getPorts()){
            if (i.getId() == id){
                info = i;
                break;
            }
        }
        return info;
    }

    public static @Port.PortType int findPortTypeByPortId(Block block, int id){
        @Port.PortType int nType = -1;
        for(Port port : block.getPorts()){
            if (port.getId() == id){
                nType = port.getType();
            }
        }
        return nType;
    }
}
