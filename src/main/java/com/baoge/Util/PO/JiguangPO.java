package com.baoge.Util.PO;

import cn.jpush.api.push.model.PushPayload;
import com.baoge.Util.JgUtil;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author 王志鹏
 * @title: JiguangPO
 * @projectName baoge
 * @description: TODO
 * @date 2019/4/4 9:44
 */
@Data
public class JiguangPO implements Serializable {

    private String usreName;
    private String userPassWord;
    private String aimsName;
    private String tag_all;
    private String alert;
    private String msg_content;
    private int number;
    private String sound;
    private String extraKey;
    private String extraValue;
    private List<String> tagList;
    private List<String> aliasList;

    public PushPayload use(Method method, JiguangPO jiguangPO) throws InvocationTargetException, IllegalAccessException {
        JgUtil jgUtil = new JgUtil();
        PushPayload payload = (PushPayload) method.invoke(jgUtil, jiguangPO);
        return payload;
    }

}
