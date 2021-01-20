package com.mark.order.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/19 20:42
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "订单条件查询对象")
public class OrderQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "课程名称")
    private String courseTitle;

    @ApiModelProperty(value = "开始创建时间", example = "2021-01-10 14:21:46")
    private Date begin;

    @ApiModelProperty(value = "结束创建时间", example = "2021-01-10 14:21:46")
    private Date end;

    @ApiModelProperty(value = "支付状态", example = "0 未支付；1 已支付")
    private Integer status;
}
