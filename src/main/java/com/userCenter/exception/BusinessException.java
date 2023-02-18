package com.userCenter.exception;

import java.io.Serial;

/**
 * 自定义业务异常类
 * final修饰成员变量：该成员变量必须在其所在类对象创建之前被初始化（且只能被初始化一次）。
 *
 * @author eddy
 */
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4459376314121780306L;

    private final int code;

    public BusinessException(int code) {
        this.code = code;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
