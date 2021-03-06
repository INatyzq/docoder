package cn.yangzq.docoder.user.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yangzq
 * @description 系统权限
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PermissionDetail", description = "系统权限")
public class PermissionDetail implements Serializable {

    private static final long serialVersionUID = -26324801012879635L;
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "父id")
    private Integer parentId;

    @ApiModelProperty(value = "权限名称")
    private String permissionName;

    @ApiModelProperty(value = "权限类型;1:菜单,2:按钮")
    private String permissionType;

    @ApiModelProperty(value = "权限描述")
    private String permissionDesc;

    @ApiModelProperty(value = "权限资源url")
    private String resourceUrl;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序序号")
    private Integer orderNum;

}
