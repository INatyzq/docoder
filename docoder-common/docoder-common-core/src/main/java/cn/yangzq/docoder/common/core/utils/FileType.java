package cn.yangzq.docoder.common.core.utils;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
*@author yangzq
*@description 文件类型校验工具类
**/
public enum  FileType {

    //图片
    JPEG("FFD8FF",FileType.IMAGE,FileType.MEDIA),
    PNG("89504E470D0A1A0A",FileType.IMAGE,FileType.MEDIA),
    GIF("47494638",FileType.IMAGE,FileType.MEDIA),
    TIFF("49492A00",FileType.IMAGE,FileType.MEDIA),
    TIFF1("4D4D002A",FileType.IMAGE,FileType.MEDIA),
    BMP("424D",FileType.IMAGE,FileType.MEDIA),
    PSD("38425053",FileType.IMAGE,FileType.MEDIA),

    //视频
    RM("2E524D46",FileType.VIDEO,FileType.MEDIA),
    RM1("2E524D58",FileType.VIDEO,FileType.MEDIA),
    RM2("50524F50",FileType.VIDEO,FileType.MEDIA),
    FLV("464C56",FileType.VIDEO,FileType.MEDIA),
    AVI("415649204C495354",FileType.VIDEO,FileType.MEDIA),
    MP4("667479706D703431",FileType.VIDEO,FileType.MEDIA),
    MP41("667479706D703432",FileType.VIDEO,FileType.MEDIA),
    MP42("6674797069736F6D",FileType.VIDEO,FileType.MEDIA),
    _3GP("6674797033677034",FileType.VIDEO,FileType.MEDIA),
    _3GP1("667479706D6D7034",FileType.VIDEO,FileType.MEDIA),
    MTV("414D56",FileType.VIDEO,FileType.MEDIA),

    //音频
    WMA("5C7833305C7832365C7862325C7837355C7838655C7836365C7863665C783131",FileType.AUDIO),
    WAV("57415645666D74",FileType.AUDIO,FileType.MEDIA),
    MP3("5C7866665C786666",FileType.AUDIO,FileType.MEDIA),
    MP31("5C7834395C7834345C783333",FileType.AUDIO,FileType.MEDIA),
    M4A("667479704D34415C783230",FileType.AUDIO,FileType.MEDIA),
    M4A1("667479704D34565C783230",FileType.AUDIO,FileType.MEDIA),
    MDI("4550",FileType.AUDIO,FileType.MEDIA),
    AU("646E73",FileType.AUDIO,FileType.MEDIA),
    AU1("5C7832455C7837335C7836455C783634",FileType.AUDIO,FileType.MEDIA),

    //文档
    XLS("D0CF11E0", FileType.EXCEL),
    XLSX("D0CF11E0", FileType.EXCEL);

    private String value = "";

    private Set<String> categorySet = new HashSet<>();

    //所有
    public static final String ALL = "ALL";
    //图片
    public static final String IMAGE = "图片";
    //视频
    public static final String VIDEO = "视频";
    //音频
    public static final String AUDIO = "音频";
    //表格
    public static final String EXCEL = "表格";
    //图片或视频
    public static final String MEDIA = "图片或视频或音频";


    private FileType(String value,String ...categoryList) {
        this.value = value;
        this.categorySet = new HashSet<>(Arrays.asList(categoryList));
    }

    public String getValue() {
        return value;
    }

    public Set<String> getCategorySet() {
        return categorySet;
    }
}
