package com.mark.commonutil.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一结果返回实体类
 * @author 木可
 * @version 1.0
 * @date 2020/12/19 16:34
 */
@ApiModel(value = "统一结果返回实体类")
@Data
public class Result {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "提示信息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();

    private Result() {}

    /**
     * 成功通用返回方法
     * @return
     */
    public static Result ok() {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setSuccess(true);
        result.setMessage("成功");
        return result;
    }

    /**
     * 失败通用返回方法
     * @return
     */
    public static Result error() {
        Result result = new Result();
        result.setCode(ResultCode.ERROR);
        result.setSuccess(false);
        result.setMessage("失败");
        return result;
    }

    public Result success(Boolean flag) {
        this.setSuccess(flag);
        return this;
    }

    public Result code(Integer code) {
        this.setCode(code);
        return this;
    }

    public Result message(String message) {
        this.setMessage(message);
        return this;
    }

    public Result data(Map<String, Object> data) {
        this.setData(data);
        return this;
    }

    public Result data(String key, Object val) {
        this.data.put(key, val);
        return this;
    }
}
