package cn.yangzq.docoder.common.mybatis.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;

/**
*@author yangzq
*@description  mybatis plus分页实体
**/
@ApiModel("分页对象")
public class Pageable<T> extends Page<T> {

    private static final long serialVersionUID = -6213633152674389294L;

}
