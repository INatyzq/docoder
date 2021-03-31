package cn.yangzq.docoder.common.core.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
*@author yangzq
*@description 文件类型工具类
**/
public class FileTypeUtil {

    private FileTypeUtil(){}

    public static synchronized String getType(InputStream is){
        byte[] data = new byte[0];
        try {
            data = new byte[is.available()];
            is.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //提取前六位作为魔数
        String magicNumberHex = getHex(data, 100);
        FileType[] types = FileType.values();
        for(FileType type:types){
            if(magicNumberHex.contains(type.getValue())){
                return type.getCategory();
            }
        }
        return null;
    }

    public static String getHex(byte[] data, int magicNumberLength) {
        //提取文件的魔数
        StringBuilder magicNumber = new StringBuilder();
        //一个字节对应魔数的两位
        int magicNumberByteLength = magicNumberLength / 2;
        for (int i = 0; i < magicNumberByteLength; i++) {
            magicNumber.append(Integer.toHexString(data[i] >> 4 & 0xF));
            magicNumber.append(Integer.toHexString(data[i] & 0xF));
        }

        return magicNumber.toString().toUpperCase();
    }

}
