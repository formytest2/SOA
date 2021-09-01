package com.tranboot.client.druid.sql.dialect.mysql.ast.clause;

import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLBlockStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLIfStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLLoopStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;

public enum MySqlStatementType {
    SELECT(SQLSelectStatement.class.getName()),
    UPDATE(MySqlUpdateStatement.class.getName()),
    INSERT(MySqlInsertStatement.class.getName()),
    DELETE(MySqlDeleteStatement.class.getName()),
    WHILE(MySqlWhileStatement.class.getName()),
    IF(SQLIfStatement.class.getName()),
    LOOP(SQLLoopStatement.class.getName()),
    BLOCK(SQLBlockStatement.class.getName()),
    DECLARE(MySqlDeclareStatement.class.getName()),
    SELECTINTO(MySqlSelectIntoStatement.class.getName()),
    CASE(MySqlCaseStatement.class.getName()),
    UNDEFINED;

    public final String name;

    private MySqlStatementType() {
        this((String)null);
    }

    private MySqlStatementType(String name) {
        this.name = name;
    }

    public static MySqlStatementType getType(SQLStatement stmt) {
        MySqlStatementType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            MySqlStatementType type = var1[var3];
            if (type.name == stmt.getClass().getName()) {
                return type;
            }
        }

        return UNDEFINED;
    }
}
