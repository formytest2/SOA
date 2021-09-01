package com.tranboot.client.core.txc;

import com.tranboot.client.model.txc.TxcRollbackMode;
import java.lang.annotation.Annotation;

public final class DefaultTxcTransaction implements TxcTransaction {
    public DefaultTxcTransaction() {
    }

    public Class<? extends Annotation> annotationType() {
        return null;
    }

    public TxcRollbackMode rollbackMode() {
        return TxcRollbackMode.PARALLEL;
    }

    public int timeout() {
        return 0;
    }
}

