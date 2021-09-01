package com.tranboot.client.druid.sql.ast;

public interface SQLSetQuantifier {
    int ALL = 1;
    int DISTINCT = 2;
    int UNIQUE = 3;
    int DISTINCTROW = 4;
}
