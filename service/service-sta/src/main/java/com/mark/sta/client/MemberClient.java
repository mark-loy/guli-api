package com.mark.sta.client;

import com.mark.commonutil.entity.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/11 15:12
 */
@FeignClient(value = "service-ucenter")
public interface MemberClient {

    /**
     * 消费者：获取某天的注册人数
     * @param date 日期
     * @return Result
     */
    @ApiOperation("获取某天的注册人数")
    @GetMapping("/api/ucenter/provider/register/{date}")
    Result getMemberRegister(@ApiParam("日期") @PathVariable("date") String date);
}
