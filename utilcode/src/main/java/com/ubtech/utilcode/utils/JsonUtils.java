package com.ubtech.utilcode.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 *     @author: wzt
 *     @time  : 2017/5/18
 *     @desc  : 数据类与json字符串互转的工具类
 *
 */

public class JsonUtils{
    private static Gson gson = new Gson();
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {

            t = gson.fromJson(jsonString, cls);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
    public static <T> T getObject(String jsonString, Type type) {
        T t = null;
        try {

            t = gson.fromJson(jsonString, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
    public static <T> List<T> getObjectList(String jsonString, Class<T> cls) {

        List<T> list = new ArrayList();
        try {

            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getListString(String jsonString) {
        List<String> list = new ArrayList<String>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    public static List<Map<String, Object>> getListMap(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = gson.fromJson(jsonString,
                    new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String, Object> getMap(String jsonString) {
        Map<String, Object> list = new WeakHashMap<String, Object>();
        try {
            list = gson.fromJson(jsonString,
                    new TypeToken<WeakHashMap<String, Object>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static  <T> T jsonToBean(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public static String object2Json(Object object) {
        return gson.toJson(object);
    }

    /**
     *
     * @autor 唐宏宇
     * TODO map转成JSON格式数据
     */
    public static String map2Json(Map<String ,String> map) {



        return gson.toJson(map);
    }

    public static String array2Json(List<String> strings) {
        JsonArray jsonArray = new JsonArray();
        for (String value : strings) {
            jsonArray.add(value);
        }
        return jsonArray.toString();
    }


}

