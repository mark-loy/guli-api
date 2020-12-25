package com.mark.serviceedu.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 一级课程分类实体类
 * @author 木可
 * @version 1.0
 * @date 2020/12/25 12:08
 */
@Data
public class OneSubjectClassify {

    private String id;

    private String title;

    private List<TwoSubjectClassify> children = new ArrayList<>();
}
