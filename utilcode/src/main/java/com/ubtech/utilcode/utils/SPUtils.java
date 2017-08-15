package com.ubtech.utilcode.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 *
 *     @author: logic.peng
 *     @email  : pdlogic1987@gmail.com
 *     @time  : 2016/8/2
 *     desc  : SP相关工具类
 *
 */
public class SPUtils {

    private   SharedPreferences pref;
    private  SharedPreferences.Editor editor;
    private static final String TAG = "SPUtils";

    /**
     * SPUtils构造函数
     * <p>在Application中初始化</p>
     *
     * @param spName spName
     */
    public SPUtils(String spName) {
        pref = Utils.getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
    }
    private SPUtils() {

        pref =  PreferenceManager.getDefaultSharedPreferences(Utils.getContext());
        editor = pref.edit();
        editor.apply();
    }
    //使用volatile关键字保其可见性
    volatile private static SPUtils INSTANCE = null;
    public static SPUtils get() {

        if (INSTANCE == null) {
            //创建实例之前可能会有一些准备性的耗时工作
            synchronized (SPUtils.class) {
                if (INSTANCE == null) {//二次检查
                    INSTANCE = new SPUtils();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, @Nullable String value) {

        checkPrefAndEditor();
        editor.putString(key, value).apply();
    }

    private void checkPrefAndEditor() {
        if (pref == null)
        {
            pref = PreferenceManager.getDefaultSharedPreferences(Utils.getContext());
        }
        if (editor == null)
        {
            editor = pref.edit();
        }
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code null}
     */
    public String getString(String key) {

        return getString(key, null);
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public String getString(String key, String defaultValue) {
        checkPrefAndEditor();
        return pref.getString(key, defaultValue);
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, int value) {
        checkPrefAndEditor();
        editor.putInt(key, value).apply();
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public int getInt(String key, int defaultValue) {
        checkPrefAndEditor();
        return pref.getInt(key, defaultValue);
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, long value) {
        checkPrefAndEditor();
        editor.putLong(key, value).apply();
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public long getLong(String key) {
        return getLong(key, -1L);
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public long getLong(String key, long defaultValue) {
        checkPrefAndEditor();
        return pref.getLong(key, defaultValue);
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, float value) {
        checkPrefAndEditor();
        editor.putFloat(key, value).apply();
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public float getFloat(String key) {
        return getFloat(key, -1f);
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public float getFloat(String key, float defaultValue) {

        checkPrefAndEditor();
        return pref.getFloat(key, defaultValue);
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, boolean value) {
        checkPrefAndEditor();
        editor.putBoolean(key, value).apply();
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code false}
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        checkPrefAndEditor();
        return pref.getBoolean(key, defaultValue);
    }

    /**
     * SP中写入String集合类型value
     *
     * @param key    键
     * @param values 值
     */
    public void put(String key, @Nullable Set<String> values) {
        checkPrefAndEditor();
        editor.putStringSet(key, values).apply();
    }

    /**
     * SP中读取StringSet
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code null}
     */
    public Set<String> getStringSet(String key) {
        return getStringSet(key, null);
    }

    /**
     * SP中读取StringSet
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public Set<String> getStringSet(String key, @Nullable Set<String> defaultValue) {
        checkPrefAndEditor();
        return pref.getStringSet(key, defaultValue);
    }

    /**
     * SP中获取所有键值对
     *
     * @return Map对象
     */
    public Map<String, ?> getAll() {
        checkPrefAndEditor();
        return pref.getAll();
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    public void remove(String key) {
        checkPrefAndEditor();
        editor.remove(key).apply();
    }

    /**
     * SP中是否存在该key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public boolean contains(String key) {
        checkPrefAndEditor();
        return pref.contains(key);
    }

    /**
     * SP中清除所有数据
     */
    public void clear() {
        checkPrefAndEditor();
        editor.clear().apply();
    }


    /**
     * 直接存放对象，反射将根据对象的属性作为key，并将对应的值保存。
     *
     * @param t
     */
    @SuppressWarnings("rawtypes")
    public  <T> void putObject(T t) {
        try {
            checkPrefAndEditor();
            String methodName = "";
            String savekey = "";
            String saveValue = "";
            SharedPreferences.Editor edit = pref.edit();
            Class cls = t.getClass();

            if (edit != null) {
                Method[] methods = cls.getDeclaredMethods();

                for (Method method : methods) {

                    methodName = method.getName();
                    if (methodName != null && methodName.startsWith("get")) {

                        Object value = method.invoke(t);
                        if (!TextUtils.isEmpty(String.valueOf(value))) {
                            saveValue = String.valueOf(value);
                        }

                        savekey = methodName.replace("get", "");
                        savekey = savekey.toLowerCase();
                        edit.putString(savekey, saveValue);
                    }
                }
                edit.commit();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取整个对象，跟put(T t)对应使用， 利用反射得到对象的属性，然后从preferences获取
     *
     * @param cls
     * @return
     */
    public  <T> Object getObject(Class<T> cls) {
        Object obj = null;
        String fieldName = "";
        try {
            obj = cls.newInstance();
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                fieldName = f.getName();
                if (!"serialVersionUID".equals(fieldName)) {
                    f.setAccessible(true);
                    f.set(obj, getString(f.getName()));
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return obj;
    }

}