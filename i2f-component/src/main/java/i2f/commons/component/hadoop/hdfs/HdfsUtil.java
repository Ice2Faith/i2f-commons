package i2f.commons.component.hadoop.hdfs;

import i2f.commons.component.hadoop.hdfs.data.IHdfsMeta;
import i2f.commons.core.utils.data.DataUtil;
import i2f.commons.core.utils.file.FileUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author ltb
 * @date 2021/11/1
 */
public class HdfsUtil implements Closeable {
    private IHdfsMeta meta;
    private FileSystem fileSystem;
    public HdfsUtil(IHdfsMeta meta){
        setMeta(meta);
    }
    public HdfsUtil setMeta(IHdfsMeta meta){
        this.meta=meta;
        return this;
    }

    public FileSystem getFileSystem() throws IOException {
        if(fileSystem==null){
            synchronized (this){
                if(fileSystem==null){
                    Configuration conf=meta.getConfig();
                    if(conf==null){
                        conf=new Configuration();
                    }
                    conf.set("fs.defaultFS",meta.getDefaultFs());
                    try{
                        fileSystem=FileSystem.get(new URI(meta.getUri()),conf,meta.getUser());
                    }catch(URISyntaxException | InterruptedException e){
                        throw new IOException(e.getMessage(),e);
                    }

                }
            }
        }
        return fileSystem;
    }

    public HdfsUtil mkdirs(String serverPath) throws  IOException {
        Path path=new Path(serverPath);
        getFileSystem().mkdirs(path);
        return this;
    }

    public HdfsUtil upload(String serverPath, String  serverFileName, InputStream is) throws  IOException {
        mkdirs(serverPath);
        Path path=new Path(serverPath,serverFileName);
        FSDataOutputStream fos=getFileSystem().create(path,true);
        DataUtil.streamCopy(is,fos,false);
        fos.close();
        return this;
    }

    public HdfsUtil upload(String serverPath, File localFile) throws  IOException {
        String fileName=localFile.getName();
        InputStream is=new FileInputStream(localFile);
        upload(serverPath,fileName,is);
        is.close();
        return this;
    }

    public OutputStream download(String serverPath, String serverFileName, OutputStream os) throws  IOException {
        Path path=new Path(serverPath,serverFileName);
        FSDataInputStream fis=getFileSystem().open(path);
        DataUtil.streamCopy(fis,os,false);
        fis.close();
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

    public HdfsUtil delete(String serverPath, String serverFileName) throws  IOException {
        Path path=new Path(serverPath,serverFileName);
        boolean rs=getFileSystem().delete(path,false);
        if(!rs){
            throw new IOException("delete file failure.");
        }
        return this;
    }

    public HdfsUtil deleteDir(String serverPath) throws  IOException {
        Path path=new Path(serverPath);
        boolean rs=getFileSystem().delete(path,true);
        if(!rs){
            throw new IOException("delete file failure.");
        }
        return this;
    }

    public FileStatus[] listFiles(String serverPath) throws  IOException {
        Path path=new Path(serverPath);
        FileStatus[] status=getFileSystem().listStatus(path);
        return status;
    }

    @Override
    public void close() throws IOException {
        if(fileSystem!=null){
            fileSystem.close();
        }
    }
}
