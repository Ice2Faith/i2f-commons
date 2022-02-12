package i2f.commons.core.utils.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/1/18 13:54
 * @desc
 */
public class ResourceFinder {
    public static ClassLoader getLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    public static URL[] findResources(String location) throws IOException {
        if(location==null){
            return new URL[0];
        }
        String lloc=location.toLowerCase();
        if(lloc.startsWith("classpath:")){
            lloc = location.substring("classpath:".length());
            Enumeration<URL> enums=getLoader().getResources(lloc);
            Set<URL> set=new HashSet<>();
            while(enums.hasMoreElements()){
                URL url=enums.nextElement();
                set.add(url);
            }
            URL[] urls=new URL[set.size()];
            int i=0;
            for(URL url : set){
                urls[i]=url;
                i++;
            }
            return urls;
        }else{
            File file=new File(location);
            URL url=file.toURI().toURL();
            return new URL[]{url};
        }
    }

}
