package com.mark.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.serviceedu.entity.EduTeacher;
import com.mark.serviceedu.mapper.EduTeacherMapper;
import com.mark.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author mark
 * @since 2020-12-18
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    @Cacheable(value = "teacher", key = "'getHotTeacher'")
    public List<EduTeacher> getHotTeacher() {
        // 查询前四条名师
        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByDesc( "gmt_create");
        teacherWrapper.last("limit 4");
        return baseMapper.selectList(teacherWrapper);
    }
}
