package cn.yangzq.docoder.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yangzq
 * @description 系统用户表
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysUser对象", description = "系统用户表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String userPwd;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "名")
    private String lastName;

    @ApiModelProperty(value = "姓")
    private String firstName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "状态,0:未激活,1:已激活,3:失效")
    private Integer status = 1;

    @ApiModelProperty(value = "性别;1:男;0:女")
    private boolean sex;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = " 创建人")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "修改人")
    private String updatedBy;

    @ApiModelProperty(value = "修改时间")
    private Date updatedTime;


}
