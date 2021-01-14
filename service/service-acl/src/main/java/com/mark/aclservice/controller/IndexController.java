package com.mark.aclservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.mark.aclservice.service.IndexService;
import com.mark.commonutil.entity.Result;
import io.swagger.annotations.Api;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 木可
 */
@Api(value = "登录管理", tags = {"用户登录服务接口"})
@RestController
@RequestMapping("/api/acl/index")
public class IndexController {

    @Resource
    private IndexService indexService;

    @GetMapping("/info")
    public Result info(){
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> userInfo = indexService.getUserInfo(username);
        return Result.ok().data(userInfo);
    }

    @GetMapping("/menu")
    public Result getMenu(){
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JSONObject> permissionList = indexService.getMenu(username);
        return Result.ok().data("permissionList", permissionList);
    }

    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }

}
