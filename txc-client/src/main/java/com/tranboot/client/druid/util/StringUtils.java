package com.tranboot.client.druid.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {
    private static final Logger LOG = LoggerFactory.getLogger(StringUtils.class);

    public StringUtils() {
    }

    public static Integer subStringToInteger(String src, String start, String to) {
        return stringToInteger(subString(src, start, to));
    }

    public static String subString(String src, String start, String to) {
        int indexFrom = start == null ? 0 : src.indexOf(start);
        int indexTo = to == null ? src.length() : src.indexOf(to);
        if (indexFrom >= 0 && indexTo >= 0 && indexFrom <= indexTo) {
            if (null != start) {
                indexFrom += start.length();
            }

            return src.substring(indexFrom, indexTo);
        } else {
            return null;
        }
    }

    public static String subString(String src, String start, String to, boolean toLast) {
        if (!toLast) {
            return subString(src, start, to);
        } else {
            int indexFrom = start == null ? 0 : src.indexOf(start);
            int indexTo = to == null ? src.length() : src.lastIndexOf(to);
            if (indexFrom >= 0 && indexTo >= 0 && indexFrom <= indexTo) {
                if (null != start) {
                    indexFrom += start.length();
                }

                return src.substring(indexFrom, indexTo);
            } else {
                return null;
            }
        }
    }

    public static Integer stringToInteger(String in) {
        if (in == null) {
            return null;
        } else {
            in = in.trim();
            if (in.length() == 0) {
                return null;
            } else {
                try {
                    return Integer.parseInt(in);
                } catch (NumberFormatException var2) {
                    LOG.warn("stringToInteger fail,string=" + in, var2);
                    return null;
                }
            }
        }
    }

    public static boolean equals(String a, String b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equalsIgnoreCase(b);
        }
    }

    public static boolean isEmpty(CharSequence value) {
        return value == null || value.length() == 0;
    }

    public static int lowerHashCode(String text) {
        if (text == null) {
            return 0;
        } else {
            int h = 0;

            for(int i = 0; i < text.length(); ++i) {
                char ch = text.charAt(i);
                if (ch >= 'A' && ch <= 'Z') {
                    ch = (char)(ch + 32);
                }

                h = 31 * h + ch;
            }

            return h;
        }
    }

    public static boolean isNumber(String str) {
        if (isEmpty(str)) {
            return false;
        } else {
            char[] chars = str.toCharArray();
            int sz = chars.length;
            boolean hasExp = false;
            boolean hasDecPoint = false;
            boolean allowSigns = false;
            boolean foundDigit = false;
            int start = chars[0] == '-' ? 1 : 0;
            int i;
            if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
                i = start + 2;
                if (i == sz) {
                    return false;
                } else {
                    while(i < chars.length) {
                        if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                            return false;
                        }

                        ++i;
                    }

                    return true;
                }
            } else {
                --sz;

                for(i = start; i < sz || i < sz + 1 && allowSigns && !foundDigit; ++i) {
                    if (chars[i] >= '0' && chars[i] <= '9') {
                        foundDigit = true;
                        allowSigns = false;
                    } else if (chars[i] == '.') {
                        if (hasDecPoint || hasExp) {
                            return false;
                        }

                        hasDecPoint = true;
                    } else if (chars[i] != 'e' && chars[i] != 'E') {
                        if (chars[i] != '+' && chars[i] != '-') {
                            return false;
                        }

                        if (!allowSigns) {
                            return false;
                        }

                        allowSigns = false;
                        foundDigit = false;
                    } else {
                        if (hasExp) {
                            return false;
                        }

                        if (!foundDigit) {
                            return false;
                        }

                        hasExp = true;
                        allowSigns = true;
                    }
                }

                if (i < chars.length) {
                    if (chars[i] >= '0' && chars[i] <= '9') {
                        return true;
                    } else if (chars[i] != 'e' && chars[i] != 'E') {
                        if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                            return foundDigit;
                        } else if (chars[i] != 'l' && chars[i] != 'L') {
                            return false;
                        } else {
                            return foundDigit && !hasExp;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return !allowSigns && foundDigit;
                }
            }
        }
    }
}
