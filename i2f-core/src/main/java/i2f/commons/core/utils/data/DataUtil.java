package i2f.commons.core.utils.data;

import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.AppendUtil;

import java.io.*;
import java.util.*;

public class DataUtil {
    public static byte[] objs2Bytes(Object ... objs) throws IOException {
        if(CheckUtil.isEmptyArray(objs)){
            return new byte[]{};
        }
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        for(Object item : objs){
            oos.writeObject(item);
        }
        oos.flush();
        oos.close();

        return bos.toByteArray();
    }

    public static<T extends Serializable> byte[] obj2Bytes(T obj) throws IOException {
        if(CheckUtil.isNull(obj)){
            return new byte[]{};
        }
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();

        return bos.toByteArray();
    }

    public static<T extends Serializable> T bytes2Obj(byte[] bytes) throws IOException, ClassNotFoundException {
        if(CheckUtil.isEmptyArray(bytes)){
            return null;
        }
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(is);
        T ret = (T) ois.readObject();
        ois.close();
        return ret;
    }

    public static <T extends Serializable> T serializeCopy(T obj) throws IOException, ClassNotFoundException {
        byte[] data = obj2Bytes(obj);
        T ret=bytes2Obj(data);
        return ret;
    }

    public static OutputStream streamCopy(InputStream is,OutputStream os,boolean autoClose) throws IOException {
        InputStream bis=new BufferedInputStream(is);
        OutputStream bos=new BufferedOutputStream(os);

        int len=0;
        byte[] buf=new byte[8192];
        while((len=bis.read(buf))>0){
            bos.write(buf,0,len);
        }

        bos.flush();

        if(autoClose){
            bis.close();
            bos.close();
        }

        return bos;
    }

    public static long streamCopySize(InputStream is,OutputStream os,long size,boolean autoCloseOs) throws IOException {
        return streamCopyRange(is,os,0,size,autoCloseOs);
    }

    public static long streamCopyRange(InputStream is,OutputStream os,long offset,long size,boolean autoCloseOs) throws IOException {
        if(offset>0){
            is.skip(offset);
        }
        long rdSize = 0;
        while (rdSize != size) {
            int bt = is.read();
            if (bt >= 0) {
                os.write(bt);
                rdSize++;
            } else {
                break;
            }
        }

        os.flush();

        if(autoCloseOs){
            os.close();
        }

        return rdSize;
    }

    public static <T> T[] arr(T ... objs){
        return (T[])objs;
    }

    public static <T> T[] toArr(Collection<T> col,Class<? extends T[]> tarType){
        if(CheckUtil.isEmptyCollection(col)){
            return null;
        }
        Object[] arr=new Object[col.size()];
        Iterator it=col.iterator();
        int ix=0;
        while (it.hasNext()){
            arr[ix]=it.next();
            ix++;
        }

        return (T[]) ArrayUtil.copyOf(arr,arr.length,tarType);
    }
    public static <T,E> T[] arrCopy(int index, int len, Class<? extends T[]> tarType, E ... srcArr){
        Object[] arr=new Object[len];
        int i=0;
        while(index+i<len){
            arr[i]=srcArr[index+i];
        }
        return (T[]) ArrayUtil.copyOf(arr,arr.length,tarType);
    }
    public static <T> T[] toArrKeys(Map<T,?> map,Class<? extends T[]> tarType){
        if(CheckUtil.isEmptyMap(map)){
            return null;
        }
        Object[] arr=new Object[map.size()];
        Iterator it=map.keySet().iterator();
        int ix=0;
        while (it.hasNext()){
            arr[ix]=it.next();
            ix++;
        }
        return (T[]) ArrayUtil.copyOf(arr,arr.length,tarType);
    }
    public static <T,E> T[] toArrVals(Map<?,T> map,Class<? extends E[]> tarType){
        if(CheckUtil.isEmptyMap(map)){
            return null;
        }
        Object[] arr=new Object[map.size()];
        Iterator it=map.keySet().iterator();
        int ix=0;
        while (it.hasNext()){
            arr[ix]=map.get(it.next());
            ix++;
        }
        return (T[]) ArrayUtil.copyOf(arr,arr.length,tarType);
    }
    public static<T> List<T> toList(T ... vals){
        return toListBase(new ArrayList<T>(),vals);
    }
    public static<T> List<T> toListBase(List<T> saver,T ... vals){
        if(saver==null){
            return saver;
        }
        if(CheckUtil.isEmptyArray(vals)){
            return saver;
        }
        for(T item : vals){
            saver.add(item);
        }
        return saver;
    }
    public static<T> List<T> toList(Collection<T> collection){
        return toListBase(new ArrayList<T>(),collection);
    }
    public static<T> List<T> toListBase(List<T> saver,Collection<T> collection){
        if(saver==null){
            return saver;
        }
        if(CheckUtil.isEmptyCollection(collection)){
            return saver;
        }
        for(T item : collection){
            saver.add(item);
        }
        return saver;
    }

