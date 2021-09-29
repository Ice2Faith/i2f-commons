package i2f.commons.core.utils.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class UdpServer {
    public DatagramSocket sock;
    public int port;
    public UdpServer(int port) throws SocketException {
        this.port=port;
        this.sock=new DatagramSocket(port);
    }
    public void close(){
        if(!this.sock.isClosed()){
            this.sock.close();
        }
    }
    public DatagramPacket recv(int maxByte) throws IOException {
        byte[] buf=new byte[maxByte];
        DatagramPacket pack=new DatagramPacket(buf,maxByte);
        sock.receive(pack);
        return pack;
    }
    public byte[] recvBytes(int maxByte) throws IOException {
        DatagramPacket pack=recv(maxByte);
        return Arrays.copyOf(pack.getData(),pack.getLength());
    }
    public String recvStr(int maxByte,String charset) throws IOException {
        byte[] data=recvBytes(maxByte);
        return new String(data, Charset.forName(charset));
    }
    public String recvStr(int maxByte) throws IOException {
        return recvStr(maxByte,"UTF-8");
    }
}
