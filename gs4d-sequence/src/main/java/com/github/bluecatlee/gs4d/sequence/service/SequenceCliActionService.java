package com.github.bluecatlee.gs4d.sequence.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseStrategy;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;
import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import com.github.bluecatlee.gs4d.sequence.model.CreateSequence;
import com.github.bluecatlee.gs4d.sequence.utils.SequenceConstant;
import com.github.bluecatlee.gs4d.sequence.utils.PropertiesUtil;
import com.github.bluecatlee.gs4d.sequence.utils.SeqStringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SequenceCliActionService {

    protected static Logger logger;
    private static MyJsonMapper mapper = MyJsonMapper.nonDefaultMapper();

    private static SequenceActionService sequenceActionService;

    public static List<Object> locklists;                            // 提前初始化的锁列表
    public static Map<String, List<CreateSequence>> sequenceObjMap;  // 序列名->序列对象列表的映射
    public static Map<String, List<Integer>> seqValueMap;            // 序列名->序列值列表
    public static Map<String, Integer> seqStoreStatusMap;            // 序列名->是否本地存储的映射

    static {
        mapper = MyJsonMapper.nonEmptyMapper();
        mapper.getMapper().setPropertyNamingStrategy(LowerCaseStrategy.SNAKE_CASE);
        logger = LoggerFactory.getLogger(SequenceCliActionService.class);
        sequenceObjMap = new HashMap();
        locklists = new ArrayList();
        seqValueMap = new HashMap();
        seqStoreStatusMap = new HashMap();
    }

    public SequenceCliActionService() {
    }

    /**
     * 获取当前sequence current_sequence会自增 返回旧值 (相当于getAndIncrement)
     * @param sysName
     * @param seqName
     * @return
     */
    public CreateSequence getSequenceFromClient(String sysName, String seqName) {
        try {
            CreateSequence createSequence = new CreateSequence();
            createSequence.setSeqProject(sysName);
            createSequence.setSeqName(seqName);
            return sequenceActionService.getSequenceToClient(createSequence);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("获取服务器序列失败" + e.getMessage());
        }
    }

    public List<CreateSequence> getSequenceModel(String seqName) {
        try {
            CreateSequence createSequence = new CreateSequence();
            createSequence.setSeqName(seqName);
            return sequenceActionService.getSequenceModelToClientCheck(createSequence);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("查询数据库异常!" + e.getMessage());
        }
    }

    /**
     * 获取序列号
     * @param systemName 系统名称
     * @param seqName 序列名称
     * @return
     */
    public Long getSequence(String systemName, String seqName) {
        Integer storeStatus = (Integer)seqStoreStatusMap.get(seqName);
        if (null == storeStatus || storeStatus.equals("")) {
            storeStatus = sequenceActionService.getSeqStoreStatus(seqName);
            if (null == storeStatus) {
                throw new SequenceException("序列号【" + seqName + "】在配置表中不存在");
            }
            seqStoreStatusMap.put(seqName, storeStatus);
        }

        Long seqNum = 0L;
        if (storeStatus == 0) {
            // 非本地存储
            seqNum = this.getStoreNotSequence(systemName, seqName);
        } else {
            // 本地存储
            seqNum = this.getStoreLocalSequence(systemName, seqName);
        }

        if (seqNum == null) {
            sequenceObjMap.clear();
            seqValueMap.clear();
        }

        return seqNum;
    }

    /**
     * 获取序列值
     * @param systemName
     * @param seqName
     * @return
     */
    public Long getStoreNotSequence(String systemName, String seqName) {
        Long seqNum = null;
        String maxNum = "";
        String maxVal = "";
        String currentNum = "";
        String dateTime = "";
        String proFix = "";
        String mapKey = seqName;
        Long disrupt = 0L;
        List crelist = null;

        try {
            if (locklists.size() <= 0) {
                throw new SequenceException("未初始化zk");
            } else {
                synchronized(locklists.get(Math.abs(seqName.hashCode()) % locklists.size())) {
                    if (null == sequenceObjMap.get(mapKey)) {
                        // 获取当前序列并存到sequenceObjMap
                        this.getSequenceToMap(systemName, seqName, "", (Properties)null, "");
                    }

                    crelist = (List)sequenceObjMap.get(mapKey);
                    if (crelist.size() <= 0) {
                        throw new SequenceException("调用服务端序列失败");
                    } else {
                        if (crelist.size() > 0 && null != crelist.get(0)) {
                            maxNum = ((CreateSequence)crelist.get(0)).getSeqNum();
                            maxVal = ((CreateSequence)crelist.get(0)).getSeqVal();
                            currentNum = ((CreateSequence)crelist.get(0)).getCurrentNum().toString();
                            proFix = ((CreateSequence)crelist.get(0)).getSeqPrefix();
                            dateTime = ((CreateSequence)crelist.get(0)).getSquenceTime();
                            Integer currentSeqVal = ((CreateSequence)crelist.get(0)).getCurrentSeqVal();  // 应该是null
                            disrupt = ((CreateSequence)crelist.get(0)).getDisrupt();
                            if (disrupt == 1L) { // 打乱
                                seqNum = this.disruptSeqNum(maxNum, maxVal, currentNum, dateTime, proFix, currentSeqVal, systemName, seqName, crelist);
                            } else {             // 不打乱
                                seqNum = this.normalSeqNum(maxNum, maxVal, currentNum, dateTime, proFix, currentSeqVal, systemName, seqName, crelist);
                            }
                        }

                        return seqNum;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("获取序列失败,序列号的名称为：" + seqName + " ===" + e.getMessage());
        }
    }

    /**
     * 打乱
     * @param maxNum
     * @param maxVal            最大值
     * @param currentNum        当前值？
     * @param dateTime
     * @param proFix
     * @param currentSeqVal     当前序列值 序列值list的索引
     * @param systemName        系统名
     * @param seqName           序列名
     * @param crelist
     * @return
     */
    private Long disruptSeqNum(String maxNum, String maxVal, String currentNum, String dateTime, String proFix, Integer currentSeqVal, final String systemName, final String seqName, List<CreateSequence> crelist) {
        String key = seqName.trim();
        List<Integer> seqValueList = (List)seqValueMap.get(key);
        if (null == seqValueList || seqValueList.size() <= 0) {
            seqValueList = SeqStringUtil.createSolution(Integer.valueOf(maxVal));
            seqValueMap.put(key, seqValueList);
        }

        if (currentSeqVal == null) {
            currentSeqVal = 0;
        }

        if (currentSeqVal == Integer.valueOf(maxVal) / 3) {
            // 当前序列值是最大序列值的1/3时... 将序列信息存放到本地sequenceObjMap中
            IniteZkConfigService.executor.submit(new Runnable() {
                public void run() {
                    SequenceCliActionService.this.getSequenceToMap(systemName, seqName, "", (Properties)null, "");
                }
            });
        }

        // 打乱的seq
        Long seqNum = this.installSeqNum(maxNum, maxVal, currentNum, dateTime, proFix, currentSeqVal, seqValueList);

        if (currentSeqVal >= Integer.valueOf(maxVal) - 1) {
            crelist.remove(crelist.get(0));
            seqValueMap.remove(key);
            if (crelist.size() <= 0) {
                this.getSequenceToMap(systemName, seqName, "", (Properties)null, "");
            }
        } else {
            ((CreateSequence)crelist.get(0)).setCurrentSeqVal(currentSeqVal + 1);
        }

        return seqNum;
    }

    /**
     * 生成seqNum (打乱)
     * @param maxNum
     * @param maxVal
     * @param currentNum
     * @param dateTime
     * @param proFix
     * @param currentSeqVal
     * @param seqValueList
     * @return
     */
    private Long installSeqNum(String maxNum, String maxVal, String currentNum, String dateTime, String proFix, Integer currentSeqVal, List<Integer> seqValueList) {
        String resultVal = SeqStringUtil.frontCompWithZore((long)(Integer)seqValueList.get(currentSeqVal), maxVal.length());  // 当前序列值按最大序列值长度进行补零
        String[] split = dateTime.split("-");               // yyyy-MM-dd
        String year = dateTime.substring(2, split[0].length());    // 年(两位)
        String month = split[1];                                   // 月
        Integer day = Integer.valueOf(split[2]);                   // 日
        String currentSeqNum = SeqStringUtil.getSquByNum(maxNum, currentNum);  // 当前序列Num按最大序列数长度进行补零

        List<String> dayList = new ArrayList();
        dayList.add(SeqStringUtil.frontCompWithZore((long)day, 2));                            // 日期两位补零  01-31
        dayList.add(SeqStringUtil.frontCompWithZore((long)(day + SequenceConstant.DAY_NUM), 2));       // 32-62
        dayList.add(SeqStringUtil.frontCompWithZore((long)(day + SequenceConstant.DAY_NUM * 2), 2));   // 63-93
        String resultDay = SeqStringUtil.getRandomNum(dayList);                                              // 随机获取一个日期(01-93)
        char[] chYear = year.toCharArray();
        char[] chMonth = month.toCharArray();
        char[] chDay = resultDay.toCharArray();
        char[] chNum = currentSeqNum.toCharArray();
        char[] chVal = resultVal.toCharArray();
        int chNumlen = chNum.length;
        int chVallen = chVal.length;

        // [prefix] year2[0] seqVal[0] day[0] year2[1] month[0] seqVal[1] month[1] day[1] seqNum[0] seqNum[1] seqVal[2] seqNum[2] seqVal[3] seqNum[3] seqVal[4] seqNum[4] seqVal[5] seqNum[5]

        // 如 902421061621527222  18位 有前缀 三位seqNum 四位seqVal 也就是说到seqNum[2] seqVal[3] 最后三位其他业务后续附加222
        // [prefix] year2[0] seqVal[0] day[0] year2[1] month[0] seqVal[1] month[1] day[1] seqNum[0] seqNum[1] seqVal[2] seqNum[2] seqVal[3]
        //   90        2          4     2      1         0         6        1       6       2          1         5         2         7
        // 转换成可读格式
        // 90   21-01-26   212   4657
        StringBuffer buff = new StringBuffer();
        buff.append(proFix.trim());
        buff.append(chYear[0]);
        if (chVallen >= 1) {
            buff.append(chVal[0]);
        }

        buff.append(chDay[0]);
        buff.append(chYear[1]);
        buff.append(chMonth[0]);
        if (chVallen >= 2) {
            buff.append(chVal[1]);
        }

        buff.append(chMonth[1]);
        buff.append(chDay[1]);
        if (chNumlen >= 1) {
            buff.append(chNum[0]);
        }

        if (chNumlen >= 2) {
            buff.append(chNum[1]);
        }

        if (chVallen >= 3) {
            buff.append(chVal[2]);
        }

        if (chNumlen >= 3) {
            buff.append(chNum[2]);
        }

        if (chVallen >= 4) {
            buff.append(chVal[3]);
        }

        if (chNumlen >= 4) {
            buff.append(chNum[3]);
        }

        if (chVallen >= 5) {
            buff.append(chVal[4]);
        }

        if (chNumlen >= 5) {
            buff.append(chNum[4]);
        }

        if (chVallen >= 6) {
            buff.append(chVal[5]);
        }

        if (chNumlen >= 6) {
            buff.append(chNum[5]);
        }

        return Long.valueOf(buff.toString());
    }

    /**
     * 生成seqNum (不打乱)
     * @param maxNum
     * @param maxVal
     * @param currentNum
     * @param dateTime
     * @param proFix
     * @param currentSeqVal
     * @param systemName
     * @param seqName
     * @param crelist
     * @return
     */
    private Long normalSeqNum(String maxNum, String maxVal, String currentNum, String dateTime, String proFix, Integer currentSeqVal, final String systemName, final String seqName, List<CreateSequence> crelist) {
        if (currentSeqVal == null) {
            currentSeqVal = 1;
        }

        if (currentSeqVal == Integer.valueOf(maxVal) / 3) {
            IniteZkConfigService.executor.submit(new Runnable() {
                public void run() {
                    SequenceCliActionService.this.getSequenceToMap(systemName, seqName, "", (Properties)null, "");
                }
            });
        }

        Long seqNum = this.makeSeqFunction(dateTime, maxVal, maxNum, proFix, currentNum, String.valueOf(currentSeqVal));

        if (currentSeqVal >= Integer.valueOf(maxVal)) {
            crelist.remove(crelist.get(0));
            if (crelist.size() <= 0) {
                this.getSequenceToMap(systemName, seqName, "", (Properties)null, "");
            }
        } else {
            ((CreateSequence)crelist.get(0)).setCurrentSeqVal(currentSeqVal + 1);
        }

        return seqNum;
    }

    @Deprecated
    public Long getStoreLocalSequence(String systemName, String seqName) {
        Long seqNum = null;
        String maxNum = "";
        String maxVal = "";
        String currentNum = "";
        String dateTime = "";
        String proFix = "";
        Long disrupt = 0L;
        String mapKey = seqName;
        List<CreateSequence> crelist = null;
        Properties proSequence = null;

        try {
            if (locklists.size() <= 0) {
                throw new SequenceException("未初始化zk");
            }

            synchronized(locklists.get(Math.abs(seqName.hashCode()) % locklists.size())) {
                String filePath = IniteZkConfigService.seqFilePath + mapKey + ".properties";
                File file = new File(filePath);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                        logger.info("存储序列的配置文件创建成功!");
                    } catch (IOException e) {
                        throw new SequenceException("存储序列的配置文件创建失败！" + e.getMessage());
                    }
                }

                proSequence = PropertiesUtil.loadProps(filePath);
                if (null == sequenceObjMap.get(mapKey)) {
                    Set<Object> keyValue = proSequence.keySet();
                    if (keyValue.size() > 3) {
                        this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                    }

                    String mapKeyValue = proSequence.getProperty(mapKey);
                    if (null != mapKeyValue && !mapKeyValue.equals("")) {
                        JSONObject jsonobject = JSONObject.fromObject(mapKeyValue);
                        CreateSequence createSequence = (CreateSequence) JSONObject.toBean(jsonobject, CreateSequence.class);
                        String proMaxVal = createSequence.getSeqVal();
                        String times = createSequence.getSquenceTime();
                        Long proCurrentNum = createSequence.getCurrentNum() + 1L;
                        Long disRupt = createSequence.getDisrupt();
                        List<CreateSequence> sequenceFromService = this.getSequenceModel(seqName);
                        if (sequenceFromService.size() > 0) {
                            Long currentNum2 = ((CreateSequence)sequenceFromService.get(0)).getCurrentNum();
                            Long disRupt2 = ((CreateSequence)sequenceFromService.get(0)).getDisrupt();
                            if (disRupt.intValue() != disRupt2.intValue()) {
                                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                            } else if (!times.equals(((CreateSequence)sequenceFromService.get(0)).getSquenceTime())) {
                                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                            } else if (proCurrentNum > currentNum2) {
                                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                            } else if (Long.valueOf(proMaxVal) <= (long)createSequence.getProCurrentVal()) {
                                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                            } else {
                                List<CreateSequence> seqlist = new ArrayList();
                                createSequence.setCurrentSeqVal(createSequence.getProCurrentVal() + 1);
                                seqlist.add(createSequence);
                                createSequence.setProCurrentVal(createSequence.getCurrentSeqVal() + 99);
                                PropertiesUtil.updateProperty(proSequence, mapKey, JSONObject.fromObject(createSequence).toString(), filePath);
                                sequenceObjMap.put(mapKey, seqlist);
                            }
                        }
                    } else {
                        this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                    }
                }

                crelist = (List)sequenceObjMap.get(mapKey);
                if (crelist.size() <= 0) {
                    throw new SequenceException("调用服务端序列失败");
                }

                if (crelist.size() > 0 && null != crelist.get(0)) {
                    maxNum = ((CreateSequence)crelist.get(0)).getSeqNum();
                    maxVal = ((CreateSequence)crelist.get(0)).getSeqVal();
                    currentNum = ((CreateSequence)crelist.get(0)).getCurrentNum().toString();
                    proFix = ((CreateSequence)crelist.get(0)).getSeqPrefix();
                    dateTime = ((CreateSequence)crelist.get(0)).getSquenceTime();
                    Integer currentSeqVal = ((CreateSequence)crelist.get(0)).getCurrentSeqVal();
                    disrupt = ((CreateSequence)crelist.get(0)).getDisrupt();
                    if (disrupt == 1L) {
                        seqNum = this.disruptOffLineSeqNum(maxNum, maxVal, currentNum, dateTime, proFix, currentSeqVal, systemName, seqName, crelist, filePath, mapKey, proSequence);
                    } else {
                        seqNum = this.normalOffLineSeqNum(maxNum, maxVal, currentNum, dateTime, proFix, currentSeqVal, systemName, seqName, crelist, filePath, mapKey, proSequence);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("获取序列失败,序列号的名称为：" + seqName + " ===" + e.getMessage());
        } finally {
            proSequence.clear();
        }

        return seqNum;
    }

    private Long disruptOffLineSeqNum(String maxNum, String maxVal, String currentNum, String dateTime, String proFix, Integer currentSeqVal, String systemName, String seqName, List<CreateSequence> crelist, String filePath, String mapKey, Properties proSequence) {
        Long seqNum = null;
        if (currentSeqVal == null) {
            currentSeqVal = 0;
        }

        String disruptValue = proSequence.getProperty(mapKey + "_disrupt");
        List<Integer> offLineSeqValueList = null;
        if (null != disruptValue && !disruptValue.equals("")) {
            offLineSeqValueList = (List)mapper.fromJson(disruptValue, List.class);
            String mapKeyValue = proSequence.getProperty(mapKey);
            if (null != mapKeyValue && !mapKeyValue.equals("")) {
                JSONObject jsonobject = JSONObject.fromObject(mapKeyValue);
                CreateSequence createSequence = (CreateSequence)JSONObject.toBean(jsonobject, CreateSequence.class);
                if (Long.valueOf(currentNum) != createSequence.getCurrentNum()) {
                    sequenceObjMap.remove(mapKey);
                    seqNum = this.makeSeqFunction(dateTime, maxVal, maxNum, proFix, currentNum, String.valueOf(currentSeqVal));
                    return seqNum;
                } else {
                    if (currentSeqVal % 100 == 0) {
                        if (null == mapKeyValue || mapKeyValue.equals("")) {
                            sequenceObjMap.remove(mapKey);
                            seqNum = this.installSeqNum(maxNum, maxVal, currentNum, dateTime, proFix, currentSeqVal, offLineSeqValueList);
                            this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                            return seqNum;
                        }

                        jsonobject = JSONObject.fromObject(mapKeyValue);
                        createSequence = (CreateSequence)JSONObject.toBean(jsonobject, CreateSequence.class);
                        createSequence.setProCurrentVal(createSequence.getProCurrentVal() + 100);
                        createSequence.setCurrentSeqVal(createSequence.getCurrentSeqVal() + 100);
                        PropertiesUtil.updateProperty(proSequence, mapKey, JSONObject.fromObject(createSequence).toString(), filePath);
                        currentSeqVal = createSequence.getCurrentSeqVal() + 4;
                    }

                    Set keyValue;
                    String cacheSequenceValue;
                    if (currentSeqVal == Integer.valueOf(maxVal) / 3 * 2) {
                        keyValue = proSequence.keySet();
                        if (keyValue.size() > 3) {
                            this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                        }

                        cacheSequenceValue = proSequence.getProperty(mapKey + "_cache");
                        if (null == cacheSequenceValue || cacheSequenceValue.equals("")) {
                            this.getSequenceToMap(systemName, seqName, "middle", proSequence, filePath);
                        }
                    }

                    seqNum = this.installSeqNum(maxNum, maxVal, currentNum, dateTime, proFix, currentSeqVal, offLineSeqValueList);
                    if (currentSeqVal >= Integer.valueOf(maxVal) - 2) {
                        sequenceObjMap.remove(mapKey);
                        keyValue = proSequence.keySet();
                        if (keyValue.size() > 3) {
                            this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                        }

                        cacheSequenceValue = proSequence.getProperty(mapKey + "_cache");
                        if (null != cacheSequenceValue && !cacheSequenceValue.equals("")) {
                            JSONObject cacheJsonobject = JSONObject.fromObject(cacheSequenceValue);
                            CreateSequence cacheSequenceModel = (CreateSequence)JSONObject.toBean(cacheJsonobject, CreateSequence.class);
                            Long proCurrentNum = Long.valueOf(cacheSequenceModel.getSeqNum());
                            String proSequenceTime = cacheSequenceModel.getSquenceTime();
                            if (proCurrentNum > Long.valueOf(currentNum) && proSequenceTime.equals(dateTime)) {
                                PropertiesUtil.updateProperty(proSequence, mapKey, JSONObject.fromObject(cacheSequenceModel).toString(), filePath);
                                PropertiesUtil.updateProperty(proSequence, mapKey + "_cache", "", filePath);
                                List<Integer> seqValueList = SeqStringUtil.createSolution(Integer.valueOf(cacheSequenceModel.getSeqVal()));
                                PropertiesUtil.updateProperty(proSequence, mapKey + "_disrupt", mapper.toJson(seqValueList), filePath);
                            } else {
                                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                            }
                        } else {
                            this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                        }
                    } else {
                        ((CreateSequence)crelist.get(0)).setCurrentSeqVal(currentSeqVal + 1);
                    }

                    return seqNum;
                }
            } else {
                sequenceObjMap.remove(mapKey);
                seqNum = this.makeSeqFunction(dateTime, maxVal, maxNum, proFix, currentNum, String.valueOf(currentSeqVal));
                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                return seqNum;
            }
        } else {
            sequenceObjMap.remove(mapKey);
            seqNum = this.makeSeqFunction(dateTime, maxVal, maxNum, proFix, currentNum, String.valueOf(currentSeqVal));
            this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
            return seqNum;
        }
    }

    private Long normalOffLineSeqNum(String maxNum, String maxVal, String currentNum, String dateTime, String proFix, Integer currentSeqVal, String systemName, String seqName, List<CreateSequence> crelist, String filePath, String mapKey, Properties proSequence) {
        Long seqNum = null;
        if (currentSeqVal == null) {
            currentSeqVal = 1;
        }

        Set keyValue;
        String cacheSequenceValue;
        JSONObject cacheJsonobject;
        CreateSequence cacheSequenceModel;
        if (currentSeqVal % 100 == 0) {
            keyValue = proSequence.keySet();
            if (keyValue.size() > 3) {
                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
            }

            cacheSequenceValue = proSequence.getProperty(mapKey);
            if (null == cacheSequenceValue || cacheSequenceValue.equals("")) {
                sequenceObjMap.remove(mapKey);
                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                return this.makeSeqFunction(dateTime, maxVal, maxNum, proFix, currentNum, String.valueOf(currentSeqVal));
            }

            cacheJsonobject = JSONObject.fromObject(cacheSequenceValue);
            cacheSequenceModel = (CreateSequence)JSONObject.toBean(cacheJsonobject, CreateSequence.class);
            if (Long.valueOf(currentNum) != cacheSequenceModel.getCurrentNum()) {
                sequenceObjMap.remove(mapKey);
                return this.makeSeqFunction(dateTime, maxVal, maxNum, proFix, currentNum, String.valueOf(currentSeqVal));
            }

            cacheSequenceModel.setProCurrentVal(cacheSequenceModel.getProCurrentVal() + 100);
            cacheSequenceModel.setCurrentSeqVal(cacheSequenceModel.getCurrentSeqVal() + 100);
            PropertiesUtil.updateProperty(proSequence, mapKey, JSONObject.fromObject(cacheSequenceModel).toString(), filePath);
            currentSeqVal = cacheSequenceModel.getCurrentSeqVal() + 4;
        }

        if (currentSeqVal == Integer.valueOf(maxVal) / 3 * 2) {
            keyValue = proSequence.keySet();
            if (keyValue.size() > 3) {
                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
            }

            cacheSequenceValue = proSequence.getProperty(mapKey + "_cache");
            if (null == cacheSequenceValue || cacheSequenceValue.equals("")) {
                this.getSequenceToMap(systemName, seqName, "middle", proSequence, filePath);
            }
        }

        seqNum = this.makeSeqFunction(dateTime, maxVal, maxNum, proFix, currentNum, String.valueOf(currentSeqVal));
        if (currentSeqVal >= Integer.valueOf(maxVal) - 1) {
            sequenceObjMap.remove(mapKey);
            keyValue = proSequence.keySet();
            if (keyValue.size() > 3) {
                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
            }

            cacheSequenceValue = proSequence.getProperty(mapKey + "_cache");
            if (null != cacheSequenceValue && !cacheSequenceValue.equals("")) {
                cacheJsonobject = JSONObject.fromObject(cacheSequenceValue);
                cacheSequenceModel = (CreateSequence)JSONObject.toBean(cacheJsonobject, CreateSequence.class);
                Long proCurrentNum = Long.valueOf(cacheSequenceModel.getSeqNum());
                String proSequenceTime = cacheSequenceModel.getSquenceTime();
                if (proCurrentNum > Long.valueOf(currentNum) && proSequenceTime.equals(dateTime)) {
                    PropertiesUtil.updateProperty(proSequence, mapKey, JSONObject.fromObject(cacheSequenceModel).toString(), filePath);
                    PropertiesUtil.updateProperty(proSequence, mapKey + "_cache", "", filePath);
                } else {
                    this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
                }
            } else {
                this.getSequenceToMap(systemName, seqName, "first", proSequence, filePath);
            }
        } else {
            ((CreateSequence)crelist.get(0)).setCurrentSeqVal(currentSeqVal + 1);
        }

        return seqNum;
    }

    /**
     * 将序列信息存放到本地sequenceObjMap中
     * @param systemName    系统名
     * @param seqName       序列名
     * @param fromWhere     来源
     * @param proSequence   属性文件
     * @param filePath      文件路径
     */
    public void getSequenceToMap(String systemName, String seqName, String fromWhere, Properties proSequence, String filePath) {
        CreateSequence createSequence = null;

        try {
            createSequence = this.getSequenceFromClient(systemName, seqName);
            if (createSequence.getSeqName() != null && createSequence != null) {
                List<CreateSequence> seqlist = (List)sequenceObjMap.get(seqName);
                if (seqlist == null) {
                    seqlist = new ArrayList();
                }

                ((List)seqlist).add(createSequence);
                sequenceObjMap.put(seqName, seqlist);
                if (fromWhere.equals("first")) {
                    if (null == createSequence.getCurrentSeqVal()) {
                        createSequence.setProCurrentVal(100);
                    } else {
                        createSequence.setProCurrentVal(createSequence.getCurrentSeqVal() + 100);
                    }

                    PropertiesUtil.updateAllPropertiesNull(proSequence, filePath);
                    PropertiesUtil.updateProperty(proSequence, seqName, JSONObject.fromObject(createSequence).toString(), filePath);
                    Long disrupt = createSequence.getDisrupt();
                    if (disrupt == 1L) {
                        List<Integer> offLineSeqValueList = SeqStringUtil.createSolution(Integer.valueOf(createSequence.getSeqVal()));
                        PropertiesUtil.updateProperty(proSequence, seqName + "_disrupt", mapper.toJson(offLineSeqValueList), filePath);
                    }
                } else if (fromWhere.equals("middle")) {
                    if (null == createSequence.getCurrentSeqVal()) {
                        createSequence.setProCurrentVal(100);
                    } else {
                        createSequence.setProCurrentVal(createSequence.getCurrentSeqVal() + 100);
                    }

                    PropertiesUtil.updateProperty(proSequence, seqName + "_cache", JSONObject.fromObject(createSequence).toString(), filePath);
                }

            } else {
                throw new Exception("数据库中没有配置序列" + seqName);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException(e.getMessage());
        }
    }

    /**
     * 组装seqVal
     * @param dateTime      序列时间
     * @param maxVal        最大值
     * @param maxNum        最大序列
     * @param proFix        前缀
     * @param currentNum    当前序列
     * @param parm          当前序列值
     * @return
     */
    private Long makeSeqFunction(String dateTime, String maxVal, String maxNum, String proFix, String currentNum, String parm) {
        dateTime = dateTime.replaceAll("-", "");   // yyyy-MM-dd
        dateTime = dateTime.substring(2, dateTime.length());
        String currentSeqNum = SeqStringUtil.getSquByNum(maxNum, currentNum);
        String currentSeqValue = SeqStringUtil.getSquByNum(maxVal, parm);
        // [前缀] + yyMMdd + 当前序列(补零) + 当前序列值(补零)
        // 2101262080001
        //          210126      208             0001
        return Long.valueOf(proFix.trim() + dateTime + currentSeqNum + currentSeqValue);
    }

    public static SequenceActionService getSequenceActionService() {
        return sequenceActionService;
    }

    public static void setSequenceActionService(SequenceActionService sequenceActionService) {
        SequenceCliActionService.sequenceActionService = sequenceActionService;
    }

    /**
     * 获取自增长序列
     * @param seqName       序列名
     * @param num           非必传 -2时表示清除缓存
     * @param tenantNumId
     * @param dataSign
     * @return
     */
    public String getAutomicSequence(String seqName, Integer num, Long tenantNumId, Long dataSign) {
        try {
            return sequenceActionService.getAutomicSeq(seqName, num, tenantNumId, dataSign);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("获取自增长序列失败:" + e.getMessage());
        }
    }

    public List<Map<String, Object>> getOfflineSeqInfo(Long subUnitNumId) {
        try {
            return sequenceActionService.getOfflineSeqInfo(subUnitNumId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("返回离线序列失败:" + e.getMessage());
        }
    }

}

