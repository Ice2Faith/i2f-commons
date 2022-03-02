package i2f.log.api.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author ltb
 * @date 2022/3/2 16:16
 * @desc
 */
public class FileLogWriterImpl extends AbsPrintStreamLogWriterImpl {
    private String file;
    public FileLogWriterImpl(String file){
        this.file=file;
    }
    @Override
    protected void writeLine(String str) {
        try{
            File logFile=new File(file);
            if(!logFile.getParentFile().exists()){
                logFile.getParentFile().mkdirs();
            }
            OutputStream os=new FileOutputStream(logFile,true);
            os.write(str.getBytes());
            os.write("\n".getBytes());
            os.flush();
            os.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
