package cn.yangzq.docoder.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yangzq
 * @description 用户特性表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UserFeature对象", description = "用户特性表")
public class UserFeature implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "等级成长值")
    private Integer levelVal;

    @ApiModelProperty(value = "标签")
    private String tags;

    @ApiModelProperty(value = "爱好")
    private String hobby;

    @ApiModelProperty(value = "个性签名")
    private String signature;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;


}
