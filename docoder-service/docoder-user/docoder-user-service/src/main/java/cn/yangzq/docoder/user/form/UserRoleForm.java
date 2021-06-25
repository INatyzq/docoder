package cn.yangzq.docoder.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
*@author yangzq
*@description 用户和角色关系Form
**/
@ApiModel("用户和角色关系Form")
@Data
public class UserRoleForm implements Serializable {

    private static final long serialVersionUID = 6492822103447369429L;

    @ApiModelProperty("操作类型;1:新增;2:删除")
    private Integer action;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("角色id")
    private Integer permissionId;

}
