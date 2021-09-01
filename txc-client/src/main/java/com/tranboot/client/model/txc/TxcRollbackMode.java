package com.tranboot.client.model.txc;

public enum TxcRollbackMode {
    SERIAL(1),          // 串行
    PARALLEL(0);        // 并行

    private int mode;

    private TxcRollbackMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return this.mode;
    }
}
