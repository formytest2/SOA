package com.tranboot.client.druid.wall;

public interface Violation {
    int getErrorCode();

    String getMessage();

    String toString();
}
