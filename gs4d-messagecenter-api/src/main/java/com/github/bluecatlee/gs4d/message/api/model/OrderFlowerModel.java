package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class OrderFlowerModel implements Serializable {
    private Long workflowId;

    public Long getWorkflowId() {
        return this.workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
}

