package com.github.bluecatlee.gs4d.monitor.api.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class BaseEntity implements Comparable<Object>, Serializable {
    private static final long serialVersionUID = -6521445851557510309L;

    public abstract Long getSeries();

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public int compareTo(Object obj) {
        int compare = -1;
        if (obj == null) {
            compare = -1;
        } else if (this == obj) {
            compare = 0;
        } else if (!(obj instanceof BaseEntity)) {
            compare = -1;
        } else if (!getClass().equals(obj.getClass())) {
            compare = -1;
        } else {
            BaseEntity castObj = (BaseEntity)obj;
            CompareToBuilder builder = new CompareToBuilder();
            builder.append(getSeries(), castObj.getSeries());
            compare = builder.toComparison();
        }
        return compare;
    }

    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj == null) {
            isEqual = false;
        } else if (this == obj) {
            isEqual = true;
        } else if (!(obj instanceof BaseEntity)) {
            isEqual = false;
        } else if (!getClass().equals(obj.getClass())) {
            isEqual = false;
        } else {
            BaseEntity castObj = (BaseEntity)obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getSeries(), castObj.getSeries());
            isEqual = builder.isEquals();
        }
        return isEqual;
    }
}

