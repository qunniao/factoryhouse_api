package com.baoge.systemConfig.Filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;



@Configuration
public class MvcInterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则，/**表示拦截所有请求
        // excludePathPatterns 用户排除拦截

        List<String> servicePathPatterns = new ArrayList<String>();
        servicePathPatterns.add("/modules/**");
        servicePathPatterns.add("/buyingPurchase/**");
        servicePathPatterns.add("/encyclopediaAnswer/**");
        servicePathPatterns.add("/encyclopedia/**");
        servicePathPatterns.add("/fplHouse/**");
        servicePathPatterns.add("/newsController/**");
        servicePathPatterns.add("/parkStore/**");
        servicePathPatterns.add("/administrator/**");
        servicePathPatterns.add("/brokerCommentController/**");
        servicePathPatterns.add("/brokerController/**");
        servicePathPatterns.add("/personal/**");
        servicePathPatterns.add("/TransactionRecord/**");
        servicePathPatterns.add("/realNameSystem/**");
        servicePathPatterns.add("/delegationInformation/**");
        servicePathPatterns.add("/order/alipay");
        servicePathPatterns.add("/order/alipayAPP");
        //servicePathPatterns.add("/**");

        List<String> excludePathPatterns = new ArrayList<String>();
        excludePathPatterns.add("/buyingPurchase/pageBuyingPurchase");
        excludePathPatterns.add("/personal/pagePersonal");
        excludePathPatterns.add("/personal/queryUserByUid");
        excludePathPatterns.add("/delegationInformation/pageDelegationInformation");
        excludePathPatterns.add("/buyingPurchase/queryByTypesAndStatus");
        excludePathPatterns.add("/buyingPurchase/queryBuyingPurchaseByBpid");
        excludePathPatterns.add("/encyclopediaAnswer/queryByElid");
        excludePathPatterns.add("/encyclopediaAnswer/queryEncyclopediaAnswerByEaid");
        excludePathPatterns.add("/encyclopedia/pageEncyclopedia");
        excludePathPatterns.add("/encyclopedia/queryEncyclopediaByElid");
        excludePathPatterns.add("/fplHouse/pageFplHouse");
        excludePathPatterns.add("/fplHouse/queryFplHousePOByProductId");
        excludePathPatterns.add("/fplHouse/queryFplHouseByUid");
        excludePathPatterns.add("/fplHouse/queryFplHouseRegionalInformation");
        excludePathPatterns.add("/fplHouse/queryFplHousePOByLatitudeAndLongitude");
        excludePathPatterns.add("/fplHouse/queryFplHouseByUidAndTypes");
        excludePathPatterns.add("/newsController/pageNews");
        excludePathPatterns.add("/newsController/queryNewsByNid");
        excludePathPatterns.add("/brokerCommentController/pageBrokerComment");
        excludePathPatterns.add("/brokerController/pageBroker");
        excludePathPatterns.add("/parkStore/pageParkStore");
        excludePathPatterns.add("/parkStore/queryParkStoreByPid");
        excludePathPatterns.add("/data/**");
        excludePathPatterns.add("/login/**");
        excludePathPatterns.add("/swagger-resources");
        excludePathPatterns.add("/v2/api-docs");
        excludePathPatterns.add("/webjars/springfox-swagger-ui/**");
        excludePathPatterns.add("/configuration/security");
        excludePathPatterns.add("/configuration/ui");

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(servicePathPatterns)
                .excludePathPatterns(excludePathPatterns);
        super.addInterceptors(registry);
    }

}
