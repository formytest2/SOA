package com.github.bluecatlee.gs4d.exchange.utils;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.exchange.model.CommonQuery;
import com.github.bluecatlee.gs4d.exchange.model.ExcuteSqlResultModel;
import com.github.bluecatlee.gs4d.sequence.utils.SeqGetUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * common_query的参数处理工具
 *       由于特定的规则 导致字段值无法出现：@@、'、 \ 等字符(序列)，这一点需要注意，不过common_query的绝大部分应用场景是不会带有这种字符序列的入参的
 */
public class CommUtil {

    private static Logger log = LoggerFactory.getLogger(CommUtil.class);

    private static final String NULL_DATE_DEFAULTVALUE = "NULL_DATE_DEFAULTVALUE";

    private static MyJsonMapper mapper = new MyJsonMapper();

    static {
        mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    /**
     * 处理sql_content中的可选部分[]
     * @param sqlContent
     * @param map
     * @return
     */
    public static String sqlHandler(String sqlContent, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sqlContent = sqlContent.replaceFirst("\\?", (value.toString().length() <= 0) ? ("##" + key + "##") : ("@@" + key + "@@"));
            if (NULL_DATE_DEFAULTVALUE.equals(value)) {
                map.put(key, null);
            }
        }

        Pattern p = Pattern.compile("\\[([^\\[]+)\\]"); // 即[] 且[]中本身不存在'['和']'字符
        Matcher matcher = p.matcher(sqlContent);
        while (matcher.find()) {
            String matchStr = matcher.group(1); // group(1)就是匹配第一组()中的内容，即[]中的内容
            if (matchStr.indexOf("##") != -1) {
                removeFromMap(matchStr, map);   // 把未传入值的参数key(且这个参数是可选的) 从map中移除
                sqlContent = sqlContent.replace(matcher.group(), "");   // []就是表示可选的 如果key为空 则整个可选部分都去掉
            }
        }
        sqlContent = sqlContent.replaceAll("@@[^@@]+@@", "\\?");    // 如果值本身包含'@@'，那么就会替换失败
        return sqlContent;
    }

    private static String removeFromMap(String sqlContent, Map<String, Object> map) {
        String removeKey = "";
        Pattern p = Pattern.compile("[@|#](\\S+)[#|@]");
        Matcher matcher = p.matcher(sqlContent);
        while (matcher.find()) {
            String matchStr = matcher.group(1); // 匹配出key
            matchStr = matchStr.replace("@", "").replace("#", "");
            map.remove(matchStr);
            removeKey = matchStr;
        }
        return removeKey;
    }

    /**
     * 按照param_content的定义解析实际参数 并把参数->值 组装到map中
     * @param commonQuery
     * @param ja            参数规则param_content
     * @param jsonOb        实际参数
     * @return
     */
    public static Map<String, Object> getParamsWithMap(CommonQuery commonQuery, JSONArray ja, JSONObject jsonOb) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < ja.size(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            String name = jo.optString("NAME").trim();  // 字段名
            if (StringUtil.isNullOrBlankTrim(name)) {
                name = jo.optString("name").trim();
            }
            if (StringUtil.isNullOrBlankTrim(name)) {
                throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "的查询中配置name参数值为空");
            }
            name = name.toLowerCase();

            String value = jsonOb.optString(name);           // 字段值

            String type = jo.getString("TYPE");         // 字段类型
            String function = jo.optString("FUNCTION"); // 功能
            String qtype = jo.optString("QTYPE");
            if (StringUtil.isNullOrBlankTrim(qtype)) {
                qtype = jo.optString("qtype");
            }
            String fuzzyQuery = "";                          // 模糊查询
            if (jo.has("fuzzyquery") || jo.containsKey("fuzzyquery")) {
                fuzzyQuery = jo.getString("fuzzyquery");
            }
            if (jo.has("FUZZYQUERY") || jo.containsKey("FUZZYQUERY")) {
                fuzzyQuery = jo.getString("FUZZYQUERY");
            }

            boolean mustHave = jo.getBoolean("MUSTHAVE");   // 是否必须

