package com.mark.order.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/9 20:27
 */
@Component
public class OrderConstantUtil implements InitializingBean {

    @Value("${wx.appid}")
    private String appId;

    @Value("${wx.pay.partner}")
    private String partner;

    @Value("${wx.pay.partnerkey}")
    private String partnerKey;

    public static String APP_ID;

    public static String PARTNER;

    public static String PARTNER_KEY;

    @Override
    public void afterPropertiesSet() throws Exception {
        APP_ID = appId;
        PARTNER = partner;
        PARTNER_KEY = partnerKey;
    }
}
