package cn.yangzq.docoder.user.api;

import cn.yangzq.docoder.user.entity.UserDetail;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
*@author yangzq
*@description 用户服务实现类
**/
@DubboService
@Component
public class UserService implements IUserService{

    @Override
    public UserDetail getUserDetail() {
        return null;
    }

    @Override
    public UserDetail getUserDetail(String token) {
        return null;
    }
}
