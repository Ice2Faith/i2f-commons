package i2f.commons.core.utils.xml;

import i2f.commons.core.utils.reflect.core.resolver.base.FieldResolver;
import i2f.commons.core.utils.reflect.core.resolver.base.ValueResolver;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class Xml2 {
    public static boolean WITH_TYPE=true;
    public static String[][] transMap={
            {"&","&amp;"},
            {"<","&lt;"},
            {">","&gt;"},
            {"\\'","&apos;"},
            {"\\\"","&quot;"},
    };
    public static SimpleDateFormat dateFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    protected static String toXmlArray(Object arr){
        if(arr==null){
            return "";
        }
        if(!arr.getClass().isArray()){
            return toXml(arr);
        }
        StringBuilder builder=new StringBuilder();
        int arrLen= Array.getLength(arr);
        for(int i=0;i<arrLen;i+=1){
            Object val=Array.get(arr,i);
            val2Xml(builder,"item",val);
        }
        return builder.toString();
    }
    protected static String toXmlCollection(Collection col){
        StringBuilder builder=new StringBuilder();
        for(Object item : col){
            val2Xml(builder,"item",item);
        }
        return builder.toString();
    }
    protected static String toXmlMap(Map map){
        StringBuilder builder=new StringBuilder();
        for(Object item : map.keySet()){
            String tagName=String.valueOf(item);
            Object val=map.get(item);
            val2Xml(builder,tagName,val);
        }
        return builder.toString();
    }
    protected static String toXmlDate(Date date){
        return dateFmt.format(date);
    }
    protected static String toXmlString(String str){
        for(String[] item : transMap){
            str=str.replaceAll(item[0],item[1]);
        }
        return str;
    }
    protected static String toXmlNumber(Number num){
        return String.valueOf(num);
    }
    protected static String toXmlNull(){
        return "";
    }

    protected static void appendXmlTag(StringBuilder builder,String type,String tag,String inner){
        appendXmlTagPrefix(builder,type, tag);
        builder.append(inner);
        appendXmlTagSuffix(builder, tag);
    }

    protected static void appendXmlTagSuffix(StringBuilder builder, String tag) {
        builder.append("</");
        builder.append(tag);
        builder.append(">");
    }

    protected static void appendXmlTagPrefix(StringBuilder builder,String type, String tag) {
        builder.append('<');
        builder.append(tag);
        if(type!=null && WITH_TYPE){
            builder.append(" type=\"");
            builder.append(type);
            builder.append("\"");
        }
        builder.append('>');
    }
    public static String toXml(Object obj){
        return toXml(obj,false);
    }
    public static String toXml(Object obj,boolean withHead){
        if(obj==null){
            return "";
        }
        StringBuilder builder=new StringBuilder();
        Class clazz=obj.getClass();
        if(withHead){
            builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        }
        appendXmlTagPrefix(builder,"object", clazz.getSimpleName());
        Set<Field> fields= FieldResolver.getAllFields(clazz,true);
        for(Field item : fields){
            String tagName= item.getName();
            Object val= ValueResolver.getVal(obj,item.getName(),false);
            val2Xml(builder, tagName, val);
        }
        appendXmlTagSuffix(builder, clazz.getSimpleName());
        return builder.toString();
    }

    protected static void val2Xml(StringBuilder builder,String tagName,Object val){
        if(val==null){
            builder.append(toXmlNull());
            return;
        }
        if(val instanceof Map){
            appendXmlTag(builder,"object", tagName, toXmlMap((Map)val));
        }else if(val instanceof Collection){
            appendXmlTag(builder,"array",tagName,toXmlCollection((Collection)val));
        }else if(val.getClass().isArray()){
            appendXmlTag(builder,"array", tagName, toXmlArray(val));
        }else if(val instanceof Date){
            appendXmlTag(builder,"date", tagName, toXmlDate((Date)val));
        }else if(val instanceof String){
            appendXmlTag(builder,"string", tagName, toXmlString((String)val));
        }else if(val instanceof Number){
            appendXmlTag(builder,"number", tagName, toXmlNumber((Number)val));
        }else{
            appendXmlTag(builder,"object",tagName,toXml(val));
        }
    }
}
