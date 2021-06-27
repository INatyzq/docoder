package cn.yangzq.docoder.user.controller;


import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.user.form.UserRoleForm;
import cn.yangzq.docoder.user.service.RolePermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
*@author yangzq
*@description 角色权限关系表 前端控制器
**/
@Api(tags = "角色权限关系接口")
@RestController
@RequestMapping("/user/rp")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @ApiOperation("批量绑定角色和权限的关系")
    @PostMapping("/bindBatch")
    public ResultVo<Object> bindBatch(@RequestBody List<UserRoleForm> forms){
        rolePermissionService.bindBatch(forms);
        return ResultVo.success();
    }

}

