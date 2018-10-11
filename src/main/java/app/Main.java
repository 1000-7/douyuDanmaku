package app;

import thread.DanmakuBiuBiuBiu;
import thread.KeepAlive;
import utils.Config;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        if (!Config.loadSuccess) {
            System.err.println("配置读取有问题");
        }
        Set<String> roomSet = Config.ROOM_MAP.keySet();
        System.out.println(roomSet.size());
        for (String room : roomSet) {
            DanmakuBiuBiuBiu danmakuBiuBiuBiu =new DanmakuBiuBiuBiu(room, Config.ROOM_MAP.get(room));
            new Thread(danmakuBiuBiuBiu, "Crawler-" + room).start();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(new KeepAlive(danmakuBiuBiuBiu.getDouyuClient()), "Crawler-" + room).start();
        }
    }
}
