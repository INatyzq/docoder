package cn.yangzq.docoder.user.common;

import java.io.File;

/**
*@author yangzq
*@description 文件路径
*/
public interface FilePath {

    interface User {

        String BASE_PATH = File.separator+"users"+File.separator+"%s";

        String AVATAR_URL = BASE_PATH+File.separator+"avatar";

    }

}
