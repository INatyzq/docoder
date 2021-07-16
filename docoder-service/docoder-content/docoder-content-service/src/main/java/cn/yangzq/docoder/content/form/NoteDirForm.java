package cn.yangzq.docoder.content.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yangzq
 * @description 笔记目录Form
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "NoteDirForm对象", description = "笔记目录Form")
public class NoteDirForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "父id")
    private Integer parentId;

    @ApiModelProperty(value = "目录名称")
    private String dirName;

    @ApiModelProperty(value = "目录描述")
    private String dirDesc;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序序号")
    private Integer orderNum;

}
