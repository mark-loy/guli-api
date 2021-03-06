package com.mark.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mark.order.entity.vo.OrderQueryVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author mark
 * @since 2021-01-09
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, HttpServletRequest request);

    Map<String, Object> getWxCodeInfo(String orderNo);

    void getOrderPage(Page<Order> orderPage, OrderQueryVO orderQuery);
}
