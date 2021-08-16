package com.github.bluecatlee.gs4d.sequence.service.impl;

import com.github.bluecatlee.gs4d.common.utils.lock.RedisLock;
import com.github.bluecatlee.gs4d.sequence.dao.AutoSequenceDao;
import com.github.bluecatlee.gs4d.sequence.dao.PlatformOfflineSubUnitSequenceDao;
import com.github.bluecatlee.gs4d.sequence.dao.SequenceDao;
import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import com.github.bluecatlee.gs4d.sequence.model.CreateSequence;
import com.github.bluecatlee.gs4d.sequence.model.PlatformAutoSequence;
import com.github.bluecatlee.gs4d.sequence.model.PlatformOfflineSequence;
import com.github.bluecatlee.gs4d.sequence.model.PlatformOfflineSubUnitSequence;
import com.github.bluecatlee.gs4d.sequence.service.SequenceActionService;
import com.github.bluecatlee.gs4d.sequence.service.SequenceTimeService;
import com.github.bluecatlee.gs4d.sequence.utils.DateUtil;
import com.github.bluecatlee.gs4d.sequence.utils.SeqStringUtil;
import net.sf.json.JSONObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("sequenceActionService")
public class SequenceActionServiceImpl implements SequenceActionService, Watcher {

    @Autowired
    private SequenceDao sequenceDAO;

    @Autowired
    private AutoSequenceDao autoSequenceDao;

    @Autowired
    private PlatformOfflineSubUnitSequenceDao platformOfflineSubUnitSequenceDao;

    @Autowired
    private SequenceTimeService sequenceTimeService;

//    public static ZooKeeper a;
//
//    public static String c = "/seqNode";
//
//    public static String d = "/seqNode/generaterMaxNum";
//
//    public static String e = "/seqNode/generaterMaxValue";
//
//    public static String f = "/seqNode/currentDayDate";

//    public static CuratorFramework curatorFramework;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

//    @Value("#{settings['zk.sequence.host.port']}")
//    public String zkAdress;

    protected static Logger logger = LoggerFactory.getLogger(SequenceActionServiceImpl.class);

//    @PostConstruct
//    private void post() throws SequenceException {
//        try {
//            ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
//            CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(this.zkAdress, retryPolicy);
//            curatorFramework.start();
//            curatorFramework = curatorFramework;
//            TreeCache treeCache = new TreeCache(curatorFramework, "/seqlocks");
//            treeCache.getListenable().addListener(new TreeCacheListener() {
//                public void childEvent(CuratorFramework cf, TreeCacheEvent treeCacheEvent) {
//                    if (treeCacheEvent.getType().name().equals("CONNECTION_SUSPENDED") || treeCacheEvent.getType().name().equals("CONNECTION_LOST")) {
//                        SequenceActionServiceImpl.curatorFramework.close();
//                        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(5000, 3);
//                        SequenceActionServiceImpl.curatorFramework = CuratorFrameworkFactory.newClient(SequenceActionServiceImpl.this.zkAdress, exponentialBackoffRetry);
//                        SequenceActionServiceImpl.curatorFramework.start();
//                    }
//
//                }
//            });
//            treeCache.start();
//            this.sequenceTimeService.editTime();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            throw new SequenceException(e.getMessage());
//        }
//    }

    public void process(WatchedEvent paramWatchedEvent) {}

    public void createSeq(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) throws KeeperException, InterruptedException, IOException {
        if (getCountBy(paramString2, paramString3).booleanValue()) {
            CreateSequence createSequence = new CreateSequence();
            long l = (new Date()).getTime();
            createSequence.setSeries(Long.valueOf(l));
            createSequence.setSeqProject(paramString2);
            createSequence.setSeqName(paramString3);
            createSequence.setSeqPrefix(paramString4);
            createSequence.setSeqVal(paramString5);
            createSequence.setSeqNum(paramString6);
            createSequence.setCreateTime(new Date());
            createSequence.setCurrentNum(Long.valueOf(1L));
            insertSeq(createSequence);
        }
    }

    public void insertSeq(CreateSequence paramCreateSequence) {
        try {
            this.sequenceDAO.insertSeq(paramCreateSequence);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new SequenceException("数据库添加序列信息失败" + exception.getMessage());
        }
    }

    /**
     * 根据序列名称查询序列信息
     * @param paramCreateSequence
     * @return
     */
    public List<CreateSequence> getSequence(CreateSequence paramCreateSequence) {
        try {
            return this.sequenceDAO.getSequence(paramCreateSequence);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new SequenceException("查询数据库序列信息失败" + exception.getMessage());
        }
    }

