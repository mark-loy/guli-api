package com.mark.serviceedu.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.commonutil.entity.Result;
import com.mark.serviceedu.entity.EduCourse;
import com.mark.serviceedu.entity.EduTeacher;
import com.mark.serviceedu.service.EduCourseService;
import com.mark.serviceedu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/7 15:02
 */
@Api(value = "名师页api", tags = {"名师页服务接口"})
@RestController
@RequestMapping("/api/edu/front/teacher")
public class TeacherController {

    @Resource
    private EduTeacherService teacherService;

    @Resource
    private EduCourseService courseService;

    /**
     * 前台名师页：
     *     分页查询名师信息
     * @param current 当前页
     * @param limit 当页显示数
     * @return Result
     */
    @ApiOperation(value = "分页查询名师信息")
    @GetMapping("/{current}/{limit}")
    public Result getTeachers(@ApiParam(value = "当前页") @PathVariable("current") Integer current,
                                   @ApiParam(value = "当页显示数") @PathVariable("limit") Integer limit) {
        // 构建page对象
        Page<EduTeacher> teacherPage = new Page<>(current, limit);
        // 构建查询条件对象
        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByDesc("gmt_create");
        // 执行分页
        teacherService.page(teacherPage, teacherWrapper);

        // 封装结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("teachers", teacherPage.getRecords());
        resultMap.put("total", teacherPage.getTotal());
        resultMap.put("size", teacherPage.getSize());
        resultMap.put("current", teacherPage.getCurrent());
        resultMap.put("pages", teacherPage.getPages());
        resultMap.put("hasNext", teacherPage.hasNext());
        resultMap.put("hasPrevious", teacherPage.hasPrevious());

        return Result.ok().data(resultMap);
    }

    /**
     * 名师页
     *     根据id获取名师详情信息，和课程信息
     * @param id 名师id
     * @return Result
     */
    @ApiOperation(value = "根据id获取名师详情信息，和课程信息")
    @GetMapping("/{id}")
    public Result getTeacherById(@ApiParam(value = "名师id") @PathVariable("id") String id) {

        // 1.查询名师的基本信息
        EduTeacher teacher = teacherService.getById(id);

        // 2.查询该名师所讲的课程
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.eq("teacher_id", id);
        List<EduCourse> courses = courseService.list(courseWrapper);

        return Result.ok().data("teacher", teacher).data("courses", courses);
    }
}
