package com.mark.serviceedu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.serviceedu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mark.serviceedu.entity.vo.CourseBasicVO;
import com.mark.serviceedu.entity.vo.CoursePublishVo;
import com.mark.serviceedu.entity.vo.PageCourseQuery;
import com.mark.serviceedu.entity.vo.front.CourseDetailVO;
import com.mark.serviceedu.entity.vo.front.CourseQueryVO;

import java.util.List;
import java.util.Map;

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

    List<EduCourse> getHotCourse();

    void updateCourseStatus(String id);

    void updateCourse(CourseBasicVO courseBasicVO);

    Map<String, Object> getCoursePageQuery(Page<EduCourse> coursePage, CourseQueryVO courseQueryVO);

    CourseDetailVO getBaseCourseInfo(String id);
}
