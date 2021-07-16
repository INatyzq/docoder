package cn.yangzq.docoder.common.core.config;

import cn.hutool.core.date.DateUtil;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
*@author yangzq
*@description 扩展jackson日期格式化支持格式
**/
public class ObjectMapperDateFormatExtend extends DateFormat {

    private static final long serialVersionUID = -6673860972843060055L;
    private DateFormat dateFormat;

    private static final List<String> formarts = new ArrayList<>(8);

    static {
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd HH:mm");
        formarts.add("yyyy-MM-dd HH:mm:ss");
        formarts.add("yyyy-MM-dd'T'HH:mm:ss");
        formarts.add("yyyy/MM");
        formarts.add("yyyy/MM/dd");
        formarts.add("yyyy/MM/dd HH:mm");
        formarts.add("yyyy/MM/dd HH:mm:ss");
    }

    //构造函数传入objectMapper默认的dateformat
    public ObjectMapperDateFormatExtend(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return new StringBuffer(DateUtil.format(date,"yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public Date parse(String dateSource, ParsePosition pos) {
        String source = dateSource.trim();
        if ("".equals(source)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, formarts.get(0),pos);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, formarts.get(1),pos);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formarts.get(2),pos);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formarts.get(3),pos);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T{1}\\d{1,2}:\\d{1,2}:\\d{1,2}.*")) {
            return parseDate(source, formarts.get(4),pos);
        }else if (source.matches("^\\d{4}/\\d{1,2}$")) {
            return parseDate(source, formarts.get(5),pos);
        } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")) {
            return parseDate(source, formarts.get(6),pos);
        } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formarts.get(7),pos);
        } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formarts.get(8),pos);
        } else {
            throw new ArgumentException("Invalid boolean value '" + source + "'");
        }
    }

    private Date parseDate(String dateStr, String format,ParsePosition pos) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr,pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //此方法在objectMapper 默认的dateformat里边用到，这里也要重写
    @Override
    public Object clone() {
        DateFormat dateFormat = (DateFormat) this.dateFormat.clone();
        return new ObjectMapperDateFormatExtend(dateFormat);
    }
}