    public static<T> Set<T> toSet(Collection<T> collection){
        if(collection instanceof Set){
            return (Set<T>)collection;
        }
        Set<T> ret=new HashSet<>();
        for(T item : collection){
            ret.add(item);
        }
        return ret;
    }

    public static<T> List<T> uniqueList(Collection<T> col){
        int colSize=col.size();
        List<T> ret=new ArrayList<>(colSize);
        Set<T> uni=new HashSet<>((int)(colSize/0.7));
        for(T item : col){
            if(!uni.contains(item)){
                uni.add(item);
                ret.add(item);
            }
        }

        return ret;
    }

    public static<T> Set<T> uniqueMerge(T[] arr1,T[] arr2){
        Set<T> ret=new HashSet<>((int)((arr1.length+ arr1.length)/0.7));
        for(T item : arr1){
            ret.add(item);
        }
        for(T item : arr2){
            ret.add(item);
        }
        return ret;
    }
    public static <T> Set<T> uniqueMerge(Iterable<T> it1,Iterable<T> it2){
        Set<T> ret=new HashSet<>(64);
        merge(it1,it2,ret);
        return ret;
    }

    public static<T> Collection<T> merge(Iterable<T> it1,Iterable<T> it2,Collection<T> save){
        Iterator<T> ito1= it1.iterator();
        while(ito1.hasNext()){
            save.add(ito1.next());
        }
        Iterator<T> ito2=it2.iterator();
        while(ito2.hasNext()){
            save.add(ito2.next());
        }
        return save;
    }

    public static <T> T[] merge(T[] arr1,T[] arr2){
        int size=arr1.length+arr2.length;
        Object[] ret=new Object[size];
        int i=0;
        for(T item : arr1){
            ret[i++]=item;
        }
        for(T item : arr2){
            ret[i++]=item;
        }
        return (T[]) ArrayUtil.copyOf(ret,size);
    }

