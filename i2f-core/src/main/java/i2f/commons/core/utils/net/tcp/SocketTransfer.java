package i2f.commons.core.utils.net.tcp;

import java.io.*;
import java.net.Socket;

public class SocketTransfer {
    protected Socket sock;
    public SocketTransfer(){

    }
    public SocketTransfer(Socket sock){
        this.sock=sock;
    }
    public InputStream getInputStream() throws IOException {
        return sock.getInputStream();
    }
    public OutputStream getOutputStream() throws IOException {
        return sock.getOutputStream();
    }
    public Socket getSocket(){
        return sock;
    }
    public BufferedReader getBufferedReader() throws IOException {
        return new BufferedReader(new InputStreamReader(sock.getInputStream()));
    }
    public BufferedWriter getBufferedWriter() throws IOException {
        return new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }
    public String readLine() throws IOException {
        return getBufferedReader().readLine();
    }
    public void writeLine(String str) throws IOException {
        BufferedWriter writer=getBufferedWriter();
        writer.write(str);
        writer.newLine();
        writer.flush();
    }
}
