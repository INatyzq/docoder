package cn.yangzq.docoder.api.user.iservice;

import cn.yangzq.docoder.api.user.entity.SysUser;
import cn.yangzq.docoder.api.user.form.UserLoginForm;
import cn.yangzq.docoder.api.user.form.UserRegisterForm;
import cn.yangzq.docoder.api.user.vo.UserDetailVO;
import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import com.baomidou.mybatisplus.extension.service.IService;

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
    UserDetailVO login(UserLoginForm form);

    /**
     * 获取用户分页列表
     * @param user
     * @param page
     * @return
     */
    Pageable<SysUser> getUserPage(SysUser user, Pageable<SysUser> page);
}
