package com.github.bluecatlee.gs4d.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class CamelUnderlineUtil {

    public static final char UNDERLINE = '_';

    /**
     * 驼峰转下划线
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param != null && !"".equals(param.trim())) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for(int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append('_');
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 下划线转驼峰
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param != null && !"".equals(param.trim())) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for(int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (c == '_') {
                    ++i;
                    if (i < len) {
                        sb.append(Character.toUpperCase(param.charAt(i)));
                    }
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String underlineToCamel2(String param) {
        if (param != null && !"".equals(param.trim())) {
            StringBuilder sb = new StringBuilder(param);
            Matcher mc = Pattern.compile("_").matcher(param);
            int k = 0;

            while(mc.find()) {
                int position = mc.end() - k++;
                sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String camel2Underline(String line) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(line)) {
            sb = new StringBuilder();
            line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
            Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
            Matcher matcher = pattern.matcher(line);

            while(matcher.find()) {
                String word = matcher.group();
                sb.append(word.toUpperCase());
                sb.append(matcher.end() == line.length() ? "" : "_");
            }
        }

        return sb.toString();
    }

    public static String underline2Camel(String line, boolean smallCamel) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(line)) {
            Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
            Matcher matcher = pattern.matcher(line);

            while(matcher.find()) {
                String word = matcher.group();
                sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
                int index = word.lastIndexOf(95);
                if (index > 0) {
                    sb.append(word.substring(1, index).toLowerCase());
                } else {
                    sb.append(word.substring(1).toLowerCase());
                }
            }
        }

        return sb.toString();
    }
}
