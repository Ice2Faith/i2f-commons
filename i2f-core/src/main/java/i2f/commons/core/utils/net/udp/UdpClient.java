package i2f.commons.core.utils.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpClient {
    public int port;
    public String ip;
    public DatagramSocket sock;
    public UdpClient(String ip,int port) throws SocketException {
        this.ip=ip;
        this.port=port;
        sock=new DatagramSocket();
    }
    public void close(){
        if(!sock.isClosed()){
            sock.close();
        }
    }
    public void send(byte[] data) throws IOException {
        InetAddress address=InetAddress.getByName(this.ip);
        DatagramPacket pack=new DatagramPacket(data, data.length,address,this.port);
        sock.send(pack);
    }
    public void send(String data,String charset) throws IOException {
        send(data.getBytes(charset));
    }
    public void send(String data) throws IOException {
        send(data,"UTF-8");
    }
}
