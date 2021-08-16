package com.github.bluecatlee.gs4d.common.model;

import java.io.Serializable;

public class Parameter implements Serializable {
    private static final long serialVersionUID = 6133756575619031755L;
    private Integer sequece;
    private Long type;
    private Object value;

    public Parameter() {
    }

    public Integer getSequece() {
        return this.sequece;
    }

    public void setSequece(Integer sequece) {
        this.sequece = sequece;
    }

    public Long getType() {
        return this.type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

