package cn.yangzq.docoder.security.server.security;

/**
*@author yangzq
*@description 认证管理
**/
public abstract class AuthManage {

    /**
     * 登录 默认终端名称
     * @param id 认证主体唯一标识
     * @param principal 认证的主体信息
     * @return token
     */
    public abstract String login(String id,Object principal);

    /**
     * 登录 指定终端名称
     * @param id 认证主体唯一标识
     * @param moduleName 指定的模块名称
     * @param principal 认证的主体信息
     * @return token
     */
    public abstract String loginWithModule(String id,String moduleName,Object principal);

    /**
     * 登录 指定终端名称
     * @param id 认证主体唯一标识
     * @param terminalName 指定的终端名称
     * @param principal 认证的主体信息
     * @return token
     */
    public abstract String loginWithTerminal(String id,String terminalName,Object principal);

    /**
     * 登录 指定终端名称
     * @param id 认证主体唯一标识
     * @param moduleName 指定的模块名称
     * @param terminalName 指定的终端名称
     * @param principal 认证的主体信息
     * @return token
     */
    public abstract String login(String id,String moduleName,String terminalName,Object principal);

    /**
     * 登出
     */
    public abstract void logout();

}
