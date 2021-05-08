package cn.yangzq.docoder.base.api;


/**
*@author yangzq
*@description 操作码 interface
**/
public interface IActionCodeService {

    String KEY_PREFIX = "ACTION:";

    /**
     * 获取操作码
    * @return: 操作码
    */
    String getActionCode();

    /**
     * 自定义值 返回键
     * @param value 自定义值
     * @return: 操作码
     */
    String getActionCode(String value);

    /**
     * 自定义键值 返回键
     * @param key 自定义键
     * @param value 自定义值
     * @return: 操作码
     */
    String getActionCode(String key,String value);

    /**
     * 获取并删除
    * @param key
    * @return 操作码
    */
    String getAndDelete(String key);

    /**
     * 删除
     * @param key 键
    **/
    void delete(String key);
}
