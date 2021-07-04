package cn.yangzq.docoder.user.service;

import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.UserLoginForm;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.vo.SysPermissionVo;
import cn.yangzq.docoder.user.vo.SysUserVo;
import cn.yangzq.docoder.user.vo.UserAuthDetailVO;
import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.vo.UserDetailVO;
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
    UserAuthDetailVO login(UserLoginForm form);

    /**
     * 获取用户详情
     * @param userId
     * @return
     */
    UserDetailVO getUserDetail(Integer userId);

    /**
     * 获取用户详情
     * @param userName
     * @return
     */
    UserDetailVO getUserDetail(String userName);

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
    UserAuthDetailVO refresh(Integer id);

    /**
     * 基于rbac的分页查询
     * @param param
     * @param page
     * @return
     */
    Pageable<SysUserVo> getRbacPage(RbacParam param, Pageable<SysPermissionVo> page);

    /**
     * 批量更新用户状态
     * @param ids
     * @param status
     */
    void updateStatus(List<Integer> ids,Integer status);
}
