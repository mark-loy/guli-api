package com.mark.serviceedu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.serviceedu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mark.serviceedu.entity.vo.CourseBasicVO;
import com.mark.serviceedu.entity.vo.CoursePublishVo;
import com.mark.serviceedu.entity.vo.PageCourseQuery;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author mark
 * @since 2020-12-26
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseBasicVO courseBasicVO);

    CoursePublishVo getCoursePublish(String id);

    void pageQuery(Page<EduCourse> coursePage, PageCourseQuery courseQuery);

    void deleteCourseRelated(String id);
}
