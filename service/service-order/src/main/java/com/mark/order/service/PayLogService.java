package com.mark.order.service;

import com.mark.order.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author mark
 * @since 2021-01-09
 */
public interface PayLogService extends IService<PayLog> {

    Map<String, String> checkPayState(String orderNo);

    void savePayLog(Map<String, String> map);
}
