package cn.yangzq.docoder.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yangzq
 * @description 系统权限表
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysPermission对象", description = "系统权限表")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "数据标识,0:无效,1:有效")
    @TableLogic
    private Integer status = 1;

    @ApiModelProperty(value = "创建者")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "修改者")
    private String updatedBy;

    @ApiModelProperty(value = "修改时间")
    private Date updatedTime;


}
