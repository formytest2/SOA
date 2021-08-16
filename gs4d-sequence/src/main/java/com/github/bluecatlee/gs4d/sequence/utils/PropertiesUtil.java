package com.github.bluecatlee.gs4d.sequence.utils;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties工具类
 */
public class PropertiesUtil {
    private static Properties properties = null;

    public PropertiesUtil() {
    }

    public static synchronized Properties loadProps(String path) {
        properties = new Properties();
        BufferedInputStream in = null;

        try {
            in = new BufferedInputStream(new FileInputStream(path));
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static String getString(Properties properties, String key) {
        return properties.getProperty(key);
    }

    public static void updateProperty(Properties properties, String key, String value, String path) {
        try {
            properties.setProperty(key, value);
            FileOutputStream outputFile = new FileOutputStream(path);
            properties.store(outputFile, (String)null);
            outputFile.flush();
            outputFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Map<String, String> readAllProperties() {
        Map<String, String> map = new HashMap();
        Enumeration en = properties.propertyNames();

        while(en.hasMoreElements()) {
            String key = (String)en.nextElement();
            String value = properties.getProperty(key);
            map.put(key, value);
        }

        return map;
    }

    public static boolean removeValue(Properties properties, String key, String path) {
        try {
            properties.remove(key);
            OutputStream fos = new FileOutputStream(path);
            properties.store(fos, "Delete '" + key + "' value");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateAllPropertiesNull(Properties properties, String path) {
        Enumeration en = properties.propertyNames();

        while(en.hasMoreElements()) {
            String key = (String)en.nextElement();
            updateProperty(properties, key, "", path);
        }

    }

    public static boolean isOSLinux() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        return os != null && os.toLowerCase().indexOf("linux") > -1;
    }

}

