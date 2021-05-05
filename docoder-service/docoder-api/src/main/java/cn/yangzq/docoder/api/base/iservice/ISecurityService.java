package cn.yangzq.docoder.api.base.iservice;

import java.util.Map;

/**
*@author yangzq
*@description  安全相关 interface
**/
public interface ISecurityService {

    /**
     * 获取验证码base64
     * @return Map{captcha:base64,text:验证码文字}
    */
    Map<String,String> getCaptchaBase64();

}
