package cn.yangzq.docoder.user.service;

import cn.yangzq.docoder.user.entity.PermissionDetail;
import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.UserLoginForm;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.vo.SysUserVo;
import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.vo.UserDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
*@author yangzq
*@description 系统用户表 服务类
**/
public interface SysUserService extends IService<SysUser> {

    /**
     * 是否已存在用户(用户名、邮箱)
    * @param form
    * @return 是否已存在
    */
    boolean isRepeat(UserRegisterForm form);

    /**
     * 登录
     * @param form
     * @return
     */
    UserDetailVo login(UserLoginForm form);

    /**
     * 获取用户详情
     * @param userId
     * @return
     */
    UserDetailVo getUserDetail(Integer userId);

    /**
     * 获取用户详情
     * @param userName
     * @return
     */
    UserDetailVo getUserDetail(String userName);

    /**
     * 获取用户分页列表
     * @param param
     * @param page
     * @return
     */
    Pageable<SysUserVo> getUserPage(SysUserVo param, Pageable<SysUserVo> page);

    /**
     * 刷新
     * @param id
     * @return
     */
    UserDetailVo refresh(Integer id);

    /**
     * 基于rbac的分页查询
     * @param param
     * @param page
     * @return
     */
    Pageable<SysUserVo> getRbacPage(RbacParam param, Pageable<PermissionDetail> page);

    /**
     * 批量更新用户状态
     * @param ids
     * @param status
     */
    void updateStatus(List<Integer> ids,Integer status);
}
