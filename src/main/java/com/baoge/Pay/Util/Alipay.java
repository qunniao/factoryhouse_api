package com.baoge.Pay.Util;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.baoge.Pay.PO.AlipayBean;
import com.baoge.Util.Constants;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.netty.util.Constant;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.baoge.Pay.Util.AlipayProperties.CHARSET;

/**
 * 支付宝支付接口
 * @author Louis
 * @date Dec 12, 2018
 */
@Component
public class Alipay {

        public static final String  SIGN_ALGORITHMS = "SHA256WithRSA";

    /**
     * 支付接口
     * @param alipayBean
     * @return
     * @throws AlipayApiException
     */

    public String pay(HttpServletRequest request,AlipayBean alipayBean) throws Exception{

        //try {
            // 1、获得初始化的AlipayClient

            String serverUrl = AlipayProperties.getGatewayUrl();
            String appId = AlipayProperties.getAppId();
            String privateKey = AlipayProperties.getPrivateKey();
            String format = "json";
            String charset = AlipayProperties.getCharset();
            String alipayPublicKey = AlipayProperties.getPublicKey();
            String signType = AlipayProperties.getSignType();
            String returnUrl = AlipayProperties.getReturnUrl();
            String notifyUrl = AlipayProperties.getNotifyUrl();
            AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
            // 2、设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            // 页面跳转同步通知页面路径
            alipayRequest.setReturnUrl(returnUrl);
            // 服务器异步通知页面路径
            alipayRequest.setNotifyUrl(notifyUrl);
            // 封装参数
            alipayRequest.setBizContent(JSON.toJSONString(alipayBean));
            // 3、请求支付宝进行付款，并获取支付结果
            String result = alipayClient.pageExecute(alipayRequest).getBody();

            // 返回付款信息
            return result;
        /*}catch(Exception e){
            return "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
        }*/
    }

    /*
        public String buildOrderParamMap(String orderNum, Double money,String content)throws Exception {

                String ali_call_back_url =  AlipayProperties.getNotifyUrl();
                String appId = AlipayProperties.getAppId();
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(currentTime);
                /*String[] parameters={
                        "method=\"alipay.trade.app.pay\"",//固定值
                        "app_id=\""+appId+"\"",//合作身份者ID（16位）
                        "charset=\"utf-8\"",
                        "notify_url=\""+ali_call_back_url+"\"",//通知地址
                        "out_trade_no=\""+orderNum+"\"",//商户内部订单号
                        "subject=\"库房滴滴\"",//测试
                        "payment_type=\"1\"",//固定值
                        "seller_id=\""+sellerAccount+"\"",//账户邮箱
                        "total_fee=\""+money+"\"",//支付金额（元）
                        "sign_type=\"RSA2\"",
                        "body=\""+content+"\"",//订单说明out_trade_no
                        "timestamp=\""+dateString+"\"",
                        "it_b_pay=\"15m\""
         };

            String[] parameters={
                    "method=alipay.trade.app.pay",//固定值
                    "app_id="+appId+"",//合作身份者ID（16位）
                    "charset=utf-8",
                    "notify_url="+ali_call_back_url+"",//通知地址
                    "sign_type=RSA2",
                    "version=1.0",
                    "format=json",
                    "timestamp="+dateString+"",
                    "biz_content={\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" + money +  "\",\"subject\":\"测试\",\"body\":\"" + content +  "\",\"out_trade_no\":\"" + orderNum +  "\"}",
            };
                String signOrderUrl = signAllString(parameters);
                return signOrderUrl;
    }
    */

    public String buildOrderParamMap(String orderNum, Double money,String content,String title)throws Exception {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayProperties.APP_ID, AlipayProperties.PRIVARY_KEY, "json", CHARSET, AlipayProperties.PUBLIC_KEY, "RSA2");
//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(content);
        model.setSubject(title);
        model.setOutTradeNo(orderNum);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(money+"");
        model.setProductCode(orderNum);
        request.setBizModel(model);
        request.setNotifyUrl(AlipayProperties.NOTIFY_URL);

            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。

        return response.getBody();
    }

        /**
         * 支付宝签名
         * @param array
         * @return
         */
        private String signAllString(String [] array){
                StringBuffer sb = new StringBuffer("");
            try {
                for (int i = 0; i < array.length; i++) {
                    if (i == (array.length - 1)) {
                        String value = array[i].substring(array[i].indexOf("=") + 1);
                        String newValue =  array[i].substring(0, array[i].indexOf("="))+"="+URLEncoder.encode(value, "UTF-8");
                        sb.append(newValue);
                    } else {
                        String value = array[i].substring(array[i].indexOf("=") + 1);
                        String newValue =  array[i].substring(0, array[i].indexOf("="))+"="+URLEncoder.encode(value, "UTF-8");
                        sb.append(newValue + "&");
                    }
                }
            }catch (Exception e) {
                    e.printStackTrace();
            }

                System.out.println(sb.toString());
                String sign = "";
            try {
                        sign = URLEncoder.encode(sign(sb.toString(),AlipayProperties.getPrivateKey(), "utf-8"), "utf-8");//private_key私钥
                } catch (Exception e) {
                        e.printStackTrace();
                }
                sb.append("&sign="+sign+"");
                return sb.toString();
        }

        /**
         * RSA签名
         * @param content 待签名数据
         * @param privateKey 商户私钥
         * @param input_charset 编码格式
         * @return 签名值
         */
        private String sign(String content, String privateKey, String input_charset)
        {
                try
                {
                        byte[] decode = Base64.decode(privateKey);
                        PKCS8EncodedKeySpec priPKCS8   = new PKCS8EncodedKeySpec(decode );
                        KeyFactory keyf= KeyFactory.getInstance("RSA");
                        PrivateKey priKey= keyf.generatePrivate(priPKCS8);

                        java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

                        signature.initSign(priKey);
                        signature.update(content.getBytes(input_charset) );

                        byte[] signed = signature.sign();

                        return Base64.encode(signed);
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }

                return null;
        }

}
