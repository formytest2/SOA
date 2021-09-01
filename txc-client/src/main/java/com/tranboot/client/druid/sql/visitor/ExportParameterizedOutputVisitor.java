package com.tranboot.client.druid.sql.visitor;

import java.util.ArrayList;
import java.util.List;

public class ExportParameterizedOutputVisitor extends SQLASTOutputVisitor implements ExportParameterVisitor {
    private final boolean requireParameterizedOutput;

    public ExportParameterizedOutputVisitor(List<Object> parameters, Appendable appender, boolean wantParameterizedOutput) {
        super(appender, true);
        this.parameters = parameters;
        this.requireParameterizedOutput = wantParameterizedOutput;
    }

    public ExportParameterizedOutputVisitor() {
        this((List)(new ArrayList()));
    }

    public ExportParameterizedOutputVisitor(List<Object> parameters) {
        this(parameters, new StringBuilder(), false);
    }

    public ExportParameterizedOutputVisitor(Appendable appender) {
        this(new ArrayList(), appender, true);
    }

    public List<Object> getParameters() {
        return this.parameters;
    }
}
