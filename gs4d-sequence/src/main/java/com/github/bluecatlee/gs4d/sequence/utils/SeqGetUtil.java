package com.github.bluecatlee.gs4d.sequence.utils;

import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import com.github.bluecatlee.gs4d.sequence.service.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 序列号工具类
 */
public class SeqGetUtil {

    private static SubIdGetService sgs = new SubIdGetService();
    private static SubSequenceService sg = new SubSequenceService();
    private static SequenceService ss = new SequenceService();
    private static IniteZkConfigService izfs = new IniteZkConfigService();
    private static SequenceCliActionService sc = new SequenceCliActionService();

    public SeqGetUtil() {
    }

    /**
     * 初始化zookeeper配置 todo 调用者服务使用前需要调用该方法进行初始化 建议调用者服务进行二次封装
     * @param zkaddress
     */
    public static void initeZkConfig(String zkaddress) {
        izfs.initZkConfig(zkaddress);
    }

    /**
     * 获取序列号(无子序列)
     * @param systemName
     * @param SeqName
     * @return
     */
    public static Long getNoSubSequence(String systemName, String SeqName) {
        return ss.getNoSubSequence(systemName, SeqName);
    }

    /**
     * 获取序列号(无子序列)
     * @param SeqName
     * @return
     */
    public static Long getNoSubSequence(String SeqName) {
        String systemName = "";
        return sc.getSequence(systemName, SeqName);
    }

    /**
     * 批量获取多个序列号(无子序列)
     * @param systemName
     * @param SeqName
     * @param size
     * @return
     */
    public static List<Object> getNoSubSequenceBath(String systemName, String SeqName, Integer size) {
        return ss.getNoSubSequenceBath(systemName, SeqName, size);
    }

    /**
     * 批量获取多个序列号(无子序列)
     * @param SeqName
     * @param size
     * @return
     */
    public static List<Object> getNoSubSequenceBath(String SeqName, Integer size) {
        String systemName = "";
        return ss.getNoSubSequenceBath(systemName, SeqName, size);
    }

    /**
     * 获取序列号
     * @param systemName
     * @param SeqName
     * @param routeId
     * @return
     */
    public static String getSequence(String systemName, String SeqName, String routeId) {
        return sg.getSequence(systemName, SeqName, routeId);
    }

    /**
     * 获取序列号
     * @param SeqName
     * @param routeId
     * @return
     */
    public static String getSequence(String SeqName, String routeId) {
        String systemName = "";
        return sg.getSequence(systemName, SeqName, routeId);
    }

    /**
     * 批量获取多个序列号
     * @param systemName
     * @param SeqName
     * @param routeId
     * @param size
     * @return
     */
    public static List<String> getSequenceBatch(String systemName, String SeqName, String routeId, Integer size) {
        return sg.getSequenceBatch(systemName, SeqName, routeId, size);
    }

    /**
     * 批量获取多个序列号
     * @param SeqName
     * @param routeId
     * @param size
     * @return
     */
    public static List<String> getSequenceBatch(String SeqName, String routeId, Integer size) {
        String systemName = "";
        return sg.getSequenceBatch(systemName, SeqName, routeId, size);
    }

    /**
     * 获取不带日期的序列号
     * @param systemName
     * @param SeqName
     * @return
     */
    public static Long getNoDateSequence(String systemName, String SeqName) {
        return sg.getNoDateSequence(systemName, SeqName);
    }

    /**
     * 获取不带日期的序列号
     * @param SeqName
     * @return
     */
    public static Long getNoDateSequence(String SeqName) {
        String systemName = "";
        return sg.getNoDateSequence(systemName, SeqName);
    }

//    /**
//     * 获取序列号(用户id用)
//     * @param systemName
//     * @return
//     */
//    public static String getMemberSequence(String systemName) {
//        return String.valueOf(ss.getNoSubSequence(systemName, "user"));
//    }

//    /**
//     * 获取序列号(用户id用)
//     * @return
//     */
//    public static String getMemberSequence() {
//        String systemName = "";
//        return String.valueOf(ss.getNoSubSequence(systemName, "user"));
//    }

