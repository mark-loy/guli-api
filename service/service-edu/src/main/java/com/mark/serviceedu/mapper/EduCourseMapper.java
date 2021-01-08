package com.mark.serviceedu.mapper;

import com.mark.serviceedu.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mark.serviceedu.entity.vo.CoursePublishVo;
import com.mark.serviceedu.entity.vo.front.CourseDetailVO;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author mark
 * @since 2020-12-26
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    /**
     * 根据id，获取课程发布信息
     * @param id 课程id
     * @return CoursePublishVo 课程发布实体
     */
    CoursePublishVo getCoursePublish(String id);

    /**
     * 根据id，获取课程详情页信息
     * @param id 课程信息
     * @return CourseDetailVO
     */
    CourseDetailVO getBaseCourseInfo(String id);
}
