package com.mark.sta.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/11 15:39
 */
@ApiModel(value = "日常统计查询对象")
@Data
public class DailyQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "统计因子")
    private String type;

    @ApiModelProperty(value = "开始日期")
    private String begin;

    @ApiModelProperty(value = "结束日期")
    private String end;
}
