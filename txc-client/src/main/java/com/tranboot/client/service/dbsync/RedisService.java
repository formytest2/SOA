//package com.tranboot.client.service.dbsync;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.parser.Feature;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.gb.soa.omp.dbsync.model.RdisSyncModel;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.SerializationException;
//
//public class RedisService {
//    private RedisTemplate<String, RdisSyncModel> dbSyncRedisTemplate;
//
//    public RedisService(RedisTemplate<String, RdisSyncModel> dbSyncRedisTemplate) {
//        this.dbSyncRedisTemplate = dbSyncRedisTemplate;
//    }
//
//    public void kvset(String redisKey, RdisSyncModel rdisSyncModel) {
//        this.dbSyncRedisTemplate.opsForValue().set(redisKey, rdisSyncModel);
//    }
//
//    public RdisSyncModel kvget(String redisKey) {
//        return (RdisSyncModel)this.dbSyncRedisTemplate.opsForValue().get(redisKey);
//    }
//
//    public static String key(String bizKey) {
//        return "waithandlesql_" + System.currentTimeMillis() + "_" + bizKey;
//    }
//
//    public RedisTemplate<String, RdisSyncModel> getDbSyncRedisTemplate() {
//        return this.dbSyncRedisTemplate;
//    }
//
//    public void setDbSyncRedisTemplate(RedisTemplate<String, RdisSyncModel> dbSyncRedisTemplate) {
//        this.dbSyncRedisTemplate = dbSyncRedisTemplate;
//    }
//
//    /** @deprecated */
//    public static final class RdisSyncModelSerializer implements RedisSerializer<RdisSyncModel> {
//        public RdisSyncModelSerializer() {
//        }
//
//        public byte[] serialize(RdisSyncModel t) throws SerializationException {
//            return JSON.toJSONBytes(t, new SerializerFeature[0]);
//        }
//
//        public RdisSyncModel deserialize(byte[] bytes) throws SerializationException {
//            return bytes == null ? null : (RdisSyncModel)JSON.parseObject(bytes, RdisSyncModel.class, new Feature[0]);
//        }
//    }
//}
