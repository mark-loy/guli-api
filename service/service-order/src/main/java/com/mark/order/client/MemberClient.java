package com.mark.order.client;

import com.mark.order.client.impl.MemberClientImpl;
import com.mark.servicebase.vo.MemberOrderVO;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/9 16:39
 */
@FeignClient(value = "service-ucenter", fallback = MemberClientImpl.class)
public interface MemberClient {

    /**
     * 消费者：获取订单中的用户信息
     * @param uid 用户id
     * @return MemberOrderVO
     */
    @GetMapping("/api/ucenter/provider/{uid}")
    MemberOrderVO getMemberOrder(@ApiParam("用户id") @PathVariable("uid") String uid);
}
