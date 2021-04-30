package cn.yangzq.docoder.user.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.base.iservice.IActionCodeService;
import cn.yangzq.docoder.base.iservice.ISecurityService;
import cn.yangzq.docoder.common.core.exception.ValidateException;
import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.UserLoginForm;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import cn.yangzq.docoder.user.maputil.FormToPoMapper;
import cn.yangzq.docoder.user.service.SysUserService;
import cn.yangzq.docoder.user.vo.RegisterPrepareVO;
import cn.yangzq.docoder.user.vo.UserDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
*@author yangzq
*@description 系统用户表 前端控制器
**/
@Api(tags = "系统用户表接口")
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private FormToPoMapper formToPoMapper;
    @Autowired
    private SysUserService userService;

    @GetMapping("/listPage")
    public ResultVo<Pageable<SysUser>> getUserPage(SysUser user,Pageable<SysUser> page){
        return ResultVo.success(userService.getUserPage(user,page));
    }

    @GetMapping("/{id}")
    public ResultVo<SysUser> getDetail(@PathVariable("id") Integer id){
        return ResultVo.success(userService.getById(id));
    }
}

