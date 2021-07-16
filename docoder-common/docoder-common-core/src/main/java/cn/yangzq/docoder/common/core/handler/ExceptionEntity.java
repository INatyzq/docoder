package cn.yangzq.docoder.common.core.handler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yangzq
 * @description 异常响应消息实体
 **/
@ApiModel("异常响应消息实体")
public class ExceptionEntity implements Serializable {

    private static final long serialVersionUID = 935751662440096097L;

    @ApiModelProperty("响应结果标识")
    private final boolean success = Boolean.FALSE;

    @ApiModelProperty("异常编码")
    private int code;

    @ApiModelProperty("异常消息")
    private Object message;

    @ApiModelProperty("异常类型")
    private String error;

    @ApiModelProperty("异常调用路径")
    private String path;

    @ApiModelProperty("异常调用时间戳")
    private Date timestamp = new Date();


    /**
     * builder
     */
    public static ExceptionEntity builder(){
        return new ExceptionEntity();
    }

    public ExceptionEntity code(int code){
        this.code = code;
        return this;
    }

    public ExceptionEntity error(String error){
        this.error = error;
        return this;
    }

    public ExceptionEntity message(String message){
        this.message = message;
        return this;
    }

    public ExceptionEntity path(String path){
        this.path = path;
        return this;
    }

    public boolean getSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
