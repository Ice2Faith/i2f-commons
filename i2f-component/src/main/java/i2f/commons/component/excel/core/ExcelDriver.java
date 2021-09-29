package i2f.commons.component.excel.core;


import i2f.commons.component.excel.core.annotation.ExcelColumn;
import i2f.commons.component.excel.core.data.ExcelColumnMeta;
import i2f.commons.core.utils.reflect.core.resolver.base.FieldResolver;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author ltb
 * @date 2021/9/17
 */
public class ExcelDriver {
    public static<T> File exports(IExcelConverter converter,List<T> list,File saveFile){
        if(list.size()==0){
            return saveFile;
        }
        Object obj=list.get(0);
        Class clazz=obj.getClass();
        List<ExcelColumnMeta> metas=getExcelBeanMetas(clazz);

        converter.write(list,metas,saveFile);

        return saveFile;
    }


    public static <T> List<T> imports(IExcelConverter converter,Class<T> clazz,File readFile){
        List<T> list=new ArrayList<>();
        List<ExcelColumnMeta> metas=getExcelBeanMetas(clazz);

        list=converter.read(readFile,metas);

        return list;
    }


    private static List<ExcelColumnMeta> getExcelBeanMetas(Class clazz) {
        List<ExcelColumnMeta> metas=new ArrayList<>();
        Set<Field> fields= FieldResolver.getAllFieldsWithAnnotations(clazz,true,false, ExcelColumn.class);
        for(Field item : fields){
            ExcelColumnMeta meta=new ExcelColumnMeta();
            meta.setField(item);
            ExcelColumn ann=item.getDeclaredAnnotation(ExcelColumn.class);
            if(ann==null){
                ann=item.getAnnotation(ExcelColumn.class);
            }
            meta.setAnnotation(ann);
        }
        Collections.sort(metas, new Comparator<ExcelColumnMeta>() {
            @Override
            public int compare(ExcelColumnMeta o1, ExcelColumnMeta o2) {
                int sort1=o1.getAnnotation().order();
                int sort2=o2.getAnnotation().order();
                return sort2-sort1;
            }
        });
        return metas;
    }
}
