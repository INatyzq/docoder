package cn.yangzq.docoder.content.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
*@author yangzq
*@description 目录树VO
**/
@ApiModel("目录树VO")
@Data
public class DirTreeVo implements Serializable {

    private static final long serialVersionUID = -1198392432842325022L;

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

    @ApiModelProperty("是否展开")
    private boolean expanded = true;

    @ApiModelProperty("孩子节点列表")
    private List<DirTreeVo> children;

}
