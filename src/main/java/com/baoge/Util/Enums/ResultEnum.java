package com.baoge.Util.Enums;

/**
 * Created by 廖师兄
 * 2017-01-21 14:23
 */
public enum ResultEnum {
    SUCCESS(0, "成功"),
    REFUSE_USER(-2, "用户被禁止登陆!"),
    NULL_USER(100100, "用户不存在!"),
    USER_PRESENCE(100100, "用户手机号已经存在!"),
    PASSWORD_ERRROR(100001, "密码错误!"),
    NULL_CODE(100103, "验证码未找到!"),
    CODE_NOT_OK(100104, "请重新获取验证码!"),
    CODE_OVERDUE(100104, "验证码已过期!"),
    CODE_ERRROR(100106, "验证码错误!"),
    PHONE_NUMBER_ERRROR(100107, "手机号未通过验证"),
    ALIBABA_SMS_ERROR(100108, "阿里短信接口异常,详情请查看日志"),
    CREATE_ACCOUNT_ERROR(100109,"用户名或密码不能为空"),
    NULL_TABLE(100110,"数据错误"),
    EXISTED_USER(100101, "用户已存在!"),
    EXISTED_ACCEPTION(100102, "已有采纳信息"),
    EXCEED_UPPERLIMIT(100103, "超过发布数量上限"),
    NULL_LOGINUSER(100104, "用户未找到,重新登陆"),
    NULL_POSITIONING(100105, "未搜索到当前位置信息"),
    NULL_UPLOAD(100106, "上传数据为空"),
    NULL_REGION(100107, "找不到区域信息"),
    REPEAT_DATA(100108, "数据重复"),
    NOT_DELETE(100109, "不可删除"),
    NOT_BALANCE(100110, "余额不足"),
    NOT_UPDATE(100109, "不可修改"),
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
