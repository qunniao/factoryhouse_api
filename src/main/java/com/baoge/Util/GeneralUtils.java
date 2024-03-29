package com.baoge.Util;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;


public final class GeneralUtils {
    /**
     * 方法描述 封装返回结果集
     *
     * @param code
     * @param msg
     */
    public static Map<String, Object> getReturnResult(Integer code, String msg) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    /**
     * 方法描述 封装返回结果集 多加一个key--value
     *
     * @param code
     * @param msg
     * @param paramName
     * @param paramValue
     * @return
     */
    public static Map<String, Object> getReturnResult(Integer code, String msg, String paramName, Object paramValue) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", code);
        map.put(paramName, paramValue);
        map.put("msg", msg);
        return map;
    }

    /**
     * 方法描述 如果对象为非空返回true 否则返回false
     *
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object obj) {
        if (obj != null) {
            return true;
        }
        return false;
    }

    /**
     * 方法描述 如果对象为空返回 true 否则返回false
     *
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        if (obj != null) {
            return false;
        }
        return true;
    }

    /**
     * 方法描述 判断Map集合非null 非空 返回true 否则返回false
     *
     * @param map
     * @return
     */
    public static boolean isNotNull(Map<Object, Object> map) {
        if (map != null && !map.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 方法描述 判断Map集合是null或者空 返回true 否则返回false
     *
     * @param map
     * @return
     */
    public static boolean isNull(Map<Object, Object> map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 方法描述 判断Map集合非null 非空 返回true 否则返回false
     *
     * @param list
     * @return
     */
    public static boolean isNotNull(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 方法描述 判断Map集合是null或者空 返回true 否则返回false
     *
     * @param list
     * @return
     */
    public static boolean isNull(List<Object> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean notEmpty(Object o) {
        return o != null && !"".equals(o) && !"null".equals(o);
    }

    public static boolean isEmpty(Object o) {
        return o == null && "".equals(o) && "null".equals(o);
    }

    public static boolean notZero(int i) {
        return i == 0;
    }

    public static void main(String[] args) {
        String str = "";
        System.out.println(isNull(str));
    }
}
