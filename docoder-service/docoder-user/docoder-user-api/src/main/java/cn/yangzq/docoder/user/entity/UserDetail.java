package cn.yangzq.docoder.user.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
*@author yangzq
*@description 用户详情
**/
@Data
public class UserDetail implements Serializable {
    private static final long serialVersionUID = -3028624092551836870L;

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String userPwd;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "姓名")
    private String fullName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "性别;1:男;0:女")
    private Integer sex;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "用户头像url")
    private String avatarUrl;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "权限列表")
    private List<PermissionDetail> permissionList;
}
