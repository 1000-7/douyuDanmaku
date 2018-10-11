package utils;

import com.alibaba.druid.filter.config.ConfigTools;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Config {
    public static boolean loadSuccess = false;
    private static final String CONF_FILE = "conf.properties";
    public static boolean DEBUG_MODE = true;
    public static boolean DB_ENABLE = false;
    public static String DB_URL = null;
    public static String DB_NAME = null;
    public static String DB_USERNAME = null;
    public static String DB_PASSWORD = null;
    public static String DOUYUAPI = null;
    public static Integer DOUYUPORT = null;
    /**
     * 键  房间简称( 即room.url.XXX中的XXX )
     * 值  房间地址
     */
    public static Map<String, Integer> ROOM_MAP = new HashMap<>();

    static {
        Configurations configs = new Configurations();
        try {
            Configuration config = configs.properties(new File(CONF_FILE));
            DEBUG_MODE = config.getBoolean("debug");
            DB_ENABLE = config.getBoolean("db.enable");
            DB_URL = config.getString("db.url");
            DB_NAME = config.getString("db.name");
            DB_USERNAME = config.getString("db.user");
            DOUYUPORT = config.getInt("douyu.api.port");
            DOUYUAPI = config.getString("douyu.api.url");
            DB_PASSWORD = ConfigTools.decrypt(config.getString("db.pass"));
            Iterator<String> rooms = config.getKeys();
            while (rooms.hasNext()) {
                String name = rooms.next();
                if (name.startsWith("room.url.")) {
                    ROOM_MAP.put(name.substring(9), config.getInt(name));
                }
            }
            LogUtil.i("读取配置成功");
            loadSuccess = true;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            LogUtil.w("读取配置失败");
        }
    }

    private static void showConfig() {
        LogUtil.d("Config", "----------------------------------------------------------------");
        LogUtil.d("Config", "DEBUG_MODE: " + DEBUG_MODE);
        LogUtil.d("Config", "DB_ENABLE: " + DB_ENABLE);
        Set<String> nameSet = ROOM_MAP.keySet();
        for (String name : nameSet) {
            LogUtil.d("Config", "ROOM_URL: " + name + " >> " + ROOM_MAP.get(name));
        }
        LogUtil.d("Config", "----------------------------------------------------------------");
    }

    public static void main(String[] args) {
        showConfig();
    }
}
