package com.baoge.Util;


import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.user.UserInfoResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.JiguangPO;

import java.lang.reflect.InvocationTargetException;

import static cn.jpush.api.push.model.notification.PlatformNotification.ALERT;

public class JgUtil {


    public static void PushSomthong(String methodName, JiguangPO jiguangPO) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, APIConnectionException, APIRequestException {

        String APP_KEY = ReadProperties.getProperties_1("", "AppKey");
        String MASTER_SECRET = ReadProperties.getProperties_1("", "MasterSecret");

        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());

        PushPayload payload = jiguangPO.use(JgUtil.class.getMethod(methodName, JiguangPO.class), jiguangPO);

        PushResult result = jpushClient.sendPush(payload);

    }


    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll(ALERT);
    }

    /**
     * 构建推送对象：所有平台，推送目标是别名为 "alias1"，通知内容为 ALERT
     *
     * @param msg
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alert(String name, String msg) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(name))
                .setNotification(Notification.alert(msg))
                .build();
    }

    /**
     * 构建推送对象：平台是 Android，目标是 tag 为 "aimsName" 的设备，内容是 Android 通知 msg，并且标题为 tiele。
     *
     * @param jiguangPO
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle(JiguangPO jiguangPO) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("kf_123456"))
                .setNotification(Notification.android(jiguangPO.getMsg_content(), "kf_123456", null))
                .build();
    }

    /**
     * 构建推送对象：平台是 iOS，
     * 推送目标是 "tag1", "tag_all" 的交集，推送内容同时包括通知与消息 - 通知信息是 ALERT，角标数字为 5，
     * 通知声音为 "happy"，并且附加字段 from = "JPush"；
     * 消息内容是 MSG_CONTENT。通知是 APNs 推送通道的，消息是 JPush 应用内消息通道的。APNs 的推送环境是“生产”（如果不显式设置的话，Library 会默认指定为开发）
     *
     * @param jiguangPO
     * @return
     */
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(JiguangPO jiguangPO) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and(jiguangPO.getAimsName(), jiguangPO.getTag_all()))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(jiguangPO.getAlert())
                                .setBadge(jiguangPO.getNumber())
                                .setSound(jiguangPO.getSound())//happy
                                .addExtra(jiguangPO.getExtraKey(), jiguangPO.getExtraValue())
                                .build())
                        .build())
                .setMessage(Message.content(jiguangPO.getMsg_content()))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }

    /**
     * 构建推送对象：平台是 Andorid 与 iOS，推送目标是 （"tag1" 与 "tag2" 的并集）交（"alias1" 与 "alias2" 的并集，
     * 推送内容是 - 内容为 MSG_CONTENT 的消息，并且附加字段 from = JPush。
     *
     * @param jiguangPO
     * @return
     */
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras(JiguangPO jiguangPO) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag(jiguangPO.getTag_all()))
                        .addAudienceTarget(AudienceTarget.alias(jiguangPO.getAliasList()))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(jiguangPO.getMsg_content())
                        .addExtra(jiguangPO.getExtraKey(), jiguangPO.getExtraValue())
                        .build())
                .build();
    }

    public static void testGetUserInfo(JiguangPO jiguangPO) throws APIConnectionException, APIRequestException {

        String APP_KEY = ReadProperties.getProperties_1("", "AppKey");
        String MASTER_SECRET = ReadProperties.getProperties_1("", "MasterSecret");
        JMessageClient client = new JMessageClient(APP_KEY, MASTER_SECRET);
        UserInfoResult res = client.getUserInfo(jiguangPO.getUsreName());
        System.out.println(res.getAddress());
        System.out.println(res.getNickname());
        System.out.println(res.getUsername());
    }

    public static void testCreateGroup(JiguangPO jiguangPO) throws SCException, APIConnectionException, APIRequestException {
        String APP_KEY = ReadProperties.getProperties_1("", "AppKey");
        String MASTER_SECRET = ReadProperties.getProperties_1("", "MasterSecret");
        JMessageClient client = new JMessageClient(APP_KEY, MASTER_SECRET);
        if (GeneralUtils.isEmpty(jiguangPO.getUserPassWord())  && GeneralUtils.isEmpty(jiguangPO.getUsreName())) {
            throw new SCException(ResultEnum.CREATE_ACCOUNT_ERROR);
        }
        String res = client.registerAdmins(jiguangPO.getUsreName(), jiguangPO.getUserPassWord());
        System.out.println(res);
    }
}
