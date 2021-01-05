package com.mark.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.commonutil.utils.JwtUtils;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.ucenter.entity.Member;
import com.mark.ucenter.entity.vo.LoginVo;
import com.mark.ucenter.entity.vo.RegisteredVo;
import com.mark.ucenter.mapper.MemberMapper;
import com.mark.ucenter.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author mark
 * @since 2021-01-04
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String toLogin(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // 判断登录表单字段非空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new CustomException(CustomExceptionEnum.FORM_DATA_ERROR);
        }

        // 根据手机号查询该用户
        QueryWrapper<Member> memberQuery = new QueryWrapper<>();
        memberQuery.eq("mobile", mobile);
        Member member = baseMapper.selectOne(memberQuery);
        // 判断member是否为空
        if (member == null) {
            throw new CustomException(CustomExceptionEnum.MOBILE_ERROR);
        }

        // 不为空，验证密码，密码需进行MD5加密后验证
        //   对密码进行MD5加密
        String mdPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        //   判断密码是否正确
        if (!mdPassword.equals(member.getPassword())) {
            throw new CustomException(CustomExceptionEnum.PASSWORD_ERROR);
        }

        // 判断该用户账号是否有效
        if (member.getIsDisabled()) {
            throw new CustomException(CustomExceptionEnum.USER_DISABLED);
        }

        // 颁发token，设置有效载荷
        Map<String, Object> map = new HashMap<>();
        // 设置用户id
        map.put("id", member.getId());
        // 设置手机号
        map.put("mobile", member.getMobile());
        // 设置用户昵称
        map.put("nickname", member.getNickname());
        // 设置用户头像
        map.put("avatar", member.getAvatar());
        // 设置签名
        map.put("sign", member.getSign());
        // 设置年龄
        map.put("age", member.getAge());
        // 设置性别
        map.put("sex", member.getSex());
        // 获取token，返回token
        return JwtUtils.getJwtToken(map);
    }

    @Override
    public void toRegister(RegisteredVo registeredVo) {
        String nickName = registeredVo.getNickName();
        String mobile = registeredVo.getMobile();
        String password = registeredVo.getPassword();
        String code = registeredVo.getCode();
        // 判断注册表单字段非空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
            throw new CustomException(CustomExceptionEnum.FORM_DATA_ERROR);
        }
        // 从redis中获取验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        // 判断验证码非空
        if (StringUtils.isEmpty(redisCode)) {
            throw new CustomException(CustomExceptionEnum.CODE_TIMEOUT);
        }
        // 判断验证码是否正确
        if (!code.equals(redisCode)) {
            throw new CustomException(CustomExceptionEnum.CODE_ERROR);
        }

        // 查询库中是否存在该手机号
        QueryWrapper<Member> memberQuery = new QueryWrapper<>();
        memberQuery.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(memberQuery);
        // 判断记录数
        if (count > 0) {
            throw new CustomException(CustomExceptionEnum.USER_REGISTERED);
        }

        // 将用户注册信息保存到数据库
        //   将密码加密
        String mdPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        Member member = new Member();
        member.setNickname(nickName);
        member.setMobile(mobile);
        member.setPassword(mdPassword);
        // 执行插入
        int isInsert = baseMapper.insert(member);
        if (isInsert != 1) {
            throw new CustomException(CustomExceptionEnum.INSERT_DATA_ERROR);
        }
    }
}
