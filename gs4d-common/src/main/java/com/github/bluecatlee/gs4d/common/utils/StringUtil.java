package com.github.bluecatlee.gs4d.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
@Deprecated
public class StringUtil {
    public static final String Get_Price_bits = "0.00";
    public static final Pattern NUMBER_PATTERN = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
    public static final Pattern INTEGER_PATTERN = Pattern.compile("^-?[\\d]+$");
    public static final String INVALID_CDATA_TEXT = "]]>";
    public static final String CHINESE_REGEX = "[\\u4e00-\\u9fa5]";
    private static final int MAX_UNICODE_DATA = 125;
    public static final Pattern BLANKSPACE_PATTERN = Pattern.compile("\\s+");
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("\\W+");
    public static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");
    public static final Pattern DATE_PATTERN = Pattern.compile("^(\\d{2,4})\\-(\\d{1,2})\\-(\\d{1,2})$");
    public static final Pattern TIME_PATTERN = Pattern.compile("^(\\d{2,4})\\-(\\d{1,2})\\-(\\d{1,2})\\s(\\d{1,2}):(\\d{1,2}):(\\d{1,2})$");
    public static final Pattern TIME_OTHER_PATTERN = Pattern.compile("^(\\d{2,4})\\/(\\d{1,2})\\/(\\d{1,2})\\s(\\d{1,2}):(\\d{1,2}):(\\d{1,2})$");
    public static final Pattern ACCOUNT_PATTERN = Pattern.compile("^[a-zA-Z]\\w+$");
    public static final String Empty = "";

    public StringUtil() {
    }