    public static String toHexString(byte[] data){
        return toHexString(data,null);
    }
    public static String toHexString(byte[] data,String separator){
        AppendUtil.AppendBuilder builder=AppendUtil.builder();
        for (int i = 0; i < data.length; i++) {
            if(i!=0){
                if(separator!=null){
                    builder.add(separator);
                }
            }
            String bhex=String.format("%02X",(int)(data[i]&0x0ff));
            builder.add(bhex);
        }
        return builder.done();
    }
    public static String toOtcString(byte[] data){
        return toOtcString(data,null);
    }
    public static String toOtcString(byte[] data,String separator){
        AppendUtil.AppendBuilder builder=AppendUtil.builder();
        for (int i = 0; i < data.length; i++) {
            if(i!=0){
                if(separator!=null){
                    builder.add(separator);
                }
            }
            String bhex=String.format("%03o",(int)(data[i]&0x0ff));
            builder.add(bhex);
        }
        return builder.done();
    }
    public static String toBinString(byte[] data){
        return toBinString(data,null);
    }
    public static String toBinString(byte[] data,String separator){
        AppendUtil.AppendBuilder builder=AppendUtil.builder();
        for (int i = 0; i < data.length; i++) {
            if(i!=0){
                if(separator!=null){
                    builder.add(separator);
                }
            }
            int bt=data[i]&0x0ff;
            for (int j = 7; j >= 0; j--) {
                int dt=(bt>>>j)&0x01;
                builder.add(dt);
            }
        }
        return builder.done();
    }
    public static String toDecString(byte[] data){
        return toDecString(data,null);
    }
    public static String toDecString(byte[] data,String separator){
        AppendUtil.AppendBuilder builder=AppendUtil.builder();
        for (int i = 0; i < data.length; i++) {
            if(i!=0){
                if(separator!=null){
                    builder.add(separator);
                }
            }
            String bhex=String.format("%03d",(int)(data[i]&0x0ff));
            builder.add(bhex);

        }
        return builder.done();
    }
    public static byte[] parseHexString(String data){
        return parseHexString(data,null);
    }
    public static byte[] parseHexString(String data,String separator){
        List<String> parts=new ArrayList<>();
        if(separator!=null){
            String[] arr=data.split(separator);
            for(String item : arr){
                parts.add(item);
            }
        }else{
            int dlen=data.length();
            for (int i = 0; (i+2) <= dlen; i+=2) {
                String item=data.substring(i,i+2);
                parts.add(item);
            }
        }
        int size=parts.size();
        byte[] ret=new byte[size];
        for (int i = 0; i < size; i++) {
            try{
                int num=Integer.parseInt(parts.get(i),16);
                ret[i]=(byte)(num&0x0ff);
            }catch(Exception e){
                ret[i]=0;
            }
        }
        return ret;
    }
    public static byte[] parseOtcString(String data){
        return parseOtcString(data,null);
    }
    public static byte[] parseOtcString(String data,String separator){
        List<String> parts=new ArrayList<>();
        if(separator!=null){
            String[] arr=data.split(separator);
            for(String item : arr){
                parts.add(item);
            }
        }else{
            int dlen=data.length();
            for (int i = 0; (i+3) <= dlen; i+=3) {
                String item=data.substring(i,i+3);
                parts.add(item);
            }
        }
        int size=parts.size();
        byte[] ret=new byte[size];
        for (int i = 0; i < size; i++) {
            try{
                int num=Integer.parseInt(parts.get(i),8);
                ret[i]=(byte)(num&0x0ff);
            }catch(Exception e){
                ret[i]=0;
            }
        }
        return ret;
    }
    public static byte[] parseDecString(String data){
        return parseDecString(data,null);
    }
    public static byte[] parseDecString(String data,String separator){
        List<String> parts=new ArrayList<>();
        if(separator!=null){
            String[] arr=data.split(separator);
            for(String item : arr){
                parts.add(item);
            }
        }else{
            int dlen=data.length();
            for (int i = 0; (i+3) <= dlen; i+=3) {
                String item=data.substring(i,i+3);
                parts.add(item);
            }
        }
        int size=parts.size();
        byte[] ret=new byte[size];
        for (int i = 0; i < size; i++) {
            try{
                int num=Integer.parseInt(parts.get(i),10);
                ret[i]=(byte)(num&0x0ff);
            }catch(Exception e){
                ret[i]=0;
            }
        }
        return ret;
    }
    public static byte[] parseBinString(String data){
        return parseBinString(data,null);
    }
    public static byte[] parseBinString(String data,String separator){
        List<String> parts=new ArrayList<>();
        if(separator!=null){
            String[] arr=data.split(separator);
            for(String item : arr){
                parts.add(item);
            }
        }else{
            int dlen=data.length();
            for (int i = 0; (i+8) <= dlen; i+=8) {
                String item=data.substring(i,i+8);
                parts.add(item);
            }
        }
        int size=parts.size();
        byte[] ret=new byte[size];
        for (int i = 0; i < size; i++) {
            int num=0;
            String item=parts.get(i);
            if(item!=null){
                char[] arr=item.toCharArray();
                for (int j = 0; j < arr.length; j++) {
                    if(arr[j]=='1'){
                        num=(num<<1)|1;
                    }else if(arr[j]=='0'){
                        num=(num<<1)|0;
                    }
                }
            }
            ret[i]=(byte)(num&0x0ff);
        }
        return ret;
    }

    public static void convertByteStream(InputStream is, OutputStream os, IMap<Byte,Byte> mapper) throws IOException {
        BufferedInputStream bis=new BufferedInputStream(is);
        BufferedOutputStream bos=new BufferedOutputStream(os);
        int bt=-1;
        while((bt=bis.read())>=0){
            byte wbt= (byte)(bt&0x0ff);
            if(mapper!=null){
                wbt=mapper.map(wbt);
            }
            bos.write(wbt);
        }
        bos.flush();
    }

    public static String hideSensibleInfo(String str,int keepStartLen) {
        return hideSensibleInfo(str,keepStartLen,0,-1);
    }

    public static String hideSensibleInfo(String str,int keepStartLen,int keepEndLen) {
        return hideSensibleInfo(str,keepStartLen,keepEndLen,-1);
    }

    public static String hideSensibleInfo(String str,int keepStartLen,int keepEndLen,int maxLen){
        if(CheckUtil.isEmptyStr(str,false)){
            return str;
        }
        String start=str.substring(0,keepStartLen);
        String end=str.substring(str.length()-keepEndLen);
        String mid=str.substring(keepStartLen,str.length()-keepEndLen);
        if(maxLen>=0){
            int llen=maxLen-keepStartLen-keepEndLen;
            if(llen>=0){
                mid=mid.substring(0,llen);
            }
        }
        int mlen=mid.length();
        StringBuilder builder=new StringBuilder(str.length());
        builder.append(start);
        for (int i = 0; i < mlen; i++) {
            builder.append("*");
        }
        builder.append(end);
        return builder.toString();
    }
}
