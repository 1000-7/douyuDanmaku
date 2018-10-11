package bean;

import org.apache.commons.lang3.StringUtils;
import utils.DanmakuUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static utils.HexUtil.*;

public class DanmakuMessage {
    //弹幕客户端类型设置
    public final static short DY_CLIENT = 689;
    private static final int MAX_BUFFER_LENGTH = 4096;
    private static DanmakuUtil du = new DanmakuUtil();

    public static byte[] txt2DouyuMessage(String txt) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //计算消息长度 = 消息长度(4) + 消息类型(2) + 加密字段(1) + 保留字段(1) + 真实消息内容长度 + 结尾标识长度(1)
        int txtLen = 4 + 2 + 1 + 1 + txt.length() + 1;
        baos.reset();
        try {
            baos.write(intToBytesLittle(txtLen));
            baos.write(intToBytesLittle(txtLen));
            baos.write(shortToBytesLittle(DY_CLIENT));
            baos.write(0);
            baos.write(0);
            baos.write(txt.getBytes("ISO_8859_1"));
            baos.write(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] loginDouyu(int roomID) {
        return txt2DouyuMessage(du.loginMessage(roomID));
    }

    public static boolean isSuccessLogin(byte[] respond) {
        boolean isSuccess = false;
        //返回数据不正确（仅包含12位信息头，没有信息内容）
        if (respond.length <= 12) {
            return isSuccess;
        }
        String response = new String(respond, 12, respond.length - 12);
        return StringUtils.contains(response, "type@=loginres");
    }

    public static byte[] joinGroupDouyu(int roomID, int groupID) {
        return txt2DouyuMessage(du.joinGroupMessage(roomID, groupID));
    }

    public static byte[] keepAliveDouyu() {
        return txt2DouyuMessage(du.keepAliveMessage());
    }




    public static void main(String[] args) {

        byte[] bytes = txt2DouyuMessage(du.joinGroupMessage(999));
        System.out.println(new String(Arrays.copyOfRange(bytes, 12, bytes.length - 1)));
        System.out.println(du.loginMessage(999));
        System.out.println(du.keepAliveMessage());
    }
}