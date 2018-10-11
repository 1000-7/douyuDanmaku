package thread;

import client.DouyuDanmakuClient;

public class DanmakuBiuBiuBiu implements Runnable {
    private DouyuDanmakuClient douyuClient;
    private String roomName;
    private int roomid;

    public DanmakuBiuBiuBiu(String roomName, int roomid) {
        this.roomName = roomName;
        this.roomid = roomid;
    }

    @Override
    public void run() {
        douyuClient = new DouyuDanmakuClient();
        douyuClient.initClient(roomid);
        while (douyuClient.getReadyFlag()) {
            douyuClient.getResponseNoUseLen();
        }
    }

    public DouyuDanmakuClient getDouyuClient() {
        return douyuClient;
    }
}
