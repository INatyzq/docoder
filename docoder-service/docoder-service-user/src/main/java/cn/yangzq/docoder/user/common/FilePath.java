package cn.yangzq.docoder.user.common;

/**
*@author yangzq
*@description 文件路径
*/
public interface FilePath {

    public interface User {

        String BASE_PATH = "/users/s%";

        String AVATAR_URL = BASE_PATH+"/avatar";

    }

}