            if ((jsonOb.containsKey(name) || jsonOb.containsKey(name.toUpperCase())) && (StringUtil.isAllNullOrBlank(new String[] { value }) || "null".equals(value))) {
                name = name.toUpperCase();          // 如果小写字段名未获取到对应的值 则尝试用大写字段名获取传递的字段值
                value = jsonOb.optString(name);
            }
            if (value == null || value.length() == 0 || "null".equals(value)) {
                if (true == mustHave) {
                    throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "的查询中,缺少参数(值):" + name);
                }
                /**
                 * sequence的生成也可以直接通过通用查询来处理, 即可以把获取sequence配置到common_query表中
                 * 但是一般不这么用 而是直接配置insert语句，对应的主键字段(一般如series)设置FUNCTION:SEQ属性
                 * @see gs4d-sequence
                 */
                if ("SEQ".equals(function)) {
                    if (StringUtil.isNullOrBlankTrim(value)) {
                        String seqName = jo.getString("SEQ_NAME");
                        if (StringUtil.isNullOrBlankTrim(seqName)) {
                            throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "中,缺少序列名称:" + name);
                        }
                        value = SeqGetUtil.getNoSubSequence(Constants.SUB_SYSTEM, jo.getString("SEQ_NAME").toLowerCase()).toString();
                        if (map.containsKey(name)) {
                            name = name + "_" + UUID.randomUUID();
                        }
                        map.put(name, value);
                        jsonOb.put(name, value);
                        continue;
                    }
                } else if ("AUTO_SEQ".equals(function) && StringUtil.isNullOrBlankTrim(value)) {
                    String seqName = jo.getString("SEQ_NAME");
                    if (StringUtil.isNullOrBlankTrim(seqName)) {
                        throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "中,缺少序列名称:" + name);
                    }
                    value = SeqGetUtil.getAutomicSequence(jo.getString("SEQ_NAME").toLowerCase(), Long.valueOf(0L), Long.valueOf(0L));
                    if (map.containsKey(name)) {
                        name = name + "_" + UUID.randomUUID();
                    }
                    map.put(name, value);
                    jsonOb.put(name, value);
                    continue;
                }

                String defaultValue = jo.optString("DEFAULT");  // 默认值
                value = defaultValue;
                String excuteFlag = jo.optString("DEFAULT_FLAG");   // 是否使用默认值 如果有值的话就不使用默认值 因此如果需要默认值则不配置DEFAULT_FLAG
                boolean excute = true;
                if (!StringUtil.isNullOrBlankTrim(excuteFlag)) {
                    excute = false;
                }
                if ((value == null || value.length() == 0 || "null".equals(value)) &&
                        (CommonQuery.SQL_FLAG_INSERT.equals(commonQuery.getSqlFlag()) || CommonQuery.SQL_FLAG_UPDATE.equals(commonQuery.getSqlFlag())) && excute) {
                    if ("NUMBER".equals(type.toUpperCase())) {
                        defaultValue = "0";
                    } else if ("STRING".equals(type.toUpperCase())) {
                        defaultValue = " ";
                    } else if ("DECIMAL".equals(type.toUpperCase())) {
                        defaultValue = "0.0";
                    } else if ("DATETIME".equals(type.toUpperCase())) {
                        defaultValue = NULL_DATE_DEFAULTVALUE;
                    }
                    value = defaultValue;
                }
            }
            jsonOb.put(name, value);

            if (value != null && value.length() > 0 && !"null".equals(value)) {
                if ("before".equals(fuzzyQuery)) {
                    value = "%" + value;
                } else if ("after".equals(fuzzyQuery)) {
                    value = value + "%";
                } else if ("around".equals(fuzzyQuery)) {
                    value = "%" + value + "%";
                }
                if ("in".equals(qtype)) {
                    value = value.replace("'", ""); // 把“'”替换掉 这里主要目的是字符串类型的值需要使用'括起来 防止下面冲突
                                                                        // 如果值中有单引号不行(单纯的替换 导致使用转义符也不行)
                    String[] ary = value.split(",");
                    StringBuffer buffer = new StringBuffer();
                    for (int j = 0; j < ary.length; j++) {
                        if (buffer.length() > 0) {
                            buffer.append(",");
                        }
                        if (!"number".equals(jo.optString("type"))) {
                            buffer.append("'" + ary[j] + "'");
                        } else {
                            buffer.append(ary[j]);
                        }
                    }
                    String sqlContent = commonQuery.getSqlContent();
                    commonQuery.setSqlContent(sqlContent.replaceFirst("in\\s*\\?", " in (" + buffer.toString() + ")")); // 匹配 "in ?" 这个格式(中间可以有0个或任意个空格)
                    continue;
                }
            } else if ("in".equals(qtype)) {
                String sqlContent = commonQuery.getSqlContent();
                commonQuery.setSqlContent(sqlContent.replaceFirst("in\\s*\\?", " in (?)"));
            }

            String key = name;
            if (map.containsKey(key)) {
                key = name + "_" + UUID.randomUUID();
            }
            if (StringUtil.isAllNotNullOrBlank(new String[] { function }) && "SHARD_ID".equals(function)) {
                value = SeqGetUtil.getSharedId(value).toString();       // 生成分片号的功能
            }

            if ("PGSQL".equals(commonQuery.getDbType().toUpperCase())) {
                if (StringUtil.isNullOrBlankTrim(value)) {
                    map.put(key, value);
                } else if ("NUMBER".equals(type.toUpperCase())) {
                    Long s = Long.valueOf(value);
                    map.put(key, s);
                } else if ("STRING".equals(type.toUpperCase())) {
                    map.put(key, value);
                } else if ("DECIMAL".equals(type.toUpperCase())) {
                    Double s = Double.valueOf(value);
                    map.put(key, s);
                } else if ("DATETIME".equals(type.toUpperCase()) || "DATE".equals(type.toUpperCase())) {
                    Date s = Date.valueOf(value);
                    map.put(key, s);
                }
            } else {
                map.put(key, value);
            }

            if (!StringUtil.isNullOrBlankTrim(jo.optString("mapfile"))) {
                commonQuery.paramMap.put(key, jo.optString("mapfile"));
            }

            continue;
        }
        return map;
    }

    public static Object[] parseSqlById(CommonQuery commonQuery, JSONObject inputParam) {
        ExcuteSqlResultModel resModel = new ExcuteSqlResultModel();
        String sqlContent = commonQuery.getSqlContent();
        String jsonContent = commonQuery.getParamContent();
        JSONArray ja = JSONArray.fromObject(jsonContent);
        Map<String, Object> map = getParamsWithMap(commonQuery, ja, inputParam);
        sqlContent = sqlHandler(sqlContent, map);
        List<Object> arrList = new ArrayList();
        Object[] os = map.values().toArray();
        for (Object o : os) {
            if (o != null) {
                arrList.add(o);
            }
        }
        Object[] arr = arrList.toArray();
        return arr;
    }

}

