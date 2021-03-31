package cn.yangzq.docoder.user.form;

import io.swagger.annotations.ApiModel;
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
}
