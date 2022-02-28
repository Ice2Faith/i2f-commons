package i2f.commons.core.utils.net.tcp.impl;


import i2f.commons.core.utils.net.tcp.IClientAccepter;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class ClientAccepter implements IClientAccepter {
    protected List<Socket> socks=new LinkedList<Socket>();
    protected abstract void sockProcess(int index,Socket sock);
    protected ExecutorService pool= new ThreadPoolExecutor(2,512,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    @Override
    public void onClientArrive(Socket sock) {
        final int index=this.socks.size();
        final Socket nsock=sock;
        this.socks.add(sock);
        pool.submit(new Runnable() {
            @Override
            public void run() {
                sockProcess(index,nsock);
            }
        });
    }

    @Override
    public void onServerClose() {
        for (Socket socket: socks){
            if(!socket.isClosed()){
                try{
                    socket.close();
                }catch(Exception e){

                }
            }
        }
    }
}
