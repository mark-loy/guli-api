package com.mark.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.mark.msm.service.MsmService;
import com.mark.msm.utils.ConstantMsmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/3 19:20
 */
@Service
@Slf4j
public class MsmServiceImpl implements MsmService {

    @Override
    public boolean sendMessageCode(String number, Map<String, Object> codeMap) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ConstantMsmUtil.KEY, ConstantMsmUtil.SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        // 构建request对象
        CommonRequest request = new CommonRequest();

        // 短信发送固定配置
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        // 短信发送参数设置
        request.putQueryParameter("PhoneNumbers", number);
        request.putQueryParameter("SignName", ConstantMsmUtil.SIGN_NAME);
        request.putQueryParameter("TemplateCode", ConstantMsmUtil.TEMPLATE_CODE);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(codeMap));

        try {
            // 发送短信
            CommonResponse response = client.getCommonResponse(request);
            return response.getHttpResponse().isSuccess();
        } catch (ClientException e) {
            log.error("短信验证码发送异常：", e);
            return false;
        }
    }
}
