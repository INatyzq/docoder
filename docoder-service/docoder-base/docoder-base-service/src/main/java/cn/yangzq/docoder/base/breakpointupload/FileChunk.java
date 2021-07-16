package cn.yangzq.docoder.base.breakpointupload;


import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
*@author yangzq
*@description 上传文件的起始、偏移以及大小等
**/
@ToString
@ApiModel("文件分片信息")
@Data
public class FileChunk implements Serializable {

    private static final long serialVersionUID = -6273532286238916336L;

    @ApiModelProperty("当前片数")
    private Integer chunkNumber;

    @ApiModelProperty("每片大小")
    private Long chunkSize;

    @ApiModelProperty("当前片大小")
    private Long currentChunkSize;

    @ApiModelProperty("文件总大小")
    private Long totalSize;

    @ApiModelProperty("文件唯一标识")
    private String identifier;

    @ApiModelProperty("文件名")
    private String filename;

    @ApiModelProperty("保存后的路径")
    private String relativePath;

    @ApiModelProperty("总分片数")
    private Integer totalChunks;

    @ApiModelProperty("文件md5")
    private String md5Str;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
        if(StrUtil.isBlank(this.md5Str)){
            this.md5Str = identifier;
        }
    }
}
