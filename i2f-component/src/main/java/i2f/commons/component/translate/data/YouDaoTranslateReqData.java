package i2f.commons.component.translate.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Data
@NoArgsConstructor
public class YouDaoTranslateReqData {
    private String q;//待翻译文本
    private String from;//源语言
    private String to;//目标语言
    private String appKey;//应用ID
    private String salt;//UUID
    private String sign;//签名
    private String signType;//签名类型
    private String curtime;//当前UTC时间戳(秒)
    private String ext;//翻译结果音频格式，支持mp3
    private String voice;//翻译结果发音选择
    private String strict;//是否严格按照指定from和to进行翻译：true/false
    private String vocabId;//用户上传的词典


    public static Builder build(){
        return new Builder();
    }
    public static Builder build(YouDaoTranslateReqData reqData){
        return new Builder(reqData);
    }
    public static class Builder{
        YouDaoTranslateReqData reqData;
        public Builder(){
            reqData=new YouDaoTranslateReqData();
        }
        public Builder(YouDaoTranslateReqData reqData){
            this.reqData=reqData;
        }
        public YouDaoTranslateReqData done(){
            return this.reqData;
        }
        public Builder begin(String appKey){
            reqData.setSignType("v3");
            reqData.setCurtime(String.valueOf(new Date().getTime()/1000));
            reqData.setAppKey(appKey);
            reqData.setSalt(String.valueOf(new Date().getTime()));
            return this;
        }
        public Builder trans(String content,String from,String to){
            reqData.setQ(content);
            reqData.setFrom(from);
            reqData.setTo(to);
            return this;
        }
        public Builder sign(String appSecret){
            String signStr=reqData.appKey+truncate(reqData.q)+reqData.salt+reqData.curtime+appSecret;
            reqData.sign=getDigest(signStr);
            return this;
        }
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}
