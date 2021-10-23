package test;

import i2f.commons.core.utils.data.DataUtil;

/**
 * @author ltb
 * @date 2021/10/13
 */
public class TestData {
    public static void main(String[] args){
        String data="1a啊ssh哦了.真傻比";
        int a=12;
        String hex= DataUtil.toHexString(data.getBytes());
        System.out.println(hex);

        String otc= DataUtil.toOtcString(data.getBytes());
        System.out.println(otc);

        String bin= DataUtil.toBinString(data.getBytes());
        System.out.println(bin);

        String dec= DataUtil.toDecString(data.getBytes());
        System.out.println(dec);

        byte[] bhex=DataUtil.parseHexString(hex);
        hex=new String(bhex);
        System.out.println(hex);

        byte[] botc=DataUtil.parseOtcString(otc);
        otc=new String(botc);
        System.out.println(otc);

        byte[] bdec=DataUtil.parseDecString(dec);
        dec=new String(bdec);
        System.out.println(dec);

        byte[] bbin=DataUtil.parseBinString(bin);
        String rb=DataUtil.toBinString(bbin);
        System.out.println(rb);
        bin=new String(bbin);
        System.out.println(bin);

    }
}
