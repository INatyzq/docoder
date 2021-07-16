package cn.yangzq.docoder.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
*@author yangzq
*@description 系统附件VO
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysAttachment对象", description = "系统附件VO")
public class AttachmentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "业务对象ID")
    private Integer objectId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "原文件名称")
    private String originalFileName;

    @ApiModelProperty(value = "文件描述")
    private String fileDesc;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件大小")
    private Double fileSize;

    @ApiModelProperty(value = "httpUrl")
    private String httpUrl;
}
