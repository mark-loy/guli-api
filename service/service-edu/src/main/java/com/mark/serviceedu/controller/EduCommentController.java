package com.mark.serviceedu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.commonutil.entity.Result;
import com.mark.serviceedu.client.UCenterClient;
import com.mark.serviceedu.entity.EduComment;
import com.mark.serviceedu.entity.EduCourse;
import com.mark.serviceedu.service.EduCommentService;
import com.mark.serviceedu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author mark
 * @since 2021-01-08
 */
@Api(value = "评论管理", tags = {"评论服务接口"})
@RestController
@RequestMapping("/api/edu/comment")
public class EduCommentController {

    @Resource
    private EduCommentService commentService;

    @Resource
    private UCenterClient uCenterClient;

    @Resource
    private EduCourseService courseService;

    /**
     * 添加评论
     * @param comment 评论实体
     * @return Result
     */
    @ApiOperation("添加评论")
    @PostMapping("/save")
    public Result saveComment(@ApiParam("评论实体") @RequestBody EduComment comment) {

        // 查询评论人是否存在
        Result existMember = uCenterClient.isExistMember(comment.getMemberId());
        // 判断调用结果
        if (!existMember.getSuccess()) {
            return Result.error().message("评论人不存在");
        }

        // 判断课程、讲师是否存在
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.eq("id", comment.getCourseId());
        courseWrapper.eq("teacher_id", comment.getTeacherId());
        int courseCount = courseService.count(courseWrapper);
        if (courseCount == 0) {
            return Result.error().message("课程或讲师不存在");
        }

        // 执行添加
        boolean isSave = commentService.save(comment);
        if (!isSave) {
            return Result.error().message("添加用户评论失败");
        }

        return Result.ok();
    }

    /**
     * 分页查询评论
     * @param current 当前页
     * @param limit 当页显示数
     * @return Result
     */
    @ApiOperation("分页查询评论")
    @GetMapping("/{current}/{limit}")
    public Result getCommentPage(@ApiParam("当前页") @PathVariable("current") Long current,
                                 @ApiParam("当页显示数") @PathVariable("limit") Long limit) {
        if (current < 1 || limit < 1) {
            return Result.error().message("请填写正确的分页参数");
        }
        // 构建分页对象
        Page<EduComment> commentPage = new Page<>(current, limit);
        // 构建条件查询对象
        QueryWrapper<EduComment> commentWrapper = new QueryWrapper<>();
        // 按创建时间排序
        commentWrapper.orderByDesc("gmt_create");
        // 执行分页
        commentService.page(commentPage, commentWrapper);

        // 封装结果
        List<EduComment> comments = commentPage.getRecords();
        long total = commentPage.getTotal();
        return Result.ok().data("comments", comments).data("total", total);
    }
}

