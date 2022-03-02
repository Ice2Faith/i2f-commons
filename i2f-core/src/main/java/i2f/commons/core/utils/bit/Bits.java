package i2f.commons.core.utils.bit;

import java.io.UnsupportedEncodingException;
import java.util.Stack;

/**
 * @author ltb
 * @date 2022/3/1 9:00
 * @desc
 */
public class Bits {
    public static long bit(long num,int bit,int len,long val){
        long mk=mask(bit,len);
        long rmk=~mk;
        num&=rmk;
        val=bit(val,bit,len);
        num|=val;
        return num;
    }
    public static long bitl(long num,int bit,int len,long val){
        long mk=mask(bit,len);
        long rmk=~mk;
        num&=rmk;
        val=bitl(val,0,len);
        num|=(val << bit);
        return num;
    }
    public static long bit(long num,int bit,int len){
        long mk=mask(bit,len);
        return num & mk;
    }
    public static long bitl(long num,int bit,int len){
        long ret=bit(num,bit,len);
        ret >>>= bit;
        return ret;
    }
    public static long maskl(int len){
        long ret=0;
        for(int i=0;i<len;i++){
            ret <<= 1;
            ret |= 1;
        }
        return ret;
    }
    public static long mask(int bit,int len){
        long ret=maskl(len);
        ret <<= bit;
        return ret;
    }

    public static String toBin(long num){
        Stack<String> stack=new Stack<>();
        for(int i=0;i<64 && num>0;i++){
            if((num&0x01)==1){
                stack.push("1");
            }else{
                stack.push("0");
            }
            num >>>= 1;
        }
        StringBuilder builder=new StringBuilder();
        while(!stack.isEmpty()){
            builder.append(stack.pop());
        }
        return builder.toString();
    }
    public static long formBin(String num){
        long ret=0;
        for(int i=0;i<num.length();i++){
            ret *=2;
            if(num.charAt(i)=='1'){
                ret+=1;
            }else{
                ret+=0;
            }
        }
        return ret;
    }

    public static boolean getBoolean(byte[] b, int off) {
        return b[off] != 0;
    }

    public static char getChar(byte[] b, int off) {
        return (char) ((b[off + 1] & 0xFF) +
                (b[off] << 8));
    }

    public static short getShort(byte[] b, int off) {
        return (short) ((b[off + 1] & 0xFF) +
                (b[off] << 8));
    }

    public static int getInt(byte[] b, int off) {
        return ((b[off + 3] & 0xFF)      ) +
                ((b[off + 2] & 0xFF) <<  8) +
                ((b[off + 1] & 0xFF) << 16) +
                ((b[off    ]       ) << 24);
    }

    public static float getFloat(byte[] b, int off) {
        return Float.intBitsToFloat(getInt(b, off));
    }

    public static long getLong(byte[] b, int off) {
        return ((b[off + 7] & 0xFFL)      ) +
                ((b[off + 6] & 0xFFL) <<  8) +
                ((b[off + 5] & 0xFFL) << 16) +
                ((b[off + 4] & 0xFFL) << 24) +
                ((b[off + 3] & 0xFFL) << 32) +
                ((b[off + 2] & 0xFFL) << 40) +
                ((b[off + 1] & 0xFFL) << 48) +
                (((long) b[off])      << 56);
    }

    public static double getDouble(byte[] b, int off) {
        return Double.longBitsToDouble(getLong(b, off));
    }

    public static int putBoolean(byte[] b, int off, boolean val) {
        b[off] = (byte) (val ? 1 : 0);
        return 1;
    }

    public static int putChar(byte[] b, int off, char val) {
        b[off + 1] = (byte) (val      );
        b[off    ] = (byte) (val >>> 8);
        return 2;
    }

    public static int putShort(byte[] b, int off, short val) {
        b[off + 1] = (byte) (val      );
        b[off    ] = (byte) (val >>> 8);
        return 2;
    }

    public static int putInt(byte[] b, int off, int val) {
        b[off + 3] = (byte) (val       );
        b[off + 2] = (byte) (val >>>  8);
        b[off + 1] = (byte) (val >>> 16);
        b[off    ] = (byte) (val >>> 24);
        return 4;
    }

    public static int putFloat(byte[] b, int off, float val) {
        return putInt(b, off,  Float.floatToIntBits(val));
    }

    public static int putLong(byte[] b, int off, long val) {
        b[off + 7] = (byte) (val       );
        b[off + 6] = (byte) (val >>>  8);
        b[off + 5] = (byte) (val >>> 16);
        b[off + 4] = (byte) (val >>> 24);
        b[off + 3] = (byte) (val >>> 32);
        b[off + 2] = (byte) (val >>> 40);
        b[off + 1] = (byte) (val >>> 48);
        b[off    ] = (byte) (val >>> 56);
        return 8;
    }

    public static int putDouble(byte[] b, int off, double val) {
        return putLong(b, off, Double.doubleToLongBits(val));
    }

    public static int getWriteStringLength(String str,String charset) throws UnsupportedEncodingException {
        if(str==null){
            return 4;
        }
        byte[] bts=str.getBytes(charset);
        return bts.length+4;
    }
    public static int putString(byte[] b,int off,String str,String charset) throws UnsupportedEncodingException {
        if(str==null){
            int hlen=putInt(b,off,-1);
            return hlen;
        }
        byte[] bts = str.getBytes(charset);
        int len=bts.length;
        int hlen=putInt(b,off,len);
        off+=hlen;
        for(int i=0;i<bts.length;i++){
            b[off+i]=bts[i];
        }
        return bts.length+hlen;
    }
    public static int getReadStringLength(byte[] b,int off){
        int len=getInt(b,off);
        if(len==-1){
            return 4;
        }
        return len+4;
    }
    public static String getString(byte[] b,int off,String charset) throws UnsupportedEncodingException {
        int len=getInt(b,off);
        if(len==-1){
            return null;
        }
        off+=4;
        byte[] buf=new byte[len];
        for(int i=0;i<len;i++){
            buf[i]=b[off+i];
        }
        return new String(buf,charset);
    }
}
