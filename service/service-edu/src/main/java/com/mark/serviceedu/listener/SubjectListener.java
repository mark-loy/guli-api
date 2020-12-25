package com.mark.serviceedu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.serviceedu.entity.EduSubject;
import com.mark.serviceedu.entity.excel.SubjectExcel;
import com.mark.serviceedu.service.EduSubjectService;

/**
 * 读取excel的监听器
 * @author 木可
 * @version 1.0
 * @date 2020/12/23 18:54
 */
public class SubjectListener extends AnalysisEventListener<SubjectExcel> {

    private EduSubjectService subjectService;

    public SubjectListener() {
    }

    public SubjectListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    /**
     * 读取完成后的操作
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 一行行读取excel
     * @param subjectExcel excel实体类
     * @param analysisContext
     */
    @Override
    public void invoke(SubjectExcel subjectExcel, AnalysisContext analysisContext) {
        // 判断课程excel是否为空
        if (subjectExcel == null) {
            throw new CustomException(CustomExceptionEnum.EXCEL_DATA_ERROR);
        }

        // 获取一级课程分类title
        String parentTitle = subjectExcel.getParentTitle();
        EduSubject parentSubject = getParentSubject(parentTitle);
        // 判断一级subject
        if (parentSubject == null) {
            // 构建subject对象
            parentSubject = new EduSubject();
            // 设置title
            parentSubject.setTitle(parentTitle);
            // 执行
            subjectService.save(parentSubject);
        }

        // 获取二级课程分类title
        String childTitle = subjectExcel.getChildTitle();
        EduSubject childSubject = getChildSubject(childTitle, parentSubject.getId());
        // 判断二级subject
        if (childSubject == null) {
            // 构建subject对象
            childSubject = new EduSubject();
            // 设置title
            childSubject.setTitle(childTitle);
            // 设置父级id
            childSubject.setParentId(parentSubject.getId());
            // 执行
            subjectService.save(childSubject);
        }
    }

    /**
     * 查询一级课程分类
     * @param title 课程标题
     * @return
     */
    private EduSubject getParentSubject(String title) {
        // 构建查询对象
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", title);
        wrapper.eq("parent_id", "0");
        return subjectService.getOne(wrapper);
    }

    /**
     * 查询二级课程分类
     * @param title 课程标题
     * @param parentId 一级课程id
     * @return
     */
    private EduSubject getChildSubject(String title, String parentId) {
        // 构建查询对象
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", title);
        wrapper.eq("parent_id", parentId);
        return subjectService.getOne(wrapper);
    }

}
