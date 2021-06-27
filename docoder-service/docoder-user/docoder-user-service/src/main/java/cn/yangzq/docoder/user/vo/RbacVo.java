package cn.yangzq.docoder.user.vo;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
*@author yangzq
*@description 权限rbacVO
**/
@ApiModel("权限rbacVO")
@Data
public class RbacVo implements Serializable {

    private static final long serialVersionUID = -6663779303992636930L;

    @ApiModelProperty("用户id集合")
    private Set<String> userIds = new HashSet<>();

    @ApiModelProperty("角色id集合")
    private Set<String> roleIds = new HashSet<>();

    @ApiModelProperty("权限id集合")
    private Set<String> permissionIds = new HashSet<>();

    @ApiModelProperty("id串")
    @JsonIgnore
    private String idsStr;

    public void setIdsStr(String idsStr) {
        if(StrUtil.isBlank(idsStr)){
            return;
        }
        String[] ids = idsStr.split("_");
        userIds.add(StrUtil.blankToDefault(ids[0],null));
        roleIds.add(StrUtil.blankToDefault(ids[1],null));
        permissionIds.add(StrUtil.blankToDefault(ids[2],null));
    }
}
