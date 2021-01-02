package com.mark.servicevod.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/30 14:08
 */
@Component
public class ConstantVodUtil implements InitializingBean {

    @Value("${aliyun.vod.file.keyid}")
    private String key;

    @Value("${aliyun.vod.file.keysecret}")
    private String secret;

    public static String KEY;

    public static String SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        KEY = key;
        SECRET = secret;
    }
}
