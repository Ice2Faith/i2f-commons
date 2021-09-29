package i2f.commons.core.utils.safe;

import java.util.Collection;
import java.util.Map;

public class AssertUtil {
    public static void exIn(String msg,Object cmpVal,Object ... vals) throws AssertException{
        if(CheckUtil.isIn(cmpVal,vals)){
            throw new AssertException(msg);
        }
    }
    public static void exNotIn(String msg,Object cmpVal,Object ... vals) throws AssertException{
        if(CheckUtil.notIn(cmpVal,vals)){
            throw new AssertException(msg);
        }
    }
    public static void when(String msg,boolean condition) throws AssertException {
        if(condition){
            throw new AssertException(msg);
        }
    }
    public static void exWhen(String msg,boolean ... conditions) throws AssertException {
        for(boolean item : conditions){
            if(item){
                throw new AssertException(msg);
            }
        }
    }
    public static void notWhen(String msg,boolean condition) throws AssertException {
        if(!condition){
            throw new AssertException(msg);
        }
    }
    public static void exNotWhen(String msg,boolean ... conditions) throws AssertException {
        for(boolean item : conditions){
            if(!item){
                throw new AssertException(msg);
            }
        }
    }
    public static void exNull(String msg,Object ... objs) throws AssertException {
        if(CheckUtil.isExNull(objs)){
            throw new AssertException(msg);
        }
    }
    public static void exEmptyStr(String msg,boolean needTrim,String ... strs) throws AssertException {
        if(CheckUtil.isExEmptyStr(needTrim,strs)){
            throw new AssertException(msg);
        }
    }
    public static void exEmptyArray(String msg,Object ... arrs) throws AssertException {
        if(CheckUtil.isExEmptyArray(arrs)){
            throw new AssertException(msg);
        }
    }
    public static void exEmptyCollection(String msg, Collection<?> ... cols) throws AssertException {
        if(CheckUtil.isExEmptyCollection(cols)){
            throw new AssertException(msg);
        }
    }
    public static void exEmptyMap(String msg, Map<?,?> ... maps) throws AssertException {
        if(CheckUtil.isExEmptyMap(maps)){
            throw new AssertException(msg);
        }
    }
    public static void exEmpty(String msg, Object ... objs) throws AssertException {
        if(CheckUtil.isExEmpty(objs)){
            throw new AssertException(msg);
        }
    }
    public static void exNotMatch(String msg,String regex,String ... strs) throws AssertException {
        if(CheckUtil.exNotMatched(regex, strs)){
            throw new AssertException(msg);
        }
    }
    public static void exNumLower(String msg, Number cmpNum, Number ... nums) throws AssertException {
        if(CheckUtil.isExNumLower(cmpNum, nums)){
            throw new AssertException(msg);
        }
    }
    public static void exNumGather(String msg,Number cmpNum,Number ... nums) throws AssertException {
        if(CheckUtil.isExNumGather(cmpNum, nums)){
            throw new AssertException(msg);
        }
    }
    public static void exNumLowerEqu(String msg,Number cmpNum,Number ... nums) throws AssertException {
        if(CheckUtil.isExNumLowerEqu(cmpNum, nums)){
            throw new AssertException(msg);
        }
    }
    public static void exNumGatherEqu(String msg,Number cmpNum,Number ... nums) throws AssertException {
        if(CheckUtil.isExNumGatherEqu(cmpNum, nums)){
            throw new AssertException(msg);
        }
    }
    public static void exNumBetweenBoth(String msg,Number min,Number max,Number ... nums) throws AssertException {
        if(CheckUtil.isExNumBetweenBoth( min, max,nums)){
            throw new AssertException(msg);
        }
    }
    public static void exNumBetweenLeft(String msg,Number min,Number max,Number ... nums) throws AssertException {
        if(CheckUtil.isExNumBetweenLeft( min, max,nums)){
            throw new AssertException(msg);
        }
    }
    public static void exNumBetweenOpen(String msg,Number min,Number max,Number ... nums) throws AssertException {
        if(CheckUtil.isExNumBetweenOpen( min, max,nums)){
            throw new AssertException(msg);
        }
    }
    public static void exNumEqu(String msg,Number cmpNum,Number ... nums) throws AssertException {
        if(CheckUtil.isExNumEqu( cmpNum,nums)){
            throw new AssertException(msg);
        }
    }
    public static void exNumNotEqu(String msg,Number cmpNum,Number ... nums) throws AssertException {
        if(CheckUtil.isExNumNotEqu( cmpNum,nums)){
            throw new AssertException(msg);
        }
    }
}

