package com.github.bluecatlee.gs4d.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Md5Util {

    private static Logger log = LoggerFactory.getLogger(Md5Util.class);

    public static String makeMd5Sum(byte[] srcContent) {
        if (srcContent == null) {
            return null;
        } else {
            String strDes = null;

            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(srcContent);
                strDes = bytes2Hex(md5.digest());
                return strDes;
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        }
    }

    private static String bytes2Hex(byte[] byteArray) {
        StringBuffer strBuf = new StringBuffer();

        for(int i = 0; i < byteArray.length; ++i) {
            if (byteArray[i] >= 0 && byteArray[i] < 16) {
                strBuf.append("0");
            }

            strBuf.append(Integer.toHexString(byteArray[i] & 255));
        }

        return strBuf.toString();
    }

    public static String md5Signature(TreeMap<String, String> params, String secret) {
        String result = null;
        StringBuffer orgin = getBeforeSign(params, new StringBuffer(secret));
        if (orgin == null) {
            return result;
        } else {
            orgin.append(secret);

            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                result = bytes2Hex(md.digest(orgin.toString().getBytes("utf-8")));
                return result;
            } catch (Exception e) {
                throw new RuntimeException("sign error !");
            }
        }
    }

    private static StringBuffer getBeforeSign(TreeMap<String, String> params, StringBuffer orgin) {
        if (params == null) {
            return null;
        } else {
            Map<String, String> treeMap = new TreeMap();
            treeMap.putAll(params);
            Iterator iter = treeMap.keySet().iterator();

            while(iter.hasNext()) {
                String name = (String)iter.next();
                orgin.append(name).append((String)params.get(name));
            }

            return orgin;
        }
    }

    public static String md5Signature(String origin) {
        String result = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = bytes2Hex(md.digest(origin.getBytes("utf-8")));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("sign error !");
        }
    }

    public static final String md5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            log.debug("newmd5:{}", new String(str));
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
