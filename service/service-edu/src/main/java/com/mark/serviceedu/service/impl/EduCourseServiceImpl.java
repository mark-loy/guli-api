package com.mark.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.serviceedu.entity.EduChapter;
import com.mark.serviceedu.entity.EduCourse;
import com.mark.serviceedu.entity.EduCourseDescription;
import com.mark.serviceedu.entity.EduVideo;
import com.mark.serviceedu.entity.vo.CourseBasicVO;
import com.mark.serviceedu.entity.vo.CoursePublishVo;
import com.mark.serviceedu.entity.vo.PageCourseQuery;
import com.mark.serviceedu.mapper.EduCourseMapper;
import com.mark.serviceedu.service.EduChapterService;
import com.mark.serviceedu.service.EduCourseDescriptionService;
import com.mark.serviceedu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mark.serviceedu.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author mark
 * @since 2020-12-26
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Resource
    private EduCourseDescriptionService descriptionService;

    @Resource
    private EduVideoService videoService;

    @Resource
    private EduChapterService chapterService;


    @Override
    @CacheEvict(value = "course", allEntries = true)
    public String saveCourseInfo(CourseBasicVO courseBasicVO) {
        // 向课程表中插入数据
        EduCourse eduCourse = new EduCourse();
        // 给EduCourse中的属性赋值
        BeanUtils.copyProperties(courseBasicVO, eduCourse);
        // 执行insert
        int isCourseInsert = baseMapper.insert(eduCourse);
        if (isCourseInsert != 1) {
            throw new CustomException(CustomExceptionEnum.INSERT_DATA_ERROR);
        }

        // 获取课程id
        String courseId = eduCourse.getId();

        // 向课程描述表中插入数据
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        // 设置描述
        eduCourseDescription.setDescription(courseBasicVO.getDescription());
        // 设置id ==> 课程描述表的id与课程表的id一致
        eduCourseDescription.setId(courseId);
        // 执行insert
        boolean isDescriptionInsert = descriptionService.save(eduCourseDescription);
        if (!isDescriptionInsert) {
            throw new CustomException(CustomExceptionEnum.INSERT_DATA_ERROR);
        }
        return courseId;
    }

    @Override
    public CoursePublishVo getCoursePublish(String id) {
        return baseMapper.getCoursePublish(id);
    }

    @Override
    public void pageQuery(Page<EduCourse> coursePage, PageCourseQuery courseQuery) {
        // 构建课程查询条件对象
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        // 设置按时间倒序排序
        courseQueryWrapper.orderByDesc("gmt_create");
        // 判断课程查询对象
        if (courseQuery == null) {
            // 执行分页查询
            baseMapper.selectPage(coursePage, courseQueryWrapper);
            return;
        }

        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();

        // 判断查询条件是否为空
        if (!StringUtils.isEmpty(title)) {
            courseQueryWrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(status)) {
            courseQueryWrapper.eq("status", status);
        }

        // 执行分页查询
        baseMapper.selectPage(coursePage, courseQueryWrapper);
    }

    @Override
    @CacheEvict(value = "course", allEntries = true)
    public void deleteCourseRelated(String id) {

        // 删除小节
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", id);
        boolean isVideo = videoService.remove(videoWrapper);
        if (!isVideo) {
            throw new CustomException(CustomExceptionEnum.DELETE_DATA_ERROR);
        }
        // 删除章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", id);
        boolean isChapter = chapterService.remove(chapterWrapper);
        if (!isChapter) {
            throw new CustomException(CustomExceptionEnum.DELETE_DATA_ERROR);
        }
        // 删除描述
        boolean isDescription = descriptionService.removeById(id);
        if (!isDescription) {
            throw new CustomException(CustomExceptionEnum.DELETE_DATA_ERROR);
        }
        // 删除课程
        int deleteById = baseMapper.deleteById(id);
        if (deleteById == 0) {
            throw new CustomException(CustomExceptionEnum.DELETE_DATA_ERROR);
        }
    }

    @Override
    @CacheEvict(value = "course", allEntries = true)
    public void updateCourse(CourseBasicVO courseBasicVO) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseBasicVO, eduCourse);
        int isUpdate = baseMapper.updateById(eduCourse);
        if (isUpdate != 1) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }
    }

    @Override
    @CacheEvict(value = "course", allEntries = true)
    public void updateCourseStatus(String id) {
        EduCourse course = new EduCourse();
        course.setId(id);
        // 设置课程状态
        course.setStatus("Normal");
        int isUpdate = baseMapper.updateById(course);
        if (isUpdate != 1) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }
    }

    @Override
    @Cacheable(value = "course", key = "'getHotCourse'")
    public List<EduCourse> getHotCourse() {
        // 查询前八条热门课程
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("buy_count", "view_count", "gmt_create");
        courseWrapper.last("limit 8");
        return baseMapper.selectList(courseWrapper);
    }
}
