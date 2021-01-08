package com.mark.serviceedu.client.impl;

import com.mark.commonutil.entity.Result;
import com.mark.serviceedu.client.UCenterClient;
import org.springframework.stereotype.Component;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/8 20:33
 */
@Component
public class UCenterClientImpl implements UCenterClient {

    @Override
    public Result isExistMember(String id) {
        return Result.error().message("查询用户失败，用户id：" + id);
    }
}
