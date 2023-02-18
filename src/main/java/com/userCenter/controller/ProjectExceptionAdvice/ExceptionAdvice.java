package com.userCenter.controller.ProjectExceptionAdvice;

import com.userCenter.exception.BusinessException;
import com.userCenter.exception.SystemException;
import com.userCenter.model.domain.ResultData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.userCenter.exception.ErrorCode.SYSTEM_ERR_CODE;

/**
 * 项目异常处理类
 *
 * @author eddy
 * @createTime 2023/2/11
 */
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(BusinessException.class)
    public ResultData doBusinessException(BusinessException businessEx) {
        return new ResultData(businessEx.getCode(), businessEx.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public ResultData doSystemException(SystemException systemEx) {
        return new ResultData(systemEx.getCode(), systemEx.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultData doException(Exception ex) {
        return new ResultData(SYSTEM_ERR_CODE);
    }
}
