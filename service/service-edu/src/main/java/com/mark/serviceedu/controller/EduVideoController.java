package com.mark.serviceedu.controller;


import com.mark.commonutil.entity.Result;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.serviceedu.client.VodClient;
import com.mark.serviceedu.entity.EduVideo;
import com.mark.serviceedu.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author mark
 * @since 2020-12-26
 */
@Api(value = "课程小节管理", tags = {"课程小节服务接口"})
@RestController
@RequestMapping("/service/edu/video")
public class EduVideoController {

    @Resource
    private EduVideoService videoService;

    @Resource
    private VodClient vodClient;

    /**
     * 保存课程小节
     * @param video 课程小节表单
     * @return Result.ok()
     */
    @ApiOperation(value = "保存课程小节")
    @PostMapping("/save")
    public Result saveVideo(@ApiParam(value = "课程小节表单") @RequestBody EduVideo video) {

        boolean isSave = videoService.save(video);
        if (!isSave) {
            throw new CustomException(CustomExceptionEnum.INSERT_DATA_ERROR);
        }

        return Result.ok();
    }

    /**
     * 根据id查询小节
     * @param id 小节id
     * @return Result.ok().data("video", video)
     */
    @ApiOperation(value = "根据id查询小节")
    @GetMapping("/{id}")
    public Result getVideoById(@ApiParam(value = "小节id") @PathVariable("id") String id) {

        EduVideo video = videoService.getById(id);

        return Result.ok().data("video", video);
    }

    /**
     * 修改课程小节
     * @param video 小节表单
     * @return Result.ok()
     */
    @ApiOperation(value = "修改课程小节")
    @PutMapping("/update")
    public Result updateVideo(@ApiParam(value = "小节表单") @RequestBody EduVideo video) {

        boolean isUpdate = videoService.updateById(video);
        if (!isUpdate) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }

        return Result.ok();
    }

    /**
     * 根据id删除小节
     * @param id 小节id
     * @return Result.ok()
     */
    @ApiOperation(value = "根据id删除小节")
    @DeleteMapping("/{id}")
    public Result deleteVideo(@ApiParam(value = "小节id") @PathVariable("id") String id) {

        // 根据小节id，查询视频id
        EduVideo video = videoService.getById(id);
        // 获取视频id
        String videoSourceId = video.getVideoSourceId();
        // 判断视频id
        if (!StringUtils.isEmpty(videoSourceId)) {
            // 存在视频id，删除视频
            Result result = vodClient.deleteVideo(videoSourceId);
            if (!result.getSuccess()) {
                // 删除视频失败
                throw new CustomException(result.getCode(), result.getMessage());
            }
        }

        // 删除小节
        boolean isDelete = videoService.removeById(id);
        if (!isDelete) {
            throw new CustomException(CustomExceptionEnum.DELETE_DATA_ERROR);
        }

        return Result.ok();
    }
}

