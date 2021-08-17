package com.github.bluecatlee.gs4d.transaction.utils;

public class SqlUtil {

    public static String assembleInsertValuesSql(int argsCount) {
        String placeHolder = "?";

        for(int i = 1; i < argsCount; ++i) {
            placeHolder = placeHolder + ",?";
        }

        return "values(" + placeHolder + ")";
    }

}

