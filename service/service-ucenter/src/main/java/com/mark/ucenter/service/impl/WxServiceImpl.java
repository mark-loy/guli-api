package com.mark.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.mark.commonutil.utils.HttpClientUtil;
import com.mark.commonutil.utils.JwtUtils;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.ucenter.service.WxService;
import com.mark.ucenter.utils.ConstantWxUtil;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/6 11:57
 */
@Service
public class WxServiceImpl implements WxService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String getWxCode(HttpServletRequest request) {
        // 定义调用的模板url
        String formatUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 获取sessionId
        String sessionId = ConstantWxUtil.WX_STATE_PREFIX + request.getSession().getId();
        // 从redis中取state
        String state =  redisTemplate.opsForValue().get(sessionId);
        if (StringUtils.isEmpty(state)) {
            // 使用uuid生成state
            state = UUID.randomUUID().toString().replace("-", "");
            // 使用redis对state进行缓存，key为sessionId，有效期为30分钟
            redisTemplate.opsForValue().set(sessionId, state, 30L, TimeUnit.MINUTES);
        }

        String  redirectUri;
        try {
            // 对回调的uri进行编码处理
            redirectUri = URLEncoder.encode(ConstantWxUtil.WX_LOGIN_CALLBACK , "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(20001, e.getMessage());
        }

        // 对模板url参数赋值，返回url
        return String.format(formatUrl, ConstantWxUtil.WX_APP_ID, redirectUri, state);
    }

    @Override
    public HashMap getAccessToken(String code, String state) {
        // 判断code是否存在
        if (StringUtils.isEmpty(code)) {
            throw new CustomException(CustomExceptionEnum.USER_AUTH_REJECT);
        }

/*        Claims claims = JwtUtils.parseToken(token);
        String sessionId = (String) claims.get("sessionId");
        // 从redis中获取state
        String redisState = redisTemplate.opsForValue().get(sessionId);
        // 判断state, 防止请求被伪造
        if (!state.equals(redisState)) {
            throw new CustomException(CustomExceptionEnum.UNAUTHORIZED_ACCESS_ERROR);
        }*/
        // 定义模板url
        String formatUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        // 给模板参数赋值
        String url = String.format(formatUrl, ConstantWxUtil.WX_APP_ID, ConstantWxUtil.WX_APP_SECRET, code);

        // 使用httpclient,发送get请求
        String resultStr = HttpClientUtil.doGet(url);

        // 判断resultStr
        if (StringUtils.isEmpty(resultStr)) {
            throw new CustomException(CustomExceptionEnum.HTTP_RESULT_ERROR);
        }
        // 转换resultStr结果为hashmap
        return JSON.parseObject(resultStr, HashMap.class);
    }

    @Override
    public HashMap getWxUser(String accessToken, String openid) {
        // 判断token非空
        if (StringUtils.isEmpty(accessToken)) {
            throw new CustomException(CustomExceptionEnum.HTTP_RESULT_ERROR);
        }
        // 定义模板url
        String formatUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        // 对模板url赋值
        String url = String.format(formatUrl, accessToken, openid);
        // 发送http请求
        String userStr = HttpClientUtil.doGet(url);
        // 转化string类型为hashmap
        return JSON.parseObject(userStr, HashMap.class);
    }
}
