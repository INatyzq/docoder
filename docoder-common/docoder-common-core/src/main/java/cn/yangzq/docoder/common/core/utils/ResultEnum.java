package cn.yangzq.docoder.common.core.utils;

/**
*@author yangzq
*@description 响应枚举
**/
public enum ResultEnum {

    SUCCESS(201,"请求处理成功"),
    ERROR(501,"请求处理失败");

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
