package com.mark.servicebase.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/9 16:52
 */
@Data
@Accessors(chain = true)
@ApiModel("订单中的用户对象")
public class MemberOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机号")
    private String mobile;

}
