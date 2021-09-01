package com.tranboot.client.druid.util;

import java.io.Closeable;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JdbcUtils implements JdbcConstants {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcUtils.class);
    private static final Properties DRIVER_URL_MAPPING = new Properties();
    private static Boolean mysql_driver_version_6 = null;

    public JdbcUtils() {
    }

    public static void close(Connection x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception var2) {
                LOG.debug("close connection error", var2);
            }

        }
    }

    public static void close(Statement x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception var2) {
                LOG.debug("close statement error", var2);
            }

        }
    }

    public static void close(ResultSet x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception var2) {
                LOG.debug("close result set error", var2);
            }

        }
    }

    public static void close(Closeable x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception var2) {
                LOG.debug("close error", var2);
            }

        }
    }

    public static void printResultSet(ResultSet rs) throws SQLException {
        printResultSet(rs, System.out);
    }

    public static void printResultSet(ResultSet rs, PrintStream out) throws SQLException {
        printResultSet(rs, out, true, "\t");
    }

    public static void printResultSet(ResultSet rs, PrintStream out, boolean printHeader, String seperator) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        int columnIndex;
        if (printHeader) {
            for(columnIndex = 1; columnIndex <= columnCount; ++columnIndex) {
                if (columnIndex != 1) {
                    out.print(seperator);
                }

                out.print(metadata.getColumnName(columnIndex));
            }
        }

        out.println();

        while(rs.next()) {
            for(columnIndex = 1; columnIndex <= columnCount; ++columnIndex) {
                if (columnIndex != 1) {
                    out.print(seperator);
                }

                int type = metadata.getColumnType(columnIndex);
                if (type != 12 && type != 1 && type != -9 && type != -15) {
                    if (type == 91) {
                        Date date = rs.getDate(columnIndex);
                        if (rs.wasNull()) {
                            out.print("null");
                        } else {
                            out.print(date.toString());
                        }
                    } else {
                        if (type == -7) {
                            boolean value = rs.getBoolean(columnIndex);
                            if (rs.wasNull()) {
                                out.print("null");
                            } else {
                                out.print(Boolean.toString(value));
                            }
                        } else if (type == 16) {
                            boolean value = rs.getBoolean(columnIndex);
                            if (rs.wasNull()) {
                                out.print("null");
                            } else {
                                out.print(Boolean.toString(value));
                            }
                        } else if (type == -6) {
                            byte value = rs.getByte(columnIndex);
                            if (rs.wasNull()) {
                                out.print("null");
                            } else {
                                out.print(Byte.toString(value));
                            }
                        } else if (type == 5) {
                            short value = rs.getShort(columnIndex);
                            if (rs.wasNull()) {
                                out.print("null");
                            } else {
                                out.print(Short.toString(value));
                            }
                        } else if (type == 4) {
                            int value = rs.getInt(columnIndex);
                            if (rs.wasNull()) {
                                out.print("null");
                            } else {
                                out.print(Integer.toString(value));
                            }
                        } else if (type == -5) {
                            long value = rs.getLong(columnIndex);
                            if (rs.wasNull()) {
                                out.print("null");
                            } else {
                                out.print(Long.toString(value));
                            }
                        } else if (type == 93) {
                            out.print(String.valueOf(rs.getTimestamp(columnIndex)));
                        } else if (type == 3) {
                            out.print(String.valueOf(rs.getBigDecimal(columnIndex)));
                        } else if (type == 2005) {
                            out.print(String.valueOf(rs.getString(columnIndex)));
                        } else {
                            if (type == 2000) {
                                Object object = rs.getObject(columnIndex);
                                if (rs.wasNull()) {
                                    out.print("null");
                                } else {
                                    out.print(String.valueOf(object));
                                }
                            } else if (type == -1) {
                                Object object = rs.getString(columnIndex);
                                if (rs.wasNull()) {
                                    out.print("null");
                                } else {
                                    out.print(String.valueOf(object));
                                }
                            } else if (type == 0) {
                                out.print("null");
                            } else {
                                Object object = rs.getObject(columnIndex);
                                if (rs.wasNull()) {
                                    out.print("null");
                                } else if (object instanceof byte[]) {
                                    byte[] bytes = (byte[])((byte[])object);
                                    String text = HexBin.encode(bytes);
                                    out.print(text);
                                } else {
                                    out.print(String.valueOf(object));
                                }
                            }
                        }
                    }
                } else {
                    out.print(rs.getString(columnIndex));
                }
            }

            out.println();
        }

    }

    public static String getTypeName(int sqlType) {
        switch(sqlType) {
            case -16:
                return "LONGNVARCHAR";
            case -15:
                return "NCHAR";
            case -9:
                return "NVARCHAR";
            case -8:
                return "ROWID";
            case -7:
                return "BIT";
            case -6:
                return "TINYINT";
            case -5:
                return "BIGINT";
            case -4:
                return "LONGVARBINARY";
            case -3:
                return "VARBINARY";
            case -2:
                return "BINARY";
            case 0:
                return "NULL";
            case 1:
                return "CHAR";
            case 2:
                return "NUMERIC";
            case 3:
                return "DECIMAL";
            case 4:
                return "INTEGER";
            case 5:
                return "SMALLINT";
            case 6:
                return "FLOAT";
            case 7:
                return "REAL";
            case 8:
                return "DOUBLE";
            case 12:
                return "VARCHAR";
            case 16:
                return "BOOLEAN";
            case 70:
                return "DATALINK";
            case 91:
                return "DATE";
            case 92:
                return "TIME";
            case 93:
                return "TIMESTAMP";
            case 2000:
                return "JAVA_OBJECT";
            case 2001:
                return "DISTINCT";
            case 2002:
                return "STRUCT";
            case 2003:
                return "ARRAY";
            case 2004:
                return "BLOB";
            case 2005:
                return "CLOB";
            case 2006:
                return "REF";
            case 2009:
                return "SQLXML";
            case 2011:
                return "NCLOB";
            default:
                return "OTHER";
        }
    }

    public static String getDriverClassName(String rawUrl) throws SQLException {
        if (rawUrl == null) {
            return null;
        } else if (rawUrl.startsWith("jdbc:derby:")) {
            return "org.apache.derby.jdbc.EmbeddedDriver";
        } else if (rawUrl.startsWith("jdbc:mysql:")) {
            if (mysql_driver_version_6 == null) {
                mysql_driver_version_6 = Utils.loadClass("com.mysql.cj.jdbc.Driver") != null;
            }

            return mysql_driver_version_6 ? "com.mysql.cj.jdbc.Driver" : "com.mysql.jdbc.Driver";
        } else if (rawUrl.startsWith("jdbc:log4jdbc:")) {
            return "net.sf.log4jdbc.DriverSpy";
        } else if (rawUrl.startsWith("jdbc:mariadb:")) {
            return "org.mariadb.jdbc.Driver";
        } else if (!rawUrl.startsWith("jdbc:oracle:") && !rawUrl.startsWith("JDBC:oracle:")) {
            if (rawUrl.startsWith("jdbc:alibaba:oracle:")) {
                return "com.alibaba.jdbc.AlibabaDriver";
            } else if (rawUrl.startsWith("jdbc:microsoft:")) {
                return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
            } else if (rawUrl.startsWith("jdbc:sqlserver:")) {
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            } else if (rawUrl.startsWith("jdbc:sybase:Tds:")) {
                return "com.sybase.jdbc2.jdbc.SybDriver";
            } else if (rawUrl.startsWith("jdbc:jtds:")) {
                return "net.sourceforge.jtds.jdbc.Driver";
            } else if (!rawUrl.startsWith("jdbc:fake:") && !rawUrl.startsWith("jdbc:mock:")) {
                if (rawUrl.startsWith("jdbc:postgresql:")) {
                    return "org.postgresql.Driver";
                } else if (rawUrl.startsWith("jdbc:edb:")) {
                    return "com.edb.Driver";
                } else if (rawUrl.startsWith("jdbc:odps:")) {
                    return "com.aliyun.odps.jdbc.OdpsDriver";
                } else if (rawUrl.startsWith("jdbc:hsqldb:")) {
                    return "org.hsqldb.jdbcDriver";
                } else if (rawUrl.startsWith("jdbc:db2:")) {
                    return "COM.ibm.db2.jdbc.app.DB2Driver";
                } else if (rawUrl.startsWith("jdbc:sqlite:")) {
                    return "org.sqlite.JDBC";
                } else if (rawUrl.startsWith("jdbc:ingres:")) {
                    return "com.ingres.jdbc.IngresDriver";
                } else if (rawUrl.startsWith("jdbc:h2:")) {
                    return "org.h2.Driver";
                } else if (rawUrl.startsWith("jdbc:mckoi:")) {
                    return "com.mckoi.JDBCDriver";
                } else if (rawUrl.startsWith("jdbc:cloudscape:")) {
                    return "COM.cloudscape.core.JDBCDriver";
                } else if (rawUrl.startsWith("jdbc:informix-sqli:")) {
                    return "com.informix.jdbc.IfxDriver";
                } else if (rawUrl.startsWith("jdbc:timesten:")) {
                    return "com.timesten.jdbc.TimesTenDriver";
                } else if (rawUrl.startsWith("jdbc:as400:")) {
                    return "com.ibm.as400.access.AS400JDBCDriver";
                } else if (rawUrl.startsWith("jdbc:sapdb:")) {
                    return "com.sap.dbtech.jdbc.DriverSapDB";
                } else if (rawUrl.startsWith("jdbc:JSQLConnect:")) {
                    return "com.jnetdirect.jsql.JSQLDriver";
                } else if (rawUrl.startsWith("jdbc:JTurbo:")) {
                    return "com.newatlanta.jturbo.driver.Driver";
                } else if (rawUrl.startsWith("jdbc:firebirdsql:")) {
                    return "org.firebirdsql.jdbc.FBDriver";
                } else if (rawUrl.startsWith("jdbc:interbase:")) {
                    return "interbase.interclient.Driver";
                } else if (rawUrl.startsWith("jdbc:pointbase:")) {
                    return "com.pointbase.jdbc.jdbcUniversalDriver";
                } else if (rawUrl.startsWith("jdbc:edbc:")) {
                    return "ca.edbc.jdbc.EdbcDriver";
                } else if (rawUrl.startsWith("jdbc:mimer:multi1:")) {
                    return "com.mimer.jdbc.Driver";
                } else if (rawUrl.startsWith("jdbc:dm:")) {
                    return "dm.jdbc.driver.DmDriver";
                } else if (rawUrl.startsWith("jdbc:kingbase:")) {
                    return "com.kingbase.Driver";
                } else if (rawUrl.startsWith("jdbc:hive:")) {
                    return "org.apache.hive.jdbc.HiveDriver";
                } else if (rawUrl.startsWith("jdbc:hive2:")) {
                    return "org.apache.hive.jdbc.HiveDriver";
                } else if (rawUrl.startsWith("jdbc:phoenix:thin:")) {
                    return "org.apache.phoenix.queryserver.client.Driver";
                } else if (rawUrl.startsWith("jdbc:phoenix://")) {
                    return "org.apache.phoenix.jdbc.PhoenixDriver";
                } else {
                    throw new SQLException("unkow jdbc driver : " + rawUrl);
                }
            } else {
                return "com.alibaba.druid.mock.MockDriver";
            }
        } else {
            return "oracle.jdbc.OracleDriver";
        }
    }

    public static String getDbType(String rawUrl, String driverClassName) {
        if (rawUrl == null) {
            return null;
        } else if (!rawUrl.startsWith("jdbc:derby:") && !rawUrl.startsWith("jdbc:log4jdbc:derby:")) {
            if (!rawUrl.startsWith("jdbc:mysql:") && !rawUrl.startsWith("jdbc:cobar:") && !rawUrl.startsWith("jdbc:log4jdbc:mysql:")) {
                if (rawUrl.startsWith("jdbc:mariadb:")) {
                    return "mariadb";
                } else if (!rawUrl.startsWith("jdbc:oracle:") && !rawUrl.startsWith("jdbc:log4jdbc:oracle:")) {
                    if (rawUrl.startsWith("jdbc:alibaba:oracle:")) {
                        return "AliOracle";
                    } else if (!rawUrl.startsWith("jdbc:microsoft:") && !rawUrl.startsWith("jdbc:log4jdbc:microsoft:")) {
                        if (!rawUrl.startsWith("jdbc:sqlserver:") && !rawUrl.startsWith("jdbc:log4jdbc:sqlserver:")) {
                            if (!rawUrl.startsWith("jdbc:sybase:Tds:") && !rawUrl.startsWith("jdbc:log4jdbc:sybase:")) {
                                if (!rawUrl.startsWith("jdbc:jtds:") && !rawUrl.startsWith("jdbc:log4jdbc:jtds:")) {
                                    if (!rawUrl.startsWith("jdbc:fake:") && !rawUrl.startsWith("jdbc:mock:")) {
                                        if (!rawUrl.startsWith("jdbc:postgresql:") && !rawUrl.startsWith("jdbc:log4jdbc:postgresql:")) {
                                            if (rawUrl.startsWith("jdbc:edb:")) {
                                                return "edb";
                                            } else if (!rawUrl.startsWith("jdbc:hsqldb:") && !rawUrl.startsWith("jdbc:log4jdbc:hsqldb:")) {
                                                if (rawUrl.startsWith("jdbc:odps:")) {
                                                    return "odps";
                                                } else if (rawUrl.startsWith("jdbc:db2:")) {
                                                    return "db2";
                                                } else if (rawUrl.startsWith("jdbc:sqlite:")) {
                                                    return "sqlite";
                                                } else if (rawUrl.startsWith("jdbc:ingres:")) {
                                                    return "ingres";
                                                } else if (!rawUrl.startsWith("jdbc:h2:") && !rawUrl.startsWith("jdbc:log4jdbc:h2:")) {
                                                    if (rawUrl.startsWith("jdbc:mckoi:")) {
                                                        return "mckoi";
                                                    } else if (rawUrl.startsWith("jdbc:cloudscape:")) {
                                                        return "cloudscape";
                                                    } else if (!rawUrl.startsWith("jdbc:informix-sqli:") && !rawUrl.startsWith("jdbc:log4jdbc:informix-sqli:")) {
                                                        if (rawUrl.startsWith("jdbc:timesten:")) {
                                                            return "timesten";
                                                        } else if (rawUrl.startsWith("jdbc:as400:")) {
                                                            return "as400";
                                                        } else if (rawUrl.startsWith("jdbc:sapdb:")) {
                                                            return "sapdb";
                                                        } else if (rawUrl.startsWith("jdbc:JSQLConnect:")) {
                                                            return "JSQLConnect";
                                                        } else if (rawUrl.startsWith("jdbc:JTurbo:")) {
                                                            return "JTurbo";
                                                        } else if (rawUrl.startsWith("jdbc:firebirdsql:")) {
                                                            return "firebirdsql";
                                                        } else if (rawUrl.startsWith("jdbc:interbase:")) {
                                                            return "interbase";
                                                        } else if (rawUrl.startsWith("jdbc:pointbase:")) {
                                                            return "pointbase";
                                                        } else if (rawUrl.startsWith("jdbc:edbc:")) {
                                                            return "edbc";
                                                        } else if (rawUrl.startsWith("jdbc:mimer:multi1:")) {
                                                            return "mimer";
                                                        } else if (rawUrl.startsWith("jdbc:dm:")) {
                                                            return "dm";
                                                        } else if (rawUrl.startsWith("jdbc:kingbase:")) {
                                                            return "kingbase";
                                                        } else if (rawUrl.startsWith("jdbc:log4jdbc:")) {
                                                            return "log4jdbc";
                                                        } else if (rawUrl.startsWith("jdbc:hive:")) {
                                                            return "hive";
                                                        } else if (rawUrl.startsWith("jdbc:hive2:")) {
                                                            return "hive";
                                                        } else {
                                                            return rawUrl.startsWith("jdbc:phoenix:") ? "phoenix" : null;
                                                        }
                                                    } else {
                                                        return "informix";
                                                    }
                                                } else {
                                                    return "h2";
                                                }
                                            } else {
                                                return "hsql";
                                            }
                                        } else {
                                            return "postgresql";
                                        }
                                    } else {
                                        return "mock";
                                    }
                                } else {
                                    return "jtds";
                                }
                            } else {
                                return "sybase";
                            }
                        } else {
                            return "sqlserver";
                        }
                    } else {
                        return "sqlserver";
                    }
                } else {
                    return "oracle";
                }
            } else {
                return "mysql";
            }
        } else {
            return "derby";
        }
    }

    public static Driver createDriver(String driverClassName) throws SQLException {
        return createDriver((ClassLoader)null, driverClassName);
    }

    public static Driver createDriver(ClassLoader classLoader, String driverClassName) throws SQLException {
        Class<?> clazz = null;
        if (classLoader != null) {
            try {
                clazz = classLoader.loadClass(driverClassName);
            } catch (ClassNotFoundException var8) {
            }
        }

        if (clazz == null) {
            try {
                ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
                if (contextLoader != null) {
                    clazz = contextLoader.loadClass(driverClassName);
                }
            } catch (ClassNotFoundException var7) {
            }
        }

        if (clazz == null) {
            try {
                clazz = Class.forName(driverClassName);
            } catch (ClassNotFoundException var6) {
                throw new SQLException(var6.getMessage(), var6);
            }
        }

        try {
            return (Driver)clazz.newInstance();
        } catch (IllegalAccessException var4) {
            throw new SQLException(var4.getMessage(), var4);
        } catch (InstantiationException var5) {
            throw new SQLException(var5.getMessage(), var5);
        }
    }

    public static int executeUpdate(DataSource dataSource, String sql, Object... parameters) throws SQLException {
        return executeUpdate(dataSource, sql, Arrays.asList(parameters));
    }

    public static int executeUpdate(DataSource dataSource, String sql, List<Object> parameters) throws SQLException {
        Connection conn = null;

        int var4;
        try {
            conn = dataSource.getConnection();
            var4 = executeUpdate(conn, sql, parameters);
        } finally {
            close(conn);
        }

        return var4;
    }

    public static int executeUpdate(Connection conn, String sql, List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;

        int updateCount;
        try {
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, parameters);
            updateCount = stmt.executeUpdate();
        } finally {
            close((Statement)stmt);
        }

        return updateCount;
    }

    public static void execute(DataSource dataSource, String sql, Object... parameters) throws SQLException {
        execute(dataSource, sql, Arrays.asList(parameters));
    }

    public static void execute(DataSource dataSource, String sql, List<Object> parameters) throws SQLException {
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            execute(conn, sql, parameters);
        } finally {
            close(conn);
        }

    }

    public static void execute(Connection conn, String sql, List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, parameters);
            stmt.executeUpdate();
        } finally {
            close((Statement)stmt);
        }

    }

    public static List<Map<String, Object>> executeQuery(DataSource dataSource, String sql, Object... parameters) throws SQLException {
        return executeQuery(dataSource, sql, Arrays.asList(parameters));
    }

    public static List<Map<String, Object>> executeQuery(DataSource dataSource, String sql, List<Object> parameters) throws SQLException {
        Connection conn = null;

        List var4;
        try {
            conn = dataSource.getConnection();
            var4 = executeQuery(conn, sql, parameters);
        } finally {
            close(conn);
        }

        return var4;
    }

    public static List<Map<String, Object>> executeQuery(Connection conn, String sql, List<Object> parameters) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, parameters);
            rs = stmt.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();

            while(rs.next()) {
                Map<String, Object> row = new LinkedHashMap();
                int i = 0;

                for(int size = rsMeta.getColumnCount(); i < size; ++i) {
                    String columName = rsMeta.getColumnLabel(i + 1);
                    Object value = rs.getObject(i + 1);
                    row.put(columName, value);
                }

                rows.add(row);
            }
        } finally {
            close(rs);
            close((Statement)stmt);
        }

        return rows;
    }

    private static void setParameters(PreparedStatement stmt, List<Object> parameters) throws SQLException {
        int i = 0;

        for(int size = parameters.size(); i < size; ++i) {
            Object param = parameters.get(i);
            stmt.setObject(i + 1, param);
        }

    }

    public static void insertToTable(DataSource dataSource, String tableName, Map<String, Object> data) throws SQLException {
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            insertToTable(conn, tableName, data);
        } finally {
            close(conn);
        }

    }

    public static void insertToTable(Connection conn, String tableName, Map<String, Object> data) throws SQLException {
        String sql = makeInsertToTableSql(tableName, data.keySet());
        List<Object> parameters = new ArrayList(data.values());
        execute((Connection)conn, sql, (List)parameters);
    }

    public static String makeInsertToTableSql(String tableName, Collection<String> names) {
        StringBuilder sql = (new StringBuilder()).append("insert into ").append(tableName).append("(");
        int nameCount = 0;

        for(Iterator var4 = names.iterator(); var4.hasNext(); ++nameCount) {
            String name = (String)var4.next();
            if (nameCount > 0) {
                sql.append(",");
            }

            sql.append(name);
        }

        sql.append(") values (");

        for(int i = 0; i < nameCount; ++i) {
            if (i != 0) {
                sql.append(",");
            }

            sql.append("?");
        }

        sql.append(")");
        return sql.toString();
    }

    static {
        try {
            ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
            Properties property;
            if (ctxClassLoader != null) {
                for(Enumeration e = ctxClassLoader.getResources("META-INF/druid-driver.properties"); e.hasMoreElements(); DRIVER_URL_MAPPING.putAll(property)) {
                    URL url = (URL)e.nextElement();
                    property = new Properties();
                    InputStream is = null;

                    try {
                        is = url.openStream();
                        property.load(is);
                    } finally {
                        close((Closeable)is);
                    }
                }
            }
        } catch (Exception var9) {
            LOG.error("load druid-driver.properties error", var9);
        }

    }
}
