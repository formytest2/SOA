package com.tranboot.client.utils;

import java.util.UUID;

public class BranchIdGenerator {

    public static long branchId() {
        return SHAHashUtils.unsignedLongHash(new Object[]{System.currentTimeMillis(), UUID.randomUUID().toString()});
    }

}

