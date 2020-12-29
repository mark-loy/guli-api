package com.mark.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.serviceedu.entity.EduChapter;
import com.mark.serviceedu.entity.EduVideo;
import com.mark.serviceedu.entity.vo.ChapterVO;
import com.mark.serviceedu.entity.vo.VideoVO;
import com.mark.serviceedu.mapper.EduChapterMapper;
import com.mark.serviceedu.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mark.serviceedu.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Resource
    private EduVideoService videoService;

    @Override
    public List<ChapterVO> getChapterVideo(String courseId) {
        // 根据课程id，查询所有章节信息
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(chapterQueryWrapper);

        // 根据课程id，查询所有小节信息
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideos = videoService.list(videoQueryWrapper);

        // 构建最终返回集合
        List<ChapterVO> result = new ArrayList<>();

        // 遍历章节信息
        eduChapters.forEach(chapter -> {
            ChapterVO chapterVO = new ChapterVO();
            BeanUtils.copyProperties(chapter, chapterVO);

            //构建小节的返回集合
            List<VideoVO> videoResult = new ArrayList<>();

            // 遍历小节信息
            eduVideos.forEach(video -> {
                // 判断小节 是否属于当前的章节
                if (chapter.getId().equals(video.getChapterId())) {
                    VideoVO videoVO = new VideoVO();
                    BeanUtils.copyProperties(video, videoVO);

                    // 加入小节集合
                    videoResult.add(videoVO);
                }
            });

            // 封装小节集合
            chapterVO.setChildren(videoResult);

            // 封装章节信息
            result.add(chapterVO);
        });

        return result;
    }
}
