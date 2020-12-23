package com.mark.serviceedu.controller;

import com.mark.commonutil.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/21 16:25
 */
@Api(value = "后台登录", tags = {"后台登录接口"})
@RestController
@RequestMapping("/service/edu/admin")
public class EduLoginController {

    /**
     * 后台管理员登录
     * @return
     */
    @ApiOperation(value = "后台管理员登录")
    @PostMapping("/login")
    public Result login() {
        return Result.ok().data("token", "token");
    }

    /**
     * 管理员信息查询
     * @return
     */
    @ApiOperation(value = "管理员信息查询")
    @GetMapping("/info")
    public Result info() {

        return Result.ok().data("roles", "admin")
                .data("name", "MK")
                .data("avatar", "https://p.qqan.com/up/2020-12/16074977589094008.jpg");
    }
}
