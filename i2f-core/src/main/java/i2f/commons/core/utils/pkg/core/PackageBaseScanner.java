package i2f.commons.core.utils.pkg.core;


import i2f.commons.core.utils.pkg.data.ClassMetaData;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * @author ltb
 * @date 2021/9/23
 */
public class PackageBaseScanner {
    public static final int CLASS_NAMES_COUNT = 1024*10;

    public static ClassLoader getLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static URL getResource(String name) {
        return getLoader().getResource(name);
    }

    public static List<ClassMetaData> scanClassNamesBaseClasses(Class ... classes){
        if(classes==null || classes.length==0){
            return scanAllClassNames();
        }
        String[] packages=new String[classes.length];
        for(int i=0;i<classes.length;i++){
            String item=classes[i].getName();
            int idx=item.lastIndexOf(".");
            if(idx>=0){
                item=item.substring(0,idx);
            }
            packages[i]=item;
        }
        return scanClassNamesBasePackages(packages);
    }
    public static List<ClassMetaData> scanClassNamesBasePackages(String ... basePackages){
        if(basePackages==null || basePackages.length==0){
            return scanAllClassNames();
        }
        List<String> items=getShortlyPrefixes(basePackages);
        Map<URL,String> urls=new HashMap<>(64);
        for(String pkg : items){
            String item=pkg.replaceAll("\\.","/");
            try{
                Enumeration<URL> urlEnum= getLoader().getResources(item);
                while(urlEnum.hasMoreElements()){
                    URL url=urlEnum.nextElement();
                    urls.put(url,pkg);
                }
            }catch(Exception e){

            }
        }

        List<ClassMetaData> list=new ArrayList<>(CLASS_NAMES_COUNT);
        for(Map.Entry<URL,String> item : urls.entrySet()){
            scanAllClassNamesByURL(item.getKey(),item.getValue(),list);
        }
        return list;
    }

    public static List<ClassMetaData> scanAllClassNamesByURL(URL url, String pkg, List<ClassMetaData> list){
        String protocol = url.getProtocol().toLowerCase();
        if ("file".equals(protocol)) {
            File file = new File(url.getFile());
            scanAllInPath(file, pkg, list);
        } else if ("jar".equals(protocol)) {
            String ufile = url.getFile();
            String fileName = ufile.substring("file:/".length());
            int idx = fileName.indexOf("!");
            if (idx < 0) {
                idx = fileName.length();
            }
            String jarFile = fileName.substring(0, idx);
            File file = new File(jarFile);
            scanAllInJar(file, null, list);
        }
        return list;
    }
    public static List<ClassMetaData> scanAllClassNames() {
        List<ClassMetaData> list = new ArrayList<>(CLASS_NAMES_COUNT);
        URL url = getResource("");

        scanAllClassNamesByURL(url,null,list);

        return list;
    }

    public static List<ClassMetaData> scanAllInPath(File path, String pkg, List<ClassMetaData> list) {
        if (path.isFile()) {
            processFile(path, pkg, list);
            return list;
        }
        File[] allFiles = path.listFiles();
        for (File item : allFiles) {
            if (item.isDirectory()) {
                String fileName = item.getName();
                String className = getClassName(fileName);
                String npkg = null;
                if (pkg == null) {
                    npkg = className;
                } else {
                    npkg = pkg + "." + className;
                }
                scanAllInPath(item, npkg, list);
            } else if (item.isFile()) {
                processFile(item, pkg, list);
            }
        }
        return list;
    }

    public static List<ClassMetaData> processFile(File file, String pkg, List<ClassMetaData> list) {
        String fileName = file.getName();
        String className = getClassName(fileName);
        String npkg = null;
        if (pkg == null) {
            npkg = className;
        } else {
            npkg = pkg + "." + className;
        }
        if (isJarFile(fileName)) {
            scanAllInJar(file, null, list);
        } else if (isClassFile(fileName)) {
            ClassMetaData data = new ClassMetaData();
            data.setClassName(npkg);
            data.setLocation(file.getAbsolutePath());
            data.setClazz(loadClassByName(data.getClassName()));
            list.add(data);
        }
        return list;
    }

    public static Class loadClassByName(String className) {
        Class clazz = null;
        try {
            /**
             * count:10000
             * null:635
             * succss:9365
             */

            //clazz = Class.forName(className);
            /**
             * count:10000
             * null:331
             * succss:9669
             */
            clazz=getLoader().loadClass(className);
        } catch (Throwable e) {

        }
        return clazz;
    }

