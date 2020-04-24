package com.baoge.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rest返回对象
 *^
 * @param <T>
 */
public class R<T> {
    private static final Logger LOG = LoggerFactory.getLogger(R.class);
    /**
     * 服务器响应数据
     */
    private T data;

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 状态码
     */
    private int code = -1;

    /**
     * 服务器响应时间
     */
    private long timestamp;

    public R() {
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public R(boolean success) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
    }

    public R(boolean success, T data) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.data = data;
    }

    public R(boolean success, T data, int code, String msg) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public R(boolean success, String msg) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
    }

    public R(boolean success, String msg, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static R ok() {
        return new R(true,"操作成功");
    }

    public static <T> R ok(T data) {
        return new R(true, data, 0,"操作成功");
    }

    public static <T> R ok(int code) {
        return new R(true, "操作成功", code);
    }

    public static <T> R ok(T data, int code) {
        return new R(true, data, code,"操作成功");
    }

    public static R fail() {
        return new R(false);
    }

    public static R fail(String msg) {
        return new R(false, msg);
    }

    public static R fail(int code) {
        return new R(false, null, code);
    }

    public static R fail(int code, String msg) {
        return new R(false, msg, code);
    }

    public void ToString(){
        LOG.info("code:"+code+"  success:"+success+"  msg:"+msg+"  timestamp:"+timestamp);
    }

}