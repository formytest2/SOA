package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.List;

public interface WallVisitor extends SQLASTVisitor {
    WallConfig getConfig();

    WallProvider getProvider();

    List<Violation> getViolations();

    void addViolation(Violation var1);

    boolean isDenyTable(String var1);

    String toSQL(SQLObject var1);

    boolean isSqlModified();

    void setSqlModified(boolean var1);

    String getDbType();

    boolean isSqlEndOfComment();

    void setSqlEndOfComment(boolean var1);
}
