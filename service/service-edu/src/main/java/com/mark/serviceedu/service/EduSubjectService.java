package com.mark.serviceedu.service;

import com.mark.serviceedu.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mark.serviceedu.entity.subject.OneSubjectClassify;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author mark
 * @since 2020-12-23
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubjectByExcel(MultipartFile multipartFile, EduSubjectService subjectService);

    List<OneSubjectClassify> getSubjectByTree();
}
