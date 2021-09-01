package com.tranboot.client.model.dbsync;

public class SqlRouter {
    private String sourcePartitionKey;
    private String targetPartitionKey;

    public SqlRouter(String sourcePartitionKey, String targetPartitionKey) {
        this.sourcePartitionKey = sourcePartitionKey;
        this.targetPartitionKey = targetPartitionKey;
    }

    public String getSourcePartitionKey() {
        return this.sourcePartitionKey;
    }

    public void setSourcePartitionKey(String sourcePartitionKey) {
        this.sourcePartitionKey = sourcePartitionKey;
    }

    public String getTargetPartitionKey() {
        return this.targetPartitionKey;
    }

    public void setTargetPartitionKey(String targetPartitionKey) {
        this.targetPartitionKey = targetPartitionKey;
    }
}

