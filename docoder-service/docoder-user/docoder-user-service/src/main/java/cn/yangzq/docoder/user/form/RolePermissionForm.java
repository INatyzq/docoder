package cn.yangzq.docoder.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
*@author yangzq
*@description 角色和权限关系Form
**/
@ApiModel("角色和权限关系Form")
@Data
public class RolePermissionForm implements Serializable {

    private static final long serialVersionUID = 6492822103447369429L;

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("权限id集合")
    private List<Integer> permissionIds;

}
