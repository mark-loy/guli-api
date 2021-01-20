package com.mark.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.wxpay.sdk.WXPayUtil;
import com.mark.commonutil.utils.JwtUtils;
import com.mark.order.client.CourseClient;
import com.mark.order.client.MemberClient;
import com.mark.order.entity.Order;
import com.mark.order.entity.vo.OrderQueryVO;
import com.mark.order.mapper.OrderMapper;
import com.mark.order.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mark.order.utils.HttpClient;
import com.mark.order.utils.OrderConstantUtil;
import com.mark.order.utils.OrderNoUtil;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.servicebase.vo.CourseOrderVO;
import com.mark.servicebase.vo.MemberOrderVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author mark
 * @since 2021-01-09
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private CourseClient courseClient;

    @Resource
    private MemberClient memberClient;

    @Resource
    private RedisTemplate<String, Map<String, Object>> redisTemplate;

    @Override
    public String saveOrder(String courseId, HttpServletRequest request) {
        // 获取课程信息
        CourseOrderVO courseInfo = courseClient.getCourseOrderById(courseId);
        if (courseInfo == null) {
            throw new CustomException(CustomExceptionEnum.SAVE_ORDER_ERROR);
        }

        // 解析token
        Claims claims = JwtUtils.getMemberIdByJwtToken(request);
        if (claims == null) {
            throw new CustomException(CustomExceptionEnum.USER_LOGIN_ERROR);
        }
        // 获取用户id
        String uid = (String) claims.get("id");
        // 获取用户信息
        MemberOrderVO memberInfo = memberClient.getMemberOrder(uid);
        if (memberInfo == null) {
            throw new CustomException(CustomExceptionEnum.SAVE_ORDER_ERROR);
        }

        // 生成订单号
        String orderNo = OrderNoUtil.getOrderNo();

        // 保存订单
        Order order = new Order();
        // 设置订单号
        order.setOrderNo(orderNo);
        // 设置课程信息
        order.setCourseId(courseId)
                .setCourseCover(courseInfo.getCover())
                .setCourseTitle(courseInfo.getTitle())
                .setTotalFee(courseInfo.getPrice())
                .setTeacherName(courseInfo.getTeacherName());
        // 设置用户信息
        order.setMemberId(uid)
                .setNickname(memberInfo.getNickname())
                .setMobile(memberInfo.getMobile());
        // 设置订单状态
        order.setStatus(0);
        // 设置支付方式 1.微信
        order.setPayType(1);

        // 执行保存
        int isInsert = baseMapper.insert(order);
        if (isInsert != 1) {
            throw new CustomException(CustomExceptionEnum.SAVE_ORDER_ERROR);
        }

        return orderNo;
    }

    @Override
    public Map<String, Object> getWxCodeInfo(String orderNo) {
        // 从redis中取支付二维码
        Map<String, Object> redisMap = redisTemplate.opsForValue().get(orderNo);
        if (redisMap != null) {
            return redisMap;
        }

        // 根据订单号获取订单信息
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("order_no", orderNo);
        Order order = baseMapper.selectOne(orderWrapper);
        if (order == null) {
            throw new CustomException(CustomExceptionEnum.SELECT_DATA_ERROR);
        }
        try {
            Map<String, String> orderMap = new HashMap<>();
            //1、设置支付参数
            orderMap.put("appid", OrderConstantUtil.APP_ID);
            orderMap.put("mch_id", OrderConstantUtil.PARTNER);
            orderMap.put("nonce_str", WXPayUtil.generateNonceStr());
            orderMap.put("body", order.getCourseTitle());
            orderMap.put("out_trade_no", orderNo);
            orderMap.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            orderMap.put("spbill_create_ip", "127.0.0.1");
            orderMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            orderMap.put("trade_type", "NATIVE");

            // 构建httpClient对象
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // 设置请求参数
            //    转换map为xml
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(orderMap, OrderConstantUtil.PARTNER_KEY));
            //    设置协议
            httpClient.setHttps(true);
            // 发送post请求
            httpClient.post();
            // 获取返回值
            String content = httpClient.getContent();
            if (StringUtils.isEmpty(content)) {
                throw new CustomException(CustomExceptionEnum.PAY_CODE_ERROR);
            }
            // 转换content类型为map
            Map<String, String> contentMap = WXPayUtil.xmlToMap(content);

            // 结果封装
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("out_trade_no", orderNo);
            resultMap.put("course_id", order.getCourseId());
            resultMap.put("total_fee", order.getTotalFee());
            resultMap.put("result_code", contentMap.get("result_code"));
            resultMap.put("code_url", contentMap.get("code_url"));

            // 微信支付二维码2小时过期
            redisTemplate.opsForValue().set(orderNo, resultMap, 120, TimeUnit.MINUTES);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("请求支付二维码异常：" + e.getMessage());
            throw new CustomException(CustomExceptionEnum.PAY_CODE_ERROR);
        }
    }

    @Override
    public void getOrderPage(Page<Order> orderPage, OrderQueryVO orderQuery) {
        // 构建条件查询对象
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        // 按创建订单时间降序排列
        orderWrapper.orderByDesc("gmt_create");
        // 验证条件非空
        if (!StringUtils.isEmpty(orderQuery.getOrderNo())) {
            orderWrapper.eq("order_no", orderQuery.getOrderNo());
        }
        if (!StringUtils.isEmpty(orderQuery.getOrderNo())) {
            orderWrapper.like("course_title", orderQuery.getCourseTitle());
        }
        if (!StringUtils.isEmpty(orderQuery.getBegin())) {
            orderWrapper.ge("gmt_create", orderQuery.getBegin());
        }
        if (!StringUtils.isEmpty(orderQuery.getEnd())) {
            orderWrapper.le("gmt_create", orderQuery.getEnd());
        }
        if (!StringUtils.isEmpty(orderQuery.getStatus())) {
            orderWrapper.eq("status", orderQuery.getStatus());
        }
        // 执行分页
        baseMapper.selectPage(orderPage, orderWrapper);
    }
}
