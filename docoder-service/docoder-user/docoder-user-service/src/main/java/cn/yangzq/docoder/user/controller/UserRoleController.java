package cn.yangzq.docoder.user.controller;


import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.user.form.RbacForm;
import cn.yangzq.docoder.user.service.UserRoleService;
import cn.yangzq.docoder.user.vo.RbacVo;
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
*@description 用户角色关系表 前端控制器
**/
@Api(tags = "用户角色关系接口")
@RestController
@RequestMapping("/user/ur")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @ApiOperation("批量绑定用户和角色的关系")
    @PostMapping("/bindBatch")
    public ResultVo<Object> bindBatch(@RequestBody List<RbacForm> forms){
        userRoleService.bindBatch(forms);
        return ResultVo.success();
    }

    @ApiOperation("批量绑定用户和角色和权限的关系")
    @PostMapping("/bindAll")
    public ResultVo<Object> bindAll(@RequestBody RbacVo rbac){
        userRoleService.bindAll(rbac);
        return ResultVo.success();
    }
}

