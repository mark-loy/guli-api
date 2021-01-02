package com.mark.servicebase.enums;

/**
 * 自定义异常枚举
 * @author 木可
 * @version 1.0
 * @date 2020/12/20 12:12
 */
public enum CustomExceptionEnum {
    SELECT_DATA_ERROR(20001, "查询数据失败"),
    FILE_UPLOAD_ERROR(20002, "文件上传失败"),
    VIDEO_UPLOAD_ERROR(20002, "视频上传失败"),
    VIDEO_DELETE_ERROR(20002, "视频删除失败"),
    EXCEL_DATA_ERROR(20002, "excel数据解析失败"),
    INSERT_DATA_ERROR(20003, "插入数据失败"),
    UPDATE_DATA_ERROR(20004, "修改数据失败"),
    DELETE_DATA_ERROR(20005, "删除数据失败");

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
