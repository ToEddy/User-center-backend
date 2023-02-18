package com.usercenter.exception;

import java.io.Serial;

/**
 * 自定义系统异常类
 * final修饰成员变量：该成员变量必须在其所在类对象创建之前被初始化（且只能被初始化一次）。
 *
 * @author eddy
 */
public class SystemException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -846201039885386443L;
    private final int code;

    public SystemException(int code) {
        this.code = code;
    }

    public SystemException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
