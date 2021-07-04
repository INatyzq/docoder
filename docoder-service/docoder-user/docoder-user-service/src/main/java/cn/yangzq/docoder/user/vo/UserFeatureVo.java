package cn.yangzq.docoder.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yangzq
 * @description 用户特性Vo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UserFeature对象", description = "用户特性Vo")
public class UserFeatureVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
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

}
