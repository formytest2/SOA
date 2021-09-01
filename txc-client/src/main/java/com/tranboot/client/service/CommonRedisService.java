package com.tranboot.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class CommonRedisService<K, V, HK, HV> {
    private RedisTemplate<K, V> redisTemplate = new RedisTemplate();

    public CommonRedisService() {
        this.redisTemplate.setHashKeySerializer(new CommonRedisService.CommonJsonSerializer((Class)null));
        this.redisTemplate.setHashValueSerializer(new CommonRedisService.CommonJsonSerializer((Class)null));
    }

    public static final class CommonJsonSerializer<T> implements RedisSerializer<T> {
        private Class<T> clazz;

        public CommonJsonSerializer(Class<T> clazz) {
            this.clazz = clazz;
        }

        public byte[] serialize(T t) throws SerializationException {
            return JSON.toJSONBytes(t, new SerializerFeature[0]);
        }

        public T deserialize(byte[] bytes) throws SerializationException {
            return bytes == null ? null : JSON.parseObject(bytes, this.clazz, new Feature[0]);
        }
    }
}
