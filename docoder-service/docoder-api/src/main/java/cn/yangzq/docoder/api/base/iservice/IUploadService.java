package cn.yangzq.docoder.api.base.iservice;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author yangzq
 * @description 上传接口
 */
public interface IUploadService {

    /**
     * 上传文件并且返回附件id
     * @param file
     * @param savePath
     * @param objId
     * @return 附件id
     * @author yangzq
     */
    Integer uploadAndSave(MultipartFile file,String savePath,Integer objId);

}
