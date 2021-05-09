package cn.yangzq.docoder.user.service;

import cn.yangzq.docoder.user.entity.UserFeature;
import com.baomidou.mybatisplus.extension.service.IService;

/**
*@author yangzq
*@description 用户特性表 服务类
*/
public interface UserFeatureService extends IService<UserFeature> {

    /**
     * 通过userId获取数据
     * @param userId
     * @return
     */
    UserFeature getByUserId(Integer userId);
}
