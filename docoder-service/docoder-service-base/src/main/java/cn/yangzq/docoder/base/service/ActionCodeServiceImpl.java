package cn.yangzq.docoder.base.service;

import cn.hutool.core.util.RandomUtil;
import cn.yangzq.docoder.api.base.iservice.IActionCodeService;
import cn.yangzq.docoder.common.core.utils.RedisUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
*@author yangzq
*@description 操作码 service
**/
@DubboService
@Component
public class ActionCodeServiceImpl implements IActionCodeService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String getActionCode() {
        String randomStr = RandomUtil.randomString(16);
        redisUtil.setEx(KEY_PREFIX+randomStr,randomStr,60, TimeUnit.SECONDS);
        return randomStr;
    }

    @Override
    public String getActionCode(String value) {
        String randomStr = RandomUtil.randomString(16);
        redisUtil.setEx(KEY_PREFIX+randomStr,value,60, TimeUnit.SECONDS);
        return randomStr;
    }

    @Override
    public String getActionCode(String key, String value) {
        redisUtil.setEx(KEY_PREFIX+key,value,60, TimeUnit.SECONDS);
        return key;
    }

    @Override
    public String getAndDelete(String key) {
        String result = null;
        synchronized (this){
            result = redisUtil.get(KEY_PREFIX+key);
            delete(key);
        }
        return result;
    }

    @Override
    public void delete(String key) {
        redisUtil.delete(KEY_PREFIX+key);
    }
}
