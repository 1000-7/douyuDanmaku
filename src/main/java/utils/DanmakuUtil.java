package utils;

public class DanmakuUtil {
    private StringBuffer buffer = new StringBuffer();

    public void add(String key, Object value) {
        buffer.append(format(key));
        buffer.append("@=");
        if (value instanceof Integer) {
            buffer.append(value);
        } else {
            buffer.append(format((String) value));
        }
        buffer.append("/");
    }

    private String format(String s) {
        return s.replaceAll("/", "@S").replaceAll("@", "@A");
    }

    public String getResult() {
//        buffer.append('\0');
        return buffer.toString();
    }

    public String loginMessage(int roomID) {
        buffer.delete(0, buffer.length());
        this.add("type", "loginreq");
        this.add("roomid", roomID);
        return getResult();
    }

    public String joinGroupMessage(int roomID) {
        return this.joinGroupMessage(roomID, -9999);
    }

    public String joinGroupMessage(int roomID, int groupID) {
        buffer.delete(0, buffer.length());
        this.add("type", "joingroup");
        this.add("rid", roomID);
        this.add("gid", groupID);
        return getResult();
    }

    public String keepAliveMessage(){
        buffer.delete(0, buffer.length());
        this.add("type", "mrkl");
        return getResult();
    }



}