    /**
     * 根据序列号获取子序列
     * @param seq
     * @return
     */
    public static Long getSubIdBySeq(String seq) {
        return sgs.getSubIdBySeq(seq);
    }

//    /**
//     * 根据日期生成三方订单使用的序列号
//     * @param systemName
//     * @param seqName
//     * @param date
//     * @return
//     */
//    public static Long getThirdOrderHdrSequenceByDate(String systemName, String seqName, String date) {
//        String oldSeq = String.valueOf(ss.getNoSubSequence(systemName, seqName));
//        String newDate = buildDate(date);
//        String newSeq = newDate + oldSeq.substring(5);
//        return Long.valueOf(newSeq);
//    }

//    /**
//     * 根据日期生成三方订单使用的序列号
//     * @param seqName
//     * @param date
//     * @return
//     */
//    public static Long getThirdOrderHdrSequenceByDate(String seqName, String date) {
//        String systemName = "";
//        String oldSeq = String.valueOf(ss.getNoSubSequence(systemName, seqName));
//        String newDate = buildDate(date);
//        String newSeq = newDate + oldSeq.substring(5);
//        return Long.valueOf(newSeq);
//    }

//    /**
//     * 根据日期生成小票号
//     * @param systemName
//     * @param SeqName
//     * @param routeId
//     * @param date
//     * @return
//     */
//    public static String getTmlNumByDate(String systemName, String SeqName, String routeId, String date) {
//        String sequence = sg.getSequence(systemName, SeqName, routeId);
//        String newDate = buildDate(date);
//        String newSeq = sequence.substring(0, 2) + newDate + sequence.substring(5);
//        return newSeq;
//    }

//    /**
//     * 根据日期生成小票号
//     * @param SeqName
//     * @param routeId
//     * @param date
//     * @return
//     */
//    public static String getTmlNumByDate(String SeqName, String routeId, String date) {
//        String systemName = "";
//        String sequence = sg.getSequence(systemName, SeqName, routeId);
//        String newDate = buildDate(date);
//        String newSeq = sequence.substring(0, 2) + newDate + sequence.substring(5, sequence.length());
//        return newSeq;
//    }

    private static String buildDate(String date) {
        Date dt = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dt = sdf.parse(date);
        } catch (ParseException e) {
            throw new SequenceException("获取序列号时时间格式转换异常" + date);
        }

        Calendar cld = Calendar.getInstance();
        cld.setTime(dt);
        String year = String.valueOf(cld.get(Calendar.YEAR)).substring(2, 4);
        String month = String.valueOf(cld.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }

        String day = String.valueOf(cld.get(Calendar.DATE));
        if (day.length() == 1) {
            day = "0" + day;
        }

        return year + month + day;
    }

    /**
     * 获取分片号
     * @param routerId
     * @return
     */
    public static Integer getSharedId(String routerId) {
        return routerId.length() >= 3 ? Integer.valueOf(routerId.substring(routerId.length() - 3)) : Integer.valueOf(routerId);
    }

    /**
     * 获取分片号
     * @param routerId
     * @return
     */
    public static Integer getSharedId(Long routerId) {
        return routerId.toString().length() >= 3 ? Integer.valueOf(routerId.toString().substring(routerId.toString().length() - 3)) : Integer.valueOf(routerId.toString());
    }

    public static String getAutomicSequence(String SeqName, Integer num, Long tenantNumId, Long dataSign) {
        return sc.getAutomicSequence(SeqName, num, tenantNumId, dataSign);
    }

    public static String getAutomicSequence(String SeqName, Long tenantNumId, Long dataSign) {
        return sc.getAutomicSequence(SeqName, 1, tenantNumId, dataSign);
    }

    public static String getAutomicSequenceNoTenAndData(String SeqName) {
        return sc.getAutomicSequence(SeqName, 1, 0L, 0L);
    }

    public static List<Map<String, Object>> getOfflineSeqInfo(Long subUnitNumId) {
        return sc.getOfflineSeqInfo(subUnitNumId);
    }
}

