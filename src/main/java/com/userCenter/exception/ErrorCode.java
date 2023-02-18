package com.userCenter.exception;

/**
 * 异常码
 * 在异常码中我们只定义了 code 和 code 对应的信息：message
 *
 * @author eddy
 */
public enum ErrorCode {
    /**
     * 枚举错误码
     */
    NULL_ERR_CODE(100, "请求数据为空"),
    PARAMS_ERR_CODE(101, "请求参数错误"),
    NO_LOGIN(200, "未登录"),
    NO_AUTH(201, "权限不足"),
    SYSTEM_ERR_CODE(9999, "系统异常");


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
