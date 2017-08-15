package com.ubt.alpha2.statistics;

import java.util.HashMap;

/**
 * @author: liwushu
 * @description: 构建统计api
 * @created: 2017/7/19
 * @version: 1.0
 * @modify: liwushu
*/
public class MapFactory {

    public static HashMap<String, String> produce(KeyValuePair ... keyValuePairs){
        HashMap map = new HashMap<String, String>();
        for(KeyValuePair keyValuePair : keyValuePairs){
            if(keyValuePair != null){
                map.put(keyValuePair.getKey(), keyValuePair.getValue());
            }
        }
        return map;
    }

}