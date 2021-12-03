package cn.yangzq.docoder.common.core.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.common.core.exception.BasicException;
import cn.yangzq.docoder.common.core.handler.ExceptionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangzq
 * @description 全局异常处理类
 **/
@Slf4j
@RestController
@RestControllerAdvice
public class GlobalExceptionHandler {


    /*@RequestMapping(value = {"/error"})
    public ExceptionEntity error(HttpServletRequest request, Exception exception, HttpServletResponse response){
        printStackTrace(exception);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage());
    }*/

    /**
     * 404异常处理
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionEntity noHandlerFoundExceptionHandler(HttpServletRequest request, NoHandlerFoundException exception, HttpServletResponse response) {
        printStackTrace(request, exception);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(), exception);
    }

    /**
     * 405异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ExceptionEntity httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException exception, HttpServletResponse response) {
        printStackTrace(request, exception);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                exception.getMessage(), exception);
    }

    /**
     * 415异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ExceptionEntity httpMediaTypeNotSupportedExceptionHandler(HttpServletRequest request, HttpMediaTypeNotSupportedException exception, HttpServletResponse response) {
        printStackTrace(request, exception);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                exception.getMessage(), exception);
    }

    /**
     * 500异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public ExceptionEntity exceptionHandler(HttpServletRequest request, Exception exception, HttpServletResponse response) {
        printStackTrace(request, exception);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(), exception);
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(value = BasicException.class)
    private ExceptionEntity basicExceptionHandler(HttpServletRequest request, BasicException exception, HttpServletResponse response) {
        printStackTrace(request, exception);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                exception.getCode(),
                exception.getMessage(), exception);
    }

    /**
     * 表单验证异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ExceptionEntity methodArgumentNotValidHandler(MethodArgumentNotValidException exception, HttpServletRequest request, HttpServletResponse response) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        String msg = JSONUtil.toJsonStr(errors);
        printExceptionMsg(request, exception.getClass().getSimpleName(), msg);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                msg, exception);
    }

    /**
     * 表单验证异常处理
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ExceptionEntity bindExceptionHandler(BindException exception, HttpServletRequest request, HttpServletResponse response) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        String msg = JSONUtil.toJsonStr(errors);
        printExceptionMsg(request, exception.getClass().getSimpleName(), msg);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                msg, exception);
    }

    /**
     * 统一异常消息处理
     */
    private ExceptionEntity commonHandler(HttpServletRequest request, HttpServletResponse response,
                                          String error, int httpCode, String message, Exception e) {

        Object data = null;
        boolean isHandle = false;
        if (!(e instanceof BasicException)) {
            message = "服务异常:请联系管理员!";
        } else {
            BasicException be = (BasicException) e;
            httpCode = be.getCode();
            data = be.getData();
            isHandle = be.isHandle();
        }
        ExceptionEntity errorEntity = ExceptionEntity.builder()
                .message(message)
                .path(request.getRequestURI())
                .code(httpCode)
                .error(error)
                .isHandle(isHandle);
        if(data!=null){
            errorEntity = errorEntity.data(data);
        }
        return determineOutput(request, response, errorEntity);
    }

    /**
     * 异常输出处理
     */
    private ExceptionEntity determineOutput(HttpServletRequest request, HttpServletResponse response, ExceptionEntity entity) {
        //response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setCharacterEncoding("UTF8");
        response.setHeader("Content-Type", "application/json");
        try {
            response.getWriter().write(JSONUtil.toJsonStr(entity));
        } catch (IOException e) {
            printStackTrace(request, e);
        }
        return null;
    }

    /**
     * 记录异常信息
     */
    private void printStackTrace(HttpServletRequest request, Exception e) {
        boolean isHandleThrow = e instanceof BasicException;
        String simpleName = e.getClass().getSimpleName();
        if (isHandleThrow) {
            String msg = StrUtil.blankToDefault(e.getMessage(), "");
            String firstStack = e.getStackTrace().length > 0 ? e.getStackTrace()[0].toString() : "";
            log.error("内部服务器异常(path:{})-{}:{}", request.getRequestURI(), simpleName, firstStack + ":" + msg);
        } else {
            log.error("内部服务器异常(path:{})-{}", request.getRequestURI(), simpleName, e);
        }
    }

    private void printExceptionMsg(HttpServletRequest request, String expName, String msg) {
        log.error("内部服务器异常(path:{})-{}:{}", request.getRequestURI(), expName, msg);
    }

    /**
     * 从堆栈中获取异常信息
     */
    private String getStackTraceInfo(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            return sw.toString();
        } catch (Exception ex) {
            return "printStackTrace()获取错误";
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }

    }

}
