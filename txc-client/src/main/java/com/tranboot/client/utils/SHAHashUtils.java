package com.tranboot.client.utils;

import com.google.common.base.Charsets;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import java.io.UnsupportedEncodingException;

public class SHAHashUtils {
    public SHAHashUtils() {
    }

    public static Hasher createHasher() {
        return Hashing.sha256().newHasher();
    }

    public static long longHash(Object... objects) {
        Hasher haser = createHasher();
        Object[] var2 = objects;
        int var3 = objects.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object o = var2[var4];
            if (o instanceof Integer) {
                haser.putInt((Integer)o);
            } else if (o instanceof String) {
                haser.putString((String)o, Charsets.UTF_8);
            } else if (o instanceof Long) {
                haser.putLong((Long)o);
            } else if (o instanceof Boolean) {
                haser.putBoolean((Boolean)o);
            } else if (o instanceof Character) {
                haser.putChar((Character)o);
            } else if (o instanceof byte[]) {
                haser.putBytes((byte[])((byte[])o));
            } else if (o instanceof Byte) {
                haser.putByte((Byte)o);
            }
        }

        return haser.hash().asLong();
    }

    public static long unsignedLongHash(Object... objects) {
        Hasher haser = createHasher();
        Object[] var2 = objects;
        int var3 = objects.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object o = var2[var4];
            if (o instanceof Integer) {
                haser.putInt((Integer)o);
            } else if (o instanceof String) {
                haser.putString((String)o, Charsets.UTF_8);
            } else if (o instanceof Long) {
                haser.putLong((Long)o);
            } else if (o instanceof Boolean) {
                haser.putBoolean((Boolean)o);
            } else if (o instanceof Character) {
                haser.putChar((Character)o);
            } else if (o instanceof byte[]) {
                haser.putBytes((byte[])((byte[])o));
            } else if (o instanceof Byte) {
                haser.putByte((Byte)o);
            }
        }

        return Integer.toUnsignedLong(haser.hash().asInt());
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(unsignedLongHash(7, "editCartItem"));
    }
}
