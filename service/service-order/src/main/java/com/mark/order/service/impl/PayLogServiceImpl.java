package com.mark.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.mark.order.entity.Order;
import com.mark.order.entity.PayLog;
import com.mark.order.mapper.PayLogMapper;
import com.mark.order.service.OrderService;
import com.mark.order.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mark.order.utils.HttpClient;
import com.mark.order.utils.OrderConstantUtil;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author mark
 * @since 2021-01-09
 */
@Service
@Slf4j
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Resource
    private OrderService orderService;

    @Override
    public Map<String, String> checkPayState(String orderNo) {
        try {
            //1、封装参数
            Map<String, String> m = new HashMap<>();
            m.put("appid", OrderConstantUtil.APP_ID);
            m.put("mch_id", OrderConstantUtil.PARTNER);
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, OrderConstantUtil.PARTNER_KEY));
            client.setHttps(true);
            client.post();

            // 获取请求返回值,并转为map格式
            return WXPayUtil.xmlToMap(client.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("请求支付二维码异常：" + e.getMessage());
            throw new CustomException(CustomExceptionEnum.PAY_CODE_ERROR);
        }
    }

    @Override
    public void savePayLog(Map<String, String> map) {
        // 获取订单号
        String orderNo = map.get("out_trade_no");
        // 根据订单号查询订单信息
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(orderWrapper);

        // 判断订单状态是否为已支付
        if (order.getStatus().equals(1)) {
            return;
        }
        // 修改订单状态
        order.setStatus(1);
        boolean isUpdate = orderService.updateById(order);
        if (!isUpdate) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }

        // 保存支付日志
        PayLog payLog = new PayLog();
        // 设置订单属性
        payLog.setOrderNo(orderNo)
                .setPayTime(new Date())
                .setTotalFee(order.getTotalFee())
                .setPayType(1);
        // 设置交易属性
        payLog.setTradeState(map.get("trade_state")).setTransactionId(map.get("transaction_id"));
        // 设置其它属性
        payLog.setAttr(JSON.toJSONString(map));

        // 执行保存
        int isInsert = baseMapper.insert(payLog);
        if (isInsert != 1) {
            throw new CustomException(CustomExceptionEnum.INSERT_DATA_ERROR);
        }
    }
}
