package com.github.bluecatlee.gs4d.sequence.utils;

public class SqlUtil {

    public static String genSqlValues(int count) {
        String str = "?";
        for (byte b = 1; b < count; b++) {
            str = str + ",?";
        }
        return "values(" + str + ")";
    }

}
