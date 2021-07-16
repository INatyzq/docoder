package cn.yangzq.docoder.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
*@author yangzq
*@description 权限树VO
**/
@ApiModel("权限树VO")
@Data
public class PermissionTreeVo implements Serializable {

    private static final long serialVersionUID = -1198392432842325022L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("父id")
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

    @ApiModelProperty("是否展开")
    private boolean expanded = true;

    @ApiModelProperty("孩子节点列表")
    private List<PermissionTreeVo> children;

    @ApiModelProperty("父id列表")
    private Set<Integer> pids = new HashSet<>();

    @ApiModelProperty("孩子id列表")
    private Set<Integer> childrenIds = new HashSet<>();
}
