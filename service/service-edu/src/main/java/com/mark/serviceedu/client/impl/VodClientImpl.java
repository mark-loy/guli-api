package com.mark.serviceedu.client.impl;

import com.mark.commonutil.entity.Result;
import com.mark.serviceedu.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/31 18:26
 */
@Component
public class VodClientImpl implements VodClient {

    @Override
    public Result deleteVideo(String id) {
        return Result.error().message("删除视频失败");
    }

}
