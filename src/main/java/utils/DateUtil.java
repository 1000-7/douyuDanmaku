package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @return 返回 日期+时间 的字符串
     */
    public static String datetime(Date date) {
        return formatter.format(date);
    }

    /**
     * @return 返回当前 日期+时间
     */
    public static String now() {
        return formatter.format(new Date());
    }

    public static void main(String[] args) {
        System.out.println(now());
    }

}
