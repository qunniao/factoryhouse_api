package com.baoge.Util;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import org.json.JSONObject;


@Data
public class SMSUtil {
    /**
     * 发送短信验证码
     */

    public static String send_SMS_Verification_Code(String code, String PhoneNumbers) {

        String SMS_secret = ReadProperties.getProperties_1("", "SMS_secret");
        String SMS_AccessKeyId = ReadProperties.getProperties_1("", "SMS_AccessKeyId");
        String SMS_AccessKeySecret = ReadProperties.getProperties_1("", "SMS_AccessKeySecret");
        String SMS_Domain = ReadProperties.getProperties_1("", "SMS_Domain");
        String SMS_Version = ReadProperties.getProperties_1("", "SMS_Version");
        String SMS_Action = ReadProperties.getProperties_1("", "SMS_Action");
        String SignName = ReadProperties.getProperties_1("", "SignName");
        String TemplateCode = ReadProperties.getProperties_1("", "TemplateCode");
//        System.out.println(SMS_secret);
//        System.out.println(SMS_AccessKeyId);
//        System.out.println(SMS_AccessKeySecret);
//        System.out.println(SMS_Domain);
//        System.out.println(SMS_Version);
//        System.out.println(SMS_Action);
//        System.out.println(SignName);
//        System.out.println(TemplateCode);
        DefaultProfile profile = DefaultProfile.getProfile(SMS_secret, SMS_AccessKeyId, SMS_AccessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain(SMS_Domain);
        request.setVersion(SMS_Version);
        request.setAction(SMS_Action);
        JSONObject code_json = new JSONObject();
        code_json.put("code", code);

        request.putQueryParameter("TemplateParam", code_json.toString());
        request.putQueryParameter("PhoneNumbers", PhoneNumbers);
        request.putQueryParameter("SignName", SignName);
        request.putQueryParameter("TemplateCode", TemplateCode);
        String responseSTR = null;
        try {
            CommonResponse response = client.getCommonResponse(request);
            responseSTR = response.getData();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            return responseSTR;
        }
    }

    public static void main(String[] args) {
        SMSUtil.send_SMS_Verification_Code(null, "");
    }
}
