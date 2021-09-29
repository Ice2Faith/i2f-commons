package i2f.commons.core.utils.net;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * @author ltb
 * @date 2021/8/11
 */
public class NetPacket {
    public static final String DEFAULT_CHARSET="UTF-8";
    public static final String PKG_HEAD="@.npkg!$";

    private int dataSize;
    private byte[] data;

    public NetPacket(byte[] data){
        this.data=data;
        this.dataSize= this.data.length;
    }
    public NetPacket(String data) throws UnsupportedEncodingException {
        this.data=data.getBytes(DEFAULT_CHARSET);
        this.dataSize=this.data.length;
    }
    public NetPacket(String data,Charset charset) throws UnsupportedEncodingException {
        this.data=data.getBytes(charset);
        this.dataSize=this.data.length;
    }
    public byte[] getData(){
        return data;
    }

    public String inflateAsString(){
        return new String(this.data, Charset.forName(DEFAULT_CHARSET));
    }
    public static void putInt(byte[] b, int off, int val) {
        b[off + 3] = (byte) (val       );
        b[off + 2] = (byte) (val >>>  8);
        b[off + 1] = (byte) (val >>> 16);
        b[off    ] = (byte) (val >>> 24);
    }
    public static int getInt(byte[] b, int off) {
        return ((b[off + 3] & 0xFF)      ) +
                ((b[off + 2] & 0xFF) <<  8) +
                ((b[off + 1] & 0xFF) << 16) +
                ((b[off    ]       ) << 24);
    }
    public byte[] getPkgBytes() throws IOException {
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        os.write(PKG_HEAD.getBytes(DEFAULT_CHARSET));
        byte[] intVal=new byte[4];
        putInt(intVal,0,dataSize);
        os.write(intVal);
        os.write(data);
        os.close();
        return os.toByteArray();
    }

    public static int readPacket(byte[] buffer,int offset,int len,List<NetPacket> pkgs) throws IOException {
        int end=offset+len;
        byte[] bdata= Arrays.copyOfRange(buffer,offset,end>buffer.length? buffer.length : end);
        int idx=readPacket(bdata,pkgs);
        return offset+idx;
    }

    public static int readPacket(byte[] buffer,int offset,List<NetPacket> pkgs) throws IOException {
        byte[] bdata= Arrays.copyOfRange(buffer,offset,buffer.length);
        int idx=readPacket(bdata,pkgs);
        return offset+idx;
    }

    public static int readPacket(byte[] buffer,List<NetPacket> pkgs) throws IOException {
        byte[] head=PKG_HEAD.getBytes(DEFAULT_CHARSET);
        byte[] rhead=new byte[head.length];
        int i=0;
        while(i<buffer.length){
            int j=0;
            while(j<rhead.length && (i+j)<buffer.length){
                rhead[j]=buffer[i+j];
                j++;
            }
            if(j==rhead.length){
                String rHeadStr=new String(rhead,Charset.forName(DEFAULT_CHARSET));
                if(PKG_HEAD.equals(rHeadStr)){
                    if(i+j+4>buffer.length){
                        break;
                    }
                    int dataSize=getInt(buffer,i+j);
                    j=i+j+4;
                    if(j+dataSize>buffer.length){
                        break;
                    }
                    int k=0;
                    byte[] rdData=new byte[dataSize];
                    while(k<dataSize && (j+k)<buffer.length){
                        rdData[k]=buffer[j+k];
                        k++;
                    }
                    if(k==dataSize){
                        NetPacket pak=new NetPacket(rdData);
                        pkgs.add(pak);
                        i=j+k;
                        continue;
                    }else{
                        break;
                    }
                }

            }else{
                break;
            }
            i++;
        }
        return i;
    }

    public static<T extends Serializable> NetPacket serializeObj(T obj) throws IOException {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
        bos.close();
        return new NetPacket(bos.toByteArray());
    }

}
