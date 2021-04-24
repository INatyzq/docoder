package cn.yangzq.docoder.user.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.base.iservice.IActionCodeService;
import cn.yangzq.docoder.base.iservice.ISecurityService;
import cn.yangzq.docoder.common.core.exception.AuthException;
import cn.yangzq.docoder.common.core.exception.ValidateException;
import cn.yangzq.docoder.common.core.utils.RedisUtil;
import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.common.security.helper.AuthProvider;
import cn.yangzq.docoder.user.config.DocoderConfig;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.UserLoginForm;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import cn.yangzq.docoder.user.maputil.FormToPOMapper;
import cn.yangzq.docoder.user.service.SysUserService;
import cn.yangzq.docoder.user.vo.RegisterPrepareVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private DocoderConfig docoderConfig;
    @Autowired
    private AuthProvider authProvider;


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
        authProvider.authenticate().setAuthHeader("web_auth").
                setCache("","")
        .authError(true,"qwe");
       /* String userName = form.getUserName();
        String userPwd = form.getUserPwd();
        userPwd = SecureUtil.md5(userPwd);

        String jwtToken = JwtUtil.sign(userName, userPwd, form.isRememberMe());

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_NAME",userName);
        wrapper.eq("USER_PWD",userPwd);
        SysUser user = userService.getOne(wrapper);

        JSONObject msg = JSONUtil.createObj();
        if(user==null){
            msg.set("userName",userName+"此账户不存在！");
            throw new AuthException(msg.toString(),true);
        }

        if(!user.getUserPwd().equals(userPwd)){
            msg.set("userPwd","密码不正确！");
            throw new AuthException(msg.toString(),true);
        }

        long timeOut = form.isRememberMe()?docoderConfig.getTokenRememberTime():docoderConfig.getTokenTime();
        redisUtil.setEx(SecurityConst.WEB_TOKEN_KEY+jwtToken,JSONUtil.toJsonStr(user),timeOut, TimeUnit.MILLISECONDS);
*/
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