    // 是否手机号码
    @Deprecated
    public static boolean isMobile(String mobile) {
        if (isBlank(mobile)) {
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,1,5-9])|(17[6，7,8]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    // 判断是否是邮件字符串
    public static boolean isEmail(String email) {
        boolean ret = false;
        String emailPat = "^([a-z0-9A-Z]+[-|\\_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        try {
            if (email.matches(emailPat)) {
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    // 是否是图片
    public static boolean isPicture(String pInput, String pImgeFlag) {
        // 文件名称为空的场合
        if (isBlank(pInput)) {
            // 返回不和合法
            return false;
        }
        // 获得文件后缀名
        String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1, pInput.length());
        // 声明图片后缀名数组
        String imgeArray [][] = {
                {"bmp", "0"}, {"dib", "1"}, {"gif", "2"},
                {"jfif", "3"}, {"jpe", "4"}, {"jpeg", "5"},
                {"jpg", "6"}, {"png", "7"} ,{"tif", "8"},
                {"tiff", "9"}, {"ico", "10"}
        };
        // 遍历名称数组
        for (int i = 0; i < imgeArray.length; i++) {
            // 判断单个类型文件的场合
            if(!isBlank(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase()) && imgeArray[i][1].equals(pImgeFlag)) {
                return true;
            }
            // 判断符合全部类型的场合
            if(isBlank(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // 判断是否是安全的xml字符串
    // 如果存在控制字符(制表符、换行符、回车符除外)，或者存在"]]>"子串，则认为是不安全的
    // 事实上，ascii码中还有127(删除)也是控制字符
    // <![CDATA[ ]]> 中可以使用敏感字符，但是是不安全的
    public static boolean isSafeXmlString(String xml) {
        if (isNullOrBlankTrim(xml)) {
            return true;
        } else {
            for(int i = 0; i < xml.length(); ++i) {
                Character c = xml.charAt(i);
                if (c < ' ' && c != '\t' & c != '\n' & c != '\r') {
                    return false;
                }
            }
            return xml.indexOf("]]>") == -1;
        }
    }

    // 获取安全的xml字符串
    public static String getSafeXmlString(String xml) {
        return getSafeXmlString(xml, true);
    }

    // 获取安全的xml字符串
    @Deprecated
    public static String getSafeXmlString(String xml, boolean isCdata) {
        if (xml == null) {
            return null;
        } else {
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < xml.length(); ++i) {
                Character c = xml.charAt(i);
                if (c >= ' ' || !(c != '\t' & c != '\n' & c != '\r')) {
                    sb.append(c);
                }
            }
            if (!isCdata) {
                return sb.toString();
            } else {
                String returnString;
                for(returnString = sb.toString(); returnString.indexOf("]]>") != -1; returnString = returnString.replace("]]>", "]]")) {  // ？？todo
                }
                return returnString;
            }
        }
    }

    // 删除换行符
    public static String replaceNewline(String str) {
        return isNullOrBlank(str) ? "" : str.replaceAll("\\n", "").replaceAll("\\r\\n", "");
    }

    // 判断是否是空(null或空串都认为是空)
    public static boolean isNullOrBlank(String param) {
        return param == null || "".equals(param);
    }

    public static boolean isNullOrBlankTrim(Object param) {
        return param == null || "".equals(param.toString().trim());
    }

    // 判断是否是空(包括空格也认为是空)
    public static boolean isNullOrBlankTrim(String param) {
        return param == null || "".equals(param.toString().trim());
    }

    public static boolean isNotNullOrBlankTrim(Object param) {
        return param != null && !"".equals(param.toString().trim()) && !"null".equals(param.toString().trim());
    }

    // 判断是否包含空格
    public static boolean isBlankSpace(String param) {
        if (param == null) {
            return false;   // null不算空格
        } else {
            Matcher m = BLANKSPACE_PATTERN.matcher(param);
            return m.find();
        }
    }

    // null转空串
    public static String nullToBlankStr(Object param) {
        return nullToDefaultValue(param, "");
    }

    public static String nullToDefaultValue(Object param, String defaultValue) {
        return param == null ? defaultValue : param.toString();
    }

    public static String nullOrBlankToDefault(Object param, String defaultValue) {
        if (param == null) {
            return defaultValue;
        } else {
            return param instanceof String && isNullOrBlankTrim((String)param) ? defaultValue : param.toString();
        }
    }

    // 判断是否是数值
    public static boolean isNumber(String param) {
        if (param == null) {
            return false;
        } else {
            Matcher m = NUMBER_PATTERN.matcher(param);
            return m.find();
        }
    }

    public static boolean isNullOrNumber(String param) {
        if (param == null) {
            return true;
        } else {
            Matcher m = NUMBER_PATTERN.matcher(param);
            return m.find();
        }
    }

    public static boolean isOtherTime(String param) {
        if (param != null) {
            Matcher m = TIME_OTHER_PATTERN.matcher(param);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDate(String param) {
        if (param != null) {
            Matcher m = DATE_PATTERN.matcher(param);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTime(String param) {
        if (param != null) {
            Matcher m = TIME_PATTERN.matcher(param);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInteger(String param) {
        if (param == null) {
            return false;
        } else {
            Matcher m = INTEGER_PATTERN.matcher(param);
            return m.find();
        }
    }

    public static boolean isNullOrInteger(String param) {
        if (param == null) {
            return true;
        } else {
            Matcher m = INTEGER_PATTERN.matcher(param);
            return m.find();
        }
    }

    // 给定字符串数组是否都为空
    @Deprecated
    public static boolean isAllNullOrBlank(String... objs) {
        if (objs.length == 1) {
            return isNullOrBlank(objs[0]);
        } else {
            String[] arr = objs;
            int len = objs.length;
            for(int index = 0; index < len; ++index) {
                String s = arr[index];
                if (!isNullOrBlank(s)) {
                    return false;
                }
            }
            return true;
        }
    }

    // 给定字符串数组是否都不为空 返回true表示都不为空
    public static boolean isAllNotNullOrBlank(String... objs) {
        if (objs.length == 1) {
            return !isNullOrBlank(objs[0]);
        } else {
            String[] arr = objs;
            int len = objs.length;
            for(int index = 0; index < len; ++index) {
                String s = arr[index];
                if (isNullOrBlank(s)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isAllNotNullOrBlank(Object... objs) {
        if (objs.length == 1) {
            return !isNullOrBlankTrim(objs[0]);
        } else {
            Object[] arr = objs;
            int len = objs.length;
            for(int index = 0; index < len; ++index) {
                Object s = arr[index];
                if (isNullOrBlankTrim(s)) {
                    return false;
                }
            }
            return true;
        }
    }

    // 一个汉字在Oracle数据库里占多少字节跟数据库的字符集有关，UTF-8时，长度为3
    public static int getOracleLength(String str) {
        int ret = -1;
        if (str == null) {
            return ret;
        } else {
            String temp = str.replaceAll("[\\u4e00-\\u9fa5]", "***");
            return temp.length();
        }
    }

    public static boolean isOverOracleLength(String str, int maxLength) {
        int length = getOracleLength(str);
        return length > maxLength;
    }

    @Deprecated
    public static String getSafeXmlOracleStr(String str, int length) {
        if (isNullOrBlankTrim(str)) {
            return null;
        } else if (length <= 0) {
            return getSafeXmlString(str);
        } else {
            int ret = 0;
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < str.length(); ++i) {
                Character c = str.charAt(i);
                if (c >= ' ' || !(c != '\t' & c != '\n' & c != '\r')) {
                    int len = 1;
                    if (c > '}') {
                        len += 2;
                    }
                    if (ret + len > length) {
                        break;
                    }
                    ret += len;
                    sb.append(c);
                }
            }
            String returnString;
            for(returnString = sb.toString(); returnString.indexOf("]]>") != -1; returnString = returnString.replace("]]>", "]]")) {
            }
            return returnString;
        }
    }

    @Deprecated
    public static String getOracleStr(String str, int length) {
        if (str == null) {
            return null;
        } else {
            int ret = 0;
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < str.length(); ++i) {
                Character c = str.charAt(i);
                int len = 1;
                if (c > '}') {
                    len += 2;
                }
                if (ret + len > length) {
                    break;
                }
                ret += len;
                sb.append(c);
            }
            return sb.toString();
        }
    }

    public static String[] getSplitArray(String strs, String regExp, int limit) {
        return strs.split(regExp, limit);
    }

    public static Long[] getSplitLongArray(String strs, String regExp) {
        String[] s = getSplitArray(strs, regExp, 0);
        return strArrToLongArr(s);
    }

    public static Long[] strArrToLongArr(String[] strArray) {
        Long[] rl = new Long[strArray.length];
        for(int i = 0; i < strArray.length; ++i) {
            rl[i] = Long.parseLong(strArray[i]);
        }
        return rl;
    }

    public static boolean isInBetweenOracleLength(String str, int minLength, int maxLength) {
        int length = getOracleLength(str);
        return length >= minLength && length <= maxLength;
    }

    public static String filterHTML(String htmlString) {
        if (htmlString == null) {
            return null;
        } else {
            Matcher m_html = HTML_PATTERN.matcher(htmlString);
            return m_html.replaceAll("?");
        }
    }

    // 判断对象是否为空 如果是Collection集合 集合为空也返回true
    public static boolean isNull(Object o) {
        if (o == null) {
            return true;
        } else if (o instanceof Collection) {
            Collection c = (Collection)o;
            return c.isEmpty();
        } else {
            return false;
        }
    }

    public static String messageFormatParser(String msg, Object param) {
        if (!isNullOrBlankTrim(msg) && !isNull(param)) {
            MessageFormat mf = new MessageFormat(msg);
            if (!(param instanceof String) && !(param instanceof Integer) && !(param instanceof Float) && !(param instanceof Double)) {
                if (param instanceof Object[]) {
                    return mf.format((Object[])((Object[])param));
                } else if (param instanceof List) {
                    List p = (List)param;
                    return mf.format(p.toArray());
                } else {
                    return mf.format(new Object[]{param});
                }
            } else {
                return mf.format(new Object[]{param});
            }
        } else {
            return "";
        }
    }

    // 获取第一个匹配的子串
    public static String getOneStringByRegex(String con, String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(con);
        return m.matches() ? m.group(1) : "";
    }

    public static List<String> getStringsByRegex(String regex, int[] index, String txt) {
        List<String> res = new ArrayList();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(txt);
        if (m.find()) {
            int[] indexArr = index;
            int len = index.length;
            for(int j = 0; j < len; ++j) {
                int i = indexArr[j];
                res.add(m.group(i));
            }
        }
        return res;
    }

    // map转string
    public static String map2String(Map map) {
        if (map != null && !map.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            Object[] keys = map.keySet().toArray();
            int len = keys.length;
            for(int index = 0; index < len; ++index) {
                Object key = keys[index];
                sb.append(key).append("->").append(map.get(key)).append(" ");
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    // list转逗号分隔字符串(嵌套list会扁平化)
    public static String list2StringWithComma(List list) {
        if (list != null && !list.isEmpty()) {
            StringBuffer buf = new StringBuffer();
            for(int i = 0; i < list.size(); ++i) {
                Object o = list.get(i);
                if (!(o instanceof String) && !(o instanceof Number)) {
                    if (o instanceof List) {
                        buf.append(list2StringWithComma((List)o));
                    }
                } else {
                    buf.append(o.toString());
                }
                if (i < list.size() - 1) {
                    buf.append(",");
                }
            }
            return buf.toString();
        } else {
            return "";
        }
    }

    // float格式化为两位小数
    public static Float getPriceWith2bit(Float price) {
        DecimalFormat df = new DecimalFormat("0.00");
        String res = df.format(price);
        return Float.parseFloat(res);
    }

    // 按顺序格式化字符串 占位方式{i}
    public static String formatParamMsg(String message, Object[] args) {
        for(int i = 0; i < args.length; ++i) {
            message = message.replace("{" + i + "}", args[i].toString());
        }
        return message;
    }

    // 格式化字符串 占位方式{key}
    public static String formatParamMsg(String message, Map params) {
        if (params == null) {
            return message;
        } else {
            Iterator keyIts = params.keySet().iterator();
            while(keyIts.hasNext()) {
                String key = (String)keyIts.next();
                Object val = params.get(key);
                if (val != null) {
                    message = message.replace("${" + key + "}", val.toString());
                }
            }
            return message;
        }
    }

    // 格式化字符串 占位方式%i
    public static StringBuilder formatMsg(CharSequence msgWithFormat, boolean autoQuote, Object[] args) {
        int argsLen = args.length;
        boolean markFound = false;
        StringBuilder sb = new StringBuilder(msgWithFormat);
        if (argsLen > 0) {
            for(int i = 0; i < argsLen; ++i) {
                String flag = "%" + (i + 1);
                for(int idx = sb.indexOf(flag); idx >= 0; idx = sb.indexOf(flag)) {
                    markFound = true;
                    sb.replace(idx, idx + 2, toString(args[i], autoQuote));
                }
            }
            if (args[argsLen - 1] instanceof Throwable) {
                StringWriter sw = new StringWriter();
                ((Throwable)args[argsLen - 1]).printStackTrace(new PrintWriter(sw));
                sb.append("\n").append(sw.toString());
            } else if (argsLen == 1 && !markFound) {
                sb.append(args[argsLen - 1].toString());
            }
        }
        return sb;
    }

    public static String toString(Object obj, boolean autoQuote) {
        StringBuilder sb = new StringBuilder();
        if (obj == null) {
            sb.append("NULL");
        } else if (obj instanceof Object[]) {
            for(int i = 0; i < ((Object[])((Object[])((Object[])obj))).length; ++i) {
                sb.append(((Object[])((Object[])((Object[])obj)))[i]).append(", ");
            }
            if (sb.length() > 0) {
                sb.delete(sb.length() - 2, sb.length());
            }
        } else {
            sb.append(obj.toString());
        }
        if (autoQuote && sb.length() > 0 && (sb.charAt(0) != '[' || sb.charAt(sb.length() - 1) != ']') && (sb.charAt(0) != '{' || sb.charAt(sb.length() - 1) != '}')) {
            sb.insert(0, "[").append("]");
        }

        return sb.toString();
    }

    public static StringBuilder formatMsg(String msgWithFormat, Object[] args) {
        return formatMsg(new StringBuilder(msgWithFormat), true, args);
    }

    // 统计某个子串在字符串中出现的次数
    public static int charCount(String str, String oneChar) {
        int count = 0;
        while(str.indexOf(oneChar) != -1) {
            ++count;
            str = str.substring(str.indexOf(oneChar) + 1);
        }
        return count;
    }

    // 统计某个字符在字符串中出现的次数
    private static int charCount(String str, char c) {
        int count = 0;
        for(int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == c) {
                ++count;
            }
        }
        return count;
    }

    // 从sql字符串中获取表名 只能处理简单sql
    public static String getTableNameFromSql(String sql) {
        if (isNullOrBlankTrim(sql)) {
            return null;
        } else {
            sql = sql.toLowerCase().trim();
            String[] splitStrs = null;
            if (sql.startsWith("select")) {
                splitStrs = sql.split("from");
            } else if (sql.startsWith("update")) {
                splitStrs = sql.split("update");
            } else if (sql.startsWith("delete")) {
                splitStrs = sql.split("delete");
            } else {
                if (!sql.startsWith("insert")) {
                    return "";
                }
                splitStrs = sql.split("into");
            }
            String str = splitStrs[1];
            str = str.trim();
            str = str.split(" ")[0];
            return str;
        }
    }

    public static <T> String concat(T[] items, String separate) {
        return concat(items, separate, "", "");
    }

    public static <T> String concat(T[] items, String separate, String prefix, String suffix) {
        if (items == null) {
            return "";
        } else {
            List<T> list = new ArrayList();
            int len = items.length;
            for(int index = 0; index < len; ++index) {
                T item = items[index];
                list.add(item);
            }
            return concat((Collection)list, separate, prefix, suffix);
        }
    }

    public static <T> String concat(Collection<T> items, String separate) {
        return concat(items, separate, "", "");
    }

    public static <T> String concat(Collection<T> items, String separate, String prefix, String suffix) {
        if (items == null) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            if (prefix == null) {
                prefix = "";
            }
            if (suffix == null) {
                suffix = "";
            }
            if (separate == null) {
                separate = "";
            }
            Iterator<T> iterator = items.iterator();
            while(iterator.hasNext()) {
                T item = iterator.next();
                builder.append(prefix + item + suffix + separate);
            }
            return builder.length() == 0 ? builder.toString() : builder.substring(0, builder.length() - separate.length()).toString();
        }
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }

    public static boolean isNull(String arg) {
        return arg == null ? true : false;
    }

    public static boolean isEmpty(String arg) {
        return "".equals(arg) ? true : false;
    }

    public static boolean isNullStr(String arg) {
        return "null".equals(arg) ? true : false;
    }

    // 检查字符串不是null, "", "null"
    public static boolean isNotEmptyStr(String arg) {
        return !(isNull(arg) || isEmpty(arg) || isNullStr(arg)) ? true : false;
    }

    // 截取字符串后面部分字符
    public static String trimWords(String str, int length, boolean addPoints) {
        String wordStr = str;
        if (wordStr == null || wordStr.equals("")) {
            return "";
        }
        int byteLen = length * 2;
        byte[] strBytes = wordStr.getBytes();
        if (strBytes.length == str.length()) {
            if (strBytes.length <= byteLen) {
                return wordStr;
            }
            byte[] trimBytes = new byte[byteLen];
            System.arraycopy(strBytes, 0, trimBytes, 0, byteLen);
            wordStr = new String(trimBytes);
        } else {
            if (wordStr.length() <= length) {
                return str;
            }
            wordStr = left(str, length);
        }
        if (addPoints) {
            wordStr += "...";
        }
        return wordStr;
    }

    // 从左起取字符串前n位。
    public static String left(String str, int length) {
        if (str == null) {
            throw new IllegalArgumentException("字符串参数值不能为null");
        }
        if (length < 0) {
            throw new IllegalArgumentException("整型参数长度不能小于0");
        }
        if (str.length() < length) {
            throw new IllegalArgumentException("字符串参数长度不能小于" + length);
        }

        return str.substring(0, length);
    }

    // 过滤html代码
    public static String htmlContentConvert(String content) {
        content = content.replace("<", "&lt;");
        content = content.replace(">", "&gt;");
        content = content.replace("&", "&amp;");
        content = content.replace("\"", "&quot;");
        content = content.replace("'", "&apos;");
        content = content.replace("'", "&quot;");
        return content;
    }

    // 将数据库表字段字符串转换为驼峰标识字符串
    public static String convertField(String fieldName) {
        // check参数
        if (!isNotEmptyStr(fieldName)) {
            return null;
        }

        // buffer
        StringBuffer tmpData = new StringBuffer();
        // 转换为小写, 分割字符串
        fieldName = fieldName.toLowerCase();
        String[] tmpDatas = fieldName.split("_");
        if ((null == tmpDatas) || (0 == tmpDatas.length)) {
            return null;
        }
        int length = tmpDatas.length;
        if (1 == length) {
            tmpData.append(tmpDatas[0]);
            return tmpData.toString();
        } else {
            String data = "";
            tmpData.append(tmpDatas[0]);
            for (int index = 1; index < length; index++) {
                data = tmpDatas[index];
                // 将第一个字母转换为大写
                if(StringUtil.isNotEmptyStr(data)){
                    if(data.length()>1){
                        data=data.substring(0,1).toUpperCase() + data.substring(1);
                    }else{
                        data=data.substring(0,1).toUpperCase();
                    }
                }
                tmpData.append(data);
            }
        }

        return tmpData.toString();
    }

    // 将字符串的第index字符转换为大写
    public static String convertString(String data, int index) {
        // check
        if (StringUtil.isNull(data) || StringUtil.isEmpty(data)) {
            return data;
        }

        // 字符串长度
        int length = data.length();
        if ((index < 0) || (length < index)) {
            return data;
        }

        // 取出第index字母并进行替换
        char beginOld = data.charAt(0);
        char beginNew = (beginOld + "").toUpperCase().charAt(0);
        data = data.replace(beginOld, beginNew);

        return data;
    }

    // 判断一个字符串是否是数字
    public static boolean isNumeric(String number) {
        String model = "[0-9]*";

        Pattern pattern = Pattern.compile(model);
        Matcher isNum = pattern.matcher(number);

        return isNum.matches();
    }

    // 判断一个字符串是否是汉字
    public static boolean isChineseCharacter(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    // 判断一个字符串是否是字母
    public static boolean isCharacter(String fstrData) {
        char c = fstrData.charAt(0);
        if (((c >= 'a'&& c <= 'z') || (c >= 'A'&& c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }

    // 判断字符串是否为 "" 或 null
    public static boolean isBlank(String str) {
        return isNull(str) || str.trim().length() == 0 || str.trim().equals("null");
    }

    // 获取字符串的非null格式,字符串为空则返回""
    public static String getStr(String str) {
        if (null == str) {
            return "";
        }
        return str;
    }

    // 判断多个字符串中是否有一个字符串为空
    public static boolean hasBlankStr(String... strs) {
        if (null != strs) {
            for (String str : strs) {
                if (isBlank(str)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    // 设置对象的字符串的默认值为""
    public static String defaultEmptyStr(Object obj) {
        return defaultStr(obj,null);
    }

    // 设置对象的字符串的默认值
    public static String defaultStr(Object obj,String defaultStr) {
        if (null == obj) {
            if (null != defaultStr) {
                return defaultStr;
            } else {
                return "";
            }
        } else {
            return obj.toString();
        }
    }

}

