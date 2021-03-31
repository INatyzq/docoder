package cn.yangzq.docoder.common.core.utils;


/**
*@author yangzq
*@description 文件类型校验工具类
**/
public enum  FileType {

    JPEG("FFD8FF",FileType.IMAGE),
    PNG("89504E470D0A1A0A",FileType.IMAGE),
    GIF("47494638",FileType.IMAGE),
    TIFF("49492A00",FileType.IMAGE),
    TIFF1("4D4D002A",FileType.IMAGE),
    BMP("424D",FileType.IMAGE),
    PSD("38425053",FileType.IMAGE),

    RM("2E524D46",FileType.VIDEO),
    RM1("2E524D58",FileType.VIDEO),
    RM2("50524F50",FileType.VIDEO),
    FLV("464C56",FileType.VIDEO),
    AVI("415649204C495354",FileType.VIDEO),
    MP4("667479706D703431",FileType.VIDEO),
    MP41("667479706D703432",FileType.VIDEO),
    MP42("6674797069736F6D",FileType.VIDEO),
    _3GP("6674797033677034",FileType.VIDEO),
    _3GP1("667479706D6D7034",FileType.VIDEO),
    MTV("414D56",FileType.VIDEO),

    WMA("5C7833305C7832365C7862325C7837355C7838655C7836365C7863665C783131",FileType.AUDIO),
    WAV("57415645666D74",FileType.AUDIO),
    MP3("5C7866665C786666",FileType.AUDIO),
    MP31("5C7834395C7834345C783333",FileType.AUDIO),
    M4A("667479704D34415C783230",FileType.AUDIO),
    M4A1("667479704D34565C783230",FileType.AUDIO),
    MDI("4550",FileType.AUDIO),
    AU("646E73",FileType.AUDIO),
    AU1("5C7832455C7837335C7836455C783634",FileType.AUDIO);

    private String value = "";

    private String category = "";

    //所有
    public static final String ALL = "ALL";
    //图片
    public static final String IMAGE = "图片";
    //视频
    public static final String VIDEO = "视频";
    //音频
    public static final String AUDIO = "音频";

    private FileType(String value,String category) {
        this.value = value;
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public String getCategory() {
        return category;
    }
}
