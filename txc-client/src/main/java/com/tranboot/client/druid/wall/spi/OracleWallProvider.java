package com.tranboot.client.druid.wall.spi;

import com.tranboot.client.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleExportParameterVisitor;
import com.tranboot.client.druid.sql.parser.SQLStatementParser;
import com.tranboot.client.druid.sql.visitor.ExportParameterVisitor;
import com.tranboot.client.druid.wall.WallConfig;
import com.tranboot.client.druid.wall.WallProvider;
import com.tranboot.client.druid.wall.WallVisitor;

public class OracleWallProvider extends WallProvider {
    public static final String DEFAULT_CONFIG_DIR = "META-INF/druid/wall/oracle";

    public OracleWallProvider() {
        this(new WallConfig("META-INF/druid/wall/oracle"));
    }

    public OracleWallProvider(WallConfig config) {
        super(config, "oracle");
    }

    public SQLStatementParser createParser(String sql) {
        return new OracleStatementParser(sql);
    }

    public WallVisitor createWallVisitor() {
        return new OracleWallVisitor(this);
    }

    public ExportParameterVisitor createExportParameterVisitor() {
        return new OracleExportParameterVisitor();
    }
}
