package cn.yangzq.docoder.user.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
*@author yangzq
*@description 权限查询参数
**/
@ApiModel("权限查询参数")
@Data
public class RbacParam {

    private String mode;

    private Integer id;

}
