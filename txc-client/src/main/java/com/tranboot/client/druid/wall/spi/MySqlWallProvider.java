package com.tranboot.client.druid.wall.spi;

import com.tranboot.client.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlExportParameterVisitor;
import com.tranboot.client.druid.sql.parser.SQLStatementParser;
import com.tranboot.client.druid.sql.visitor.ExportParameterVisitor;
import com.tranboot.client.druid.wall.WallConfig;
import com.tranboot.client.druid.wall.WallProvider;
import com.tranboot.client.druid.wall.WallVisitor;

public class MySqlWallProvider extends WallProvider {
    public static final String DEFAULT_CONFIG_DIR = "META-INF/druid/wall/mysql";

    public MySqlWallProvider() {
        this(new WallConfig("META-INF/druid/wall/mysql"));
    }

    public MySqlWallProvider(WallConfig config) {
        super(config, "mysql");
    }

    public SQLStatementParser createParser(String sql) {
        return new MySqlStatementParser(sql);
    }

    public WallVisitor createWallVisitor() {
        return new MySqlWallVisitor(this);
    }

    public ExportParameterVisitor createExportParameterVisitor() {
        return new MySqlExportParameterVisitor();
    }
}
