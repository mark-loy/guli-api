package com.mark.ucenter.service;

import com.mark.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mark.ucenter.entity.vo.LoginVo;
import com.mark.ucenter.entity.vo.RegisteredVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author mark
 * @since 2021-01-04
 */
public interface MemberService extends IService<Member> {

    String toLogin(LoginVo loginVo);

    void toRegister(RegisteredVo registeredVo);

    Integer getRegister(String date);
}
