package com.github.bluecatlee.gs4d.sequence.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SeqStringUtil {

    public static final String EMPTY = "";
    private static final int PAD_LIMIT = 8192;

    public SeqStringUtil() {
    }

    public static List<Integer> createSolution(int len) {
        List<Integer> list = new ArrayList();

        for(int i = 0; i < len; ++i) {
            list.add(i + 1);
        }

        Collections.shuffle(list);
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 随机获取List<String>中的一个元素
     * @param list
     * @return
     */
    public static String getRandomNum(List<String> list) {
        Random random = new Random();
        int n = random.nextInt(list.size());
        return (String)list.get(n);
    }

    /**
     * 根据填充字符和填充次数生成填充字符串
     * @param repeat
     * @param padChar
     * @return
     * @throws IndexOutOfBoundsException
     */
    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        } else {
            char[] buf = new char[repeat];

            for(int i = 0; i < buf.length; ++i) {
                buf[i] = padChar;
            }

            return new String(buf);
        }
    }

    public static String rightPad(String str, int size) {
        return rightPad(str, size, ' ');
    }

    /**
     * 右填充
     *      如果要填充的长度小于等于8192，则直接在原始串右边拼接填充串
     * @param str       原始串
     * @param size      填充后的长度
     * @param padChar   填充字符
     * @return
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        } else {
            int pads = size - str.length(); // 填充长度
            if (pads <= 0) {
                return str;
            } else {
                return pads > PAD_LIMIT ? rightPad(str, size, String.valueOf(padChar)) : str.concat(padding(pads, padChar));
            }
        }
    }

    /**
     * 右填充
     * @param str       原始串
     * @param size      填充后的长度
     * @param padStr    填充串
     * @return
     */
    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        } else {
            if (isEmpty(padStr)) {
                padStr = " ";               // 填充串默认为空格
            }

            int padLen = padStr.length();   // 填充串长度
            int strLen = str.length();      // 原始串长度
            int pads = size - strLen;
            if (pads <= 0) {
                return str;                                     // 填充长度小于原始串长度时 直接返回原始串
            } else if (padLen == 1 && pads <= PAD_LIMIT) {
                return rightPad(str, size, padStr.charAt(0));   // 原始串右边拼接填充串(填充字符重复pads次)
            } else if (pads == padLen) {
                return str.concat(padStr);                      // 填充长度刚好等于填充串的长度时 在原始串右边拼接填充串即可
            } else if (pads < padLen) {
                return str.concat(padStr.substring(0, pads));   // 填充长度小于填充串长度时 在原始串右边拼接填充串的一部分
            } else {
                char[] padding = new char[pads];                // 填充长度大于填充串长度 且填充串长度不为1 则在原始串的右边重复拼接原始串 直到长度达到
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return str.concat(new String(padding));
            }
        }
    }

    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        } else {
            int pads = size - str.length();
            if (pads <= 0) {
                return str;
            } else {
                return pads > PAD_LIMIT ? leftPad(str, size, String.valueOf(padChar)) : padding(pads, padChar).concat(str);
            }
        }
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        } else {
            if (isEmpty(padStr)) {
                padStr = " ";
            }

            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if (pads <= 0) {
                return str;
            } else if (padLen == 1 && pads <= PAD_LIMIT) {
                return leftPad(str, size, padStr.charAt(0));
            } else if (pads == padLen) {
                return padStr.concat(str);
            } else if (pads < padLen) {
                return padStr.substring(0, pads).concat(str);
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return (new String(padding)).concat(str);
            }
        }
    }

    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 生成一个字符串 长度与parm相同 最后部分与numStr相同 前面部分补零。  numStr长度必须不大于parm长度
     * @param parm
     * @param numStr
     * @return
     */
    public static String getSquByNum(String parm, String numStr) {
        String b = EMPTY;
        String[] a = new String[parm.length()];

        for(int i = 0; i < a.length; ++i) {
            a[i] = "0";
            b = b + a[i];
        }

        String str_m = b.substring(0, b.length() - numStr.length()) + numStr;
        return str_m;
    }

    /**
     * 生成一个字符串 最后部分与sourceNum的字符串值相同 如果字符串长度不足formatLength，前面补零
     * @param sourceNum
     * @param formatLength
     * @return
     */
    public static String frontCompWithZore(Long sourceNum, int formatLength) {
        return String.format("%0" + formatLength + "d", sourceNum);
    }

}
