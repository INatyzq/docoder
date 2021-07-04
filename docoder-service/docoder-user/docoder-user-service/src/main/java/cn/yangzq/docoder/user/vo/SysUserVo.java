package cn.yangzq.docoder.user.vo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yangzq
 * @description 系统用户VO
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "系统用户VO", description = "系统用户VO")
public class SysUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "姓名")
    private String fullName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "状态,0:未激活,1:已激活,3:失效")
    private Integer status = 1;

    @ApiModelProperty(value = "性别;1:男;0:女")
    private Integer sex;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "用户头像url")
    private String avatarUrl;

    public void setAvatarUrl(String avatarUrl) {
        if(StrUtil.isNotBlank(avatarUrl)){
            avatarUrl = avatarUrl.replace("\\", "/");
            this.avatarUrl = avatarUrl;
        }
    }
}
