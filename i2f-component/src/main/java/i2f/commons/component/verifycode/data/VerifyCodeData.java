package i2f.commons.component.verifycode.data;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.Map.Entry;

/**
 * @author ltb
 * @date 2021/8/12
 */
@Data
public class VerifyCodeData {
    private BufferedImage img; //显示图片源
    private String code; // 填写的验证码
    private String showText; // 验证码的显示文字
    private String key; // 验证码对应的KEY，作为缓存的KEY
    private String imgBase64; // 验证码的显示图片base64编码

    // 获取保存到缓存中的键值对
    public Entry<String, String> getCacheEntry(){
        Entry<String, String> entry=new AbstractMap.SimpleEntry<String,String>(key,code);
        return entry;
    }

    // 获取显示到WEB页面的键值对
    public Entry<String,String> getWebShowEntry(){
        Entry<String, String> entry=new AbstractMap.SimpleEntry<String,String>(key,imgBase64);
        return entry;
    }
}
