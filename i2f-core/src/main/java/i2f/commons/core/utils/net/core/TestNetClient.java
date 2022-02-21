package i2f.commons.core.utils.net.core;

import i2f.commons.core.utils.net.tcp.TcpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/2/20 15:43
 * @desc
 */
public class TestNetClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scanner=new Scanner(System.in);
        System.out.println("scans lan server ...");
        Map<InetAddress, Set<InetAddress>> lan= NetUtil.getAllLanInfo();
        System.out.println("scans lan online server ...");
        Set<String> finds=new HashSet<>();
        for(Map.Entry<InetAddress,Set<InetAddress>> item : lan.entrySet()){
            InetAddress myaddr=item.getKey();
            if(myaddr instanceof Inet6Address){
                continue;
            }
            Set<InetAddress> lanaddrs=item.getValue();
            if(lanaddrs==null || lanaddrs.size()==0){
                continue;
            }
            for(InetAddress addr : lanaddrs){
                if(addr instanceof Inet6Address){
                    continue;
                }
                try{
                    TcpClient pclient=new TcpClient(addr.getHostAddress(),TestNetServer.SERVER_PORT);
                    pclient.close();
                    finds.add(addr.getHostAddress());
                }catch(Exception e){
                    System.out.println("connect "+addr.getHostAddress()+" error of "+e.getMessage());
                }
            }
        }
        try{
            TcpClient pclient=new TcpClient("127.0.0.1",TestNetServer.SERVER_PORT);
            pclient.close();
            finds.add("127.0.0.1");
        }catch(Exception e){
            System.out.println("connect 127.0.0.1 error of "+e.getMessage());
        }

        String[] list=new String[finds.size()];
        int pi=0;
        for(String item : finds){
            list[pi++]=item;
        }

        if(list.length==0){
            System.out.println("not found any server,exit.");
            return;
        }

        System.out.println("find server list:");
        for(int i=0;i<list.length;i+=1){
            System.out.println("\t"+i+" : "+list[i]);
        }
        System.out.print("please choice:");
        int idx=scanner.nextInt();
        String ip=list[idx];

        TcpClient client=new TcpClient(ip,TestNetServer.SERVER_PORT);
        Socket sock=client.getSocket();
        TransferClientProcessor target=new TransferClientProcessor(sock);
        Thread thread=new Thread(target);
        thread.start();
        OutputStream os= client.getOutputStream();
        NetTransfer.sendString("hello",os);
        while(true){
            System.out.println("-------------------");
            System.out.println("command guide:");
            System.out.println("\t[file:] start to send a file");
            System.out.println("\t[exit] close application");
            System.out.println("\telse send normal string");
            System.out.println("example:");
            System.out.println("\tfile:D:\\test01\\tree.gif");
            System.out.println("\t\twill send the file");
            System.out.print(">/ ");
            String cmd=scanner.nextLine();
            if("exit".equals(cmd)){
                NetTransfer.sendString("exit",os);
            } else if (cmd.startsWith("file:")) {
                String filePath = cmd.substring("file:".length());
                File file=new File(filePath);
                if(file.exists()){
                    System.out.println("file "+file.getAbsolutePath()+" sending...");
                    NetTransfer.sendFile(file.getAbsolutePath(),os);
                    System.out.println("file "+file.getAbsolutePath()+" sent.");
                }else{
                    System.out.println("file not exist,please try again.");
                }
            }else{
                NetTransfer.sendString(cmd,os);
            }
        }
    }

    public static class TransferClientProcessor implements Runnable{
        public Socket sock;
        public TransferClientProcessor(Socket sock){
            this.sock=sock;
        }
        @Override
        public void run() {
            try{
                InputStream is=sock.getInputStream();
                while(true){
                    NetTransferResponse resp=NetTransfer.recv(is);
                    if(resp.isTextPlain()){
                        String str=resp.getAsString();
                        System.out.println("server "+sock.getInetAddress().getHostAddress()+":"+str);
                        if("exit".equals(str)){
                            sock.close();
                            System.out.println("client close");
                            break;
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}
