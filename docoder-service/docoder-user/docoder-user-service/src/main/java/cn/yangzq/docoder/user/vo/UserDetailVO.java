package cn.yangzq.docoder.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
*@author yangzq
*@description 用户详情
**/
@ApiModel("用户详情")
@Data
public class UserDetailVO implements Serializable {

    private static final long serialVersionUID = -1434300466895349995L;

    @ApiModelProperty("用户id")
    private Integer id;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别;1:男;0:女")
    private Integer sex;

    @ApiModelProperty(value = "用户头像url")
    private String avatarUrl;

    @ApiModelProperty("用户权限")
    private List<SysPermissionVo> permissionList;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("记住我")
    private boolean rememberMe;

}
