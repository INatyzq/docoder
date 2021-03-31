package cn.yangzq.docoder.user.maputil;

import cn.yangzq.docoder.user.entity.SysUser;
import cn.yangzq.docoder.user.form.UserLoginForm;
import cn.yangzq.docoder.user.form.UserRegisterForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
*@author yangzq
*@description 表单到PO的转换Mapper
**/
@Mapper(componentModel = "spring")
public interface FormToPOMapper {

    FormToPOMapper INSTANCE = Mappers.getMapper(FormToPOMapper.class);

    @Mappings({})
    SysUser loginForm(UserLoginForm loginForm);

    @Mappings({})
    SysUser registerForm(UserRegisterForm registerForm);
}
