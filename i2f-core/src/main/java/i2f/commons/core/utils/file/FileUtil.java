package i2f.commons.core.utils.file;


import i2f.commons.core.data.interfaces.IFilter;
import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.data.DataUtil;
import i2f.commons.core.utils.file.core.FileMime;
import i2f.commons.core.utils.file.core.FileSpecies;
import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;
import i2f.commons.core.utils.str.StringUtil;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class FileUtil {
    public static String getSpecies(String fileName) {
        return FileSpecies.getSpecieName(fileName);
    }

    public static String getMimeType(String fileName) {
        return FileMime.getMimeType(fileName);
    }

    public static File useParentDir(File file) {
        File parent = file.getParentFile();
        return useDir(parent);
    }

    public static File useDir(File path) {
        if (!path.exists()) {
            path.mkdirs();
        }
        return path;
    }

    /**
     * 获取可由多级路径联合的可写文件对象，如果多级路径不存在，则自动建立
     * @param path 至少一级路径
     * @param subPaths 可多加后续路径
     * @return
     */
    public static File getWritableFile(String path,String ... subPaths){
        File file=new File(path);
        if(subPaths!=null && subPaths.length>0){
            for(String item : subPaths){
                file=new File(file,item);
            }
        }
        File parent=file.getParentFile();
        if(parent!=null && !parent.exists()){
            parent.mkdirs();
        }
        return file;
    }

    public static File getFile(String path, String subPath) {
        return new File(StringUtil.pathGen(path, subPath));
    }

    public static File getFile(File path, String subPath) {
        return getFile(path.getAbsolutePath(), subPath);
    }

    public static String getNameSuffix(String fileName) {
        return new File(fileName).getName();
    }

    public static String getNameNoSuffix(String fileName) {
        return StringUtil.trimExtension(getNameSuffix(fileName));
    }

    public static String getNameNoSuffix(File file) {
        return getNameNoSuffix(file.getName());
    }

    public static void merge(File dstFile, boolean withMeta, File[] srcFiles, IFileFilter filter) throws IOException {
        if (CheckUtil.isEmptyArray(srcFiles)) {
            return;
        }
        OutputStream os = mergeNext(dstFile, null, withMeta, "", srcFiles, filter);
        if (CheckUtil.notNull(os)) {
            os.flush();
            os.close();
        }
    }

    private static OutputStream mergeNext(File dstFile, OutputStream os, boolean withMeta, String rootPath, File[] files, IFileFilter filter) throws IOException {
        for (File item : files) {
            if (!item.exists()) {
                continue;
            }
            String fileName = AppendUtil.strSep("/", rootPath, item.getName());
            if (item.isFile()) {
                if (checkWhenFilter(item, filter)) {
                    if (CheckUtil.isNull(os)) {
                        useParentDir(dstFile);
                        os = new FileOutputStream(dstFile);
                    }
                    if (withMeta) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);
                        byte[] fnbts = fileName.getBytes("UTF-8");
                        int fnlen = fnbts.length;
                        long fsize = item.length();
                        oos.writeInt(fnlen);
                        oos.write(fnbts);
                        oos.writeLong(fsize);
                        oos.flush();
                        oos.close();
                        os.write(bos.toByteArray());
                    }
                    InputStream is = new FileInputStream(item);
                    DataUtil.streamCopy(is, os, false);
                    is.close();
                }
            } else if (item.isDirectory()) {
                File[] childFiles = item.listFiles();
                os = mergeNext(dstFile, os, withMeta, fileName, childFiles, filter);
            }
        }
        return os;
    }

    public static void splitMeta(File dstPath, File metaFile, IFileFilter filter) throws IOException {
        if (!metaFile.exists()) {
            return;
        }
        if (!metaFile.isFile()) {
            return;
        }
        useDir(dstPath);
        InputStream is = new BufferedInputStream(new FileInputStream(metaFile));


        while (true) {
            ObjectInputStream ois = null;
            int fnlen = 0;
            try {
                ois = new ObjectInputStream(is);
                fnlen = ois.readInt();
            } catch (EOFException e) {
                is.close();
                return;
            }
            int rdlen = 0;
            byte[] fnbts = new byte[fnlen];
            rdlen = ois.read(fnbts);
            if (rdlen != fnlen) {
                ois.close();
                return;
            }
            String fileName = new String(fnbts, "UTF-8");
            long fsize = ois.readLong();
            if (fsize < 0) {
                ois.close();
                return;
            }

            File preFile = new File(fileName);
            if (checkWhenFilter(preFile, filter)) {
                File dstFile = new File(dstPath, fileName);
                useParentDir(dstFile);
                OutputStream os = new BufferedOutputStream(new FileOutputStream(dstFile));
                long rdSize = DataUtil.streamCopySize(is, os, fsize, true);
                if (rdSize != fsize) {
                    is.close();
                    return;
                }
            }
        }

    }

    public static void load(File srcFile, OutputStream os) throws IOException {
        if (CheckUtil.isExNull(srcFile, os)) {
            return;
        }
        InputStream is = new FileInputStream(srcFile);
        DataUtil.streamCopy(is, os, true);
    }

    public static void save(InputStream is, File dstFile) throws IOException {
        if (CheckUtil.isExNull(is, dstFile)) {
            return;
        }
        useParentDir(dstFile);
        OutputStream os = new FileOutputStream(dstFile);
        DataUtil.streamCopy(is, os, true);
    }

    public static void copy(File dstFile, File srcFile, IFileFilter filter) throws IOException {
        if (CheckUtil.isExNull(srcFile, dstFile)) {
            return;
        }
        if (!srcFile.exists()) {
            return;
        }
        if (srcFile.isFile()) {
            if (checkWhenFilter(srcFile, filter)) {
                useParentDir(dstFile);
                InputStream is = new FileInputStream(srcFile);
                OutputStream os = new FileOutputStream(dstFile);
                DataUtil.streamCopy(is, os, true);
            }
        } else if (srcFile.isDirectory()) {
            File[] files = srcFile.listFiles();
            for (File item : files) {
                File newFile = new File(dstFile, item.getName());
                copy(newFile, item, filter);
            }
        }
    }

    public static void delete(File file, IFileFilter filter) {
        if (CheckUtil.isNull(file)) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            if (checkWhenFilter(file, filter)) {
                file.delete();
            }
        }
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            for (File item : subFiles) {
                if (item.isFile()) {
                    if (checkWhenFilter(item, filter)) {
                        item.delete();
                    }
                } else if (item.isDirectory()) {
                    delete(item, filter);
                }
            }
            if (checkWhenFilter(file, filter)) {
                file.delete();
            }
        }
    }

    public static List<File> tree(File file, int level, IFileFilter filter) {
        List<File> ret = new ArrayList<>();
        if (CheckUtil.isNull(file) || level == 0) {
            return ret;
        }
        if (!file.exists()) {
            return ret;
        }
        if (file.isFile()) {
            addFileItem2List(ret, file, filter);
            return ret;
        }
        if (file.isDirectory()) {
            addFileItem2List(ret, file, filter);

            File[] subFiles = file.listFiles();
            for (File item : subFiles) {
                addFileItem2List(ret, item, filter);
                if (item.isDirectory()) {
                    List<File> subList = tree(item, level - 1, filter);
                    for (File subItem : subList) {
                        addFileItem2List(ret, subItem, filter);
                    }
                }
            }

        }
        return ret;
    }

    private static boolean checkWhenFilter(File file, IFileFilter filter) {
        if (CheckUtil.notNull(filter)) {
            if (!filter.save(file)) {
                return false;
            }
        }
        return true;
    }

    private static void addFileItem2List(List<File> list, File file, IFileFilter filter) {
        if (checkWhenFilter(file, filter)) {
            list.add(file);
        }
    }

    public static void save(String content, File dstFile) throws IOException {
        save(content, "UTF-8", dstFile);
    }

    public static void save(String content, String charset, File dstFile) throws IOException {
        byte[] data = content.getBytes(charset);
        save(data, 0, data.length, dstFile);
    }

    public static void save(byte[] data, File dstFile) throws IOException {
        save(data, 0, data.length, dstFile);
    }

    public static void save(byte[] data, int offset, int len, File dstFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(dstFile);
        fos.write(data, offset, len);
        fos.close();
    }

    public static String loadTxtFile(File file,Charset charset) throws IOException {
        StringBuilder builder=new StringBuilder();
        InputStream is=new FileInputStream(file);
        InputStreamReader reader=new InputStreamReader(is,charset);
        char[] cbuf=new char[2048];
        int len=0;
        while((len=reader.read(cbuf))>0){
            builder.append(cbuf,0,len);
        }
        reader.close();
        return builder.toString();
    }
    public static String loadTxtFile(File file) throws IOException {
        Charset charset=Charset.forName("UTF-8");
        return loadTxtFile(file,charset);
    }
    public static List<String> readTxtFile(File file) throws IOException {
        return readTxtFile(file, 0, -1, false, false);
    }

    public static List<String> readTxtFile(File file, boolean trimLine, boolean ignoreBlankLine) throws IOException {
        return readTxtFile(file, 0, -1, ignoreBlankLine, trimLine);
    }

    public static List<String> readTxtFile(File file, int offsetLine, int lineCount, boolean ignoreBlankLine, boolean trimLine) throws IOException {
        return readTxtFile(file,offsetLine,lineCount,ignoreBlankLine,trimLine,null,null);
    }

    public static List<String> readTxtFile(File file, int offsetLine, int lineCount, boolean ignoreBlankLine, boolean trimLine, IFilter<String> filter, IMap<String, String> map) throws IOException {
        InputStream is = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        if (offsetLine < 0) {
            offsetLine = 0;
        }
        List<String> ret = new LinkedList<>();
        String line = "";
        int curLine = 0;
        while (curLine < offsetLine) {
            line = reader.readLine();
            if (line == null) {
                return ret;
            }
            curLine++;
        }
        int curCount = 0;
        while (true) {
            boolean save = false;
            if (lineCount < 0) {
                save = true;
            }
            if (curCount < lineCount) {
                save = true;
            }
            line = reader.readLine();
            if (line == null) {
                break;
            }
            if (!save) {
                continue;
            }
            if (trimLine) {
                line = line.trim();
            }
            if (ignoreBlankLine && "".equals(line)) {
                continue;
            }

            if (filter != null) {
                if (!filter.choice(line)) {
                    continue;
                }
            }
            if (map != null) {
                line = map.map(line);
            }
            ret.add(line);
            curCount++;
        }

        reader.close();
        return ret;
    }

    public static<T> List<T> readCvsFile(File file, String sepRegex, boolean hasHeadLine, Charset charset,IMap<Map<String,String>,T> mapper) throws IOException {
        List<Map<String,String>> data=readCvsFile(file, sepRegex, hasHeadLine, charset);
        List<T> ret=new LinkedList<>();
        for(Map<String,String> item : data){
            T val= mapper.map(item);
            ret.add(val);
        }
        return ret;
    }

    public static List<Map<String,String>> readCvsFile(File file, String sepRegex, boolean hasHeadLine, Charset charset) throws IOException {
        List<Map<String,String>> ret=new LinkedList<>();
        BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),charset));
        String[] heads=new String[0];
        String line="";
        boolean isFirst=true;
        while((line=reader.readLine())!=null){
            String[] elems=line.split(sepRegex);
            if(isFirst){
                isFirst=false;
                if(hasHeadLine){
                    heads=elems;

                    continue;
                }else{
                    heads=new String[elems.length];
                    for (int i = 0; i < elems.length; i++) {
                        heads[i]=String.valueOf(i);
                    }
                }
            }
            Map<String,String> record=new HashMap<>((int)(heads.length/0.7));
            for (int i = 0; i < heads.length; i++) {
                record.put(heads[i],elems[i]);
            }
            ret.add(record);

        }
        reader.close();
        return ret;
    }

    public static File convertByteStream(File inFile,File outFile,IMap<Byte,Byte> mapper) throws IOException {
        InputStream is=new FileInputStream(inFile);
        File uoutFile=useParentDir(outFile);
        OutputStream os=new FileOutputStream(uoutFile);
        DataUtil.convertByteStream(is,os,mapper);
        is.close();
        os.close();
        return uoutFile;
    }

    /**
     * 支持classpath写法
     * @param fileName
     * @return
     */
    public static File getFileWithClasspath(String fileName){
        if(fileName==null){
            return null;
        }
        fileName=fileName.trim();
        if(fileName.startsWith("classpath:")){
            String fname=fileName.substring("classpath:".length());
            URL url=Thread.currentThread().getContextClassLoader().getResource(fname);
            return new File(url.getFile());
        }else{
            return new File(fileName);
        }
    }
}
