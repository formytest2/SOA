package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.List;
import java.util.Map;

public interface SQLObject {
    void accept(SQLASTVisitor var1);

    SQLObject getParent();

    void setParent(SQLObject var1);

    Map<String, Object> getAttributes();

    Object getAttribute(String var1);

    void putAttribute(String var1, Object var2);

    Map<String, Object> getAttributesDirect();

    void addBeforeComment(String var1);

    void addBeforeComment(List<String> var1);

    List<String> getBeforeCommentsDirect();

    void addAfterComment(String var1);

    void addAfterComment(List<String> var1);

    List<String> getAfterCommentsDirect();

    boolean hasBeforeComment();

    boolean hasAfterComment();

    void output(StringBuffer var1);
}

