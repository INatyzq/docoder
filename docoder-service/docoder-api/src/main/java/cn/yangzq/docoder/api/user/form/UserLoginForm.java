package cn.yangzq.docoder.api.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
*@author yangzq
*@description 用户登录Form
**/
@ApiModel("用户登录表单")
@Data
public class UserLoginForm implements Serializable {
    private static final long serialVersionUID = 8095792265113174985L;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String userPwd;

    @ApiModelProperty("记住我")
    private boolean rememberMe;
}
