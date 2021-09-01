package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.wall.spi.MySqlWallProvider;
import com.tranboot.client.druid.wall.spi.OracleWallProvider;

public class WallUtils {
    public WallUtils() {
    }

    public static boolean isValidateMySql(String sql) {
        MySqlWallProvider provider = new MySqlWallProvider();
        return provider.checkValid(sql);
    }

    public static boolean isValidateMySql(String sql, WallConfig config) {
        MySqlWallProvider provider = new MySqlWallProvider(config);
        return provider.checkValid(sql);
    }

    public static boolean isValidateOracle(String sql) {
        OracleWallProvider provider = new OracleWallProvider();
        return provider.checkValid(sql);
    }

    public static boolean isValidateOracle(String sql, WallConfig config) {
        OracleWallProvider provider = new OracleWallProvider(config);
        return provider.checkValid(sql);
    }
}
