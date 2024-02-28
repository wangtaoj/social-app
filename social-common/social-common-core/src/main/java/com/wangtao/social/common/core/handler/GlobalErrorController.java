package com.wangtao.social.common.core.handler;

import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.response.ServerResponse;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * 全局异常处理器未处理情况，最终由Servlet容器处理的异常场景，然后转发到/error请求
 * 1. 过滤器，因为压根还没执行DispatcherServlet，所以SpringMVC全局异常处理就不可能来
 *    处理过滤器中发生的异常
 * 2. 通过response.sendError()标记的异常，此方法只是打一个标记，并没有实际抛出异常，所以
 *    SpringMVC全局异常处理是捕获不到的，最终由Servlet容器处理。
 *    如SpringMVC处理404时默认就是调用response.sendError(404)
 * 3. 如果有异常抛出，tomcat本身会打印异常堆栈日志
 * @author wangtao
 * Created at 2024-02-28
 */
@RequestMapping("${server.error.path:${error.path:/error}}")
@Controller
public class GlobalErrorController extends AbstractErrorController {

    public GlobalErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    public ResponseEntity<ServerResponse<?>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        // 用于获取message, exception属性
        ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.EXCEPTION
        );
        Map<String, Object> map = getErrorAttributes(request, errorAttributeOptions);
        ServerResponse<?> body = null;
        String message = (String) map.get("message");
        // 值为status.getReasonPhrase(), 如状态码为404, 则就是Not Found
        String error = (String) map.get("error");
        String exceptionName = (String) map.get("exception");
        if (Objects.equals("No message available", message)) {
            if (Objects.equals(NullPointerException.class.getName(), exceptionName)) {
                message = "null";
            } else if (status.is5xxServerError()) {
                message = "系统异常";
            } else if (status.is4xxClientError()) {
                message = error;
                body = ServerResponse.error(ResponseEnum.CLIENT_ERROR, message);
            }
            else {
                message = error;
            }
        }
        if (Objects.isNull(body)) {
            body = ServerResponse.error(ResponseEnum.SYS_ERROR, message);
        }
        return new ResponseEntity<>(body, status);
    }
}