    /**
     * 根据series更新sequence的当前序列值 current_sequence
     * @param paramCreateSequence
     */
    public void updateSeqValnum(CreateSequence paramCreateSequence) {
        try {
            this.sequenceDAO.updateSeqValnum(paramCreateSequence);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new SequenceException(exception.getMessage());
        }
    }

    public void updateSeqValnumAndSeqnum(CreateSequence paramCreateSequence) {
        try {
            this.sequenceDAO.updateSeqValnumAndSeqnum(paramCreateSequence);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new SequenceException("数据库更新序列信息失败"+ exception.getMessage());
        }
    }

    public CreateSequence getSequenceToClient(CreateSequence paramCreateSequence) {
        RedisLock redisLock = null;
        List<CreateSequence> list = null;
        String sequenceTime = "";
        Boolean bool = Boolean.valueOf(true);
        try {
            if (getCountBy(paramCreateSequence.getSeqProject(), paramCreateSequence.getSeqName()).booleanValue()) {
                throw new SequenceException(paramCreateSequence.getSeqName() + "序列在数据库中不存在");
            }
            long l = System.currentTimeMillis();
            String seqName = paramCreateSequence.getSeqName();
            String seqKey = seqName + "_common";
            boolean bool1 = isSpecialTime("common");
            if (bool1) {
                seqKey = "update_job_lock";
            }
            redisLock = new RedisLock(this.stringRedisTemplate, seqKey, 10, 30, 30);
            if (!redisLock.tryLock()) {
                bool = Boolean.valueOf(false);
                logger.info("=============获取锁超时时间" + (System.currentTimeMillis() - l));
                throw new Exception("Redis分布式锁超时,序列号名称为" + seqName);
            }
            logger.info("获取到的redis的key是: " + seqKey);
            list = getSequence(paramCreateSequence);
            sequenceTime = this.sequenceTimeService.getTime();           // 序列时间
            if (list.size() > 0) {
                for (CreateSequence createSequence : list) {
                    createSequence.setSquenceTime(sequenceTime);
                    if (null != createSequence) {
                        Long newNum = Long.valueOf(createSequence.getCurrentNum().longValue() + 1L);        // 当前序列值+1
                        String seqNum = createSequence.getSeqNum();
                        Long seqNumEnd = createSequence.getSeqNumEnd();
                        Long series = createSequence.getSeries();
                        if (seqNumEnd != null && seqNumEnd.intValue() > 0) {
                            if (newNum.intValue() > seqNumEnd.intValue()) {
                                throw new SequenceException("序列名[" + createSequence.getSeqName() + "]的currentNum超出结束值范围");
                            }
                        } else if (newNum.intValue() > Integer.valueOf(seqNum).intValue()) {
                            throw new SequenceException("当前num值超出最大num值范围");
                        }
                        paramCreateSequence.setCurrentNum(newNum);
                        paramCreateSequence.setSeries(series);
                        updateSeqValnum(paramCreateSequence);       // 更新新的sequence
                        logger.info("序列名为：" + createSequence.getSeqName() + " 当前currentNum为: " + createSequence.getCurrentNum());
                        return createSequence;                      // 返回旧的sequence
                    }
                }
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new SequenceException(exception.getMessage());
        } finally {
            if (redisLock != null && bool.booleanValue()) {
                redisLock.unlock();
                logger.info("解锁时间： "+ System.currentTimeMillis());
            }
        }
        return null;
    }

    /**
     * 获取序列名在表中记录数 有记录则返回false ...
     * @param paramString1
     * @param paramString2
     * @return
     */
    public Boolean getCountBy(String paramString1, String paramString2) {
        boolean bool = true;
        Integer integer = Integer.valueOf(0);
        try {
            integer = this.sequenceDAO.getCountBy(paramString1, paramString2.trim());
            if (integer.intValue() > 0) {
                bool = false;
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new SequenceException("查询数据库序列数量失败"+ exception.getMessage());
        }
        return Boolean.valueOf(bool);
    }

    public List<CreateSequence> getSequenceModelToClientCheck(CreateSequence paramCreateSequence) {
        List<CreateSequence> list = getSequence(paramCreateSequence);
        if (list.size() > 0) {
            String str = this.sequenceTimeService.getTime();
            for (CreateSequence createSequence : list)
                createSequence.setSquenceTime(str);
        }
        return list;
    }

    public String getAutomicSeq(String seqName, Integer num, Long tenantNumId, Long dataSign) {
        long l1 = System.currentTimeMillis();
        RedisLock redisLock = null;
        Boolean bool = Boolean.valueOf(true);
        Long flowCode = null;
        List<PlatformAutoSequence> list = null;
        PlatformAutoSequence platformAutoSequence = null;
        StringBuffer stringBuffer = new StringBuffer();
        String autoSeqKey = seqName + "_auto";
        boolean bool1 = isSpecialTime("auto");          // 在autoSenquence进行清除的时间段内 锁需要对应的清除任务使用的锁 保证数据一致
        if (bool1) {
            autoSeqKey = "sequence_auto_clear";
        }
        try {
            redisLock = new RedisLock(this.stringRedisTemplate, autoSeqKey, 10, 40, 10);
            if (!redisLock.tryLock()) {
                bool = Boolean.valueOf(false);
                logger.info("=============获取锁超时时间: " + (System.currentTimeMillis() - l1));
                throw new Exception("Redis分布式锁超时，序列名称为：" + seqName);
            }
            logger.info("获取到的redis的key是" + autoSeqKey);
            String cacheFlowCodeKey = seqName + tenantNumId + dataSign + "_auto_single";            // {seqName}{tenantNumId}{dataSign}_auto_single         缓存值
            if (tenantNumId.longValue() == 0L && dataSign.longValue() == 0L) {
                cacheFlowCodeKey = seqName.trim() + "_auto_com";
            }
            String cacheFlowCodeValue = "";
            String cacheSequenceConfigKey = cacheFlowCodeKey + "_info_key";                         // {seqName}{tenantNumId}{dataSign}_auto_single_info_key  缓存配置
            String cacheSequenceConfigValue = "";
            try {
                if (num.intValue() == -2) {
                    this.stringRedisTemplate.delete(cacheFlowCodeKey);
                    this.stringRedisTemplate.delete(cacheSequenceConfigKey);
                }
                cacheFlowCodeValue = (String)this.stringRedisTemplate.opsForValue().get(cacheFlowCodeKey);
                cacheSequenceConfigValue = (String)this.stringRedisTemplate.opsForValue().get(cacheSequenceConfigKey);
            } catch (Exception exception) {
                this.stringRedisTemplate.opsForValue().set(cacheFlowCodeKey, "");
                this.stringRedisTemplate.opsForValue().set(cacheSequenceConfigKey, "");
                logger.error(exception.getMessage(), exception);
                throw new SequenceException("redis服务器异常！" + exception.getMessage());
            }
            if (cacheSequenceConfigValue == null || cacheSequenceConfigValue.trim().equals("")) {
                // 缓存中没有配置时 查数据库 并设置到缓存中
                list = this.autoSequenceDao.getAutoSequenceInfo(seqName, tenantNumId, dataSign);
                if (list.isEmpty() || list.size() <= 0) {
                    throw new SequenceException("在platform_auto_sequence表中未查询到匹配的序列信息，序列名称为：" + seqName);
                }
                platformAutoSequence = list.get(0);
                String autosequence = JSONObject.fromObject(platformAutoSequence).toString();
                this.stringRedisTemplate.opsForValue().set(cacheSequenceConfigKey, autosequence);
            } else {
                JSONObject jSONObject = JSONObject.fromObject(cacheSequenceConfigValue);
                platformAutoSequence = (PlatformAutoSequence)JSONObject.toBean(jSONObject, PlatformAutoSequence.class);
            }
            Long initValue = platformAutoSequence.getInitValue();
            Long currentNum = platformAutoSequence.getCurrentNum();
            Long series = platformAutoSequence.getSeries();
            num = platformAutoSequence.getCacheNum();
            if (cacheFlowCodeValue == null || cacheFlowCodeValue.trim().equals("")) {
                // 缓存中没有流水号时
                if (currentNum.longValue() == 1L || (initValue != null && initValue == currentNum)) {
                    flowCode = currentNum;
                    this.autoSequenceDao.updateAutoCurrentVal(Long.valueOf(currentNum.longValue() + num.intValue()), series);   // 初始情况 todo 这里数据库和缓存不一致了 感觉不应该加cache_num
                    this.stringRedisTemplate.opsForValue().set(cacheFlowCodeKey, String.valueOf(flowCode));
                } else {
                    flowCode = Long.valueOf(currentNum.longValue() + num.intValue());  // 不是初始情况但是缓存中没有 考虑是缓存丢失的情况 利用cache_num可以恢复并避免重复
                    this.stringRedisTemplate.opsForValue().set(cacheFlowCodeKey, String.valueOf(flowCode));
                    this.autoSequenceDao.updateAutoCurrentVal(flowCode, series);
                }
            } else {
                Long cacheFlowCode = Long.valueOf(cacheFlowCodeValue);
                flowCode = Long.valueOf(cacheFlowCode.longValue() + 1L);                // 流水号自增并写回缓存 一定条件(cache_num的整数倍时)下刷回数据库
                if (cacheFlowCode.longValue() % num.intValue() == 0L) {
                    this.autoSequenceDao.updateAutoCurrentVal(flowCode, series);
                    logger.info(">>>>>>>>>更新数据库：" + (System.currentTimeMillis() - l1));
                }
                this.stringRedisTemplate.opsForValue().set(cacheFlowCodeKey, String.valueOf(flowCode));
            }
            String seqPrefix = platformAutoSequence.getSeqPrefix().trim();
            Integer isYear = platformAutoSequence.getIsYear();
            Integer isMonth = platformAutoSequence.getIsMonth();
            Integer isDay = platformAutoSequence.getIsDay();
            Integer isFlowCode = platformAutoSequence.getIsFlowCode();
            stringBuffer.append(seqPrefix);         // [prefix] [yy] [MM] [dd] [flowCode(补零)]
            Date date = new Date();
            if (isYear.intValue() == 6)
                stringBuffer.append(DateUtil.safeParse(date, "yy"));
            if (isMonth.intValue() == 6)
                stringBuffer.append(DateUtil.safeParse(date, "MM"));
            if (isDay.intValue() == 6)
                stringBuffer.append(DateUtil.safeParse(date, "dd"));
            if (isFlowCode.intValue() == 6) {
                Long flowCodeLength = platformAutoSequence.getFlowCodeLength();
                if (flowCodeLength.longValue() > 0L) {
                    int i = flowCode.toString().length();
                    if (i <= flowCodeLength.intValue()) {
                        String flowCodeP = SeqStringUtil.frontCompWithZore(flowCode, flowCodeLength.intValue());
                        stringBuffer.append(flowCodeP);
                    } else if (i > flowCodeLength.intValue()) {
                        throw new SequenceException("流水号的长度超过指定长度，自增长序列为：" + seqName);
                    }
                } else {
                    stringBuffer.append(flowCode);
                }
            }
        } catch (Exception exception) {
            logger.info("抛异常时间： " + System.currentTimeMillis());
            logger.error(exception.getMessage(), exception);
            throw new SequenceException("获取自增长序列失败! " + exception.getMessage());
        } finally {
            if (redisLock != null && bool.booleanValue()) {
                redisLock.unlock();
                logger.info("解锁时间" + System.currentTimeMillis());
            }
        }
        long l2 = System.currentTimeMillis() - l1;
        if (l2 > 100L) {
            logger.info("****************************" + (System.currentTimeMillis() - l1));
        }
        logger.info("=======获取自增序列结束时间：" + (System.currentTimeMillis() - l1));
        return stringBuffer.toString();
    }

    public Integer getSeqStoreStatus(String paramString) {
        Integer integer = Integer.valueOf(0);
        try {
            integer = this.sequenceDAO.getSeqStoreStatus(paramString.trim());
            if (integer == null)
                integer = Integer.valueOf(0);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new SequenceException("查询数据库序列存储方式失败！" + exception.getMessage());
        }
        return integer;
    }

    @Deprecated
    public List<Map<String, Object>> getOfflineSeqInfo(Long subUnitNumId) {
        ArrayList<HashMap<Object, Object>> arrayList = new ArrayList();
        List<PlatformOfflineSequence> list1 = null;
        List<PlatformOfflineSubUnitSequence> list2 = null;
        try {
            if (subUnitNumId == null) {
                throw new SequenceException("门店编号不能为空");
            }
            List<CreateSequence> list = null;
            list2 = this.platformOfflineSubUnitSequenceDao.getOfflineSubUnitSequence(subUnitNumId);
            if (list2.size() > 0) {
                for (PlatformOfflineSubUnitSequence e : list2) {
                    String seqName = e.getSeqName();
                    CreateSequence createSequence1 = new CreateSequence();
                    createSequence1.setSeqName(seqName);
                    list = this.sequenceDAO.getSequence(createSequence1);
                    if (list.size() <= 0) {
                        throw new SequenceException("platform_sequence表未查询到配置的序列,序列名称为：" + seqName);
                    }
                    CreateSequence createSequence2 = list.get(0);
                    Long startNum = e.getStartNum();
                    Long endNum = e.getEndNum();
                    HashMap<Object, Object> hashMap = new HashMap<>();
                    hashMap.put("seq_name", seqName);
                    hashMap.put("SEQ_PROJECT", createSequence2.getSeqProject());
                    hashMap.put("SEQ_PREFIX", createSequence2.getSeqPrefix());
                    hashMap.put("SEQ_NUM", createSequence2.getSeqNum());
                    hashMap.put("SEQ_VAL", createSequence2.getSeqVal());
                    hashMap.put("CURRENT_NUM", startNum);
                    hashMap.put("SEQ_NUM_START", startNum);
                    hashMap.put("SEQ_NUM_END", endNum);
                    hashMap.put("disrupt", createSequence2.getDisrupt());
                    hashMap.put("is_store_local", Integer.valueOf(1));
                    arrayList.add(hashMap);
                }
                return (List)arrayList;
            }
            list1 = this.platformOfflineSubUnitSequenceDao.getOfflineSubSequence();
            if (list1.size() <= 0) {
                return (List)arrayList;
            }
            for (PlatformOfflineSequence d : list1) {
                String seqName = d.getSeqName();
                CreateSequence createSequence1 = new CreateSequence();
                createSequence1.setSeqName(seqName);
                list = this.sequenceDAO.getSequence(createSequence1);
                if (list.size() <= 0) {
                    throw new SequenceException("platform_sequence表未查询到配置的序列,序列名称为：" + d.getSeqName());
                }
                CreateSequence createSequence2 = list.get(0);
                Long offlineGetNumCount = d.getOfflineGetNumCount();
                Long offlineCurrentNum = d.getOfflineCurrentNum();
                Long long_3 = offlineCurrentNum;
                Long long_4 = Long.valueOf(offlineCurrentNum.longValue() + offlineGetNumCount.longValue());
                Long offlineEndNum = d.getOfflineEndNum();
                if (long_4.intValue() >= offlineEndNum.intValue()) {
                    throw new SequenceException("离线序列的结束值不能超过限定的结束值，序列名称为：" + d.getSeqName());
                }
                this.platformOfflineSubUnitSequenceDao.updateOfflineCurrentNum(seqName);
                PlatformOfflineSubUnitSequence e = new PlatformOfflineSubUnitSequence();
                e.setSeries(Long.valueOf(System.currentTimeMillis()));
                e.setSeqName(seqName);
                e.setSubUnitNumId(subUnitNumId);
                e.setStartNum(long_3);
                e.setEndNum(long_4);
                this.platformOfflineSubUnitSequenceDao.insertOfflineSeq(e);
                HashMap<Object, Object> hashMap = new HashMap<>();
                hashMap.put("seq_name", seqName);
                hashMap.put("SEQ_PROJECT", createSequence2.getSeqProject());
                hashMap.put("SEQ_PREFIX", createSequence2.getSeqPrefix());
                hashMap.put("SEQ_NUM", createSequence2.getSeqNum());
                hashMap.put("SEQ_VAL", createSequence2.getSeqVal());
                hashMap.put("CURRENT_NUM", long_3);
                hashMap.put("SEQ_NUM_START", long_3);
                hashMap.put("SEQ_NUM_END", long_4);
                hashMap.put("disrupt", createSequence2.getDisrupt());
                hashMap.put("is_store_local", Integer.valueOf(1));
                arrayList.add(hashMap);
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
        return (List)arrayList;
    }

    private static boolean isSpecialTime(String date) {
        Integer integer1 = Integer.valueOf(0);
        Integer integer2 = Integer.valueOf(0);
        logger.info("序列号来自哪里fromWhere====="+ date.trim());
        if (date.trim().equals("common")) {
            integer1 = Integer.valueOf(2354);
            integer2 = Integer.valueOf(2356);
        } else if (date.trim().equals("auto")) {
            integer1 = Integer.valueOf(2400);
            integer2 = Integer.valueOf(2402);
        }
        Boolean bool = Boolean.valueOf(false);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str1 = simpleDateFormat.format(new Date());
            int i = Integer.parseInt(str1.substring(11, 13));   // 小时数(24时)
            String str2 = str1.substring(14, 16);               // 分钟数
            if (i < 12) {
                i += 24;
            }
            String str3 = String.valueOf(i) + str2;
            int j = Integer.parseInt(str3);
            if (integer1.intValue() <= j && j <= integer2.intValue()) {
                bool = Boolean.valueOf(true);                               // 小时分钟数(如： 1801)在2354~2356之间返回true   这三分钟定时任务清除数据 需要获取不同的锁
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return bool.booleanValue();
    }

}
