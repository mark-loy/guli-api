package com.mark.msm.utils;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/3 20:59
 */
@Component
public class ConstantMsmUtil implements InitializingBean {

    @Value("${aliyun.keyid}")
    private String key;

    @Value("${aliyun.keysecret}")
    private String secret;

    @Value("${aliyun.sms.sign-name}")
    private String signName;

    @Value("${aliyun.sms.template-code}")
    private String templateCode;

    public static String KEY;

    public static String SECRET;

    public static String SIGN_NAME;

    public static String TEMPLATE_CODE;

    @Override
    public void afterPropertiesSet() throws Exception {
        KEY = key;
        SECRET = secret;
        SIGN_NAME = signName;
        TEMPLATE_CODE = templateCode;
    }
}
