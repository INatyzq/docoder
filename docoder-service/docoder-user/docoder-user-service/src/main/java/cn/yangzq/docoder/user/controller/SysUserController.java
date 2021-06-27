package cn.yangzq.docoder.user.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.yangzq.docoder.base.api.ISysAttachmentService;
import cn.yangzq.docoder.base.entity.po.SysAttachment;
import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.AvatarForm;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.service.SysUserService;
import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.common.FilePath;
import cn.yangzq.docoder.user.config.DocoderConfig;
import cn.yangzq.docoder.user.vo.SysPermissionVo;
import cn.yangzq.docoder.user.vo.SysUserVo;
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
import java.util.Date;
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

    @ApiOperation("用户分页列表")
    @GetMapping("/listPage")
    public ResultVo<Pageable<SysUser>> getUserPage(SysUser user, Pageable<SysUser> page){
        return ResultVo.success(userService.getUserPage(user,page));
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/user")
    public ResultVo<SysUser> update(@RequestBody SysUser user){
        userService.updateById(user);
        return ResultVo.success();
    }

    @ApiOperation("通过ID获取用户详情")
    @GetMapping("/{id}")
    public ResultVo<SysUser> getDetail(@PathVariable("id") Integer id){
        return ResultVo.success(userService.getById(id));
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
    public ResultVo<Pageable<SysUserVo>> getRbacPage(RbacParam param, Pageable<SysPermissionVo> page){
        return ResultVo.success(userService.getRbacPage(param,page));
    }
}

