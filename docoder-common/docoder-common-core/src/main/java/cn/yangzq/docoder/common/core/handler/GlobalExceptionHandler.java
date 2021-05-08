package cn.yangzq.docoder.common.core.handler;

import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.common.core.exception.AuthException;
import cn.yangzq.docoder.common.core.exception.BasicException;
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

import cn.yangzq.docoder.common.core.utils.ResultVo;

/**
 * @author yangzq
 * @description 全局异常处理类
 **/
@Slf4j
@RestController
@RestControllerAdvice
public class GlobalExceptionHandler {


    /*@RequestMapping(value = {"/error"})
    public ResultVO error(HttpServletRequest request, Exception exception, HttpServletResponse response){
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
    public ResultVo errorHandler(HttpServletRequest request, NoHandlerFoundException exception, HttpServletResponse response) {
        printStackTrace(exception);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage());
    }

    /**
     * 405异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultVo errorHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException exception, HttpServletResponse response) {
        printStackTrace(exception);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                exception.getMessage());
    }

    /**
     * 415异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResultVo errorHandler(HttpServletRequest request, HttpMediaTypeNotSupportedException exception, HttpServletResponse response) {
        printStackTrace(exception);
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                exception.getMessage());
    }

    /**
     * 500异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public ResultVo errorHandler(HttpServletRequest request, Exception exception, HttpServletResponse response) {
        printStackTrace(exception);
        int code = HttpStatus.UNSUPPORTED_MEDIA_TYPE.value();
        if (exception instanceof AuthException) {
            code = ((AuthException) exception).getCode();
        }
        return commonHandler(request, response,
                exception.getClass().getSimpleName(),
                code,
                exception.getMessage());
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(value = BasicException.class)
    private ResultVo errorHandler(HttpServletRequest request, BasicException exception, HttpServletResponse response) {
        printStackTrace(exception);
        ResultVo errorEntity = ResultVo.failed()
                .message(exception.getMessage())
                .code(exception.getCode())
                .isHandle(exception.isHandle());
        return determineOutput(request, response, errorEntity);
    }

    /**
     * 表单验证异常处理
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResultVo validExceptionHandler(BindException exception, HttpServletRequest request, HttpServletResponse response) {
        printStackTrace(exception);
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResultVo.failed().message(JSONUtil.toJsonStr(errors));
    }

    /**
     * 表单验证异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultVo methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception, HttpServletRequest request, HttpServletResponse response) {
        printStackTrace(exception);
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ResultVo<Object> result = ResultVo.failed().message(JSONUtil.toJsonStr(errors)).isHandle(true);
        return determineOutput(request, response, result);
    }

    /**
     * 统一异常消息处理
     */
    private ResultVo commonHandler(HttpServletRequest request, HttpServletResponse response,
                                   String error, int httpCode, String message) {
        ResultVo errorEntity = ResultVo.failed()
                .message(message)
                .code(httpCode);
        return determineOutput(request, response, errorEntity);
    }

    /**
     * 异常输出处理
     */
    private ResultVo determineOutput(HttpServletRequest request, HttpServletResponse response, ResultVo entity) {
        //response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setCharacterEncoding("UTF8");
        response.setHeader("Content-Type", "application/json");
        try {
            response.getWriter().write(JSONUtil.toJsonStr(entity));
        } catch (IOException e) {
            printStackTrace(e);
        }
        return null;
    }

    /**
     * 记录异常信息
     */
    private void printStackTrace(Exception e) {
        String traceInfo = getStackTraceInfo(e);
        System.out.println(traceInfo);
        //log.error(this.getClass().getSimpleName(),e.getMessage());
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
