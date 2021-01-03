package com.mark.serviceedu.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.commonutil.entity.Result;
import com.mark.serviceedu.entity.EduCourse;
import com.mark.serviceedu.entity.EduTeacher;
import com.mark.serviceedu.service.EduCourseService;
import com.mark.serviceedu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bouncycastle.crypto.engines.TEAEngine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/2 14:00
 */
@Api(value = "首页管理", tags = {"首页服务接口"})
@RestController
@RequestMapping("/service/edu/front/index")
public class IndexController {

    @Resource
    private EduCourseService courseService;

    @Resource
    private EduTeacherService teacherService;

    /**
     * 查询首页的热门课程和名师
     * @return Result.ok().data("courses", courses).data("teachers", teachers)
     */
    @ApiOperation(value = "查询首页的热门课程和名师")
    @GetMapping("/all")
    public Result getCourseTeacher() {

        List<EduCourse> courses = courseService.getHotCourse();

        List<EduTeacher> teachers = teacherService.getHotTeacher();

        return Result.ok().data("courses", courses).data("teachers", teachers);
    }
}
