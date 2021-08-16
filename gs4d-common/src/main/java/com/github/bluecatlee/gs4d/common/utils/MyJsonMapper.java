package com.github.bluecatlee.gs4d.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class MyJsonMapper {

    private static Logger logger = LoggerFactory.getLogger(MyJsonMapper.class);
    public static final PropertyNamingStrategy UPPER_SNAKE_CASE_STRATEGY = new MyJsonMapper.UpperSnakeCaseStrategy();
    private ObjectMapper mapper;

    public MyJsonMapper() {
        this((JsonInclude.Include)null);
    }

    public MyJsonMapper(JsonInclude.Include include) {
        this.mapper = new ObjectMapper();
        if (include != null) {
            this.mapper.setSerializationInclusion(include);
        }
        // 禁用 反序列化遇到未知字段抛异常的特性
        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public ObjectMapper getMapper() {
        return this.mapper;
    }

    // 属性为空(null)时不参与序列化
    public static MyJsonMapper nonEmptyMapper() {
        return new MyJsonMapper(JsonInclude.Include.NON_EMPTY);
    }

    // 属性为默认值时不参与序列化
    public static MyJsonMapper nonDefaultMapper() {
        return new MyJsonMapper(JsonInclude.Include.NON_DEFAULT);
    }

    public String toJson(Object object) {
        try {
            return this.mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                return this.mapper.readValue(jsonString, clazz);
            } catch (IOException e) {
                logger.warn("parse json string error:" + jsonString, e);
                return null;
            }
        }
    }

    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                return this.mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                logger.warn("parse json string error:" + jsonString, e);
                return null;
            }
        }
    }

    public JavaType contructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return this.mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    public JavaType contructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return this.mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    public void update(String jsonString, Object object) {
        try {
            this.mapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }

    }

    public String toJsonP(String functionName, Object object) {
        return this.toJson(new JSONPObject(functionName, object));
    }

    public void enableEnumUseToString() {
        this.mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);           // 序列化枚举类型值的时候使用枚举的toString()方法,默认使用name()方法
        this.mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    public void enableJaxbAnnotation() {
        JaxbAnnotationModule module = new JaxbAnnotationModule();                       // 启用JaxbAnnotation模块
        this.mapper.registerModule(module);
    }

    /**
     * 自定义蛇形大写命名策略
     */
    public static class UpperSnakeCaseStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase {
        private static final long serialVersionUID = 1L;

        public UpperSnakeCaseStrategy() {
        }

        public String translate(String input) {
            if (input == null) {
                return input;
//            } else if ("code".equals(input)) {
//                return "Code";
//            } else if ("message".equals(input)) {
//                return "Message";
            } else {
                int length = input.length();
                StringBuilder result = new StringBuilder(length * 2);
                int resultLength = 0;
                boolean wasPrevTranslated = false;

                for(int i = 0; i < length; ++i) {
                    char c = input.charAt(i);
                    if (i > 0 || c != '_') {
                        if (Character.isUpperCase(c)) {
                            if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
                                result.append('_');
                                ++resultLength;
                            }

                            c = Character.toLowerCase(c);
                            wasPrevTranslated = true;
                        } else {
                            wasPrevTranslated = false;
                        }

                        result.append(c);
                        ++resultLength;
                    }
                }

                return resultLength > 0 ? result.toString().toUpperCase() : input;
            }
        }
    }

}

