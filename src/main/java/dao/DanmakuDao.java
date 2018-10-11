package dao;

import bean.Danmaku;
import utils.DBUtil;
import utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class DanmakuDao {
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS danmaku(id INT PRIMARY KEY AUTO_INCREMENT,uid INT NOT NULL,snick VARCHAR(64) NOT NULL,content VARCHAR(256) NOT NULL,date DATETIME NOT NULL, rid INT NOT NULL );";

    private static final String SQL_INSERT_DANMAKU = "INSERT INTO danmaku(uid,snick, content, date,rid) VALUES(%d, '%s', '%s', '%s', %d) ";

    public static void createTable() {
        DBUtil.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * 保存弹幕数据到数据库
     */
    public static boolean saveDanmaku(List<Danmaku> danmakuList) {
        if (danmakuList.size() == 0) {
            return false;
        }
        List<String> sqlList = new ArrayList<>();
        for (Danmaku danmaku : danmakuList) {
            if (danmaku == null) {
                continue;
            }
            System.out.println(danmaku.toString());
            sqlList.add(String.format(
                    SQL_INSERT_DANMAKU,
                    danmaku.getUid(),
                    danmaku.getSnick(),
                    danmaku.getTxt(),
                    DateUtil.datetime(danmaku.getDate()),
                    danmaku.getRid())
            );
        }
        return DBUtil.execSQL(sqlList);
    }

    public static void main(String[] args) {
        createTable();
        //测试
        List<Danmaku> danmakus = new ArrayList<>();
        danmakus.add(new Danmaku(99999999, "X", "X", 9999));
        danmakus.add(new Danmaku(99999999, "XX", "XX", 9999));
        danmakus.add(new Danmaku(99999999, "XX", "XX", 9999));

        saveDanmaku(danmakus);
    }
}
