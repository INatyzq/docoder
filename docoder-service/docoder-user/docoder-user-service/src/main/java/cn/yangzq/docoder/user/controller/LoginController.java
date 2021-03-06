package cn.yangzq.docoder.user.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.base.api.IActionCodeService;
import cn.yangzq.docoder.base.api.ISecurityService;
import cn.yangzq.docoder.user.config.UserContextHolder;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.entity.UserDetail;
import cn.yangzq.docoder.user.form.UserLoginForm;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import cn.yangzq.docoder.user.service.SysUserService;
import cn.yangzq.docoder.user.vo.RegisterPrepareVo;
import cn.yangzq.docoder.common.core.exception.ValidateException;
import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.user.maputil.FormToPoMapper;
import cn.yangzq.docoder.user.vo.UserDetailVo;
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
public class LoginController {

    @Autowired
    private FormToPoMapper formToPoMapper;
    @DubboReference
    private ISecurityService securityService;
    @DubboReference
    private IActionCodeService actionCodeService;
    @Autowired
    private SysUserService userService;


    @ApiOperation("注册前数据准备")
    @GetMapping("/registerPrepare")
    public ResultVo<RegisterPrepareVo> registerPrepare(){
        return ResultVo.success(prepare());
    }

    @ApiOperation("刷新注册码")
    @GetMapping("/refreshCaptcha/{registerCode}")
    public ResultVo<RegisterPrepareVo> refreshCaptcha(@PathVariable String registerCode){
        actionCodeService.delete(registerCode);
        return ResultVo.success(prepare());
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResultVo<UserDetailVo> login(@RequestBody UserLoginForm form){
        UserContextHolder.login("yzq20211203");
        UserContextHolder.storeUserInPrincipal("{'userName':'yzq'}");
        UserDetail currentUser = UserContextHolder.getCurrentUser();
        System.out.println(currentUser);
        return ResultVo.success(userService.login(form));
    }

    @ApiOperation("刷新用户信息")
    @GetMapping("/refresh/{id}")
    public ResultVo<UserDetailVo> refresh(@PathVariable("id") Integer id){
        return ResultVo.success(userService.refresh(id));
    }

    @ApiOperation("是否重复")
    @PostMapping("/isRepeat")
    public ResultVo<Object> isRepeat(@RequestBody UserRegisterForm form){
        return ResultVo.success().data(userService.isRepeat(form));
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public ResultVo<Object> register(@RequestBody @Valid UserRegisterForm form){
        String actionCode = form.getActionCode();
        String captcha = actionCodeService.getAndDelete(actionCode);

        JSONObject message = JSONUtil.createObj();
        if(StrUtil.isBlank(captcha)){
            message.set("captcha","验证码已失效，请重新输入！");
            throw new ValidateException(message.toString(),true);
        }
        if(!captcha.equals(form.getCaptcha())){
            message.set("captcha","验证码不正确！");
            throw new ValidateException(message.toString(),true);
        }
        SysUser sysUser = formToPoMapper.toSysUser(form);
        sysUser.setUserPwd(SecureUtil.md5(sysUser.getUserPwd()));
        userService.save(sysUser);
        return ResultVo.success();
    }

    /**
     * 注册前需要的数据
     */
    private RegisterPrepareVo prepare(){
        Map<String, String> captchaMap = securityService.getCaptchaBase64();
        String actionCode = actionCodeService.getActionCode(captchaMap.get("text"));
        RegisterPrepareVo prepare = new RegisterPrepareVo();
        prepare.setRandomCode(actionCode);
        prepare.setCaptchaStr(captchaMap.get("captcha"));
        return prepare;
    }
}

