package i2f.commons.core.utils.net.core;

import i2f.commons.core.utils.net.tcp.TcpServer;
import i2f.commons.core.utils.net.tcp.impl.ClientAccepter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author ltb
 * @date 2022/2/20 15:43
 * @desc
 */
public class TestNetServer {
    public static final String SERVER_IP="127.0.0.1";
    public static final Integer SERVER_PORT = 5500;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("server listening...");
        TcpServer server=new TcpServer(SERVER_PORT,new TransferClientAccepter());
        System.in.read();
        System.out.println("server shutdown.");
    }

    public static class TransferClientAccepter extends ClientAccepter {
        public static final String FILE_BASE_LOCATION="D:\\net_test";
        @Override
        protected void sockProcess(int index, Socket sock) {
            System.out.println("client accept:"+sock.getInetAddress().getHostAddress());
            try{
                InputStream is= sock.getInputStream();
                OutputStream os=sock.getOutputStream();
                while(true) {
                    NetTransferResponse resp = NetTransfer.recv(is);
                    if(resp.isTextPlain()){
                        String str=resp.getAsString();
                        System.out.println("client "+sock.getInetAddress().getHostAddress()+":"+str);
                        if("exit".equals(str)){
                            System.out.println("client exit:"+sock.getInetAddress().getHostAddress());
                            NetTransfer.sendString("exit",os);
                            sock.close();
                            break;
                        }
                        if("hello".equals(str)){
                            System.out.println("client hello:"+sock.getInetAddress().getHostAddress());
                            InetAddress addr=sock.getInetAddress();
                            String clientIp=addr.getHostAddress();
                            String hostName=addr.getHostName();
                            NetTransfer.sendString("ip="+clientIp+","+"host="+hostName,os);
                        }
                    }else if(resp.isFile()){
                        String fileName=resp.getName();
                        File file=new File(FILE_BASE_LOCATION,fileName);
                        System.out.println("save client file:"+fileName+"\n\t-> "+file.getAbsolutePath());
                        if(!file.getParentFile().exists()){
                            file.getParentFile().mkdirs();
                        }
                        resp.saveAsFile(file);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
