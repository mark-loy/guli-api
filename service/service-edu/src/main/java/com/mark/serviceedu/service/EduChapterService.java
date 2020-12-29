package com.mark.serviceedu.service;

import com.mark.serviceedu.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mark.serviceedu.entity.vo.ChapterVO;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author mark
 * @since 2020-12-26
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVO> getChapterVideo(String courseId);

}
