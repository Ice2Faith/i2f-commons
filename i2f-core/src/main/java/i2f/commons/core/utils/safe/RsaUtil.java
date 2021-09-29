package i2f.commons.core.utils.safe;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

//私钥加密，公钥解密，网络传输中，传输公钥和密文
public class RsaUtil {
    public static int UPDATE_SIZE=116;//require lower than 117
    public static int DEFAULT_KEY_SIZE=1024;
    public static String CHAR_SET_NAME="UTF-8";
    public static class RsaKeyData {
        public KeyPair keyPair;
        public RSAPrivateKey privateKey;
        public RSAPublicKey publicKey;
        public String privateKeyBase64;
        public String publicKeyBase64;
        public RsaKeyData(){

        }
        public RsaKeyData(KeyPair keyPair){
            parseKeyPair(keyPair);
        }
        public void parseKeyPair(KeyPair keyPair){
            this.keyPair=keyPair;
            privateKey=(RSAPrivateKey)keyPair.getPrivate();
            publicKey=(RSAPublicKey) keyPair.getPublic();
            privateKeyBase64=Base64Util.encode(privateKey.getEncoded());
            publicKeyBase64=Base64Util.encode(publicKey.getEncoded());
        }
    }
    public static String getEncryptPrivateKey(RsaKeyData keyData){
        return keyData.privateKeyBase64;
    }
    public static String getDecryptPublicKey(RsaKeyData keyData){
        return keyData.publicKeyBase64;
    }

    public static RsaKeyData genRsaKeyData() throws NoSuchAlgorithmException{
        return genRsaKeyData(DEFAULT_KEY_SIZE);
    }
    public static RsaKeyData genRsaKeyData(int size) throws NoSuchAlgorithmException {
        return getRsaKeyData(genRsaKeyPair(size));
    }
    public static KeyPair genRsaKeyPair() throws NoSuchAlgorithmException {
        return genRsaKeyPair(DEFAULT_KEY_SIZE);
    }
    public static KeyPair genRsaKeyPair(int size) throws NoSuchAlgorithmException {
        KeyPairGenerator generator=KeyPairGenerator.getInstance("RSA");
        generator.initialize(size,new SecureRandom());
        return generator.generateKeyPair();
    }
    public static RsaKeyData getRsaKeyData(KeyPair keyPair){
        return new RsaKeyData(keyPair);
    }
    public static byte[] rsaEncrypt(byte[] data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,privateKey);
        byte[] result=new byte[data.length];
        int plen=0;
        while(plen<data.length){
            int cplen=UPDATE_SIZE;
            if(data.length-plen<UPDATE_SIZE){
                cplen=data.length-plen;
            }
            byte[] part=new byte[cplen];
            System.arraycopy(data,plen,part,0,cplen);
            byte[] enPart=cipher.update(part);
            System.arraycopy(part,0,result,plen,cplen);
            plen+=UPDATE_SIZE;
        }
        return result;
    }
    public static String rsaEncryptDataBase64(byte[] data,String privateKeyBase64) throws Exception {
        byte[] decodePrivateKey= Base64Util.decode(privateKeyBase64);
        RSAPrivateKey privateKey=(RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodePrivateKey));
        byte[] encodeData=rsaEncrypt(data,privateKey);
        String ret=Base64Util.encode(encodeData);
        return ret;
    }
    public static byte[] rsaDecrypt(byte[] data, RSAPublicKey publicKey) throws Exception{
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        byte[] result=new byte[data.length];
        int plen=0;
        while(plen<data.length){
            int cplen=UPDATE_SIZE;
            if(data.length-plen<UPDATE_SIZE){
                cplen=data.length-plen;
            }
            byte[] part=new byte[cplen];
            System.arraycopy(data,plen,part,0,cplen);
            byte[] enPart=cipher.update(part);
            System.arraycopy(part,0,result,plen,cplen);
            plen+=UPDATE_SIZE;
        }
        return result;
    }
    public static byte[] rsaDecryptDataBase64(String base64EncryptStr,String publicKeyBase64) throws Exception {
        byte[] enData=Base64Util.decode(base64EncryptStr.getBytes(CHAR_SET_NAME));
        byte[] dePublicKey=Base64Util.decode(publicKeyBase64);
        RSAPublicKey publicKey=(RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(dePublicKey));
        byte[] deData=rsaDecrypt(enData,publicKey);
        return deData;
    }
    public static String rsaEncryptBase64(String data,String privateKeyBase64) throws Exception{
        return rsaEncryptDataBase64(data.getBytes(CHAR_SET_NAME),privateKeyBase64);
    }
    public static String rsaDecryptBase64(String base64Data,String publicKeyBase64) throws Exception{
        byte[] data=rsaDecryptDataBase64(base64Data,publicKeyBase64);
        return new String(data, Charset.forName(CHAR_SET_NAME));
    }
    public static String doEncrypt(String data,RsaKeyData keyData) throws Exception {
        return rsaEncryptBase64(data,keyData.privateKeyBase64);
    }
    public static String doDecrypt(String base64Data,RsaKeyData keyData) throws Exception{
        return rsaDecryptBase64(base64Data,keyData.publicKeyBase64);
    }
}
