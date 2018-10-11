package bean;

import org.apache.commons.lang3.StringUtils;
import utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private String data;
    private Map<String, Object> response;

    public Response(String data) {
        this.response = formatResponse(data);
    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public Map<String, Object> formatResponse(String data) {
        Map<String, Object> rtnMsg = new HashMap<String, Object>();

        //处理数据字符串末尾的'/0字符'
        data = StringUtils.substringBeforeLast(data, "/");

        //对数据字符串进行拆分
        String[] buff = data.split("/");

        //分析协议字段中的key和value值
        for (String tmp : buff) {
            //获取key值
            String key = StringUtils.substringBefore(tmp, "@=");
            //获取对应的value值
            Object value = StringUtils.substringAfter(tmp, "@=");

            //如果value值中包含子序列化值，则进行递归分析
            if (StringUtils.contains((String) value, "@A")) {
                value = ((String) value).replaceAll("@S", "/").replaceAll("@A", "@");
                value = this.formatResponse((String) value);
            }
            //将分析后的键值对添加到信息列表中
            rtnMsg.put(key, value);
        }

        return rtnMsg;
    }


}
