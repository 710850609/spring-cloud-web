package me.linbo.api.core.exception;

import lombok.extern.slf4j.Slf4j;
import me.linbo.api.core.vo.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author LinBo
 * @date 2019-10-23 14:32
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Response defaultErrorHandler(Exception e) {
        log.error("拦截到异常", e);
        return Response.error(e);
    }
}
