package cn.yangzq.docoder.user.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.base.iservice.IActionCodeService;
import cn.yangzq.docoder.base.iservice.ISecurityService;
import cn.yangzq.docoder.common.core.exception.ValidateException;
import cn.yangzq.docoder.common.core.utils.RedisUtil;
import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.UserLoginForm;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import cn.yangzq.docoder.user.maputil.FormToPOMapper;
import cn.yangzq.docoder.user.service.SysUserService;
import cn.yangzq.docoder.user.vo.RegisterPrepareVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
*@author yangzq
*@description 系统用户表 前端控制器
**/
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private FormToPOMapper formToPOMapper;
    @DubboReference
    private ISecurityService securityService;
    @DubboReference
    private IActionCodeService actionCodeService;
    @Autowired
    private SysUserService userService;


    @GetMapping("/registerPrepare")
    public ResultVo<RegisterPrepareVO> registerPrepare(){
        return ResultVo.success(prepare());
    }

    @GetMapping("/refreshCaptcha/{registerCode}")
    public ResultVo<RegisterPrepareVO> refreshCaptcha(@PathVariable String registerCode){
        actionCodeService.delete(registerCode);
        return ResultVo.success(prepare());
    }

    @PostMapping("/login")
    public ResultVo<Object> login(@RequestBody UserLoginForm form){
        return ResultVo.success();
    }

    @PostMapping("/isRepeat")
    public ResultVo<Object> isRepeat(@RequestBody UserRegisterForm form){
        return ResultVo.success().data(userService.isRepeat(form));
    }

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
        SysUser sysUser = formToPOMapper.registerForm(form);
        sysUser.setUserPwd(SecureUtil.md5(sysUser.getUserPwd()));
        userService.save(sysUser);
        return ResultVo.success();
    }

    private RegisterPrepareVO prepare(){
        Map<String, String> captchaMap = securityService.getCaptchaBase64();
        String actionCode = actionCodeService.getActionCode(captchaMap.get("text"));
        RegisterPrepareVO prepare = new RegisterPrepareVO();
        prepare.setRandomCode(actionCode);
        prepare.setCaptchaStr(captchaMap.get("captcha"));
        return prepare;
    }
}

