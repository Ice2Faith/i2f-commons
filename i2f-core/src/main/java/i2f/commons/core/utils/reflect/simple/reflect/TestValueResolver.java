package i2f.commons.core.utils.reflect.simple.reflect;


import i2f.commons.core.utils.db.annotations.DBColumn;
import i2f.commons.core.utils.db.annotations.DBTable;
import i2f.commons.core.utils.reflect.simple.reflect.core.ReflectResolver;
import i2f.commons.core.utils.reflect.simple.reflect.domain.TestDomain;
import i2f.commons.core.utils.reflect.simple.reflect.model.TestModel;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author ltb
 * @date 2022/3/14 11:23
 * @desc
 */
public class TestValueResolver {
    public static void main(String[] args){
        Map<String, Object> obj=new HashMap<>();

        Map<String, Object> map=new HashMap<>();
        map.put("name","zhang");
        map.put("age",12);

        Set<String> attrs=new HashSet<>();
        attrs.add("s1");
        attrs.add("s2");
        map.put("set",attrs);

        TestModel model=new TestModel();
        model.setEntryId("entryId");
        model.setEntryKey("entryKey");
        model.setCreateUser("createUser");
        map.put("model",model);

        obj.put("map",map);

        obj.put("arr",new String[]{"1","2","3"});
        List<Integer> list=new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        obj.put("list",list);

        System.out.println(ValueResolver.get(obj,"arr[0]"));
        System.out.println(ValueResolver.get(obj,"list[1]"));
        System.out.println(ValueResolver.get(obj,"map.set[0]"));
        System.out.println(ValueResolver.get(obj,"map.model.entryId"));
        System.out.println(ValueResolver.get(obj,"map.model.entryKey"));
        System.out.println(ValueResolver.get(obj,"map.model.createUser"));

        ValueResolver.set(obj,"map.model.createUser","newUser");
        System.out.println(ValueResolver.get(obj,"map.model.createUser"));

        ValueResolver.set(obj,"map.model.createUser",12);
        System.out.println(ValueResolver.get(obj,"map.model.createUser"));

        Class clazz= TestDomain.class;
        List<PropertyAccessor> readers= ReflectResolver.getLogicalReadableFields(clazz);
        List<PropertyAccessor> writers= ReflectResolver.getLogicalWritableFields(clazz);

        Map<String,Object> dstMap=new HashMap<>();
        BeanResolver.copy(model,dstMap,false);

        TestDomain dstObj=new TestDomain();
        BeanResolver.copy(dstMap,dstObj,false);

        TestModel dstModel=new TestModel();
        BeanResolver.copy(dstObj,dstModel,true);

        Set<Field> force=ReflectResolver.forceAllFields(TestModel.class);

        Class cls= TestModel.class;
        Field fld=ReflectResolver.findField(cls,"modifyUser");
        DBTable dbTable=ReflectResolver.findElementAnnotation(cls,DBTable.class,true,true,true);
        DBColumn dbColumn=ReflectResolver.findElementAnnotation(fld,DBColumn.class,true,true,true);

    }
}
