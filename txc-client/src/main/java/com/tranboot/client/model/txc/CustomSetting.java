package com.tranboot.client.model.txc;

public class CustomSetting {
    public final String field;
    public final String table;
    public final Integer type;

    public CustomSetting(String field, String table, Integer type) {
        this.field = field;
        this.table = table;
        this.type = type;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CustomSetting)) {
            return false;
        } else {
            CustomSetting that = (CustomSetting)obj;
            return this.field.equals(that.field) && this.table.equals(that.table) && this.type == that.type;
        }
    }
}
