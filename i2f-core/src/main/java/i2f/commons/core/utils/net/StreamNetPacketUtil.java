package i2f.commons.core.utils.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2021/8/12
 */
public class StreamNetPacketUtil {
    public static void writePacket(OutputStream os,NetPacket pack) throws IOException {
        byte[] data= pack.getPkgBytes();
        os.write(data);
        os.flush();
    }
    public static void writeData(OutputStream os,byte[] data) throws IOException {
        writePacket(os,new NetPacket(data));
    }
    public static void write(OutputStream os,String content) throws IOException {
        writePacket(os,new NetPacket(content));
    }
    public static void write(OutputStream os,String content, Charset charset) throws IOException {
        writePacket(os,new NetPacket(content,charset));
    }
    private InputStream is;
    private byte[] buffer=new byte[10*1024];
    private int lastLen=0;
    private List<NetPacket> packets=new ArrayList<>();
    public StreamNetPacketUtil(InputStream is){
        this.is=is;
    }

    public byte[] readNext() throws IOException {
        parseInputPackets();
        if(packets.size()>0){
            NetPacket pack=packets.get(0);
            packets.remove(0);
            return pack.getData();
        }
        return new byte[0];
    }
    public String readNextString() throws IOException {
        byte[] data=readNext();
        if(data.length>0){
            return new String(data,Charset.forName(NetPacket.DEFAULT_CHARSET));
        }
        return null;
    }
    public String readNextString(Charset charset) throws IOException {
        byte[] data=readNext();
        if(data.length>0){
            return new String(data,charset);
        }
        return null;
    }
    private void parseInputPackets() throws IOException {
        int i=lastLen;
        while(lastLen< buffer.length){
            int rd=is.read();
            if(rd==-1){
                break;
            }
            buffer[i]=(byte)rd;
            i++;
        }
        List<NetPacket> list=new ArrayList<>();
        int idx=NetPacket.readPacket(buffer,0,i,list);
        int j=0;
        while((idx+j)<i){
            buffer[j]=buffer[idx+j];
            j++;
        }
        lastLen=j;
        if(list.size()>0){
            packets.addAll(list);
        }
    }
}
