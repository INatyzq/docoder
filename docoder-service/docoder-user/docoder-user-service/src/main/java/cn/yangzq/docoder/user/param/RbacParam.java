package cn.yangzq.docoder.user.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*@author yangzq
*@description 权限查询参数
**/
@ApiModel("权限查询参数")
@Data
public class RbacParam {

    @ApiModelProperty("模式;user;role;permission")
    private String mode;

    @ApiModelProperty("模式对应的id")
    private Integer id;

    @ApiModelProperty("查询参数")
    private String searchVal;

}
