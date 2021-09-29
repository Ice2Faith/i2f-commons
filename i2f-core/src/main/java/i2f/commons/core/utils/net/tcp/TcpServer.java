package i2f.commons.core.utils.net.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public int port;
    public ServerSocket serverSock;
    public IClientAccepter accepter;
    public Thread acceptThread;
    public boolean isShutdown=true;
    public Exception shutdownExcept;
    public TcpServer(int port,IClientAccepter accepter) throws IOException {
        this.isShutdown=true;
        this.port=port;
        this.accepter=accepter;
        serverSock=new ServerSocket(port);
        listen();
    }
    public void listen(){
        if(!isShutdown){
            return;
        }
        isShutdown=false;
        shutdownExcept=null;
        acceptThread=new Thread(new Runnable() {
            @Override
            public void run() {
                isShutdown=false;
                try{
                    while(!serverSock.isClosed()){
                        Socket socket= serverSock.accept();
                        accepter.onClientArrive(socket);
                    }
                }catch(Exception e){
                    shutdownExcept=e;
                }
                isShutdown=true;
            }
        });
        acceptThread.start();
    }
    public void close() throws IOException {
        if(!serverSock.isClosed()){
            accepter.onServerClose();
            serverSock.close();
        }
    }
}
