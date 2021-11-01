package i2f.commons.component.sftp;

import com.jcraft.jsch.*;
import i2f.commons.component.sftp.data.ISftpMeta;
import i2f.commons.core.utils.file.FileUtil;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * @author ltb
 * @date 2021/11/1
 */
public class SftpUtil implements Closeable{
    private ISftpMeta meta;
    private Session session;
    private ChannelSftp channelSftp;
    public SftpUtil(ISftpMeta meta){
        setMeta(meta);
    }
    public SftpUtil setMeta(ISftpMeta meta){
        this.meta=meta;
        return this;
    }

    public SftpUtil login() throws JSchException {
        JSch jsch=new JSch();
        if(meta.getPrivateKey()!=null){
            jsch.addIdentity(meta.getPrivateKey());
        }

        session=jsch.getSession(meta.getUserName(),meta.getHost(),meta.getPort());

        if(meta.getPassword()!=null){
            session.setPassword(meta.getPassword());
        }

        Properties config=new Properties();
        config.put("StrictHostKeyChecking","no");

        Properties cusConfig=meta.getConfig();
        for(Map.Entry<Object,Object> item : cusConfig.entrySet()){
           config.put(item.getKey(),item.getValue());
        }

        session.setConfig(config);
        session.connect();

        Channel channel=session.openChannel("sftp");
        channel.connect();

        channelSftp=(ChannelSftp)channel;

        return this;
    }

    public SftpUtil logout(){
        if(channelSftp!=null){
            if(channelSftp.isConnected()){
                channelSftp.disconnect();
            }
        }
        if(session!=null){
            if(session.isConnected()){
                session.disconnect();
            }
        }
        return  this;
    }

    public SftpUtil mkdirs(String serverPath) throws SftpException {
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
            channelSftp.mkdir(ppath);
        }
        return this;
    }

    public SftpUtil upload(String serverPath, String  serverFileName, InputStream is) throws SftpException {
        mkdirs(serverPath);
        channelSftp.cd(serverPath);
        channelSftp.put(is,serverFileName);
        return this;
    }

    public SftpUtil upload(String serverPath, File localFile) throws SftpException, IOException {
        String fileName=localFile.getName();
        InputStream is=new FileInputStream(localFile);
        upload(serverPath,fileName,is);
        is.close();
        return this;
    }

    public OutputStream download(String serverPath,String serverFileName,OutputStream os) throws SftpException {
        if(serverPath!=null && !"".equals(serverPath)){
            channelSftp.cd(serverPath);
        }
        channelSftp.get(serverFileName,os);
        return os;
    }

    public File download(String serverPath,String serverFileName,File localFile) throws IOException, SftpException {
        FileUtil.useParentDir(localFile);
        OutputStream os=new FileOutputStream(localFile);
        download(serverPath,serverFileName,os);
        os.close();
        return localFile;
    }

    public File download2Dir(String serverPath,String serverFileName,File localDir) throws  SftpException {
        FileUtil.useDir(localDir);
        File localFile=new File(localDir,serverFileName);
        try{
            OutputStream os=new FileOutputStream(localFile);
            download(serverPath,serverFileName,os);
            os.close();
        }catch(IOException e){
            throw new SftpException(0,e.getMessage(),e);
        }
        return localFile;
    }

    public SftpUtil delete(String serverPath,String serverFileName) throws SftpException {
        if(serverPath!=null && !"".equals(serverPath)){
            channelSftp.cd(serverPath);
        }
        channelSftp.rm(serverFileName);
        return this;
    }

    public SftpUtil deleteDir(String serverPath) throws SftpException {
        channelSftp.rmdir(serverPath);
        return this;
    }

    public Vector<?> listFiles(String serverPath) throws SftpException {
        return channelSftp.ls(serverPath);
    }

    @Override
    public void close() throws IOException {
        logout();
    }
}
