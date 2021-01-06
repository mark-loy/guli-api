package com.mark.ucenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.commonutil.utils.JwtUtils;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.ucenter.entity.Member;
import com.mark.ucenter.service.MemberService;
import com.mark.ucenter.service.WxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/6 11:11
 */
@Api(value = "用户微信登录", tags = {"用户微信登录服务接口"})
@Controller
@RequestMapping("/api/ucenter/wx")
public class MemberWxController {

    @Resource
    private WxService wxService;

    @Resource
    private MemberService memberService;

    /**
     * 微信登陆：第一步
     *     获取code,生成微信二维码
     * @return String
     */
    @ApiOperation(value = "获取code,生成微信二维码")
    @GetMapping("/code")
    public String getWxCode(HttpServletRequest request) {
        String url = wxService.getWxCode(request);
        return "redirect:" + url;
    }

    /**
     * 微信登录：第二步
     *      实现扫码登录
     * @param code 随机值
     * @param state 状态
     * @return
     */
    @ApiOperation(value = "实现扫码登录")
    @GetMapping("/callback")
    public String getAccessToken(@RequestParam("code") String code,
                                 @RequestParam("state") String state) {
        // 通过第一步的code,获取token，openid
        HashMap tokenMap = wxService.getAccessToken(code, state);
        String accessToken = (String) tokenMap.get("access_token");
        String openid = (String) tokenMap.get("openid");

        // 通过token，获取用户信息
        HashMap userMap =  wxService.getWxUser(accessToken, openid);
        // 获取微信昵称
        String nickname = (String) userMap.get("nickname");
        // 获取用户性别
        Integer sex = (Integer) userMap.get("sex");
        // 获取用户头像
        String avatar = (String) userMap.get("headimgurl");

        // 根据openid查询库中是否存在该微信用户
        QueryWrapper<Member> memberQuery = new QueryWrapper<>();
        memberQuery.eq("openid", openid);
        Member baseMember = memberService.getOne(memberQuery);
        if (baseMember == null) {
            // 库中不存在该用户，新增该用户信息
            // 保存用户信息到库中
            baseMember = new Member();
            baseMember.setOpenid(openid);
            baseMember.setNickname(nickname);
            baseMember.setSex(sex);
            baseMember.setAvatar(avatar);
            // 执行保存
            boolean isSave = memberService.save(baseMember);
            if (!isSave) {
                throw new CustomException(CustomExceptionEnum.INSERT_DATA_ERROR);
            }
        } else {
            // 存在该用户，则更新用户信息
            baseMember.setNickname(nickname);
            baseMember.setSex(sex);
            baseMember.setAvatar(avatar);
            // 执行更新
            boolean isUpdate = memberService.update(baseMember, memberQuery);
            if (!isUpdate) {
                throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
            }
        }

        // 颁发token，设置有效载荷
        Map<String, Object> map = new HashMap<>();
        // 设置用户id
        map.put("id", baseMember.getId());
        // 设置手机号
        map.put("mobile", baseMember.getMobile());
        // 设置用户昵称
        map.put("nickname", baseMember.getNickname());
        // 设置用户头像
        map.put("avatar", baseMember.getAvatar());
        // 设置签名
        map.put("sign", baseMember.getSign());
        // 设置年龄
        map.put("age", baseMember.getAge());
        // 设置性别
        map.put("sex", baseMember.getSex());

        // 生成token
        String jwtToken = JwtUtils.getJwtToken(map);

        // 重定向到首页
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }
}
