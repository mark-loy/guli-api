package com.mark.ucenter.controller;


import com.mark.commonutil.entity.Result;
import com.mark.commonutil.utils.JwtUtils;
import com.mark.ucenter.entity.Member;
import com.mark.ucenter.entity.vo.LoginVo;
import com.mark.ucenter.entity.vo.MemberVo;
import com.mark.ucenter.entity.vo.RegisteredVo;
import com.mark.ucenter.service.MemberService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author mark
 * @since 2021-01-04
 */
@Api(value = "用户管理", tags = {"用户中心服务接口"})
@RestController
@RequestMapping("/service/ucenter")
public class MemberController {

    @Resource
    private MemberService memberService;

    /**
     * 用户登录
     * @param loginVo 登录表单
     * @return Result.ok().data("token", token);
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public Result toLogin(@ApiParam(value = "登录表单") @RequestBody LoginVo loginVo) {
        if (loginVo == null) {
            return Result.error().message("登录表单不能为空");
        }
        // 调用登录方法
        String token = memberService.toLogin(loginVo);
        return Result.ok().data("token", token);
    }

    /**
     * 用户注册
     * @param registeredVo 注册表单
     * @return Result.ok();
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public Result toRegister(@ApiParam(value = "注册表单") @RequestBody RegisteredVo registeredVo) {
        if (registeredVo == null) {
            return Result.error().message("注册表单不能为空");
        }
        // 调用注册方法
        memberService.toRegister(registeredVo);
        return Result.ok();
    }

    /**
     * 解析token，获取用户信息
     * @param request request请求
     * @return Result
     */
    @ApiOperation(value = "解析token，获取用户信息")
    @GetMapping("/member")
    public Result getMemberByToken(HttpServletRequest request) {
        // 解析token，获取载荷
        Claims claims = JwtUtils.getMemberIdByJwtToken(request);
        if (claims == null) {
            return Result.error().message("token不存在");
        }
        // 构造返回的memberVo对象
        MemberVo memberVo = new MemberVo();
        memberVo.setId((String)claims.get("id"));
        memberVo.setMobile((String)claims.get("mobile"));
        memberVo.setNickname((String)claims.get("nickname"));
        memberVo.setSex((Integer)claims.get("sex"));
        memberVo.setAge((Integer)claims.get("age"));
        memberVo.setAvatar((String)claims.get("avatar"));
        memberVo.setSign((String)claims.get("sign"));
        return Result.ok().data("member", memberVo);
    }
}

