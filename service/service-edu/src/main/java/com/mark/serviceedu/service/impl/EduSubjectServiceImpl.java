package com.mark.serviceedu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.serviceedu.entity.EduSubject;
import com.mark.serviceedu.entity.excel.SubjectExcel;
import com.mark.serviceedu.entity.subject.OneSubjectClassify;
import com.mark.serviceedu.entity.subject.TwoSubjectClassify;
import com.mark.serviceedu.listener.SubjectListener;
import com.mark.serviceedu.mapper.EduSubjectMapper;
import com.mark.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author mark
 * @since 2020-12-23
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubjectByExcel(MultipartFile multipartFile, EduSubjectService subjectService) {
        // 判断文件类型是否为空
        if (multipartFile == null) {
            throw new CustomException(CustomExceptionEnum.FILE_UPLOAD_ERROR);
        }

        try {
            // 获取输入流
            InputStream in = multipartFile.getInputStream();
            // 读取excel内容
            EasyExcel.read(in, SubjectExcel.class, new SubjectListener(subjectService)).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException(CustomExceptionEnum.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public List<OneSubjectClassify> getSubjectByTree() {
        // 查询所有的一级分类
        QueryWrapper<EduSubject> oneWrapper = new QueryWrapper<>();
        oneWrapper.eq("parent_id", "0");
        List<EduSubject> oneSubjectClassify = baseMapper.selectList(oneWrapper);

        // 查询所有的二级分类
        QueryWrapper<EduSubject> twoWrapper = new QueryWrapper<>();
        twoWrapper.ne("parent_id", "0");
        List<EduSubject> twoSubjectClassify = baseMapper.selectList(twoWrapper);

        // 构建一级分类集合，用于返回
        List<OneSubjectClassify> subjectClassifiesResult = new ArrayList<>();

        // 封装一级分类 ==》遍历一级分类
        oneSubjectClassify.forEach(one -> {
            OneSubjectClassify oneSubjectResult = new OneSubjectClassify();
            // 将数据库中的一级分类赋值给实体类中的一级分类
            BeanUtils.copyProperties(one, oneSubjectResult);

            // 构建二级分类集合，用于存入一级分类实体类的children属性
            List<TwoSubjectClassify> twoSubjectClassifies = new ArrayList<>();

            // 封装二级分类 ==》遍历二级分类
            twoSubjectClassify.forEach(two -> {
                // 判断当前的二级分类，是否属于当前的一级分类
                if (one.getId().equals(two.getParentId())) {
                    TwoSubjectClassify twoSubjectResult = new TwoSubjectClassify();
                    BeanUtils.copyProperties(two, twoSubjectResult);
                    // 存入二级分类集合
                    twoSubjectClassifies.add(twoSubjectResult);
                }
            });

            // 将二级分类封装到children属性中
            oneSubjectResult.setChildren(twoSubjectClassifies);

            // 封装一级分类
            subjectClassifiesResult.add(oneSubjectResult);
        });

        return subjectClassifiesResult;
    }
}
