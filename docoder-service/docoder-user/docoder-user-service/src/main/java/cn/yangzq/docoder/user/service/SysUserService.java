package cn.yangzq.docoder.user.service;

import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.UserRegisterForm;
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
}
