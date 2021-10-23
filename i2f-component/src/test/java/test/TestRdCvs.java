package test;

import i2f.commons.core.utils.file.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/10/18
 */
public class TestRdCvs {
    public static void main(String[] args) throws IOException {
        List<Map<String,String>> data= FileUtil.readCvsFile(new File("D:\\01test\\test.cvs"),",",false, Charset.forName("GBK"));

    }
}
