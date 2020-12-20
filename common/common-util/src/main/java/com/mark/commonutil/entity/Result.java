package com.mark.commonutil.entity;

import io.swagger.annotations.Api;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/19 16:34
 */
@Data
public class Result {

    private Boolean success;

    private Integer code;

    private String message;

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
