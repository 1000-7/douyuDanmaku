package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    private static final String URL = Config.DB_URL;
    private static final String USERNAME = Config.DB_USERNAME;
    private static final String PASSWORD = Config.DB_PASSWORD;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public enum DBFactory {
        /**
         * 枚举实现单例模式
         */
        dbFactory;

        private Connection connection;

        DBFactory() {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME,
                        PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        }

        public Connection getConnection() {
            return connection;
        }
    }

    public static Connection getConnection() {
        return DBFactory.dbFactory.getConnection();
    }

    /**
     * 执行一条SQL语句
     */
    public static boolean execSQL(String sql) {
        List<String> sqlList = new ArrayList<>();
        sqlList.add(sql);
        return execSQL(sqlList);
    }

    /**
     * 在一个事务里，依次执行一系列SQL
     *
     * @return 如果执行成功返回 true, 否则 false
     */
    public static boolean execSQL(List<String> sqlList) {


        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = null;
            for (String sql : sqlList) {
                stmt = conn.prepareStatement(sql);
                stmt.execute();
                LogUtil.d("DB", "Execute SQL statement [" + sql + "]");
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //执行失败
        return false;
    }

    public static void main(String[] args) {
        // 测试
        List<String> sqls = new ArrayList<>();
        sqls.add("INSERT INTO Danmaku(uid,snick, content, date,rid) VALUES(99999999, 'XXxX', 'XXxX', '2016-1-1', 88888888);");
        sqls.add("INSERT INTO Danmaku(uid,snick, content, date,rid) VALUES(99999999, 'XXxxX', 'XXxxX', '2016-1-1', 88888888);");
        sqls.add("INSERT INTO Danmaku(uid,snick, content, date,rid) VALUES(99999999, 'XXxxxxX', 'XXxxxxX', '2016-1-1', 88888888);");

        execSQL(sqls);
        System.out.println("done!");
    }
}
