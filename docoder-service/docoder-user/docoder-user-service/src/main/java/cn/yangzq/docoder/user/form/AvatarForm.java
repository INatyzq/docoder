package cn.yangzq.docoder.user.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
*@author yangzq
*@description 修改头像form
*/
@Data
public class AvatarForm {

    private MultipartFile file;

    private Integer userId;

    private String oldFileName;

}
