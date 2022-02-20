package i2f.commons.core.utils.net.core;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author ltb
 * @date 2022/2/20 15:37
 * @desc
 */
public class NetTransfer{
    public static String formatDate(Date date){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        return fmt.format(date);
    }
    public static String getTempFileName(){
        return "tmp_"+System.currentTimeMillis()
                +"_"+System.nanoTime()
                +"_"+ UUID.randomUUID().toString().replaceAll("-","")
                +"_"+Thread.currentThread().getId();
    }

    public static void send(String str, String mimeType, OutputStream os) throws IOException {
        send(str,"UTF-8",mimeType,os);
    }
    public static void send(String str,String charset,String mimeType,OutputStream os) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(str.getBytes(charset));
        bos.close();

        ByteArrayInputStream bis=new ByteArrayInputStream(bos.toByteArray());
        send(bis,mimeType,os);
    }

    public static void send(File file, String mimeType, OutputStream os) throws IOException {
        InputStream fis = new FileInputStream(file);
        send(file,mimeType,os);
    }

    public static void send(InputStream is,String mimeType, OutputStream os) throws IOException {
        NetTransferHead head=new NetTransferHead();
        head.setDate(formatDate(new Date()));
        head.setMimeType(mimeType);

        long length=0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf=new byte[4096];
        boolean enough=false; // 数据是否能用ByteArrayInputStream存储下
        while((int)(length+4096)==(length+4096)){
            int len=is.read(buf);
            if(len>0){
                bos.write(buf,0,len);
                length+=len;
            }else{
                enough=true;
                break;
            }
        }

        InputStream tis=null;
        File file=null;
        if(enough){
            tis=new ByteArrayInputStream(bos.toByteArray());
        }else{ // 内存不足以存储数据时，借用临时文件
            String fileName=getTempFileName();
            file=File.createTempFile(fileName, ".data");
            FileOutputStream fos = new FileOutputStream(file);
            int len=0;

           ByteArrayInputStream bis=new ByteArrayInputStream(bos.toByteArray());
           while((len=bis.read(buf))>0){
               fos.write(buf,0,len);
           }

            while((len=is.read(buf))>0){
                fos.write(buf,0,len);
                length+=len;
            }
            fos.flush();
            fos.close();

            tis=new FileInputStream(file);
        }

        head.setContentLength(length);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(head);
        oos.flush();

        int len=0;
        while((len=tis.read(buf))>0){
            os.write(buf,0,len);
        }
        os.flush();
        tis.close();
        if(file!=null){
            file.delete();
        }
    }

    public static NetTransferResponse recv(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream ois=new ObjectInputStream(is);
        NetTransferHead head= (NetTransferHead) ois.readObject();
        NetTransferResponse response=new NetTransferResponse(head);
        long length= head.getContentLength();

        int ilen=(int)length;
        if(ilen==length){ // 判断是否能用ByteArrayOutputStream存储数据
            ByteArrayOutputStream bos = new ByteArrayOutputStream(ilen);
            byte[] buf=new byte[4096];
            int len=0;
            while((len=is.read(buf))>0){
                bos.write(buf,0,len);
            }
            ByteArrayInputStream bis=new ByteArrayInputStream(bos.toByteArray());
            response.setInputStream(bis);
        }else{ // 不能用内存储存的时候，借助临时文件
            String fileName=getTempFileName();
            File tmpFile = File.createTempFile(fileName,".data");
            FileOutputStream fos = new FileOutputStream(tmpFile);
            byte[] buf=new byte[4096];
            long plen=0;
            while(plen<length){
                if(length-plen>=4096){
                    int len=is.read(buf);
                    fos.write(buf,0,len);
                    plen+=len;
                }else{
                    int len=is.read(buf,0,(int)(length-plen));
                    fos.write(buf,0,len);
                    plen+=len;
                }
            }

            fos.close();

            InputStream fis=new NetTransferInputStream(tmpFile.getAbsolutePath());
            response.setInputStream(fis);
        }

        return response;
    }
}
