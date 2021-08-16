package com.github.bluecatlee.gs4d.message.consumer.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;

import java.io.Serializable;

public class MessagePack implements Serializable {

    private long code;
    private String message = "成功";
    private String consumerRes;
    public static long OK = 0;
    public static long EXCEPTION = -1;

    private static MyJsonMapper mapper;
    private static MyJsonMapper snakeMapper;
    static {
        mapper = new MyJsonMapper(Include.ALWAYS);
        snakeMapper = new MyJsonMapper(Include.ALWAYS);
        snakeMapper.getMapper().setPropertyNamingStrategy(MyJsonMapper.UPPER_SNAKE_CASE_STRATEGY);
    }

    public MessagePack() {
    }

    public MessagePack(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @JsonProperty("Code")
    public long getCode() {
        return this.code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    @JsonProperty("Message")
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("CONSUMER_RES")
    public String getConsumerRes() {
        return this.consumerRes;
    }

    public void setConsumerRes(String consumerRes) {
        this.consumerRes = consumerRes;
    }

    public String toJson() {
        return mapper.toJson(this);
    }

    public String toUpperSnakeCaseJson() {
        return snakeMapper.toJson(this);
    }

}
