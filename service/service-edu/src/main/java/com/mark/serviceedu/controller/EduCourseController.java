package com.mark.serviceedu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.commonutil.entity.Result;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.serviceedu.entity.EduCourse;
import com.mark.serviceedu.entity.EduCourseDescription;
import com.mark.serviceedu.entity.vo.CourseBasicVO;
import com.mark.serviceedu.entity.vo.CoursePublishVo;
import com.mark.serviceedu.entity.vo.PageCourseQuery;
import com.mark.serviceedu.service.EduCourseDescriptionService;
import com.mark.serviceedu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
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
@Api(value = "课程管理", tags = {"课程服务接口"})
@RestController
@RequestMapping("/service/edu/course")
public class EduCourseController {

    @Resource
    private EduCourseService courseService;

    @Resource
    private EduCourseDescriptionService descriptionService;

    /**
     * 添加课程基本信息
     * @param courseBasicVO 课程基本信息表单
     * @return Result.ok().data("courseId", courseId)
     */
    @ApiOperation(value = "添加课程基本信息")
    @PostMapping("/save")
    public Result saveCourseInfo(@ApiParam(value = "课程基本信息表单") @RequestBody CourseBasicVO courseBasicVO) {

        String courseId = courseService.saveCourseInfo(courseBasicVO);

        return Result.ok().data("courseId", courseId);
    }

    /**
     * 根据id查询课程信息
     * @param id 课程id
     * @return Result.ok().data("course", resultCourse)
     */
    @ApiOperation(value = "根据id查询课程信息")
    @GetMapping("/{id}")
    public Result getCourseById(@ApiParam("课程id") @PathVariable("id") String id) {
        // 查询课程信息
        EduCourse eduCourse = courseService.getById(id);
        if (eduCourse == null) {
            throw new CustomException(CustomExceptionEnum.SELECT_DATA_ERROR);
        }
        CourseBasicVO resultCourse = new CourseBasicVO();
        BeanUtils.copyProperties(eduCourse, resultCourse);

        // 查询课程描述信息
        EduCourseDescription eduDescription = descriptionService.getById(id);
        if (eduDescription == null) {
            throw new CustomException(CustomExceptionEnum.SELECT_DATA_ERROR);
        }
        BeanUtils.copyProperties(eduDescription, resultCourse);

        return Result.ok().data("course", resultCourse);
    }

    /**
     * 修改课程信息
     * @param courseBasicVO 课程信息表单
     * @return Result.ok()
     */
    @ApiOperation(value = "修改课程信息")
    @PutMapping("/update")
    public Result updateCourseById(@ApiParam(value = "课程信息表单") @RequestBody CourseBasicVO courseBasicVO) {
        // 修改课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseBasicVO, eduCourse);
        boolean isCourseUpdate = courseService.updateById(eduCourse);
        if (!isCourseUpdate) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }

        // 修改课程描述信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseBasicVO, eduCourseDescription);
        boolean isDescriptionUpdate = descriptionService.updateById(eduCourseDescription);
        if (!isDescriptionUpdate) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }

        return Result.ok();
    }

    /**
     * 获取课程发布信息
     * @param id 课程id
     * @return Result.ok().data("coursePublish", coursePublish)
     */
    @ApiOperation(value = "获取课程发布信息")
    @GetMapping("/publish/{id}")
    public Result getCoursePublish(@ApiParam(value = "课程id") @PathVariable("id") String id) {

        CoursePublishVo coursePublish = courseService.getCoursePublish(id);

        return Result.ok().data("coursePublish", coursePublish);
    }

    /**
     * 修改课程状态
     * @param id 课程id
     * @return Result.ok()
     */
    @ApiOperation(value = "修改课程状态")
    @PutMapping("/update/{id}")
    public Result updateCourseStatus(@ApiParam(value = "课程id") @PathVariable("id") String id) {

        EduCourse course = new EduCourse();
        course.setId(id);
        // 设置课程状态
        course.setStatus("Normal");
        boolean isUpdate = courseService.updateById(course);
        if (!isUpdate) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }
        return Result.ok();
    }

    /**
     * 多组合条件分页查询课程
     * @param current 当前页
     * @param size 当页显示数
     * @param courseQuery 条件对象
     * @return Result.ok().data("total", total).data("courses", courses)
     */
    @ApiOperation(value = "多组合条件分页查询课程")
    @PostMapping("/page/{current}/{size}")
    public Result getCoursePageQuery(@ApiParam(value = "当前页") @PathVariable("current") Integer current,
                                     @ApiParam(value = "当页显示数") @PathVariable("size") Integer size,
                                     @ApiParam(value = "条件对象") @RequestBody(required = false) PageCourseQuery courseQuery) {

        // 构建课程分页对象
        Page<EduCourse> coursePage = new Page<>(current, size);

        courseService.pageQuery(coursePage, courseQuery);

        // 获取分页记录
        List<EduCourse> courses = coursePage.getRecords();
        // 获取总记录数
        long total = coursePage.getTotal();

        return Result.ok().data("total", total).data("courses", courses);
    }

    /**
     * 删除课程
     * @param id 课程id
     * @return Result.ok()
     */
    @ApiOperation(value = "删除课程")
    @DeleteMapping("/{id}")
    public Result deleteCourse(@ApiParam(value = "课程id") @PathVariable("id") String id) {

        courseService.deleteCourseRelated(id);

        return Result.ok();
    }
}

