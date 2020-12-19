package com.mark.serviceedu.controller;


import com.mark.serviceedu.entity.EduTeacher;
import com.mark.serviceedu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author mark
 * @since 2020-12-18
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/service/edu/teacher")
public class EduTeacherController {

    @Resource
    private EduTeacherService eduTeacherService;

    /**
     * 查询所有讲师
     * @return
     */
    @ApiOperation(value = "查询所有讲师")
    @GetMapping("/all")
    public List<EduTeacher> findAll() {
        return eduTeacherService.list(null);
    }

    /**
     * 逻辑删除讲师
     * @param id 讲师id
     * @return
     */
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("/delete/{id}")
    public boolean deleteTeacher(@ApiParam(value = "讲师id", required = true) @PathVariable("id") String id) {
        return  eduTeacherService.removeById(id);
    }
}

