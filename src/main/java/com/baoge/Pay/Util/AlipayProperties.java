package com.baoge.Pay.Util;

import com.baoge.Util.Constants;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 应用启动加载文件
 * @author Louis
 * @date Dec 12, 2018
 */

@Component
public class AlipayProperties {

    public static final String APP_ID = "2019050764414072";
    public static final String PRIVARY_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDOaLOm+/OiWN6H\n" +
            "gw//Bn5MhIj/4jvD3KVDvgHbqgMCLwZ9bUTnceUi+ndnvBcZGXPUf+5HZfDRevXd\n" +
            "kssrfaGhZT8Syk5+SkpP3loEFnEhwKN+04shTmBFS9g+ypreyf+063etm9IYdqLx\n" +
            "EpHwEZzxOHFwl0wzDfYywIFkGbkUmOUnInASS61xYwHXpF670cHF/W4xS3xN/kgd\n" +
            "DF5QJXLnSY0K71ZqjN7mMtuuK5d/7e/r2uTAEDnajlbGxlX0fsAprbmU6YXI2V+r\n" +
            "VSfnUvVOEWxS/hnIc9uqDP77SJPHcSs7lMyCW4zraISA+s1EIdfSTCdCi9QfZjYv\n" +
            "u6pNiPYnAgMBAAECggEAD+yD754AbcXtZQb3dPVLSRvwdc0+Tx32ogNGoWu8T67R\n" +
            "EVwctL5Urx4jmB0qqwULqpk0CM4UhhS2YyGwCurmTU1CUHDLHXMdyIue5jHVCIWU\n" +
            "F3S6hxdE1hPD2xRGXmm3EH4Z/YrH6ps8dty35Y12TiXMmqMRfd7V35R6Zv6VCMGf\n" +
            "bynaFOG1ss2JZU2VZ/CjLJmt1n3ih9/wyG0DUOnOMfOrQdC9XjfVfrptm7dJJjir\n" +
            "wVCAOBeImtYN7p3/vH1l+VY1tMI3bPswsqMi9qtDRru1K9HeKtSqN7krOD/myr1g\n" +
            "6IlOrAeY/l/cf3S89YluD8j+5CL72fVEZz2an3YUAQKBgQDtr+lrOqEk/ELHhy7/\n" +
            "qk074is6wv+ReWJi6sLrDPG9wFyJWrK4/Q9SRf1GuREPsPzKnceluu1/GKLdfR0w\n" +
            "7h3Vs1Wnw5VRpDTXo3/RI/Ryz4v4vsC8d3jGL5JM0CgvnbixNV+vBFx6hHGnfvCq\n" +
            "WMQ+wwRBbWA2SyirOYbecLjX6QKBgQDeT93RDvfE/RA3vXjhOBkGB6BBMcDtx4WV\n" +
            "YWLHnuRcQMForTLvTaHRJ4Eb7/69ZydS+gL3SzlgtqVb4vsNAArq7m+cmqY4PAEA\n" +
            "2XZFQQdSmH8thuvYn2tvldokUxHbhd0nF4NVHuTku5RJmXh+wOpgscJLPmRTc8dY\n" +
            "6SR/2/ejjwKBgQDZd8u/GiN6LXwEBsGPlgeYNLSMzQD73u2Px2+Tk4JStH92BI/l\n" +
            "BVRRWEwFuynLC4oe8AFMtxy43peUwvZEYHjXn39NQXuK+QyIB8XYWmc77H0Kl6zM\n" +
            "QxszaS8YLJO4DTyAlm73OKxMRIWgVFpp4VGcgt2P5zzSMC4Di1zEhKn6kQKBgQDD\n" +
            "4HsBCL8mwhLYCgnH5zWlKnL4/m2PFI2O4Q+38zgiUKwZHwIR98TsFHKduwgougrc\n" +
            "AY07dvxHSqbIH2Y0PiVgelW9FkX+pl/7759VxcY98D+eTZLgexaeatqZxtelJH2H\n" +
            "RCBjj6T+JIizkJWqmS/porYetgqPvSOYI1QVlCrN4QKBgF+rF1aC0UWYLX6L/en5\n" +
            "tKgebGgufjizr4Rk95WB8doMK46XMfQE7CGYY5zIPY8/eupeaU0YhAMZb+AhqJY1\n" +
            "PZma5VI2Eir+pnP+36BUjNIF/HDzvj+5JNHVliZHEO/NjlGLw2uZZHNwtY+R21Sz\n" +
            "ewtT1kcNOSE84QOCaet9dOxq";
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzmizpvvzoljeh4MP/wZ+\n" +
            "TISI/+I7w9ylQ74B26oDAi8GfW1E53HlIvp3Z7wXGRlz1H/uR2Xw0Xr13ZLLK32h\n" +
            "oWU/EspOfkpKT95aBBZxIcCjftOLIU5gRUvYPsqa3sn/tOt3rZvSGHai8RKR8BGc\n" +
            "8ThxcJdMMw32MsCBZBm5FJjlJyJwEkutcWMB16Reu9HBxf1uMUt8Tf5IHQxeUCVy\n" +
            "50mNCu9Waoze5jLbriuXf+3v69rkwBA52o5WxsZV9H7AKa25lOmFyNlfq1Un51L1\n" +
            "ThFsUv4ZyHPbqgz++0iTx3ErO5TMgluM62iEgPrNRCHX0kwnQovUH2Y2L7uqTYj2\n" +
            "JwIDAQAB";
    public static final String NOTIFY_URL = Constants.SERVER_HOST+"order/alipayAsynchronousCallback";
    public static final String RETURN_URL = "returnUrl";
    public static final String SIGN_TYPE = "RSA2";
    public static final String CHARSET = "utf-8";
    public static final String GATEWAY_URL = "gatewayUrl";
    public static final String LOG_PATH = "logPath";
    public static final String SELLERACCOUNT = "sellerAccount";
    public static final String PARTNER = "partner";
    /**
     * 保存加载配置参数
     */
    private static Map<String, String> propertiesMap = new HashMap<String, String>();


