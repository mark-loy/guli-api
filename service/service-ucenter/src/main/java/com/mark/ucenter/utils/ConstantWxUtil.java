package com.mark.ucenter.utils;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/6 11:16
 */
@Component
public class ConstantWxUtil implements InitializingBean {

    @Value("${wx.appid}")
    private String appId;

    @Value("${wx.appsecret}")
    private String appSecret;

    @Value("${wx.callback}")
    private String callback;

    @Value("${wx.state.prefix}")
    private String statePrefix;

    public static String WX_APP_ID;

    public static String WX_APP_SECRET;

    public static String WX_LOGIN_CALLBACK;

    public static String WX_STATE_PREFIX;

    @Override
    public void afterPropertiesSet() throws Exception {
        WX_APP_ID = appId;
        WX_APP_SECRET = appSecret;
        WX_LOGIN_CALLBACK = callback;
        WX_STATE_PREFIX = statePrefix;
    }
}
