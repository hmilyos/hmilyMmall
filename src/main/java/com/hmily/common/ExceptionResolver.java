package com.hmily.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(value = Exception.class)

    @ResponseBody
    public ServerResponse handle(HttpServletRequest request, Exception e) {
        log.error("{} Exception", request.getRequestURI(), e);
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),
                "服务器内部错误",  e.toString());
    }

}

