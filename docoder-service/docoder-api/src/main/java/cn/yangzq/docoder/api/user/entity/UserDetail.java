package cn.yangzq.docoder.api.user.entity;

import cn.yangzq.docoder.api.user.vo.PermissionVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
*@author yangzq
*@description 用户详情
**/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDetail extends SysUser {
    private static final long serialVersionUID = -3028624092551836870L;

    private String token;

    private List<PermissionVO> permissionList;
}