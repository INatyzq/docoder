package cn.yangzq.docoder.user.api;

import cn.yangzq.docoder.user.entity.UserDetail;

/**
*@author yangzq
*@description 用户接口
**/
public interface IUserService {

    /**
     * 获取认证用户详情
     * @return UserDetail
     */
    UserDetail getUserDetail();

    /**
     * 获取认证用户详情
     * @return UserDetail
     */
    UserDetail getUserDetail(String token);

}
