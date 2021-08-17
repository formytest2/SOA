package com.github.bluecatlee.gs4d.transaction.utils;

public class AU {

    public static Long R = 100L;
    public static Long S = 50000L;

    public static Long d(Long var0, Long var1) {
        return (var0 - 1L) * R;
    }

    public static Long e(Long var0, Long var1) {
        return (var0 - 1L) * var1;
    }

    public static Long c(Long var0, Long var1) {
        Long var2 = 0L;
        if (var0 > var1) {
            Long var3 = var0 % var1;
            var2 = var0 / var1;
            if (var3 > 0L) {
                var2 = var2 + 1L;
            }
        } else if (var0 > 0L && var0 <= var1) {
            var2 = 1L;
        }

        return var2;
    }
}

