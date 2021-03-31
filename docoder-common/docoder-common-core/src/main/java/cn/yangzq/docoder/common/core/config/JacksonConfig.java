package cn.yangzq.docoder.common.core.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author yangzq
 * @description Jackson配置类
 **/
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfig {

    /**
     * 属性自定义配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(Locale.CHINA);
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            builder.simpleDateFormat(DatePattern.NORM_DATE_PATTERN);
            builder.simpleDateFormat(DatePattern.NORM_DATETIME_MINUTE_PATTERN);
            builder.serializerByType(Long.class, ToStringSerializer.instance);

            //针对于JDK新时间类。序列化时带有T的问题，自定义格式化字符串
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            builder.modules(javaTimeModule);
        };
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置日期格式
        ObjectMapper objectMapper = new ObjectMapper();
        //关闭序列化的时候没有为属性找到getter方法,报错
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //关闭反序列化的时候，没有找到属性的setter报错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //序列化的时候序列对象的所有属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //反序列化扩展日期格式支持
        DateFormat dateFormat = objectMapper.getDateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+:08:00"));
        ObjectMapperDateFormatExtend dateFormatExtend = new ObjectMapperDateFormatExtend(dateFormat);
        objectMapper.setConfig(objectMapper.getSerializationConfig().with(dateFormatExtend));
        objectMapper.setConfig(objectMapper.getDeserializationConfig().with(dateFormatExtend));
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2HttpMessageConverter;
    }


    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                String fieldName = jsonGenerator.getOutputContext().getCurrentName();
                try {
                    //反射获取字段类型
                    Field field = jsonGenerator.getCurrentValue().getClass().getDeclaredField(fieldName);
                    if (Objects.equals(field.getType(), String.class)) {
                        //字符串型空值""
                        jsonGenerator.writeString("");
                        return;
                    } else if (Objects.equals(field.getType(), List.class)) {
                        //列表型空值返回[]
                        jsonGenerator.writeStartArray();
                        jsonGenerator.writeEndArray();
                        return;
                    } else if (Objects.equals(field.getType(), Map.class)) {
                        //map型空值返回{}
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeEndObject();
                        return;
                    }
                } catch (NoSuchFieldException e) {
                }
                jsonGenerator.writeString("");
            }
        });
        return objectMapper;
    }
}
