package com.mark.order.client.impl;

import com.mark.order.client.MemberClient;
import com.mark.servicebase.vo.MemberOrderVO;
import org.springframework.stereotype.Component;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/9 16:40
 */
@Component
public class MemberClientImpl implements MemberClient {
    @Override
    public MemberOrderVO getMemberOrder(String uid) {
        return null;
    }
}
