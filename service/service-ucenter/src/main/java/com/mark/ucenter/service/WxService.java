package com.mark.ucenter.service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/6 11:56
 */
public interface WxService {
    String getWxCode(HttpServletRequest request);

    HashMap getAccessToken(String code, String state);

    HashMap getWxUser(String accessToken, String openid);
}
