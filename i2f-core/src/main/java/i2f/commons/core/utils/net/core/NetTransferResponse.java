package i2f.commons.core.utils.net.core;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

/**
 * @author ltb
 * @date 2022/2/20 15:38
 * @desc
 */
@Data
@NoArgsConstructor
public class NetTransferResponse extends NetTransferHead{
    private InputStream inputStream;
    public NetTransferResponse(NetTransferHead head){
        this.setDate(head.getDate());
        this.setMimeType(head.getMimeType());
        this.setContentLength(head.getContentLength());
        this.setCharset(head.getCharset());
        this.setName(head.getName());
    }
    public void saveAsFile(File file) throws IOException{
        if(!file.getParentFile().exists()){
            file.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buf=new byte[4096];
        int len=0;
        while((len=inputStream.read(buf))>0){
            fos.write(buf,0,len);
        }
        inputStream.close();
        fos.close();
    }
    public boolean isTextPlain(){
        return NetTransferHead.MIME_TEXT_PLAIN.equals(getMimeType());
    }
    public boolean isTextJson(){
        return NetTransferHead.MIME_TEXT_JSON.equals(getMimeType());
    }
    public boolean isTextXml(){
        return NetTransferHead.MIME_TEXT_XML.equals(getMimeType());
    }
    public boolean isTextHtml(){
        return NetTransferHead.MIME_TEXT_HTML.equals(getMimeType());
    }
    public boolean isFile(){
        return NetTransferHead.MIME_OCTET_STREAM.equals(getMimeType());
    }
    public boolean isImageJpg(){
        return NetTransferHead.MIME_IMAGE_JPEG.equals(getMimeType());
    }
    public boolean isImagePng(){
        return NetTransferHead.MIME_IMAGE_PNG.equals(getMimeType());
    }
    public boolean isImageGif(){
        return NetTransferHead.MIME_IMAGE_GIF.equals(getMimeType());
    }
    public boolean isVideoMp4(){
        return NetTransferHead.MIME_VIDEO_MP4.equals(getMimeType());
    }
    public boolean isAudioMp3(){
        return NetTransferHead.MIME_AUDIO_MPEG.equals(getMimeType());
    }

    public String getAsString() throws IOException {
        return getAsString("UTF-8");
    }
    public String getAsString(String charset) throws IOException{
        int size=(int)getContentLength();
        if(size!=getContentLength()){
            throw new IndexOutOfBoundsException("content length("+getContentLength()+") gather than int max value");
        }
        ByteArrayOutputStream bos=new ByteArrayOutputStream(size);
        byte[] buf=new byte[4096];
        int len=0;
        while((len=inputStream.read(buf))>0){
            bos.write(buf,0,len);
        }
        inputStream.close();
        bos.close();

        return new String(bos.toByteArray(),charset);
    }
}
