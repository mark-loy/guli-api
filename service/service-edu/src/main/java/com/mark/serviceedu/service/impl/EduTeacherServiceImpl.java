package com.mark.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.serviceedu.entity.EduTeacher;
import com.mark.serviceedu.mapper.EduTeacherMapper;
import com.mark.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
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

    @Override
    @CacheEvict(value = "teacher", allEntries = true)
    public void saveTeacher(EduTeacher eduTeacher) {
        int isSave = baseMapper.insert(eduTeacher);
        if (isSave != 1) {
            throw new CustomException(CustomExceptionEnum.INSERT_DATA_ERROR);
        }
    }

    @Override
    @CacheEvict(value = "teacher", allEntries = true)
    public void deleteTeacher(String id) {
        int isDelete = baseMapper.deleteById(id);
        if (isDelete != 1) {
            throw new CustomException(CustomExceptionEnum.DELETE_DATA_ERROR);
        }
    }

    @Override
    @CacheEvict(value = "teacher", allEntries = true)
    public void updateTeacher(EduTeacher eduTeacher) {
        int isUpdate = baseMapper.updateById(eduTeacher);
        if (isUpdate != 1) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }
    }
}
