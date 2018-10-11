package utils;

import bean.Danmaku;
import com.vdurmont.emoji.EmojiParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseFormat {
    private static final String REGEX_CHAT_DANMAKU = "type@=chatmsg/.*rid@=(\\d*)/.*uid@=(\\d*).*nn@=(.*)/txt@=(.*?)/.*";

    private static Matcher getMatcher(String content, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return pattern.matcher(content);
    }

    /**
     * 解析弹幕信息
     */
    public static Danmaku parseDanmaku(String response) {
        if (response == null) {
            return null;
        }

        Matcher matcher = getMatcher(response, REGEX_CHAT_DANMAKU);
        Danmaku danmaku = null;

        if (matcher.find()) {
            String txt = EmojiParser.parseToHtmlDecimal(matcher.group(4));
            danmaku = new Danmaku(Integer.parseInt(matcher.group(2)),
                    matcher.group(3),
                    txt,
                    Integer.parseInt(matcher.group(1)));
        }

        LogUtil.d("Parse Danmaku", danmaku + "");

        return danmaku;
    }
}
