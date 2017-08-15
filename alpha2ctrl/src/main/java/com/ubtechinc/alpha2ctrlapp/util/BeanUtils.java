package com.ubtechinc.alpha2ctrlapp.util;

import android.support.annotation.NonNull;

import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageLite;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author：tanghongyu
 * @date：6/15/2017 3:40 PM
 * @modifier：tanghongyu
 * @modify_date：6/15/2017 3:40 PM
 * 对象拷贝工具类
 * version
 */

public class BeanUtils {
    //属性缓存
    private static Map<String, Field[]> fieldsMap = new HashMap<>();
    //方法缓存
    private static Map<String, Map<String, Method>> methodsMap = new HashMap<>();

    /**
     * 从Proto实体拷贝数据到java Bean
     *
     * @param protoObj  Proto实体
     * @param targetObj java Bean
     */
    public static void copyBeanFromProto(@NonNull Object protoObj, @NonNull Object targetObj) {
        // 获取源对象类型
        Class<?> protoClass = protoObj.getClass();
        Class<?> targetClass = targetObj.getClass();

        // 获得目标对象所有属性
        Field[] fields = getCacheField(targetClass);//缓存，取target的属性，因为Proto文件生成的属性包含_下划线

        // 遍历所有属性
        for (int i = 0; i < fields.length; i++) {
            // 属性对象
            Field field = fields[i];
            // 属性名

            try {
                Method getMethod = getCacheMethod(protoClass, addProtoGetString(field));
                if (getMethod == null) {
                    // 得到get方法对象, 因为Proto文件生成的get boolean方法是 get+属性名,拼接get方法会出问题， 而正常的java文件则是 is+属性名或属性名
                    continue;
                }

                // 对源对象调用get方法获取属性值
                Object value = getMethod.invoke(protoObj);

                Method setMethod;
                setMethod = getCacheMethod(targetClass, addSetString(field), new Class[]{field.getType()});
                if (setMethod == null) {
                    continue;
                }
                if (field.getType() == byte[].class) {//protoBuffer传过来的都是ByteString,需要特殊处理
                    byte[] valueByte = ((ByteString) value).toByteArray();
                    setMethod.invoke(targetObj, new Object[]{valueByte});
                } else {
                    // 对目标对象调用set方法装入属性值
                    setMethod.invoke(targetObj, new Object[]{value});
                }


            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 拷贝数据到Proto实体
     *
     * @param sourceObj javaBean
     * @param protoObj  Proto实体
     */
    public static void copyBeanToProto(@NonNull Object sourceObj, @NonNull Object protoObj) {
        // 获取源对象类型
        Class<?> sourceClass = sourceObj.getClass();
        Class<?> targetClass = protoObj.getClass();
        // 获得目标对象所有属性
        Field[] fields = getCacheField(sourceClass);//缓存，sourceClass，因为Proto文件生成的属性包含_下划线

// 遍历所有属性
        for (int i = 0; i < fields.length; i++) {
// 属性对象
            Field field = fields[i];

            try {
                Method getMethod = getCacheMethod(sourceClass, addGetString(field));
                if (getMethod == null) {
                    // 得到get方法对象, 因为Proto文件生成的get boolean方法是 get+属性名， 而正常的java文件则是 is+属性名或属性名
                    continue;
                }

                // 对源对象调用get方法获取值
                Object value = getMethod.invoke(sourceObj);

                Method setMethod = getCacheMethod(targetClass, addProtoSetString(field),  new Class[]{field.getType()});
                if (setMethod == null) {

                    continue;
                }

                // 对目标对象调用set方法装入属性值
                setMethod.invoke(protoObj, new Object[]{value});

            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }


    public static void copyBean(@NonNull Object sourceObj, @NonNull Object targetObj) {
        // 获取源对象类型
        Class<?> sourceClass = sourceObj.getClass();
        Class<?> targetClass = targetObj.getClass();
        // 获得目标对象所有属性
        Field[] fields = getCacheField(sourceClass);
// 遍历所有属性
        for (int i = 0; i < fields.length; i++) {
// 属性对象
            Field field = fields[i];
// 属性名

            try {

                Method getMethod = getCacheMethod(sourceClass, addGetString(field));
                if (getMethod == null) {//找不到声明的方法
                    continue;
                }

                // 对源对象调用get方法获取属性值
                Object value = getMethod.invoke(sourceObj);

                Method setMethod = getCacheMethod(targetClass, addSetString(field), new Class[]{field.getType()});
                if (setMethod == null) {
                    continue;
                }

// 对目标对象调用set方法装入属性值
                setMethod.invoke(targetObj, new Object[]{value});

            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private static Field[] getCacheField(Class classObj) {
        Field[] fields = fieldsMap.get(classObj.getName());
        if (fields == null) {

            if (GeneratedMessageLite.class.isAssignableFrom(classObj)) {
                fields = classObj.getDeclaredFields();//不遍历父类方法

            } else {
                fields = getAllDeclaredFields(classObj);//普通javabean需要遍历父类方法
            }

            fieldsMap.put(classObj.getName(), fields);
        }
        return fields;
    }

    private static Method getCacheMethod(Class classObj, String methodName,  Class<?>... parameterTypes) {

        Map<String, Method> methodMap = methodsMap.get(classObj.getName());
        Method method = null;
        if (methodMap == null) {
            methodMap = Maps.newHashMap();
        }
        try {

            if (GeneratedMessageLite.class.isAssignableFrom(classObj)) {
                method = classObj.getDeclaredMethod(methodName, parameterTypes);
            } else {
                method = getDeclaredMethod(classObj, methodName, parameterTypes);//普通javabean需要遍历父类方法
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        methodMap.put(methodName, method);
        methodsMap.put(classObj.getName(), methodMap);

        return method;
    }


    public static <T> T[] copy(T[] source, T[] target) {
        T[] result = Arrays.copyOf(source, source.length + target.length);
        System.arraycopy(target, 0, result, source.length, target.length);
        return result;
    }

    /**
     * 遍历所有声明属性，包含父类的属性
     *
     * @param classObj
     * @return
     */
    public static Field[] getAllDeclaredFields(Class classObj) {
        Field[] fields = null;
        for (; classObj != Object.class; classObj = classObj.getSuperclass()) {
            Field[] newFields = classObj.getDeclaredFields();
            if (fields == null) {
                fields = newFields;
            } else {
                fields = copy(newFields, fields);
            }

        }
        return fields;
    }

    /**
     * 遍历获取所有声明方法，包括父类
     *
     * @param classObj
     * @return
     */
    public static Method[] getAllDeclaredMethods(Class classObj) {

        Method[] methods = null;
        for (; classObj != Object.class; classObj = classObj.getSuperclass()) {
            Method[] newMethods = classObj.getDeclaredMethods();
            if (methods == null) {
                methods = newMethods;
            } else {
                methods = copy(newMethods, methods);
            }

        }
        return methods;
    }


    public static Method getDeclaredMethod(Class clazz, String methodName, Class<?>... parameterTypes) {
        Method method = null;

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {

            }
        }

        return method;
    }

    public static String addGetString(Field field) {

        String fieldName = field.getName();

        StringBuffer sb = new StringBuffer();

        if (field.getType() == boolean.class) {
            String topLetter = fieldName.substring(0, 2);
            if (topLetter.equals("is")) {//boolean 方法的get是is + 属性名称
                sb.append(fieldName);
            } else {
                sb.append("is").append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1));
            }
        } else {
            // 拼接get方法名如getName, // 得到get方法对象, 因为Proto文件生成的get boolean方法是 get+属性名， 而正常的java文件则是 is+属性名或属性名
            sb.append("get").append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1));
        }

        return sb.toString();


    }

    public static String addProtoGetString(Field field) {
        String fieldName = field.getName();
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        return sb.toString();
    }

    public static String addSetString(Field field) {

        String fieldName = field.getName();

        StringBuffer sb = new StringBuffer();
        sb.append("set");
        String topLetter = fieldName.substring(0, 2);
        //例如属性 isystemApp=setIsystemApp; isSystemApp=setSystemApp; systemApp=setSystemApp
        if (field.getType() == boolean.class && topLetter.equals("is") && Character.isUpperCase(fieldName.charAt(2))) {
            sb.append(fieldName.substring(2, 3).toUpperCase()).append(fieldName.substring(3));
        } else {
            // 拼接set方法名
            sb.append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1));
        }

        return sb.toString();
    }

    public static String addProtoSetString(Field field) {
        String fieldName = field.getName();
        StringBuffer sb = new StringBuffer();
        sb.append("set");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        return sb.toString();
    }

}
