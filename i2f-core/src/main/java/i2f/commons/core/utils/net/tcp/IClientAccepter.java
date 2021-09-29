package i2f.commons.core.utils.net.tcp;

import java.net.Socket;

public interface IClientAccepter {
    void onClientArrive(Socket sock);
    void onServerClose();
}
