package thread;

import client.DouyuDanmakuClient;
import utils.LogUtil;

public class KeepAlive implements Runnable {
    private DouyuDanmakuClient douyuClient;

    public KeepAlive(DouyuDanmakuClient douyuClient) {
        this.douyuClient = douyuClient;
    }

    @Override
    public void run() {
//        douyuClient = DouyuDanmakuClient.getInstance();
        LogUtil.i("KeepLive", "心跳包线程启动 ...");
        while (true) {
            douyuClient.keepAlive();
            try {
                Thread.sleep(45000);
                LogUtil.d("KeepLive", "Keep Live ...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
