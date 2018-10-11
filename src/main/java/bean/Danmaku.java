package bean;

import java.util.Date;

/**
 * @Author unclewang
 * @Date 2018/10/10 18:49
 */
public class Danmaku {
    private int uid;
    private String snick;
    private String txt;
    private Date date;
    private int rid;

    public Danmaku(int uid, String snick, String content, int rid) {
        this.uid = uid;
        this.snick = snick;
        this.txt = content;
        this.date = new Date();
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "Danmaku{" +
                "uid=" + uid +
                ", snick='" + snick + '\'' +
                ", txt='" + txt + '\'' +
                ", date=" + date +
                ", rid=" + rid +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSnick() {
        return snick;
    }

    public void setSnick(String snick) {
        this.snick = snick;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }
}
