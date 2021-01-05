package com.mark.msm.controller;

import com.mark.commonutil.entity.Result;
import com.mark.msm.service.MsmService;
import com.mark.msm.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/3 19:19
 */
@Api(value = "短信服务", tags = {"短信服务接口"})
@RestController
@RequestMapping("/service/msm")
public class MsmController {

    @Resource
    private MsmService msmService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发送短信验证码
     * @param number 手机号码
     * @return Result
     */
    @ApiOperation(value = "发送短信验证码")
    @GetMapping("/send/{number}")
    public Result sendMessageCode(@ApiParam(value = "手机号码") @PathVariable("number") String number) {
        // 首先查询redis中是否存在该手机号的验证码
        String code = redisTemplate.opsForValue().get(number);
        // 判断验证码是否为空
        if (!StringUtils.isEmpty(code)) {
            // 不为空直接返回验证码
            return Result.ok().message("短信验证码已发送");
        }
        // 验证码为空，重新获取四位数验证码
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("code", code);
        // 调用短信发送方法
        boolean isSend = msmService.sendMessageCode(number, codeMap);
        // 判断短信发送结果
        if (isSend) {
            // 将本次的短信验证码存入redis,设置有效期为5分钟
            redisTemplate.opsForValue().set(number, code, 5, TimeUnit.MINUTES);
            return Result.ok().message("短信验证码发送成功");
        }
        return Result.error().message("短信验证码发送失败");
    }
}
