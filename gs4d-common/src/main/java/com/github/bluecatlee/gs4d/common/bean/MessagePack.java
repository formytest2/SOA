package com.github.bluecatlee.gs4d.common.bean;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@JsonPropertyOrder({"code", "message"})
public class MessagePack implements Serializable {

//    @ApiField(description = "返回编号")
    private long code;

//    @ApiField(description = "返回信息")
    private String message = "成功";

    private String fullMessage = null;

    public static long OK = 0L;
    public static long EXCEPTION = -1L;

    public MessagePack() {
    }

    public long getCode() {
        return this.code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFullMessage() {
        return this.fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.ALWAYS);

        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            System.out.println("write to json string error:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String toLowerCaseJson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.ALWAYS);
        mapper.setPropertyNamingStrategy(LowerCaseStrategy.SNAKE_CASE);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(sdf);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            System.out.println("write to json string error:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}

