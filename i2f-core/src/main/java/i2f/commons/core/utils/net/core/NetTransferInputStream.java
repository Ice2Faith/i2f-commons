package i2f.commons.core.utils.net.core;

import java.io.*;

/**
 * @author ltb
 * @date 2022/2/20 15:38
 * @desc
 */
public class NetTransferInputStream extends FileInputStream {
    private String name;

    public NetTransferInputStream(String name) throws FileNotFoundException {
        super(name);
        this.name=name;
    }

    public NetTransferInputStream(File file) throws FileNotFoundException {
        super(file);
        this.name=file.getAbsolutePath();
    }

    public NetTransferInputStream(FileDescriptor fdObj) {
        super(fdObj);
        this.name=null;
    }

    @Override
    public void close() throws IOException {
        super.close();
        if(this.name!=null){
            File file = new File(name);
            file.delete();
        }
    }
}
