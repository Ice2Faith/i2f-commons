package i2f.commons.core.utils.safe;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author ltb
 * @date 2021/9/2
 */
public class AESUtil {
    public static final String CHAR_SET="UTF-8";
    public static String genKey(String key){
        if(key==null || "".equals(key)){
            return "A1B2C3D4E5F67870";
        }
        StringBuilder builder=new StringBuilder(key);
        int idx=0;
        int adlen=0;
        int klen=key.length();
        while((klen+adlen)%16!=0){
            idx=((idx+3)*7)%klen;
            builder.append(key.charAt(idx));
            adlen++;
        }
        return builder.toString();
    }
    public static Cipher getAes(String key,int mode){
        try{
            byte[] keyRaw=key.getBytes(CHAR_SET);
            SecretKeySpec keySpec=new SecretKeySpec(keyRaw,"AES" );
            Cipher cipher=Cipher.getInstance("AES/ECB/ISO10126Padding");
            cipher.init(mode,keySpec);
            return cipher;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String encrypt(byte[] data,String key){
        try{
            Cipher cipher=getAes(key,Cipher.ENCRYPT_MODE);
            byte[] sdata=cipher.doFinal(data);
            return Base64Util.encode(sdata);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] decrypt(String data,String key){
        try{
            byte[] sdata=Base64Util.decode(data);
            Cipher cipher=getAes(key,Cipher.DECRYPT_MODE);
            return cipher.doFinal(sdata);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String encryptJson(String json,String key){
        try{
            json=Base64Util.encode(json.getBytes(CHAR_SET));
            return encrypt(json.getBytes(CHAR_SET),key);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptJson(String data,String key){
        try{
            byte[] sdata=decrypt(data, key);
            String json=new String(sdata,CHAR_SET);
            json=new String(Base64Util.decode(json),CHAR_SET);
            return json;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
