package com.mark.servicevod.controller;

import com.aliyuncs.exceptions.ClientException;
import com.mark.commonutil.entity.Result;
import com.mark.servicevod.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/30 11:36
 */
@Api(value = "视频管理", tags = {"视频服务接口"})
@RestController
@RequestMapping("/api/vod")
@CrossOrigin
public class VodController {

    @Resource
    private VodService vodService;

    /**
     * 上传视频
     * @param file 视频文件
     * @return Result.ok().data("videoId", videoId)
     * @throws IOException IO异常
     */
    @ApiOperation(value = "上传视频")
    @PostMapping("/upload")
    public Result videoUpload(@ApiParam(value = "视频文件") @RequestParam("file")MultipartFile file) throws IOException {

        String videoId = vodService.upload(file);

        return Result.ok().data("videoId", videoId);
    }

    /**
     * 删除单个视频
     * @param id 视频id
     * @return Result.ok()
     */
    @ApiOperation(value = "删除单个视频")
    @DeleteMapping("/{id}")
    public Result deleteVideo(@ApiParam(value = "视频id") @PathVariable("id") String id) throws ClientException {

        vodService.deleteVideo(id);

        return Result.ok();
    }

    /**
     * 删除多个视频
     * @param videoIds 视频id
     * @return Result.ok()
     * @throws ClientException
     */
    @ApiOperation(value = "删除多个视频")
    @DeleteMapping("/delete/batch")
    public Result deleteVideoBatch(@ApiParam(value = "视频id") @RequestParam("videoIds")List<String> videoIds) throws ClientException {
        // 转换list类型 如：1,2,3
        String ids = StringUtils.join(videoIds.toArray(), ",");
        // 调用删除视频
        vodService.deleteVideo(ids);
        return Result.ok();
    }
}
