package cn.yangzq.docoder.common.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*@author yangzq
*@description swagger忽略的参数
**/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreSwaggerParameter {
}
