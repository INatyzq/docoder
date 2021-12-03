package cn.yangzq.docoder.user.vo;

import cn.yangzq.docoder.user.entity.PermissionDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
*@author yangzq
*@description 用户详情
**/
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户详情")
@Data
public class UserDetailVo extends SysUserVo implements Serializable {

    private static final long serialVersionUID = -1434300466895349995L;

    @ApiModelProperty("用户权限")
    private List<PermissionDetail> permissionList;

    @ApiModelProperty("用户特性")
    private UserFeatureVo feature;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("记住我")
    private Boolean rememberMe;
}
