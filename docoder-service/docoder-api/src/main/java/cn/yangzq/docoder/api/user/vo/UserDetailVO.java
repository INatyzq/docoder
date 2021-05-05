package cn.yangzq.docoder.api.user.vo;

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

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户权限")
    private List<PermissionVO> permissionList;

    @ApiModelProperty("token")
    private String token;

}
