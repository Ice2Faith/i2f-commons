package i2f.commons.core.utils.net.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/2/20 15:43
 * @desc
 */
public class TestNetTransfer {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        NetTransfer.send("hello","text/plain",bos);

        ByteArrayInputStream bis=new ByteArrayInputStream(bos.toByteArray());
        NetTransferResponse response=NetTransfer.recv(bis);

        String data = response.getAsString();
    }
}
