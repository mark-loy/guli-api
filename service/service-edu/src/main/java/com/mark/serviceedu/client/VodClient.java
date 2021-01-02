package com.mark.serviceedu.client;

import com.mark.commonutil.entity.Result;
import com.mark.serviceedu.client.impl.VodClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/31 13:36
 */
@FeignClient(value = "service-vod", fallback = VodClientImpl.class)
public interface VodClient {

    /**
     * 远程调用 ==》删除单个视频
     * @param id 视频id
     * @return Result.ok()
     */
    @DeleteMapping("/service/vod/{id}")
    Result deleteVideo(@PathVariable("id") String id);

}
