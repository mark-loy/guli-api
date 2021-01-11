package com.mark.ucenter.mapper;

import com.mark.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author mark
 * @since 2021-01-04
 */
public interface MemberMapper extends BaseMapper<Member> {

    Integer getRegister(String date);
}
