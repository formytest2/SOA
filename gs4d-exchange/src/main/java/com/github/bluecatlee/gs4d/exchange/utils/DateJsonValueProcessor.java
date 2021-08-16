package com.github.bluecatlee.gs4d.exchange.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateFormat dateFormat;

    public DateJsonValueProcessor(String datePattern) {
        try {
            this.dateFormat = new SimpleDateFormat(datePattern);
        } catch (Exception e) {
            this.dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        }
    }

    public DateJsonValueProcessor() {
        try {
            this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (Exception exception) {}
    }

    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    private Object process(Object value) {
        return this.dateFormat.format((Date)value);
    }
}
