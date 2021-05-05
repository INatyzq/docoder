package cn.yangzq.docoder.user.controller;


import cn.yangzq.docoder.api.base.entity.SysAttachment;
import cn.yangzq.docoder.api.base.iservice.IUploadService;
import cn.yangzq.docoder.api.base.iservice.SysAttachmentService;
import cn.yangzq.docoder.api.user.entity.SysUser;
import cn.yangzq.docoder.api.user.iservice.SysUserService;
import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.common.FilePath;
import cn.yangzq.docoder.user.config.DocoderConfig;
import cn.yangzq.docoder.user.config.UserContextHolder;
import cn.yangzq.docoder.user.maputil.FormToPoMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @DubboReference
    private SysAttachmentService attachmentService;
    @Autowired
    private UserContextHolder userContextHolder;
    @Autowired
    private DocoderConfig docoderConfig;

    @ApiOperation("用户分页列表")
    @GetMapping("/listPage")
    public ResultVo<Pageable<SysUser>> getUserPage(SysUser user, Pageable<SysUser> page){
        return ResultVo.success(userService.getUserPage(user,page));
    }

    @ApiOperation("通过ID获取用户详情")
    @GetMapping("/{id}")
    public ResultVo<SysUser> getDetail(@PathVariable("id") Integer id){
        return ResultVo.success(userService.getById(id));
    }

    @ApiOperation("更新用户头像")
    @PostMapping("/avatar")
    public ResultVo<Object> updateAvatar(@RequestBody MultipartFile file){
        SysAttachment attachment = buildAttachment(file, docoderConfig.getDataDir()+FilePath.User.AVATAR_URL, userContextHolder.getCurrentUser().getId());
        attachmentService.save(attachment);
        return ResultVo.success();
    }

    private SysAttachment buildAttachment(MultipartFile file, String savePath, Integer objId){
        String filename = file.getOriginalFilename();
        String fileType = "."+filename.split("[.]")[1];
        double fileSize = file.getSize()/1024D;
        SysAttachment attachment = new SysAttachment();
        attachment.setObjectId(objId);
        attachment.setFileName(filename);
        attachment.setFilePath(savePath);
        attachment.setFileType(fileType);
        attachment.setFileSize(fileSize);
        attachment.setStatus(1);
        return attachment;
    }
}

