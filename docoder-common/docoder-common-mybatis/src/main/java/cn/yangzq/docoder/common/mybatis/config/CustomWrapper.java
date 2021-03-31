package cn.yangzq.docoder.common.mybatis.config;

import com.google.common.base.CaseFormat;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;

import java.util.Map;

/**
*@author yangzq
*@description CustomWrapper
**/
public class CustomWrapper extends MapWrapper {

    public CustomWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject, map);
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        if(useCamelCaseMapping){
            //guava库CaseFormat,里面有转换驼峰的,免得自己重复造轮子,pom添加
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,name);
        }
        return name;
    }
}
