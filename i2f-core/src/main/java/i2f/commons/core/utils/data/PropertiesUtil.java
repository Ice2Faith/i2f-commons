package i2f.commons.core.utils.data;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
    public static Properties load(InputStream is) throws IOException {
        Properties properties=new Properties();
        properties.load(is);
        is.close();
        return properties;
    }
    public static Properties load(File file) throws IOException {
        FileInputStream fis=new FileInputStream(file);
        return load(fis);
    }
    public static Properties loadResource(String fileName) throws IOException {
        InputStream is=PropertiesUtil.class.getResourceAsStream(fileName);
        return load(is);
    }
    public static void save(Properties properties,OutputStream os) throws IOException {
        properties.save(os,"nop");
        os.close();
    }
    public static void save(Map<String,String> map,OutputStream os) throws IOException {
        Properties properties=new Properties();
        for(String key : map.keySet()){
            properties.setProperty(key,map.get(key));
        }
        save(properties,os);
    }
    public static void save(Map<String,String> map,File file) throws IOException {
        FileOutputStream fos=new FileOutputStream(file);
        save(map,fos);
    }
}
