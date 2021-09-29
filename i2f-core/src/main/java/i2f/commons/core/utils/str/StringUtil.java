package i2f.commons.core.utils.str;


import i2f.commons.core.utils.data.DataUtil;
import i2f.commons.core.utils.safe.CheckUtil;

import java.io.File;
import java.util.Vector;

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

}
