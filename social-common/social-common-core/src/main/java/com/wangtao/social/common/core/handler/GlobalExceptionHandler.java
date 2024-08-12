package com.wangtao.social.common.core.handler;

import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * @author wangtao
 * Created at 2023-09-16
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerResponse<?>> exception(Exception e, HttpServletRequest request) {
        log.error("{} encounter a error.", request.getServletPath(), e);
        ServerResponse<?> serverResponse = ServerResponse.error(ResponseEnum.SYS_ERROR, e.getMessage());
        return new ResponseEntity<>(serverResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ServerResponse<?>> businessException(BusinessException e, HttpServletRequest request) {
        log.error("{} encounter a error.", request.getServletPath(), e);
        ServerResponse<?> serverResponse = ServerResponse.error(e);
        return new ResponseEntity<>(serverResponse, e.getResponseEnum().getHttpStatus());
    }

    /**
     * 404
     * Spring Boot3版本可再增加NoResourceFoundException
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<ServerResponse<?>> notFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("{} encounter a error.", request.getServletPath(), e);
        ServerResponse<?> serverResponse = ServerResponse.error(ResponseEnum.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(serverResponse, ResponseEnum.NOT_FOUND.getHttpStatus());
    }

    /**
     * 参数校验异常
     * 校验注解写在bean对象中的字段上
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ServerResponse<?>> bindException(BindException e, HttpServletRequest request) {
        log.error("{} encounter a error.", request.getServletPath(), e);
        // 取第一个错误的信息
        String errMsg = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        ServerResponse<?> serverResponse = ServerResponse.error(ResponseEnum.PARAM_ILLEGAL, errMsg);
        return new ResponseEntity<>(serverResponse, ResponseEnum.PARAM_ILLEGAL.getHttpStatus());
    }

    /**
     * 参数校验异常
     * 校验注解直接写在Controller方法参数上
     * 此时@Validated注解需要写到类上面才会去校验
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ServerResponse<?>> constraintViolationException(ConstraintViolationException e,
                                                                          HttpServletRequest request) {
        log.error("{} encounter a error.", request.getServletPath(), e);
        // 取第一个错误的信息
        String errMsg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).findFirst().orElse(null);
        ServerResponse<?> serverResponse = ServerResponse.error(ResponseEnum.PARAM_ILLEGAL, errMsg);
        return new ResponseEntity<>(serverResponse, ResponseEnum.PARAM_ILLEGAL.getHttpStatus());
    }
}
