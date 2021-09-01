package com.tranboot.client.druid.support.monitor.annotation;

public enum AggregateType {
    None,
    Sum,
    Max,
    Last;

    private AggregateType() {
    }
}
