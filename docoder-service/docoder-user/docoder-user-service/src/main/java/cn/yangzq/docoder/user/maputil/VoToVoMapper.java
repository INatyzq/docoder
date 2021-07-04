package cn.yangzq.docoder.user.maputil;

import cn.yangzq.docoder.user.vo.UserAuthDetailVO;
import cn.yangzq.docoder.user.vo.UserDetailVO;
import org.mapstruct.Mapper;


/**
*@author yangzq
*@description VO转VO mapper
**/
@Mapper(componentModel = "spring")
public interface VoToVoMapper {

    UserAuthDetailVO userDetail(UserDetailVO detail);

}
