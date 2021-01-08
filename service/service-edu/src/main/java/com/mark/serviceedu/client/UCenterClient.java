package com.mark.serviceedu.client;

import com.mark.commonutil.entity.Result;
import com.mark.serviceedu.client.impl.UCenterClientImpl;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/8 20:23
 */
@FeignClient(value = "service-ucenter", fallback = UCenterClientImpl.class)
public interface UCenterClient {

    /**
     * 远程调用 ==》判断用户是否存在
     * @param id 用户id
     * @return Result
     */
    @GetMapping("/api/ucenter/exist/{id}")
    Result isExistMember(@ApiParam("用户id") @PathVariable("id") String id);
}
