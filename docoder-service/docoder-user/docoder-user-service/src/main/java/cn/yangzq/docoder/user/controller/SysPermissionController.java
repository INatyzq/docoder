package cn.yangzq.docoder.user.controller;


import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.user.entity.PermissionDetail;
import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.form.SysPermissionForm;
import cn.yangzq.docoder.user.maputil.FormToPoMapper;
import cn.yangzq.docoder.user.maputil.PoToVoMapper;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.service.SysPermissionService;
import cn.yangzq.docoder.user.vo.PermissionTreeVo;
import cn.yangzq.docoder.user.vo.RbacVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
*@author yangzq
*@description 系统权限表 前端控制器
**/
@Api(tags = "系统权限接口")
@RestController
@RequestMapping("/user/permission")
public class SysPermissionController {

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private PoToVoMapper poToVoMapper;
    @Autowired
    private FormToPoMapper formToPoMapper;

    @ApiOperation("获取所有数据")
    @GetMapping("/listAll")
    public ResultVo<List<PermissionDetail>> getListAll(){
        List<SysPermission> list = permissionService.list();
        return ResultVo.success(poToVoMapper.toPermissionDetailList(list));
    }

    @ApiOperation("获取树形数据")
    @GetMapping("/tree")
    public ResultVo<List<PermissionTreeVo>> getTree(){
        return ResultVo.success(permissionService.getTree());
    }

    @ApiOperation("保存或更新")
    @PostMapping("/saveOrUpdate")
    public ResultVo<Object> saveOrUpdate(@RequestBody SysPermissionForm form){
        SysPermission permission = formToPoMapper.toSysPermission(form);
        permissionService.saveOrUpdate(permission);
        return ResultVo.success();
    }

    @ApiOperation("移动节点")
    @PostMapping("/move")
    public ResultVo<Object> moveNode(@RequestBody SysPermissionForm form){
        SysPermission permission = formToPoMapper.toSysPermission(form);
        permissionService.updateById(permission);
        return ResultVo.success();
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public ResultVo<Object> deleteById(@PathVariable("id")Integer id){
        permissionService.removeById(id);
        return ResultVo.success();
    }

    @ApiOperation("获取rbac")
    @GetMapping("/rbac")
    public ResultVo<RbacVo> getRbac(RbacParam param){
       return ResultVo.success(permissionService.getRbac(param));
    }
}

