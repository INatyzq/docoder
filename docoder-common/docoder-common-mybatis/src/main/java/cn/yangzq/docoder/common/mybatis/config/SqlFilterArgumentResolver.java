package cn.yangzq.docoder.common.mybatis.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
*@author yangzq
*@description 解决Mybatis Plus SQL注入问题
**/
public class SqlFilterArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 可能存在注入操作的关键字
     */
    private final static String[] KEYWORDS = { "master", "truncate", "insert", "select",
            "delete", "update", "declare", "alter", "drop", "sleep" };

    /**
     * 判断Controller是否包含page参数
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();
        Class<?> superclass = parameterType.getSuperclass();
        return parameterType.isAssignableFrom(Page.class)||(superclass.isAssignableFrom(Page.class) && superclass!=Object.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        String orderBy = request.getParameter("orderBy");
        String[] orderItem = new String[]{};
        if(StrUtil.isNotBlank(orderBy)){
            orderItem = orderBy.split(";");
        }

        String current = request.getParameter("current");
        String size = request.getParameter("size");

        Page page = new Page();
        if (StrUtil.isNotBlank(current)) {
            page.setCurrent(Long.parseLong(current));
        }

        if (StrUtil.isNotBlank(size)) {
            page.setSize(Long.parseLong(size));
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        Optional.of(orderItem).ifPresent(orders->{
            orderItemList.addAll(
                    Arrays.stream(orders).filter(sqlInjectPredicate()).map(order->{
                        String[] item = order.split(":");
                        if(item.length==2){
                            boolean asc = "asc".equals(item[1]);
                            return new OrderItem(item[0],asc);
                        }
                        return null;
                    }).collect(Collectors.toList())
            );
        });
        page.addOrder(orderItemList);
        return BeanUtil.copyProperties(page,methodParameter.getParameterType());
    }

    /**
     * 判断用户输入里面有没有注入操作关键字
     * @return Predicate结果
     */
    private Predicate<String> sqlInjectPredicate() {
        return sql -> {
            for (String keyword : KEYWORDS) {
                if (StrUtil.containsIgnoreCase(sql, keyword)) {
                    return false;
                }
            }
            return true;
        };
    }
}
