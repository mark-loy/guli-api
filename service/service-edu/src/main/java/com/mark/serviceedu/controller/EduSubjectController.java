package com.mark.serviceedu.controller;


import com.mark.commonutil.entity.Result;
import com.mark.serviceedu.entity.subject.OneSubjectClassify;
import com.mark.serviceedu.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author mark
 * @since 2020-12-23
 */
@Api(value = "课程分类管理", tags = {"课程分类服务接口"})
@RestController
@RequestMapping("/api/edu/subject")
public class EduSubjectController {

    @Resource
    private EduSubjectService subjectService;

    /**
     * 上传excel添加课程分类
     * @param multipartFile excel
     * @return Result.ok()
     */
    @ApiOperation(value = "添加课程分类")
    @PostMapping("/save")
    public Result saveSubjectByExcel(@ApiParam("excel") @RequestParam("file")MultipartFile multipartFile) {
        subjectService.saveSubjectByExcel(multipartFile, subjectService);
        return Result.ok();
    }

    /**
     * 查询课程分类
     * @return Result.ok()
     */
    @ApiOperation(value = "查询课程分类")
    @GetMapping("/list")
    public Result getSubjectClassify() {
        List<OneSubjectClassify> oneList =  subjectService.getSubjectByTree();
        return Result.ok().data("subjectClassify", oneList);
    }

}

