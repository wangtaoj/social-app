package com.wangtao.social.common.core.handler;

import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.core.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

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
}
