package i2f.commons.component.ftp;

import i2f.commons.component.ftp.data.IFtpMeta;
import i2f.commons.core.utils.file.FileUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * @author ltb
 * @date 2021/11/1
 */
public class FtpUtil  implements Closeable {
    private IFtpMeta meta;
    private FTPClient ftpClient;
    public FtpUtil(IFtpMeta meta){
        setMeta(meta);
    }
    public FtpUtil setMeta(IFtpMeta meta){
        this.meta=meta;
        return this;
    }

    public FtpUtil login() throws IOException {
        ftpClient=new FTPClient();
        ftpClient.connect(meta.getHost(),meta.getPort());
        ftpClient.login(meta.getUsername(),meta.getPassword());
        int reply= ftpClient.getReplyCode();
        if(FTPReply.isPositiveCompletion(reply)){
            ftpClient.logout();
            ftpClient.disconnect();
            ftpClient=null;
            throw new IOException("ftp not positive completion");
        }
        return this;
    }
    public FtpUtil logout() throws IOException {
        if(ftpClient!=null){
            ftpClient.logout();
            if(ftpClient.isConnected()){
                ftpClient.disconnect();
            }
        }
        return this;
    }

    public FtpUtil mkdirs(String serverPath) throws  IOException {
        serverPath=serverPath.replaceAll("\\\\","/");
        String[] dirs=serverPath.split("\\/");
        StringBuilder builder=new StringBuilder();
        for(String item : dirs){
            if("".equals(item)){
                continue;
            }
            builder.append("/");
            builder.append(item);
            String ppath=builder.toString();
            ftpClient.makeDirectory(ppath);
        }
        return this;
    }

    public FtpUtil upload(String serverPath, String  serverFileName, InputStream is) throws  IOException {
        mkdirs(serverPath);
        ftpClient.changeWorkingDirectory(serverPath);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        boolean rs=ftpClient.storeFile(serverFileName,is);
        if(!rs){
            throw new IOException("upload file failure!");
        }
        return this;
    }

    public FtpUtil upload(String serverPath, File localFile) throws  IOException {
        String fileName=localFile.getName();
        InputStream is=new FileInputStream(localFile);
        upload(serverPath,fileName,is);
        is.close();
        return this;
    }

    public OutputStream download(String serverPath,String serverFileName,OutputStream os) throws  IOException {
        if(serverPath!=null && !"".equals(serverPath)){
            ftpClient.changeWorkingDirectory(serverPath);
        }
        ftpClient.retrieveFile(serverFileName,os);
        return os;
    }

    public File download(String serverPath,String serverFileName,File localFile) throws IOException {
        FileUtil.useParentDir(localFile);
        OutputStream os=new FileOutputStream(localFile);
        download(serverPath,serverFileName,os);
        os.close();
        return localFile;
    }

    public File download2Dir(String serverPath,String serverFileName,File localDir) throws IOException {
        FileUtil.useDir(localDir);
        File localFile=new File(localDir,serverFileName);
        OutputStream os=new FileOutputStream(localFile);
        download(serverPath,serverFileName,os);
        os.close();
        return localFile;
    }

    public FtpUtil delete(String serverPath, String serverFileName) throws  IOException {
        if(serverPath!=null && !"".equals(serverPath)){
            ftpClient.changeWorkingDirectory(serverPath);
        }
        ftpClient.deleteFile(serverFileName);
        return this;
    }

    public FtpUtil deleteDir(String serverPath) throws  IOException {
        ftpClient.removeDirectory(serverPath);
        return this;
    }

    public FTPFile[] listFiles(String serverPath) throws  IOException {
        return ftpClient.listFiles(serverPath);
    }

    @Override
    public void close() throws IOException {
        logout();
    }
}
