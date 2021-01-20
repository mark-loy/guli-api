package com.mark.servicecms.client;

import com.mark.commonutil.entity.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/20 16:21
 */
@FeignClient(value = "service-oss")
@Component
public interface OssClient {

    /**
     * 删除上传文件
     * @param filename 路径 + 文件名
     * @return Result
     */
    @DeleteMapping("/api/oss/")
    Result deleteFile(@RequestParam("filename") String filename);
}
