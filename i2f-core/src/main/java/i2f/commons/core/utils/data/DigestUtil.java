package i2f.commons.core.utils.data;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class DigestUtil {
    /**
     * 计算字符串的MD5
     *
     * @param str
     * @return
     */
    public static String makeMD5(String str) {
        return makeMD5(str.getBytes());
    }

    /**
     * 获取二进制数据的MD5值
     * 对于字符串请如下使用
     * String md5=makeMD5("aa".getBytes());
     *
     * @param data 字节数组，二进制值
     * @return MD5串
     */
    public static String makeMD5(byte[] data) {
        try {
            byte[] secretBytes = MessageDigest.getInstance("md5").digest(data);
            return secretBytes2MD5(secretBytes);
        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }

    private static String secretBytes2MD5(byte[] secretBytes) {
        String md5code = new BigInteger(1, secretBytes).toString(16).toUpperCase();
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static String makeMD5(InputStream is) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");

            byte[] buffer = new byte[8192];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }

            is.close();

            byte[] secretBytes = md5.digest();
            return secretBytes2MD5(secretBytes);
        } catch (NoSuchAlgorithmException e) {

        } catch (IOException e) {

        }
        return null;
    }

    /**
     * 获取文件的MD5值
     * 如下使用
     * String md5=makeMD5(new File("C:\\aaa.txt"));
     *
     * @param file 被计算的文件
     * @return 文件的MD5值
     */
    public static String makeMD5(File file) {
        try {
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            String md5code = makeMD5(fis);
            fis.close();
            return md5code;
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        return "";
    }

    public static String makeUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static Random rand = new Random();

    public static String makeCheckCode(int len) {
        return makeCheckCode(len, false);
    }

    public static String makeCheckCode(int len, boolean onlyNumber) {
        String ret = "";
        int bounce = 10 + 26 + 26;
        if (onlyNumber) {
            bounce = 10;
        }
        for (int i = 0; i < len; i++) {
            int val = rand.nextInt(10 + 26 + 26);
            if (val < 10) {
                ret += (char) (val + '0');
            } else if (val < (10 + 26)) {
                ret += (char) (val - 10 + 'a');
            } else {
                ret += (char) (val - 10 - 26 + 'A');
            }
        }
        return ret;
    }

}
