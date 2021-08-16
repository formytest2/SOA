package com.github.bluecatlee.gs4d.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GipUtil {

    public static String compress(String str) throws IOException {
        if(str != null && str.length() != 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            gzip.close();
            return out.toString("ISO-8859-1");
        } else {
            return str;
        }
    }

    public static String uncompress(String str) throws IOException {
        if(str != null && str.length() != 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];

            int n;
            while((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }

            return out.toString("UTF-8");
        } else {
            return str;
        }
    }

    public static String gzip(String primStr) {
        if(primStr != null && primStr.length() != 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = null;

            try {
                gzip = new GZIPOutputStream(out);
                gzip.write(primStr.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(gzip != null) {
                    try {
                        gzip.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            return Base64.getEncoder().encodeToString(out.toByteArray());
        } else {
            return primStr;
        }
    }

    public static String gunzip(String compressedStr) {
        if(compressedStr == null) {
            return null;
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = null;
            GZIPInputStream ginzip = null;
            byte[] compressed = null;
            String decompressed = null;

            try {
                compressed = Base64.getDecoder().decode(compressedStr);
                in = new ByteArrayInputStream(compressed);
                ginzip = new GZIPInputStream(in);
                byte[] buffer = new byte[1024];

                int offset;
                while((offset = ginzip.read(buffer)) != -1) {
                    out.write(buffer, 0, offset);
                }

                decompressed = out.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(ginzip != null) {
                    try {
                        ginzip.close();
                    } catch (IOException e) {
                        ;
                    }
                }

                if(in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        ;
                    }
                }

                if(out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        ;
                    }
                }

            }

            return decompressed;
        }
    }
}

