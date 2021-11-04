package i2f.commons.core.utils.str;


import i2f.commons.core.data.Pair;
import i2f.commons.core.utils.data.DataUtil;
import i2f.commons.core.utils.safe.CheckUtil;
import i2f.commons.core.utils.str.data.RegexFindPartMeta;
import i2f.commons.core.utils.str.data.RegexMatchItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String subMore(String str,int maxLen){
        return subMore(str,maxLen,"...");
    }
    public static String subMore(String str,int maxLen,String suffix){
        String lstr=str;
        if(str.length()>maxLen){
            lstr=str.substring(0,maxLen-1);
        }
        if(suffix!=null){
            return AppendUtil.str(lstr,suffix);
        }
        return lstr;
    }
    public static String pathGen(String path){
        String orginalPathSep="/";
        int iwdx=path.indexOf("\\");
        int iudx=path.indexOf("/");
        if(iwdx>=0 && iudx>=0){
            if(iwdx>iudx){
                orginalPathSep="\\";
            }else{
                orginalPathSep="/";
            }
        }else{
            if(iwdx>=0){
                orginalPathSep="\\";
            }
            if(iudx>=0){
                orginalPathSep="/";
            }
        }
        String[] pathArr=new String[]{};
        if("/".equals(orginalPathSep)){
            path=path.replaceAll("\\\\","/");
            pathArr=path.split("\\/");
        }else{
            path=path.replaceAll("\\/","\\");
            pathArr=path.split("\\\\");
        }
        Vector<String> vector=new Vector<>();
        pathRoute(pathArr, vector);
        return AppendUtil.buffer().addCollection(false,orginalPathSep,null,null,vector).done();
    }
    public static String pathGen(String basePath,String relativePath){
        String orginalPathSep="/";

        String workPathSep="/";
        String workPathSepRegex="\\/";
        if(basePath.indexOf("\\")>=0){
            orginalPathSep="\\";
            basePath=basePath.replaceAll("\\\\",workPathSep);
        }
        if(relativePath.indexOf("\\")>=0){
            relativePath=relativePath.replaceAll("\\\\",workPathSep);
        }
        String[] baseArr=basePath.split(workPathSepRegex);
        String[] relativeArr=relativePath.split(workPathSepRegex);

        Vector<String> vector=new Vector<>();
        for(String item : baseArr){
            if(CheckUtil.isEmptyStr(item,false)){
                continue;
            }
            vector.add(item);
        }
        pathRoute(relativeArr, vector);
        return AppendUtil.buffer().addCollection(false,orginalPathSep,null,null,vector).done();
    }

    private static void pathRoute(String[] relativeArr, Vector<String> vector) {
        for (String item : relativeArr) {
            if (CheckUtil.isEmptyStr(item, false)) {
                continue;
            }
            if (".".equals(item)) {
                continue;
            }
            if ("..".equals(item)) {
                if (vector.size() > 0) {
                    vector.remove(vector.size() - 1);
                }
            } else {
                vector.add(item);
            }
        }
    }

    public static String trimExtension(String str){
        return trimExtension(str,".");
    }
    public static String trimExtension(String str,String extensionFlag){
        if(CheckUtil.isNull(str)){
            return str;
        }
        String ret=str;
        int exIndex=str.lastIndexOf(extensionFlag);
        if(exIndex>=0){
            ret=str.substring(0,exIndex);
        }
        return ret;
    }
    public static String getExtension(String str){
        return getExtension(str,".");
    }
    public static String getExtension(String str,String extensionFlag){
        if(CheckUtil.isNull(str)){
            return str;
        }
        int exIndex=str.lastIndexOf(extensionFlag);
        if(exIndex>=0){
            return str.substring(exIndex);
        }
        return "";
    }
    public static String firstUpperCase(String str){
        if(CheckUtil.isEmptyStr(str,false)){
            return str;
        }
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }
    public static String firstLowerCase(String str){
        if(CheckUtil.isEmptyStr(str,false)){
            return str;
        }
        return str.substring(0,1).toLowerCase()+str.substring(1);
    }
    public static String[] splitBySpace(String str){
        return split(str,true,"\\s+",-1,false);
    }
    public static String[] split(String str,String regex,boolean removeEmpty){
        return split(str,false,regex,-1,removeEmpty);
    }
    public static String[] split(String str,boolean trimBefore,String regex,int limit,boolean removeEmpty){
        String[] ret=new String[]{};
        if(str==null){
            return ret;
        }
        if(trimBefore){
            str=str.trim();
        }
        ret=str.split(regex,limit);
        Vector<String> result=new Vector<>();
        for(String item : ret){
            if(removeEmpty){
                if("".equals(item)){
                    continue;
                }
            }
            result.add(item);
        }
        return DataUtil.toArr(result,String[].class);
    }

    public static String path2ClassName(String path){
        if(path==null){
            return path;
        }
        path=trimExtension(path);
        path=path.replaceAll("\\\\","/");
        path=path.replaceAll("\\/",".");
        return path;
    }
    public static String className2Path(String className){
        if(className==null){
            return className;
        }
        className=className.replaceAll("\\.",File.separator);
        return className;
    }

    /**
     * 去除串 srcStr 中存在的首尾的子串 strs 其中一个 ，并且支持是否先trim之后再做去除
     * @param srcStr
     * @param needTrim
     * @param strs
     * @return
     */
    public static String trimStr(String srcStr,boolean needTrim,String ... strs){
        return trimFixStr(srcStr, needTrim,null,null, strs);
    }
    public static String trimFixStr(String srcStr,boolean needTrim,String prefix,String suffix,String ... strs){
        if(strs==null || strs.length==0){
            return srcStr;
        }
        if(needTrim){
            srcStr=srcStr.trim();
        }
        for(String item : strs){
            boolean isExist=false;
            if(srcStr.startsWith(item)){
                srcStr=srcStr.substring(item.length());
                isExist=true;
            }
            if(srcStr.endsWith(item)){
                srcStr=srcStr.substring(0,srcStr.length()-item.length());
                isExist=true;
            }
            if(isExist){
                break;
            }
        }

        if(prefix!=null){
            srcStr=prefix+srcStr;
        }
        if(suffix!=null){
            srcStr=srcStr+suffix;
        }

        return srcStr;
    }

    public static String combNotEmpty(boolean useSpace,String linker,String ... vals){
        if(useSpace){
            return combNotEmpty(" ",linker,vals);
        }else{
            return combNotEmpty(null,linker,vals);
        }
    }
    public static String combNotEmpty(String spliter,String linker,String ... vals){
        if(CheckUtil.isEmptyArray(vals)){
            return "";
        }
        if(CheckUtil.isNull(linker)){
            linker="";
        }
        StringBuffer buffer=new StringBuffer();
        int count=0;
        for(String item : vals){
            if(CheckUtil.isEmptyStr(item,false)){
                continue;
            }
            item=trimStr(item,false,linker);
            item=item.trim();
            if(CheckUtil.isEmptyStr(item,false)){
                continue;
            }
            if(count!=0){
                if(CheckUtil.notNull(spliter)){
                    buffer.append(spliter);
                }
                buffer.append(linker);
                if(CheckUtil.notNull(spliter)){
                    buffer.append(spliter);
                }
            }
            buffer.append(item);
            count++;
        }
        return buffer.toString();
    }

    public static String toPascal(String str){
        if(CheckUtil.isEmptyStr(str,false)){
            return str;
        }
        if(!str.contains("_") && !str.contains("-")){
            return firstUpperCase(str);
        }
        String[] arr=split(str,true,"_|-",-1,true);
        StringBuffer buffer=new StringBuffer();
        for(String item : arr){
            buffer.append(firstUpperCase(item));
        }
        return buffer.toString();
    }
    public static String toCamel(String str){
        if(CheckUtil.isEmptyStr(str,false)){
            return str;
        }
        if(!str.contains("_") && !str.contains("-")){
            return firstLowerCase(str);
        }
        String[] arr=split(str,true,"_|-",-1,true);
        StringBuffer buffer=new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if(i==0){
                buffer.append(firstLowerCase(arr[i]));
            }else{
                buffer.append(firstUpperCase(arr[i]));
            }
        }
        return buffer.toString();
    }
    public static String toUnderScore(String str){
        if(str.contains("_")){
            return str.trim();
        }
        StringBuffer buffer=new StringBuffer();
        char[] arr=str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if(arr[i]>='A' && arr[i]<='Z'){
                buffer.append("_");
                buffer.append((char)(arr[i]|32));
            }else{
                buffer.append(arr[i]);
            }
        }
        return buffer.toString();
    }

    public static List<RegexMatchItem> regexFinds(String str,String regex){
        List<RegexMatchItem> ret=new ArrayList<>();
        Pattern patten=Pattern.compile(regex);
        Matcher matcher=patten.matcher(str);
        while (matcher.find()){
            MatchResult result=matcher.toMatchResult();

            RegexMatchItem item=new RegexMatchItem();
            item.srcStr=str;
            item.regexStr=regex;
            item.matchStr=matcher.group();
            item.idxStart= result.start();
            item.idxEnd= result.end();

            ret.add(item);
        }
        return ret;
    }

    /**
     * 将字符串分解为描述的有序对象，对象中包含匹配串或者非匹配串部分，为有序ArrayList
     * 以便根据此List分别对匹配和非匹配部分提取或者重新构造字符串
     * @param str
     * @param regex
     * @return
     */
    public static List<RegexFindPartMeta> regexFindParts(String str, String regex){
        List<RegexFindPartMeta> ret=new ArrayList<>(64);
        Pattern patten=Pattern.compile(regex);
        Matcher matcher=patten.matcher(str);
        int lidx=0;
        while (matcher.find()){
            MatchResult result=matcher.toMatchResult();

            RegexFindPartMeta oth=new RegexFindPartMeta();
            oth.part=str.substring(lidx,result.start());
            oth.isMatch=false;
            ret.add(oth);

            lidx=result.end();
            String group=matcher.group();

            RegexFindPartMeta mth=new RegexFindPartMeta();
            mth.part=group;
            mth.isMatch=true;
            ret.add(mth);

        }

        RegexFindPartMeta oth=new RegexFindPartMeta();
        oth.part=str.substring(lidx);
        oth.isMatch=false;
        ret.add(oth);

        return ret;
    }

    /**
     * 示例：
     * 文件大小：
     * (size,true,0,"byte",1024,"kb",1024,"mb",1024,"gb")
     * 数值：
     * (num,true,0,"分",10,"角",10,"元",10,"百",10,"千",10,"万")
     * @param num
     * @param fullMode
     * @param sizeUnits
     * @return
     */
    public static String number2VisualStringKvs(long num,boolean fullMode, Object ... sizeUnits){
        int len=sizeUnits.length/2;
        Pair<Long,String>[] pairs=new Pair[len];
        for(int i=0,j=0;(i+2)<= sizeUnits.length;i+=2,j++){
            long size=Long.parseLong(String.valueOf(sizeUnits[i]));
            String unit=String.valueOf(sizeUnits[i+1]);
            pairs[j]=new Pair<>(size,unit);
        }
        return number2VisualString(num,fullMode,pairs);
    }
    /**
     * 将传入的num根据指定的分档和单位输出可视化的结果
     * 例如：
     * 169，传入分档：
     * 0：毫秒，1000：秒，60：分，60：小时，24：天
     * 分档也就是每个分档之间的换算尺度，也就是换算进制
     * 则返回结果为：169毫秒
     * fullMode指定是否全部单位显示还是以最大单位档小数显示
     * @param num
     * @param segmentSizeUnitPairs
     * @return
     */
    public static String number2VisualString(long num,boolean fullMode, Pair<Long,String>... segmentSizeUnitPairs){
        if(segmentSizeUnitPairs==null || segmentSizeUnitPairs.length==0){
            return String.valueOf(num);
        }
        Pair<Long,String>[] segments=new Pair[segmentSizeUnitPairs.length];
        long psize=1;
        segments[0]=segmentSizeUnitPairs[0];
        for (int i = 1; i < segmentSizeUnitPairs.length; i++) {
            psize*=segmentSizeUnitPairs[i].key;
            segments[i]=new Pair<>(psize,segmentSizeUnitPairs[i].val);
        }
        if(num>=segments[segments.length-1].key){
            long lsize=segments[segments.length-1].key;
            if(lsize==0){
                return String.format("%d%s",num,segments[segments.length - 1].val);
            }
            if(fullMode){
                if(num%lsize==0) {
                    return String.format("%d%s", (num / lsize), segments[segments.length - 1].val);
                }else{
                    return String.format("%d%s", (num / lsize), segments[segments.length - 1].val)+number2VisualString(num%lsize,fullMode,segmentSizeUnitPairs);
                }
            }else{
                return String.format("%.04f%s",(num*1.0/segments[segments.length-1].key),segments[segments.length-1].val);
            }
        }
        for (int i = segments.length-1; i >=0 ; i--) {
            if(num>=segments[i-1].key && num<segments[i].key){
                long lsize=segments[i-1].key;
                if(lsize==0){
                    return String.format("%d%s",num,segments[i - 1].val);
                }
                if(fullMode){
                    if(num%lsize==0) {
                        return String.format("%d%s", (num / lsize), segments[i - 1].val);
                    }else{
                        return String.format("%d%s", (num / lsize), segments[i - 1].val)+number2VisualString(num%lsize,fullMode,segmentSizeUnitPairs);
                    }
                }else{
                    return String.format("%.04f%s",(num*1.0/segments[i-1].key),segments[i-1].val);
                }
            }
        }
        return String.valueOf(num);
    }

    /**
     * 以分为单位的中国元转换为可视化串
     * @param fen
     * @return
     */
    public static String toVsCnYuanByFen(long fen){
        return number2VisualStringKvs(fen,true,0,"分",10,"角",10,"元",10,"百",10,"千",10,"万",10,"十万",10,"百万",10,"千万",10,"亿",10,"十亿",10,"百亿",10,"千亿");
    }

    /**
     * 以个为单位的计数转换为可视化串
     * @param num
     * @return
     */
    public static String toVsCnNum(long num){
        return number2VisualStringKvs(num,true,0,"个",10,"十",10,"百",10,"千",10,"万",10,"十万",10,"百万",10,"千万",10,"亿",10,"十亿",10,"百亿",10,"千亿");
    }

    /**
     * 以字节为单位的大小转换为可视化串
     * @param bt
     * @return
     */
    public static String toVsSizeByByte(long bt){
        return number2VisualStringKvs(bt,true,0,"Byte",1024,"KB",1024,"MB",1024,"GB",1024,"TB");
    }

    /**
     * 以毫秒为单位的时间转换为可视化串
     * @param milliSecond
     * @return
     */
    public static String toVsCnDateByMilliSecond(long milliSecond){
        return number2VisualStringKvs(milliSecond,true,0,"毫秒",1000,"秒",60,"分",60,"时",24,"天");
    }

}
