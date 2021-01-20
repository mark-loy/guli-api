package com.mark.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.commonutil.entity.Result;
import com.mark.commonutil.utils.JwtUtils;
import com.mark.order.entity.Order;
import com.mark.order.entity.vo.OrderQueryVO;
import com.mark.order.service.OrderService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author mark
 * @since 2021-01-09
 */
@Api(value = "订单管理", tags = {"订单服务接口"})
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 保存课程订单信息
     * @param courseId 课程id
     * @param request 当前请求
     * @return Result
     */
    @ApiOperation("保存课程订单信息")
    @PostMapping("/{courseId}")
    public Result saveOrder(@ApiParam("课程id") @PathVariable("courseId") String courseId, HttpServletRequest request) {
        // 保存订单
        String orderNo = orderService.saveOrder(courseId, request);

        return Result.ok().data("orderNo", orderNo);
    }

    /**
     * 根据id获取订单信息
     * @param orderNo 订单号
     * @return Result
     */
    @ApiOperation("根据id获取订单信息")
    @GetMapping("/{orderNo}")
    public Result getOrderById(@ApiParam("订单号") @PathVariable("orderNo") String orderNo) {

        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(orderWrapper);

        return Result.ok().data("order", order);
    }

    /**
     * 获取微信支付二维码
     * @param orderNo 订单号
     * @return Result
     */
    @ApiOperation("获取微信支付二维码")
    @GetMapping("/code/{orderNo}")
    public Result getPayWxCode(@ApiParam("订单号") @PathVariable("orderNo") String orderNo) {

        // 获取二维码url等支付信息
        Map<String, Object> map = orderService.getWxCodeInfo(orderNo);

        return Result.ok().data(map);
    }

    /**
     * 检查订单状态
     * @param courseId 课程id
     * @return Result
     */
    @ApiOperation("检查订单状态")
    @GetMapping("/check/{courseId}")
    public Result checkOrderState(@ApiParam("课程id") @PathVariable("courseId") String courseId, HttpServletRequest request) {
        // 解析token
        Claims claims = JwtUtils.getMemberIdByJwtToken(request);
        if (claims == null) {
            return Result.ok().message("用户未登录");
        }
        // 登录后，获取用户id
        String uid = (String) claims.get("id");

        // 通过课程id和用户id，查询订单状态
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("course_id", courseId);
        orderWrapper.eq("member_id", uid);
        orderWrapper.select("status");
        Order order = orderService.getOne(orderWrapper);
        if (order == null) {
            return Result.ok().message("用户未购买本课程");
        }
        // 存在订单，获取订单状态
        Integer status = order.getStatus();
        return Result.ok().data("status", status);
    }

    /**
     * 订单多组合条件分页查询
     * @param current 当前页
     * @param limit 当页显示数
     * @param orderQuery 查询对象
     * @return Result
     */
    @ApiOperation("订单多组合条件分页查询")
    @PostMapping("/page/{current}/{limit}")
    public Result getOrderQueryPage(@ApiParam("当前页")@PathVariable("current")  Long current,
                                    @ApiParam("当页显示数")@PathVariable("limit")  Long limit,
                                    @ApiParam("查询对象") @RequestBody(required = false) OrderQueryVO orderQuery) {
        if (current < 1 || limit < 1) {
            return Result.error().message("分页参数不合法");
        }

        // 构建分页对象
        Page<Order> orderPage = new Page<>(current, limit);

        // 查询订单分页信息
        orderService.getOrderPage(orderPage, orderQuery);

        // 获取分页结果
        List<Order> orders = orderPage.getRecords();
        long total = orderPage.getTotal();
        return Result.ok().data("orders", orders).data("total", total);
    }

    /**
     * 删除订单
     * @param orderId 订单id
     * @return Result
     */
    @ApiOperation("删除订单")
    @DeleteMapping("/{oid}")
    public Result deleteOrder(@ApiParam("订单id") @PathVariable("oid") String orderId) {

        orderService.removeById(orderId);

        return Result.ok();
    }

}

