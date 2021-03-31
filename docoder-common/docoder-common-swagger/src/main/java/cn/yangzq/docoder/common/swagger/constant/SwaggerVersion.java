package cn.yangzq.docoder.common.swagger.constant;

/**
*@author yangzq
*@description  swagger版本枚举
**/
public enum SwaggerVersion {

    //模块和版本
    MODULE_BASE("基础模块","V1.0"),
    MODULE_COURSE("课程模块","V1.0"),
    MODULE_ATTENDANCE("考勤模块","V1.0"),
    MODULE_CLASS_CARD("班牌模块","V1.0");

    //分组名
    private String groupName;

    //版本
    private String version = "V1.0";

    private SwaggerVersion(String groupName){
        this.groupName = groupName;
    }

    private SwaggerVersion(String groupName,String version){
        this.groupName = groupName;
        this.version = version;
    }

    @Override
    public String toString() {
        return groupName+"_"+version;
    }
}
