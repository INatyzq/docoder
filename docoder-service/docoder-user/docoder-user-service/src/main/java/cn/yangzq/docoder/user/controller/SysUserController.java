package cn.yangzq.docoder.user.controller;


import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.AvatarForm;
import cn.yangzq.docoder.user.maputil.PoToVoMapper;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.service.SysUserService;
import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.common.FilePath;
import cn.yangzq.docoder.user.config.DocoderConfig;
import cn.yangzq.docoder.user.entity.PermissionDetail;
import cn.yangzq.docoder.user.vo.SysUserVo;
import cn.yangzq.docoder.user.vo.UserDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
*@author yangzq
*@description 系统用户表 前端控制器
**/
@Api(tags = "系统用户表接口")
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;
    @Autowired
    private DocoderConfig docoderConfig;
    @Autowired
    private PoToVoMapper poToVoMapper;

    @ApiOperation("用户分页列表")
    @GetMapping("/listPage")
    public ResultVo<Pageable<SysUserVo>> getUserPage(SysUserVo param, Pageable<SysUserVo> page){
        return ResultVo.success(userService.getUserPage(param,page));
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/user")
    public ResultVo<SysUserVo> update(@RequestBody SysUser user){
        String userPwd = user.getUserPwd();
        if(StrUtil.isNotBlank(userPwd)){
            user.setUserPwd(SecureUtil.md5(userPwd));
        }
        userService.updateById(user);
        return ResultVo.success();
    }

    @ApiOperation("批量启用用户")
    @PostMapping("/ableByIds")
    public ResultVo<Object> ableByIds(@RequestBody List<Integer> ids){
        userService.updateStatus(ids,1);
        return ResultVo.success();
    }

    @ApiOperation("批量禁用用户")
    @PostMapping("/disableByIds")
    public ResultVo<Object> disableByIds(@RequestBody List<Integer> ids){
        userService.updateStatus(ids,2);
        return ResultVo.success();
    }

    @ApiOperation("批量删除用户")
    @PostMapping("/deleteByIds")
    public ResultVo<Object> deleteByIds(@RequestBody List<Integer> ids){
        userService.updateStatus(ids,-1);
        return ResultVo.success();
    }

    @ApiOperation("通过ID获取用户详情")
    @GetMapping("/{id}")
    public ResultVo<SysUserVo> getUser(@PathVariable("id") Integer id){
        return ResultVo.success(poToVoMapper.toSysUserVo(userService.getById(id)));
    }

    @ApiOperation("通过ID获取用户详情")
    @GetMapping("/getUserDetail/{id}")
    public ResultVo<UserDetailVo> getUserDetail(@PathVariable("id") Integer id){
        return ResultVo.success(userService.getUserDetail(id));
    }

    @ApiOperation("更新用户头像")
    @PostMapping("/avatar")
    public ResultVo<Object> updateAvatar(AvatarForm avatarForm) throws IOException {
        MultipartFile file = avatarForm.getFile();
        Integer userId = avatarForm.getUserId();
        String oldFileName = avatarForm.getOldFileName();

        String avatarUrl = String.format(FilePath.User.AVATAR_URL,userId);

        String originalFilename = file.getOriginalFilename();
        String fileType = "."+originalFilename.split("[.]")[1];
        String filename = "user"+userId+"_"+RandomUtil.randomString(6)+fileType;
        String savePath = avatarUrl+File.separator+filename;
        SysUser user = new SysUser();
        user.setId(userId);
        user.setAvatarUrl(savePath);
        userService.updateById(user);
        FileUtil.del(docoderConfig.getDataDir()+avatarUrl+File.separator+oldFileName);
        FileUtil.writeBytes(file.getBytes(),docoderConfig.getDataDir()+savePath);
        return ResultVo.success(user.getAvatarUrl());
    }

    @ApiOperation("更新用户头像")
    @GetMapping("/showAvatar")
    public ResultVo<Object> avatar(@RequestParam("avatarUrl") String avatarUrl, HttpServletResponse response) throws IOException {
        OutputStream os = null;
        response.setContentType("image/png");
        try {
            BufferedImage img = ImgUtil.read(docoderConfig.getDataDir()+avatarUrl);
            os = response.getOutputStream();
            if(img!=null){
                ImgUtil.write(img,"image/png",os);
            }
        }catch (Exception e){
            throw e;
        }finally {
            if(os!=null){
                os.close();
            }
        }
        return ResultVo.success();
    }

    @ApiOperation("基于rbac的分页查询")
    @GetMapping("/rbacPage")
    public ResultVo<Pageable<SysUserVo>> getRbacPage(RbacParam param, Pageable<PermissionDetail> page){
        return ResultVo.success(userService.getRbacPage(param,page));
    }
}