    public static List<ClassMetaData> scanAllInJar(File jar, String pkg, List<ClassMetaData> list) {
        if (!isJarFile(jar.getName())) {
            scanAllInPath(jar, pkg, list);
            return list;
        }

        if (!jar.exists() || !jar.isFile()) {
            return list;
        }

        File tmpDir=new File(System.getProperty("java.io.tmpdir"));
        tmpDir=new File(tmpDir,UUID.randomUUID().toString());


        try {
            JarFile jarFile = new JarFile(jar);
            JarInputStream jis = new JarInputStream(new BufferedInputStream(new FileInputStream(jar)));

            JarEntry entry = jis.getNextJarEntry();
            while (entry != null) {
                String name = entry.getName();

                String className = getClassName(name);
                className = className.replaceAll("\\/", ".");
                className = trimClasses(className);

                if (isClassFile(name)) {
                    ClassMetaData data = new ClassMetaData();
                    data.setClassName(className);
                    data.setLocation(jar.getAbsolutePath() + "/!" + name);
                    data.setClazz(loadClassByName(data.getClassName()));
                    list.add(data);
                } else if (isJarFile(name)) {
                    String pfile = jar.getAbsolutePath() + "!" + name;
                    pfile = pfile.replaceAll("\\\\", "/");
                    URL url = new URL("jar", "", -1, pfile);
                    File file = new File(url.getFile());

                    File tfile=new File(tmpDir, name);
                    if(!tfile.getParentFile().exists()){
                        tfile.getParentFile().mkdirs();
                    }
                    unpackJar(jarFile,entry,tfile);
                    scanAllInJar(tfile,null,list);
                    tfile.delete();
                }
                entry = jis.getNextJarEntry();
            }
            jis.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        tmpDir.delete();

        return list;
    }

    // 解压 jar 包;
    public static void unpackJar(JarFile jarFile, JarEntry entry, File file) throws IOException {
        try (InputStream is = jarFile.getInputStream(entry)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.flush();
            }
        }
    }


    public static String trimClasses(String className) {
        int idx = className.lastIndexOf("classes.");
        if (idx >= 0) {
            return className.substring(idx + "classes.".length());
        }
        return className;
    }

    public static String getClassName(String fileName) {
        int idx = fileName.lastIndexOf(".");
        if (idx >= 0) {
            return fileName.substring(0, idx);
        }
        return fileName;
    }

    public static boolean isJarFile(String fileName) {
        String suffix = getSuffix(fileName).toLowerCase();
        if (".jar".equals(suffix)) {
            return true;
        }
        return false;
    }

    public static boolean isClassFile(String fileName) {
        String suffix = getSuffix(fileName).toLowerCase();
        if (".class".equals(suffix)) {
            return true;
        }
        return false;
    }

    public static String getSuffix(String fileName) {
        int idx = fileName.lastIndexOf(".");
        if (idx >= 0) {
            String suffix = fileName.substring(idx);
            return suffix;
        }
        return "";
    }

    // 取多个包名的最短前缀包：
    // 输入： com com.i2f org org.cglib
    // 输出： com org
    public static List<String> getShortlyPrefixes(String ... pfxes) {
        return getShortlyCommonPrefixes(".","\\.",pfxes);
    }
    public static List<String> getShortlyCommonPrefixes(String separator,String separatorRegex,String ... pfxes) {
        Set<String> set=new HashSet<>(36);
        for(String item : pfxes){
            set.add(item);
        }
        String[] arr=new String[set.size()];
        int p=0;
        for(String item : set){
            arr[p++]=item;
        }
        List<String> ret=new ArrayList<>(arr.length);
        List<String[]> partsList=new ArrayList<>(arr.length);
        Set<Integer> lens=new TreeSet<>();
        int maxStrLen=0;
        for(String item : arr){
            String[] parts=item.split(separatorRegex);
            partsList.add(parts);
            if(item.length()>maxStrLen){
                maxStrLen=item.length();
            }
            lens.add(parts.length);
        }
        StringBuilder builder=new StringBuilder(maxStrLen+8);
        Set<Integer> excludeIdx = new TreeSet<>();
        Iterator<Integer> it=lens.iterator();
        while(it.hasNext()){
            int len=it.next();

            for(int i=0;i<arr.length;i++){
                String[] item= partsList.get(i);
                if(item.length<len){
                    continue;
                }
                if(excludeIdx.contains(i)){
                    continue;
                }
                builder.setLength(0);
                for(int j=0;j<len;j++){
                    if(j!=0){
                        builder.append(separator);
                    }
                    builder.append(item[j]);
                }
                String prefix=builder.toString();

                if(!set.contains(prefix)){
                    continue;
                }
                for(int j=0;j<arr.length;j++){
                    if(excludeIdx.contains(j)){
                        continue;
                    }
                    if(arr[j].equals(prefix)){
                        continue;
                    }
                    String pitem=arr[j];
                    if(pitem.length()>prefix.length()){
                        if(pitem.startsWith(prefix+separator)){
                            excludeIdx.add(j);
                        }
                    }else{
                        if(pitem.startsWith(prefix)){
                            excludeIdx.add(j);
                        }
                    }


                }
            }
        }

        for(int i=0;i<arr.length;i++){
            if(!excludeIdx.contains(i)){
                ret.add(arr[i]);
            }
        }

        return ret;
    }

}
