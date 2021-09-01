package com.tranboot.client.druid.wall.violation;

public interface ErrorCode {
    int SYNTAX_ERROR = 1001;
    int SELECT_NOT_ALLOW = 1002;
    int SELECT_INTO_NOT_ALLOW = 1003;
    int INSERT_NOT_ALLOW = 1004;
    int DELETE_NOT_ALLOW = 1005;
    int UPDATE_NOT_ALLOW = 1006;
    int MINUS_NOT_ALLOW = 1007;
    int INTERSET_NOT_ALLOW = 1008;
    int MERGE_NOT_ALLOW = 1009;
    int REPLACE_NOT_ALLOW = 1010;
    int HINT_NOT_ALLOW = 1400;
    int CALL_NOT_ALLOW = 1300;
    int COMMIT_NOT_ALLOW = 1301;
    int ROLLBACK_NOT_ALLOW = 1302;
    int START_TRANSACTION_NOT_ALLOW = 1303;
    int BLOCK_NOT_ALLOW = 1304;
    int SET_NOT_ALLOW = 1200;
    int DESC_NOT_ALLOW = 1201;
    int SHOW_NOT_ALLOW = 1202;
    int USE_NOT_ALLOW = 1203;
    int NONE_BASE_STATEMENT_NOT_ALLOW = 1999;
    int TRUNCATE_NOT_ALLOW = 1100;
    int CREATE_TABLE_NOT_ALLOW = 1101;
    int ALTER_TABLE_NOT_ALLOW = 1102;
    int DROP_TABLE_NOT_ALLOW = 1103;
    int COMMENT_STATEMENT_NOT_ALLOW = 1104;
    int RENAME_TABLE_NOT_ALLOW = 1105;
    int LOCK_TABLE_NOT_ALLOW = 1106;
    int LIMIT_ZERO = 2200;
    int MULTI_STATEMENT = 2201;
    int FUNCTION_DENY = 2001;
    int SCHEMA_DENY = 2002;
    int VARIANT_DENY = 2003;
    int TABLE_DENY = 2004;
    int OBJECT_DENY = 2005;
    int ALWAYS_TRUE = 2100;
    int CONST_ARITHMETIC = 2101;
    int XOR = 2102;
    int BITWISE = 2103;
    int NONE_CONDITION = 2104;
    int LIKE_NUMBER = 2105;
    int EMPTY_QUERY_HAS_CONDITION = 2106;
    int DOUBLE_CONST_CONDITION = 2107;
    int SAME_CONST_LIKE = 2108;
    int CONST_CASE_CONDITION = 2109;
    int EVIL_HINTS = 2110;
    int EVIL_NAME = 2111;
    int EVIL_CONCAT = 2112;
    int ALWAYS_FALSE = 2113;
    int NOT_PARAMETERIZED = 2200;
    int MULTI_TENANT = 2201;
    int INTO_OUTFILE = 3000;
    int READ_ONLY = 4000;
    int UNION = 5000;
    int COMPOUND = 8000;
    int OTHER = 9999;
}
