package com.mark.serviceedu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/27 14:14
 */
@Data
public class ChapterVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private List<VideoVO> children = new ArrayList<>();
}
