package com.mark.serviceedu.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.commonutil.entity.Result;
import com.mark.serviceedu.entity.EduCourse;
import com.mark.serviceedu.entity.vo.ChapterVO;
import com.mark.serviceedu.entity.vo.front.CourseDetailVO;
import com.mark.serviceedu.entity.vo.front.CourseQueryVO;
import com.mark.serviceedu.service.EduChapterService;
import com.mark.serviceedu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/7 18:26
 */
@Api(value = "课程页api", tags = {"课程页服务接口"})
@RestController
@RequestMapping("/api/edu/front/course")
public class CourseController {

    @Resource
    private EduCourseService courseService;

    @Resource
    private EduChapterService chapterService;

    /**
     * 课程页：
     *     组合条件分页查询课程信息
     * @param current 当前页
     * @param limit 当页显示数
     * @param courseQueryVO 条件查询对象
     * @return Result
     */
    @ApiOperation(value = "组合条件分页查询课程信息")
    @PostMapping("/{current}/{limit}")
    public Result getCoursePage(@ApiParam(value = "当前页") @PathVariable("current") Integer current,
                                @ApiParam(value = "当页显示数") @PathVariable("limit") Integer limit,
                                @ApiParam(value = "条件查询对象") @RequestBody(required = false) CourseQueryVO courseQueryVO) {
        // 构建分页对象
        Page<EduCourse> coursePage = new Page<>(current, limit);
        // 组合条件分页查询课程信息
        Map<String, Object> courses = courseService.getCoursePageQuery(coursePage, courseQueryVO);

        return Result.ok().data(courses);
    }

    /**
     * 课程详情页：
     *     根据课程id查询课程信息
     * @param id 课程id
     * @return Result
     */
    @ApiOperation(value = "根据课程id查询课程信息")
    @GetMapping("/{id}")
    public Result getCourseById(@ApiParam(value = "课程id") @PathVariable("id") String id) {
        // 查询课程的基本信息
        CourseDetailVO courseDetail = courseService.getBaseCourseInfo(id);

        // 查询该课程的章节，小节信息
        List<ChapterVO> chapterVideo = chapterService.getChapterVideo(id);

        return Result.ok().data("courseBase", courseDetail).data("chapterVideo", chapterVideo);
    }
}
