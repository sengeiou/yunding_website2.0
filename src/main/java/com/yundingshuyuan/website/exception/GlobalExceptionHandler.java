package com.yundingshuyuan.website.exception;

import com.yundingshuyuan.website.response.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Fant.J.
 */
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger("GlobalExceptionHandler");

    //@ExceptionHandler(value = BaseException.class)
    //@ResponseBody
    //public Object baseErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    //    logger.error("---BaseException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost(), req.getRequestURL(), e.getMessage());
    //    return e.getMessage();
    //}

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ServerResponse defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        //if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
        //    logger.info("路径不存在");
        //    return ServerResponse.createByErrorCodeMessage(404,"路径不存在");
        //} else {
        //    logger.info("服务器响应错误");
        //    return ServerResponse.createByErrorCodeMessage(500,"服务器错误");
        //}
        /**
         * 其他,则返回未知异常
         */
        e.printStackTrace();
        /*将异常(getStackTrace)转化成String*/
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return ServerResponse.createByErrorCodeMessage(500,e.getLocalizedMessage());
    }
}
