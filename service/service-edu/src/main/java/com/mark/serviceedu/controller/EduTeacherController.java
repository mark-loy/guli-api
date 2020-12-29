package com.mark.serviceedu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.commonutil.entity.Result;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.serviceedu.entity.EduTeacher;
import com.mark.serviceedu.entity.vo.PageTeacherQuery;
import com.mark.serviceedu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
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
@Api(value = "讲师管理", tags = {"讲师服务接口"})
@RestController
@RequestMapping("/service/edu/teacher")
public class EduTeacherController {

    @Resource
    private EduTeacherService teacherService;

    /**
     * 查询所有讲师
     * @return Result.ok().data("teachers", list)
     */
    @ApiOperation(value = "查询所有讲师")
    @GetMapping("/all")
    public Result findAll() {
        List<EduTeacher> list = teacherService.list(null);
        return Result.ok().data("teachers", list);
    }

    /**
     * 逻辑删除讲师
     * @param id 讲师id
     * @return
     */
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("/delete/{id}")
    public Result deleteTeacher(@ApiParam(value = "讲师id", required = true) @PathVariable("id") String id) {
        boolean isRemove = teacherService.removeById(id);
        if (isRemove) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * 多条件组合分页查询
     * @param current 当前页
     * @param size 当页显示数
     * @param pageTeacherQuery 条件
     * @return
     */
    @ApiOperation(value = "多条件组合分页查询")
    @PostMapping("/page/{current}/{size}")
    public Result pageTeacherCondition(@ApiParam(value = "当前页") @PathVariable("current") Integer current,
                                       @ApiParam(value = "当页显示数") @PathVariable("size") Integer size,
                                       @ApiParam(value = "查询条件") @RequestBody(required = false)PageTeacherQuery pageTeacherQuery) {
        // 构建page对象
        Page<EduTeacher> teacherPage = new Page<>(current, size);
        // 构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        // 讲师姓名
        String name = pageTeacherQuery.getName();
        // 讲师级别
        Integer level = pageTeacherQuery.getLevel();
        // 开始时间
        String begin = pageTeacherQuery.getBegin();
        // 结束时间
        String end = pageTeacherQuery.getEnd();

        // 判断条件是否为空
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.le("gmt_create", end);
        }
        // 按开始时间倒序排
        wrapper.orderByDesc("gmt_create");

        // 执行
        teacherService.page(teacherPage, wrapper);

        // 获取总记录数
        long total = teacherPage.getTotal();
        // 获取当页数据
        List<EduTeacher> records = teacherPage.getRecords();
        return Result.ok().data("total", total).data("teachers", records);
    }

    /**
     * 添加讲师
     * @param eduTeacher 讲师对象
     * @return
     */
    @ApiOperation(value = "添加讲师")
    @PostMapping("/add")
    public Result addTeacher(@ApiParam(name = "讲师对象") @RequestBody EduTeacher eduTeacher) {
        boolean isSave = teacherService.save(eduTeacher);
        if (isSave) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * 根据id查询讲师
     * @param id 讲师id
     * @return
     */
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("/{id}")
    public Result getTeacher(@ApiParam(value = "讲师id") @PathVariable("id") String id) {
        EduTeacher teacher = teacherService.getById(id);
        return Result.ok().data("teacher", teacher);
    }

    /**
     * 根据id修改讲师
     * @param eduTeacher 讲师对象
     * @return
     */
    @ApiOperation(value = "根据id修改讲师")
    @PutMapping("/update")
    public Result updateTeacher(@ApiParam(value = "讲师对象") @RequestBody EduTeacher eduTeacher) {
        boolean isUpdate = teacherService.updateById(eduTeacher);
        if (isUpdate) {
            return Result.ok();
        }
        return Result.error();
    }
}

