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
    DELETE_DATA_ERROR(20005, "删除数据失败"),
    FORM_DATA_ERROR(20006, "表单数据错误"),
    MOBILE_ERROR(20007, "账号错误"),
    PASSWORD_ERROR(20008, "密码错误"),
    CODE_TIMEOUT(20009, "验证码已过期"),
    CODE_ERROR(20009, "验证码错误"),
    USER_DISABLED(20010, "用户已禁用"),
    USER_REGISTERED(20010, "用户已注册");

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
