package cn.yangzq.docoder.base.common;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.yangzq.docoder.base.entity.SysAttachment;
import cn.yangzq.docoder.common.core.utils.FileType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
*@author yangzq
*@description 文件路径枚举
*/
public enum FilePathEnum {

    DEFAULT("电子班牌","默认","/editor","classcard/default", FileType.ALL, WebSiteEnum.CLASS_CARD_NOTICE),
    CLASSCARD_ANNOUNCEMENT("电子班牌","通知公告","/editor","/classcard/announcement", FileType.IMAGE, WebSiteEnum.CLASS_CARD_NOTICE),
    CLASSCARD_STUDENT_STYLE("电子班牌","学生风采","/editor","/classcard/student_style",FileType.IMAGE, WebSiteEnum.CLASS_CARD_NOTICE),
    CLASSCARD_SCHOOL_VIDEO("电子班牌","校园/班级视频","/editor","/classcard/school_video",FileType.VIDEO, WebSiteEnum.CLASS_CARD_NOTICE),
    CLASSCARD_HOME_LOGO("电子班牌","首页logo","/editor","/classcard/device_logo",FileType.IMAGE, WebSiteEnum.CLASS_CARD_NOTICE),
    CLASSCARD_SCREENSAVER_IMAGE("电子班牌","设备屏保图片","/editor","/classcard/screensaver/image",FileType.IMAGE, WebSiteEnum.CLASS_CARD_NOTICE),
    CLASSCARD_SCREENSAVER_VIDEO("电子班牌","设备屏保视频","/editor","/classcard/screensaver/video",FileType.VIDEO, WebSiteEnum.CLASS_CARD_NOTICE);

    private static final Map<String,FilePathEnum> filePathMap = new HashMap<>();

    static {
        filePathMap.put("default",DEFAULT);
        filePathMap.put("announcement",CLASSCARD_ANNOUNCEMENT);
        filePathMap.put("studentstyle",CLASSCARD_STUDENT_STYLE);
        filePathMap.put("schoolvideo",CLASSCARD_SCHOOL_VIDEO);
        filePathMap.put("homeLogo",CLASSCARD_HOME_LOGO);
        filePathMap.put("screensaverImage",CLASSCARD_SCREENSAVER_IMAGE);
        filePathMap.put("screensaverVideo",CLASSCARD_SCREENSAVER_VIDEO);
    }

    public static final String SEPARATOR = "/";

    private String moduleName;

    private String functionName;

    private String basePath;

    private String path;

    private String fileType;

    private WebSiteEnum webSite;

    FilePathEnum(String moduleName, String functionName, String basePath, String path, String fileType, WebSiteEnum webSite) {
        this.moduleName = moduleName;
        this.functionName = functionName;
        this.basePath = basePath;
        this.path = path;
        this.fileType = fileType;
        this.webSite = webSite;
    }

    public static FilePathEnum getInstance(String type){
        return filePathMap.get(type);
    }

    /**
     * SysAttachment-构建相关的附件信息
     * @param userId
     * @param filename
     * @param fileSize
     * @return
     */
    public SysAttachment buildAttachment(Integer userId, String filename, double fileSize){

        //日期设置
        Date currentDate =  new Date();

        //构建附件信息
        SysAttachment attachment = new SysAttachment();
        attachment.setOriginalFileName(filename);
        String fileType = filename.substring(filename.lastIndexOf("."));
        //文件名=日期+6随机字符
        String randomStr = RandomUtil.randomString(6);
        String fileName = DateUtil.format(currentDate,"yyyyMMddHHmmss").concat(randomStr).concat(fileType);
        attachment.setFileName(fileName);
        attachment.setFileDesc(this.getModuleName() + "/" + this.getFunctionName());
        attachment.setFileSize(calFileSize(fileSize));
        attachment.setFileType(fileType);
        attachment.setCreatedBy(userId);
        attachment.setCreatedTime(new Date());

        //设置相关资源路径信息
        WebSiteEnum webSite = this.getWebSite();
        String datePath = DateUtil.format(currentDate, "/yyyy/MM/dd/");

        String urlPath = String.format("/%s%s",datePath,fileName);
        attachment.setFilePath(webSite.FTP_HEADER + webSite.getHost() + ":" + webSite.getFtpPort() +
                this.getBasePath()+ this.getPath()+urlPath);

        attachment.setHttpUrl(webSite.HTTP_HEADER + webSite.getHost() + ":" + webSite.getHttpPort() +
                this.getPath() + urlPath);
        return attachment;
    }

    /**
     * SysAttachment-计算文件大小
     * @param size
     * @return
     */
    private double calFileSize(double size) {
        BigDecimal result = new BigDecimal(size).multiply(new BigDecimal("0.0009766"));
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * SysAttachment的http_url、ftp_url
     * FTP站点
     */
    public enum WebSiteEnum{

        CLASS_CARD_NOTICE("119.23.46.3","21","8078");

        private String host;
        private String ftpPort;
        private String httpPort;

        public final String FTP_HEADER = "ftp://";
        public final String HTTP_HEADER = "http://";

        WebSiteEnum(String host, String ftpPort, String httpPort) {
            this.host = host;
            this.ftpPort = ftpPort;
            this.httpPort = httpPort;
        }

        public String getHost() {
            return host;
        }

        public String getFtpPort() {
            return ftpPort;
        }

        public String getHttpPort() {
            return httpPort;
        }
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getPath() {
        return path;
    }

    public String getFileType() {
        return fileType;
    }

    public WebSiteEnum getWebSite() {
        return webSite;
    }

}
