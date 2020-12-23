package com.mark.serviceoss.controller;

import com.mark.commonutil.entity.Result;
import com.mark.serviceoss.service.OSSService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/23 10:56
 */
@Api(value = "对象存储管理", tags = {"对象存储服务接口"})
@RestController
@RequestMapping("/service/oss")
public class OSSController {

    @Resource
    private OSSService ossService;

    @ApiOperation(value = "文件上传api")
    @PostMapping("/fileupload")
    public Result fileUpload(@ApiParam(value = "文件") @RequestParam("file") MultipartFile multipartFile) {

        String url = ossService.fileUpload(multipartFile);

        return Result.ok().data("url", url);
    }

}
