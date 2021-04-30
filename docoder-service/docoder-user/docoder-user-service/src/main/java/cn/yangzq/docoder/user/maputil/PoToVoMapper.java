package cn.yangzq.docoder.user.maputil;

import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.vo.PermissionVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
*@author yangzq
*@description PO转VO mapper
**/
@Mapper(componentModel = "spring")
public interface PoToVoMapper {

    List<PermissionVO> permission(List<SysPermission> permissions);

}
