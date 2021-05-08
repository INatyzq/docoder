package cn.yangzq.docoder.base.api;

import cn.yangzq.docoder.base.api.ISecurityService;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
*@author yangzq
*@description 安全服务类
**/
@DubboService
@Component
public class SecurityServiceImpl implements ISecurityService {

    @Override
    public Map<String,String> getCaptchaBase64() {
        // 三个参数分别为宽、高、位数
        GifCaptcha captcha = new GifCaptcha (130, 48, 5);
        // 设置字体
        try {
            // 有默认字体，可以不用设置
            captcha.setFont(Captcha.FONT_1);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        // 设置类型，纯数字、纯字母、字母数字混合
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        Map<String,String> result = new HashMap<>(2);
        result.put("captcha",captcha.toBase64());
        result.put("text",captcha.text());
        return result;
    }
}
