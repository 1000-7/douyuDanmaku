package client;

import bean.Danmaku;
import bean.DanmakuMessage;
import bean.Response;
import dao.DanmakuDao;
import org.apache.commons.lang3.StringUtils;
import utils.Config;
import utils.LogUtil;
import utils.ResponseFormat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.HexUtil.bytesToIntLittle;
import static utils.HexUtil.bytesToShortLittle;

/**
 * @Author unclewang
 * @Date 2018/10/10 19:18
 */
public class DouyuDanmakuClient {

    public DouyuDanmakuClient() {
    }

    //第三方弹幕协议服务器地址
    private static final String DOUYUURL = Config.DOUYUAPI;
    //第三方弹幕协议服务器端口
    private static final int PORT = Config.DOUYUPORT;
    //设置字节获取buffer的最大值
    private static final int MAX_BUFFER_LENGTH = 4096;

    private Socket socket;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;
    //获取弹幕线程及心跳线程运行和停止标记
    private boolean readyFlag = false;

    public void initClient(int roomId) {
        this.initClient(roomId, -9999);
    }

    public void initClient(int roomId, int groupId) {
        this.connectDouyuServer();
        this.loginRoom(roomId);
        this.joinGroup(roomId, groupId);
        readyFlag = true;
    }

    /**
     * @param roomId
     */
    private void loginRoom(int roomId) {
        byte[] login = DanmakuMessage.loginDouyu(roomId);
        try {
            bos.write(login);
            bos.flush();
            //初始化弹幕服务器返回值读取包大小
            byte[] recvByte = new byte[MAX_BUFFER_LENGTH];
            bis.read(recvByte, 0, recvByte.length);
            if (DanmakuMessage.isSuccessLogin(recvByte)) {
                LogUtil.d("Receive login response successfully!");
            } else {
                LogUtil.e("Receive login response failed!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param roomId
     * @param groupId
     */
    private void joinGroup(int roomId, int groupId) {
        byte[] join = DanmakuMessage.joinGroupDouyu(roomId, groupId);
        try {
            bos.write(join);
            bos.flush();
            LogUtil.d("Send join group request successfully!");
        } catch (IOException e) {
            LogUtil.e("Send join group request failed!");
        }
    }

    /**
     * 获取弹幕客户端就绪标记
     *
     * @return
     */
    public boolean getReadyFlag() {
        return readyFlag;
    }

    /**
     * 服务器心跳连接
     */
    public void keepAlive() {
        //获取与弹幕服务器保持心跳的请求数据包
        byte[] keepAlive = DanmakuMessage.keepAliveDouyu();

        try {
            //向弹幕服务器发送心跳请求数据包
            bos.write(keepAlive, 0, keepAlive.length);
            bos.flush();
            LogUtil.d("Send keep alive request successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Send keep alive request failed!");
        }
    }


    private void connectDouyuServer() {
        try {
            socket = new Socket(DOUYUURL, PORT);
            bos = new BufferedOutputStream(socket.getOutputStream());
            bis = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.d("Server Connect Successfully!");
    }


    public void getResponseNoUseLen() {
        List<Danmaku> danmakus = new ArrayList<>();
        //初始化获取弹幕服务器返回信息包大小
        byte[] recvByte = new byte[MAX_BUFFER_LENGTH];
        String response;
        try {
            int recvLen = bis.read(recvByte, 0, recvByte.length);
            byte[] realByte = new byte[recvLen];
            //按照实际获取的字节长度读取返回信息
            System.arraycopy(recvByte, 0, realByte, 0, recvLen);
            if (realByte.length < 12) {
                return;
            }
            response = new String(realByte, 12, realByte.length - 12);

            while (response.lastIndexOf("type@=") > 5) {
                String mm = StringUtils.substring(response, response.lastIndexOf("type@="));
//                System.err.println(mm);
                if (mm.contains("type@=chatmsg/")) {
                    Danmaku danmaku = ResponseFormat.parseDanmaku(mm);
                    danmakus.add(danmaku);
                }
                Response rs = new Response(mm);
                parseServerMsg(rs);
                response = StringUtils.substring(response, 0, response.lastIndexOf("type@=") - 12);
            }
            DanmakuDao.saveDanmaku(danmakus);
            Response rs = new Response(StringUtils.substring(response, response.lastIndexOf("type@=")));
            parseServerMsg(rs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getResponseUseLen() throws IOException {
        //初始化获取弹幕服务器返回信息包大小
        //head多8位
        int headLen = 0;
        int txtLen = 0;
        byte[] MessageLen = readStream(bis, 0, 4);
        headLen = bytesToIntLittle(MessageLen, 0);
        readStream(bis, 0, 4);
        byte[] serverHeadCode = readStream(bis, 0, 2);
        int headCode = bytesToShortLittle(serverHeadCode, 0);
        readStream(bis, 0, 2);
        txtLen = headLen - 8;
        System.out.println(txtLen);
        byte[] bytes = new byte[txtLen];
        int len = 0;
        int readLen = 0;
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        while ((len = bis.read(bytes, 0, txtLen)) != -1) {
            byteArray.write(bytes, 0, len);
            readLen += len;
            if (readLen == txtLen) {
                break;
            }
        }
//        System.out.println(byteArray.toString());
//        System.out.println(response);
//        readStream(bis, 0, 1);

    }

    public static byte[] readStream(InputStream is, int off, int len) {
        byte[] bytes = new byte[len];
        try {
            is.read(bytes, off, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 解析从服务器接受的协议，并根据需要订制业务需求
     *
     * @param rs
     */
    private void parseServerMsg(Response rs) {
        Map<String, Object> msg = rs.getResponse();

        if (msg.get("type") != null) {

            //服务器反馈错误信息
            if (msg.get("type").equals("error")) {
                LogUtil.e(msg.toString());
                //结束心跳和获取弹幕线程
                this.readyFlag = false;

            }

            /***@TODO 根据业务需求来处理获取到的所有弹幕及礼物信息***********/

            //判断消息类型
            if (msg.get("type").equals("chatmsg")) {//弹幕消息

                LogUtil.d("弹幕消息===>" + msg.toString());
            } else if (msg.get("type").equals("dgb")) {//赠送礼物信息
                LogUtil.d("礼物消息===>" + msg.toString());
            } else {
                LogUtil.d("其他消息===>" + msg.toString());
            }

            //@TODO 其他业务信息根据需要进行添加

            /*************************************************************/
        }


    }

    public static void main(String[] args) throws IOException {
        DouyuDanmakuClient douyuDanmakuClient = new DouyuDanmakuClient();
        douyuDanmakuClient.initClient(229346);
        while (true) {
            douyuDanmakuClient.getResponseNoUseLen();
        }
    }

}
