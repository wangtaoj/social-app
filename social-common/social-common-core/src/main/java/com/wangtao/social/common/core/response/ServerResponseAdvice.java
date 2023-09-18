package com.wangtao.social.common.core.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 用于包装Controller返回的结果
 * @author wangtao
 * Created at 2023-09-18
 */
@ControllerAdvice
public class ServerResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 此方法会去搜索父类或者接口上的方法, 参见子类HandlerMethodParameter
        ServerReponseDecorator serverReponseDecorator = returnType.getMethodAnnotation(ServerReponseDecorator.class);
        if (serverReponseDecorator == null) {
            /*
             * 从类上寻找
             * 由于ServerReponseDecorator被@Inherited标记
             * 因此还会从父类寻找, 但是不包括接口
             */
            serverReponseDecorator = returnType.getDeclaringClass().getAnnotation(ServerReponseDecorator.class);
        }
        return serverReponseDecorator != null && !serverReponseDecorator.ignore();
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        if (body instanceof ServerResponse) {
            return body;
        }
        /*
         * 返回结果为null或者无返回值也是通过MappingJackson2HttpMessageConverter处理
         * 因此这里无需做特殊处理
         */
        ServerResponse<?> result = ServerResponse.success(body);
        // 字符串处理器, 需要转成字符串, 它只接收字符串当参数, 否则会转型异常
        if (selectedConverterType == StringHttpMessageConverter.class) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                throw new BusinessException(ResponseEnum.SYS_ERROR, e.getMessage(), e);
            }
        }
        return result;
    }

}
