package i2f.commons.core.utils.net.tcp.impl;


import i2f.commons.core.utils.net.tcp.IClientAccepter;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public abstract class ClientAccepter implements IClientAccepter {
    protected List<Socket> socks=new LinkedList<Socket>();
    protected abstract void sockProcess(int index,Socket sock);
    @Override
    public void onClientArrive(Socket sock) {
        int index=this.socks.size();
        this.socks.add(sock);
        sockProcess(index,sock);
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
