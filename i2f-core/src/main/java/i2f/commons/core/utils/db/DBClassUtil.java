package i2f.commons.core.utils.db;


import i2f.commons.core.utils.db.core.DbClassResolver;
import i2f.commons.core.utils.str.AppendUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DBClassUtil {
    public static String genCreateTableByBean(Class clazz){
        return DbClassResolver.genCreateTableByBean(clazz);
    }
    public static String genDropTableByBean(Class clazz,boolean isExists){
        return DbClassResolver.genDropTableByBean(clazz,isExists);
    }

    public static String genTablesByBean(boolean isExists,Class ... classes){
        List<Class> list=new ArrayList<>(classes.length);
        for(Class item : classes){
            list.add(item);
        }
        return genTablesByBean(isExists,list);
    }

    public static String genTablesByBean(boolean isExists, Collection<Class> classes){
        AppendUtil.AppendBuilder builder=AppendUtil.builder();
        for(Class item : classes){
            builder.addsLine(genDropTableByBean(item,isExists))
                    .addsLine(genCreateTableByBean(item))
                    .addsLine("");
        }
        return builder.done();
    }
}
