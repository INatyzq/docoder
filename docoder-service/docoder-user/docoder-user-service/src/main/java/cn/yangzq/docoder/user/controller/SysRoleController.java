package cn.yangzq.docoder.user.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.entity.PermissionDetail;
import cn.yangzq.docoder.user.entity.SysRole;
import cn.yangzq.docoder.user.form.SysRoleForm;
import cn.yangzq.docoder.user.maputil.FormToPoMapper;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.service.SysRoleService;
import cn.yangzq.docoder.user.vo.SysRoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
*@author yangzq
*@description 系统角色表 前端控制器
**/
@Api("系统角色接口")
@RestController
@RequestMapping("/user/role")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;
    @Autowired
    private FormToPoMapper formToPoMapper;

    @ApiOperation("获取角色分页列表")
    @GetMapping("/listPage")
    public ResultVo<Pageable<SysRoleVo>> getListPage(SysRoleVo param,Pageable<SysRoleVo> page){
        return ResultVo.success(roleService.getPage(param,page));
    }

    @ApiOperation("单条新增或修改")
    @PostMapping("/saveOrUpdate")
    public ResultVo<Object> saveOrUpdate(@RequestBody SysRoleForm form){
        SysRole role = formToPoMapper.toSysRole(form);
        roleService.saveOrUpdate(role);
        return ResultVo.success();
    }

    @ApiOperation("批量新增或修改")
    @PostMapping("/saveOrUpdateBatch")
    public ResultVo<Object> saveOrUpdate(@RequestBody List<SysRoleForm> forms){
        List<SysRole> roles = formToPoMapper.toSysRoleList(forms);
        roleService.saveOrUpdateBatch(roles);
        return ResultVo.success();
    }

    @ApiOperation("批量删除")
    @PostMapping("/deleteByIds")
    public ResultVo<Object> deleteBatch(@RequestBody List<Integer> ids){
        Assert.isFalse(CollectionUtil.isEmpty(ids),"集合不能为空！");
        roleService.removeByIds(ids);
        return ResultVo.success();
    }

    @ApiOperation("基于rbac的分页查询")
    @GetMapping("/rbacPage")
    public ResultVo<Pageable<SysRoleVo>> getRbacPage(RbacParam param, Pageable<PermissionDetail> page){
        return ResultVo.success(roleService.getRbacPage(param,page));
    }
}

