package com.mark.serviceedu.service;

import com.mark.serviceedu.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author mark
 * @since 2020-12-18
 */
public interface EduTeacherService extends IService<EduTeacher> {

    List<EduTeacher> getHotTeacher();

    void saveTeacher(EduTeacher eduTeacher);

    void deleteTeacher(String id);

    void updateTeacher(EduTeacher eduTeacher);
}
