package com.mark.servicebase.enums;

/**
 * 自定义异常枚举
 * @author 木可
 * @version 1.0
 * @date 2020/12/20 12:12
 */
public enum CustomExceptionEnum {
    NOT_FOUND(20001, "没有发现对象"),
    FILE_UPLOAD_ERROR(20002, "文件上传失败"),
    EXCEL_DATA_ERROR(20002, "excel数据解析失败");

    private final Integer code;
    private final String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    CustomExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
