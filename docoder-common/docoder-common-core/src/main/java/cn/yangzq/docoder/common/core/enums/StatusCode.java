package cn.yangzq.docoder.common.core.enums;

/**
 * @author yangzq
 * @description 状态码枚举类
 **/
public interface StatusCode {

    //服务内部错误
    int INTERNAL_SERVER_ERROR = 5000;

    //认证失败
    int AUTH_FAILED = 4010;

    //账户不存在
    int ACCOUNT_NOT_FOUND = 4011;

    //账户不存在
    int ACCOUNT_ALREADY_EXIST = 4012;

    //密码错误
    int PASSWORD_INCORRECT = 4013;

    //认证参数缺失
    int AUTH_PARAMETER_MISSING = 4014;

    int AUTH_BUSINESS = 4015;

    //权限拒绝
    int PERMISSION_DENIED = 4030;

    //没有找到资源
    int NOT_FOUND = 4040;

    //未知的文件类型
    int UNKNOWN_FILE_TYPE = 4041;

    //不支持的文件类型
    int NO_SUPPORT_FILE_TYPE = 4042;

}
