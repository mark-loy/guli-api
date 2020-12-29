package com.mark.serviceedu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/29 11:43
 */
@ApiModel(value = "课程查询对象", description = "课程查询封装对象")
@Data
public class PageCourseQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程状态 Normal：发布，Draft：未发布")
    private String status;

}