    /**
     */
    public static void loadProperties() {
        // 获得PathMatchingResourcePatternResolver对象
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            // 加载resource文件(也可以加载resources)
            Resource resources = resolver.getResource("config/alipay.properties");
            PropertiesFactoryBean config = new PropertiesFactoryBean();
            config.setLocation(resources);
            config.afterPropertiesSet();
            Properties prop = config.getObject();
            // 循环遍历所有得键值对并且存入集合
            for (String key : prop.stringPropertyNames()) {
                propertiesMap.put(key, (String) prop.get(key));
            }
        } catch (Exception e) {
            new Exception("配置文件加载失败");
        }
    }

    /**
     * 获取配置参数值
     * @param key
     * @return
     */
    public static String getKey(String key) {
        return propertiesMap.get(key);
    }

    public static String getAppId() {
        return propertiesMap.get(APP_ID);
    }

    public static String getPrivateKey() {
        return propertiesMap.get(PRIVARY_KEY);
    }

    public static String getPublicKey() {
        return propertiesMap.get(PUBLIC_KEY);
    }

    public static String getNotifyUrl() {
        return propertiesMap.get(NOTIFY_URL);
    }

    public static String getReturnUrl() {
        return propertiesMap.get(RETURN_URL);
    }

    public static String getSignType() {
        return propertiesMap.get(SIGN_TYPE);
    }

    public static String getCharset() {
        return propertiesMap.get(CHARSET);
    }

    public static String getGatewayUrl() {
        return propertiesMap.get(GATEWAY_URL);
    }

    public static String getLogPath() {
        return propertiesMap.get(LOG_PATH);
    }

    public static String getSelleraccount(){return propertiesMap.get(SELLERACCOUNT);}

    public static String getPartner(){return propertiesMap.get(PARTNER);}

}
