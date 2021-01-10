package com.mark.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.commonutil.entity.Result;
import com.mark.order.entity.Order;
import com.mark.order.service.OrderService;
import com.mark.order.service.PayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author mark
 * @since 2021-01-09
 */
@Api(value = "订单日志管理", tags = {"订单日志服务接口"})
@RestController
@RequestMapping("/api/order/pay")
public class PayLogController {

    @Resource
    private PayLogService logService;

    @Resource
    private OrderService orderService;

    /**
     * 检查微信支付状态
     * @param orderNo 订单号
     * @return Result
     */
    @ApiOperation("检查微信支付状态")
    @GetMapping("/state/{orderNo}")
    public Result checkOrderState(@ApiParam("订单号") @PathVariable("orderNo") String orderNo) {

        // 检查支付是否成功
        Map<String, String> map = logService.checkPayState(orderNo);
        if (map == null) {
            return Result.error().message("支付失败");
        }
        // 判断支付状态
        if ("SUCCESS".equals(map.get("trade_state"))) {
            // 支付成功，保存支付信息日志
            logService.savePayLog(map);
        } else {
            return Result.ok().code(25000).message("支付中");
        }

        return Result.ok().message("支付成功");
    }

}

