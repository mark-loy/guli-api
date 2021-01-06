package com.mark.serviceedu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.commonutil.entity.Result;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.serviceedu.entity.EduChapter;
import com.mark.serviceedu.entity.EduVideo;
import com.mark.serviceedu.entity.vo.ChapterVO;
import com.mark.serviceedu.service.EduChapterService;
import com.mark.serviceedu.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author mark
 * @since 2020-12-26
 */
@Api(value = "课程章节管理", tags = {"课程章节服务接口"})
@RestController
@RequestMapping("/api/edu/chapter")
public class EduChapterController {

    @Resource
    private EduChapterService chapterService;

    @Resource
    private EduVideoService videoService;

    /**
     * 查询课程章节 ==》tree
     * @param courseId 课程id
     * @return Result.ok().data("chapters", chapters)
     */
    @ApiOperation(value = "查询课程章节")
    @GetMapping("/list/{id}")
    public Result getChapters(@ApiParam(value = "课程id") @PathVariable("id") String courseId) {

        List<ChapterVO> chapters = chapterService.getChapterVideo(courseId);

        return Result.ok().data("chapters", chapters);
    }

    /**
     * 添加章节
     * @param chapter 章节表单
     * @return Result.ok()
     */
    @ApiOperation(value = "添加章节")
    @PostMapping("/save")
    public Result saveChapter(@ApiParam(value = "章节表单") @RequestBody EduChapter chapter) {

        boolean isSave = chapterService.save(chapter);
        if (!isSave) {
            throw new CustomException(CustomExceptionEnum.INSERT_DATA_ERROR);
        }

        return  Result.ok();
    }

    /**
     * 根据id查询章节信息
     * @param id 章节id
     * @return Result.ok().data("chapter", chapter)
     */
    @ApiOperation(value = "根据id查询章节信息")
    @GetMapping("/{id}")
    public Result getChapterById(@ApiParam(value = "章节id") @PathVariable("id") String id) {

        EduChapter chapter = chapterService.getById(id);

        return  Result.ok().data("chapter", chapter);
    }

    /**
     * 修改章节
     * @param chapter 章节表单
     * @return Result.ok()
     */
    @ApiOperation(value = "修改章节")
    @PutMapping("/update")
    public Result updateChapter(@ApiParam(value = "章节表单") @RequestBody EduChapter chapter) {

        boolean isUpdate = chapterService.updateById(chapter);
        if (!isUpdate) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }

        return Result.ok();
    }

    /**
     * 根据id删除章节
     * @param id 章节id
     * @return Result.ok() | Result.error().message("请先删除小节")
     */
    @ApiOperation(value = "根据id删除章节")
    @DeleteMapping("/{id}")
    public Result deleteChapter(@ApiParam(value = "章节id") @PathVariable("id") String id) {

        // 根据章节id，查询是否存在小节
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("chapter_id", id);
        int videoCount = videoService.count(videoWrapper);
        // 判断小节数量
        if (videoCount == 0) {
            // 不存在小节，执行章节删除
            boolean isDelete = chapterService.removeById(id);
            if (!isDelete) {
                throw new CustomException(CustomExceptionEnum.DELETE_DATA_ERROR);
            }
            return Result.ok();
        }
        return Result.error().message("请先删除小节");
    }
}

