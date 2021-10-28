package i2f.commons.core.utils.generator.regex.impl;

import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.generator.regex.core.IGenerate;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ltb
 * @date 2021/10/20
 */
public class DefaultValueMapper implements IMap<Object,String> {
    public String NULL_VAL="null";
    public String DATE_FMT="yyyy-MM-dd HH:mm:ss SSS";
    public String ITERABLE_SEPARATOR=",";
    public String BEGIN_ITERABLE="";
    public String END_ITERABLE="";
    @Override
    public String map(Object val) {
        if(val==null){
            return onNull();
        }
        Class clazz=val.getClass();
        if(val instanceof Iterable){ // Iterable , Collection , List , Set ...
            return onIterable((Iterable)val);
        }
        if(val instanceof Map){
            return onMap((Map)val);
        }
        if(clazz.isArray()){
            return onArray(val);
        }
        if(val instanceof IGenerate){
            return ((IGenerate)val).gen();
        }
        if(val instanceof String){
            return onString((String)val);
        }
        if(val instanceof Date){
            return onDate((Date)val);
        }
        if(int.class.equals(clazz)
        || val instanceof Integer){
            return onInt((Integer)val);
        }
        if(long.class.equals(clazz)
                || val instanceof Long){
            return onLong((Long)val);
        }
        if(short.class.equals(clazz)
                || val instanceof Short){
            return onShort((Short)val);
        }
        if(byte.class.equals(clazz)
                || val instanceof Byte){
            return onByte((Byte)val);
        }
        if(char.class.equals(clazz)
                || val instanceof Character){
            return onChar((Character)val);
        }
        if(val instanceof BigInteger){
            return onBigInteger((BigInteger)val);
        }
        if(float.class.equals(clazz)
        || val instanceof Float){
            return onFloat((Float)val);
        }
        if(double.class.equals(clazz)
                || val instanceof Double){
            return onDouble((Double)val);
        }
        if(val instanceof BigDecimal){
            return onBigDecimal((BigDecimal)val);
        }
        if(boolean.class.equals(clazz)
        || val instanceof Boolean){
            return onBoolean((Boolean)val);
        }
        if(val instanceof Map.Entry){
            return onEntry((Map.Entry)val);
        }
        if(val instanceof AtomicInteger){
            int v=((AtomicInteger)val).get();
            return onInt(v);
        }
        if(val instanceof AtomicLong){
            long v=((AtomicLong)val).get();
            return onLong(v);
        }
        if(val instanceof AtomicBoolean){
            boolean v=((AtomicBoolean)val).get();
            return onBoolean(v);
        }
        if(val instanceof AtomicReference){
            Object v=((AtomicReference)val).get();
            return map(v);
        }
        return onCustomObject(val);
    }

    private String onBoolean(Boolean val) {
        return String.valueOf(val);
    }

    private String onCustomObject(Object val) {
        return String.valueOf(val);
    }

    private String onBigDecimal(BigDecimal val) {
        return String.valueOf(val);
    }

    private String onDouble(Double val) {
        return String.valueOf(val);
    }

    private String onFloat(Float val) {
        return String.valueOf(val);
    }

    private String onBigInteger(BigInteger val) {
        return String.valueOf(val);
    }

    private String onChar(Character val) {
        return String.valueOf(val);
    }

    private String onByte(Byte val) {
        return String.valueOf(val);
    }

    private String onShort(Short val) {
        return String.valueOf(val);
    }

    private String onLong(Long val) {
        return String.valueOf(val);
    }

    private String onInt(Integer val) {
        return String.valueOf(val);
    }

    protected String onDate(Date val) {
        return new SimpleDateFormat(DATE_FMT).format(val);
    }

    protected String onString(String val) {
        return val;
    }

    protected String onArray(Object val) {
        int len=Array.getLength(val);
        List list=new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            list.add(Array.get(val,i));
        }
        return onIterable(list);
    }

    protected String onMap(Map val) {
        return onIterable(val.entrySet());
    }

    protected String onEntry(Map.Entry entry){
        return String.valueOf(entry);
    }

    protected String onIterable(Iterable val) {
        StringBuilder builder=new StringBuilder(256);
        builder.append(BEGIN_ITERABLE);
        boolean isFirst=true;
        Iterator it=val.iterator();
        while(it.hasNext()){
            if(!isFirst){
                builder.append(ITERABLE_SEPARATOR);
            }
            Object cur=it.next();
            builder.append(map(cur));
            isFirst=false;
        }
        builder.append(END_ITERABLE);
        return builder.toString();
    }

    protected String onNull(){
        return NULL_VAL;
    }
}
