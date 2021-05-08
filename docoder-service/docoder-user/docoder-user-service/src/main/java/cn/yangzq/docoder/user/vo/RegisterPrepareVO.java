package cn.yangzq.docoder.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
*@author yangzq
*@description 注册前必要属性 VO
**/
@ApiModel("注册前必要属性")
@Data
public class RegisterPrepareVO implements Serializable {
    private static final long serialVersionUID = -596664140893208972L;

    @ApiModelProperty("随机操作码")
    private String randomCode;

    @ApiModelProperty("验证码base64")
    private String captchaStr;

}
