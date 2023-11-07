package lc.activiti.exception;

import lc.activiti.lcenum.HttpRequestStatus;
import lc.activiti.lcenum.LCExceptionUtils;
import lc.activiti.lcenum.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//import org.apache.shiro.authz.AuthorizationException;

/**
 * 全局异常拦截
 */
@Slf4j
@ControllerAdvice
@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result excpetionHandler(Exception ex) {
        log.error("内部错误:{}", ex);
        Result<Object> errResult = new Result<Object>();
        if (ex==null)
            errResult.setMessage("内部错误");
        else
            errResult.setMessage(LCExceptionUtils.getExpetionMessage(ex));
        errResult.setStatus(HttpRequestStatus.Failed.getStatus());
        return errResult;
    }
}
