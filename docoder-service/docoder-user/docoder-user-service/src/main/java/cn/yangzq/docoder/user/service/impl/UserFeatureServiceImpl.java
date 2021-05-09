package cn.yangzq.docoder.user.service.impl;

import cn.yangzq.docoder.user.entity.UserFeature;
import cn.yangzq.docoder.user.mapper.UserFeatureMapper;
import cn.yangzq.docoder.user.service.UserFeatureService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
*@author yangzq
*@description 用户特性表 服务实现类
*/
@Transactional
@Service
public class UserFeatureServiceImpl extends ServiceImpl<UserFeatureMapper, UserFeature> implements UserFeatureService {

    @Autowired
    private UserFeatureMapper featureMapper;

    @Override
    public UserFeature getByUserId(Integer userId) {
        QueryWrapper<UserFeature> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_ID",userId);
        return featureMapper.selectOne(wrapper);
    }
}
