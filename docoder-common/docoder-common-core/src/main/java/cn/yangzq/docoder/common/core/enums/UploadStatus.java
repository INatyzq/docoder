package cn.yangzq.docoder.common.core.enums;

/**
*@author yangzq
*@description  FTP上传状态
**/
public enum  UploadStatus {

    Create_Directory_Fail("目录创建失败"),
    File_Exits("文件已存在"),
    Remote_Bigger_Local("远程文件大于本地文件"),
    Upload_From_Break_Failed("断点上传失败"),
    Delete_Remote_Failed("远程删除失败"),
    Create_Directory_Success("目录创建成功"),
    Upload_From_Break_Success("断点上传成功"),
    Upload_New_File_Success("新文件上传成功"),
    Upload_New_File_Failed("新文件上传失败");

    private String msg;

    private UploadStatus(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
