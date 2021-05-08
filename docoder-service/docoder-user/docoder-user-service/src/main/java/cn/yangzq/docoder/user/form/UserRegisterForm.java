package cn.yangzq.docoder.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
*@author yangzq
*@description 用户注册Form
**/
@ApiModel("用户注册表单")
@Data
public class UserRegisterForm implements Serializable {
    private static final long serialVersionUID = 8095792265113174985L;

    @ApiModelProperty("用户名")
    @Length(min = 6,message = "用户名长度不正确(>=6)")
    private String userName;

    @ApiModelProperty("密码")
    @Length(min = 6,message = "密码长度不正确")
    private String userPwd;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty("验证码")
    @NotEmpty(message = "验证码不能为空")
    private String captcha;

    @ApiModelProperty("操作码")
    @NotEmpty(message = "操作码不能为空")
    private String actionCode;
}
