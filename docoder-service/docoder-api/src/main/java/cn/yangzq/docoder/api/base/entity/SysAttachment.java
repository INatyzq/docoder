package cn.yangzq.docoder.api.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*@author yangzq
*@description 系统附件表
*/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysAttachment对象", description = "系统附件表")
public class SysAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "业务对象ID")
    private Integer objectId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件路劲")
    private String filePath;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件大小")
    private Double fileSize;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private Integer createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "修改人")
    private Integer updatedBy;

    @ApiModelProperty(value = "修改时间")
    private Date updatedTime;


}
