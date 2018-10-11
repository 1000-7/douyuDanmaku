import com.alibaba.druid.filter.config.ConfigTools;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;

public class Test {
    @org.junit.Test
    public void test() throws Exception {
        System.out.println(ConfigTools.encrypt("wx19"));
        System.out.println(ConfigTools.decrypt("JkrNeboDIjW5Rajfy2S3KJ1AcVJgXDHhTvUxWy5mfc1FnlytuRspUNj/0WJwjxao6p33FGEkmxGM885ClqhbBg=="));
    }

    @org.junit.Test
    public void testString() throws Exception {
        System.out.println(StringUtils.substring("asdadasdads","asdadasdads".lastIndexOf("asd")));
    }

    @org.junit.Test
    public void testEmoji() throws Exception {
        String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
        String result = EmojiParser.parseToUnicode(str);
        System.out.println(result);
        // Prints:
        // "An 😀awesome 😃string 😄with a few 😉emojis!"
        String str1 = "An 😀awesome 😃string with a few 📺😉emojis!";
        String result1 = EmojiParser.parseToAliases(str);
        System.out.println(result1);
        String str2 = "Here is a boy: \uD83D\uDC66\uD83C\uDFFF!";
        System.out.println(EmojiParser.parseToAliases(str2));
        System.out.println(EmojiParser.parseToAliases(str2, EmojiParser.FitzpatrickAction.PARSE));
// Prints twice: "Here is a boy: :boy|type_6:!"
        System.out.println(EmojiParser.parseToAliases(str2, EmojiParser.FitzpatrickAction.REMOVE));
// Prints: "Here is a boy: :boy:!"
        System.out.println(EmojiParser.parseToAliases(str2, EmojiParser.FitzpatrickAction.IGNORE));
// Prints: "Here is a boy: :boy:🏿!"
// Prints:
// "An :grinning:awesome :smiley:string with a few :wink:emojis!"
    }

    @org.junit.Test
    public void method(){
        String str = "An 😀awesome 😃string with a few 😉emojis!";

        String resultDecimal = EmojiParser.parseToHtmlDecimal(str);
        System.out.println(resultDecimal);
// Prints:
// "An &#128512;awesome &#128515;string with a few &#128521;emojis!"

        String resultHexadecimal = EmojiParser.parseToHtmlHexadecimal(str);
        System.out.println(resultHexadecimal);
// Prints:
// "An &#x1f600;awesome &#x1f603;string with a few &#x1f609;emojis!"
    }
}

