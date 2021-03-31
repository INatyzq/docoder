package cn.yangzq.docoder.common.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.io.Serializable;

/**
*@author yangzq
*@description 统一响应实体
**/
public class ResultVo<T> implements Serializable {

    private static final long serialVersionUID = -4442581732578065515L;

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "响应码")
    private Integer code;

    @ApiModelProperty(value = "响应消息")
    private String message;

    @ApiModelProperty(value = "响应数据")
    private T data;

    @ApiModelProperty(value = "响应数据")
    private boolean isHandle = false;

    protected ResultVo(){}

    private static <T> ResultVo<T> restResult(boolean isSuccess,int code, String msg,T data){
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setSuccess(isSuccess);
        resultVo.setCode(code);
        resultVo.setMessage(msg);
        resultVo.setData(data);
        return resultVo;
    }

    /**
     * 请求处理成功
     * @return ResultApi
     */
    @ApiOperation(value = "请求处理成功")
    public static <T> ResultVo<T> success(){
        return restResult(true,ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),null);
    }

    @ApiOperation(value = "请求处理成功")
    public static <T> ResultVo<T> success(T data){
        return restResult(true,ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),data);
    }

    public static <T> ResultVo<T> success(Integer code, String msg){
        return restResult(true,code,msg,null);
    }

    /**
     * 请求处理失败
     * @return ResultApi
     */
    @ApiOperation(value = "请求处理失败")
    public static <T> ResultVo<T> failed(){
        return restResult(false,ResultEnum.ERROR.getCode(),ResultEnum.ERROR.getMsg(),null);
    }

    public static <T> ResultVo<T> failed(Integer code, String msg){
        return restResult(false,code,msg,null);
    }

    @ApiOperation(value = "是否成功处理请求")
    public ResultVo<T> success(Boolean success){
        return this;
    }

    @ApiOperation(value = "设置响应码")
    public ResultVo<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    @ApiOperation(value = "设置响应消息")
    public ResultVo<T> message(String message){
        this.setMessage(message);
        return this;
    }

    @ApiOperation(value = "设置响应数据")
    public ResultVo<T> data(T data){
        this.setData(data);
        return this;
    }

    public ResultVo<T> isHandle(boolean handle) {
        this.isHandle = handle;
        return this;
    }

    /**
     * 转化json
     */
    public String toJSON(){
        return JSONUtil.toJsonStr(this);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public <T> T self(Class<T> tClass){
        return BeanUtil.copyProperties(this,tClass);
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }

    public boolean isHandle() {
        return isHandle;
    }

}
