package test;

import i2f.commons.core.data.Pair;
import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.data.ContainerUtil;
import i2f.commons.core.utils.generator.regex.core.ObjectFinder;
import i2f.commons.core.utils.generator.regex.core.impl.IfGenerate;
import i2f.commons.core.utils.generator.simple.IGeneratable;
import i2f.commons.core.utils.generator.regex.RegexGenerator;
import i2f.commons.core.utils.generator.simple.impl.ForGenerator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ltb
 * @date 2021/10/18
 */
public class TestPatten {
    public static void main(String[] args2){
        testEmbedGen();


        Object val= ObjectFinder.referenceDotKeyConvert(1,"Math.exp");

        System.out.println("val:"+val);
    }


    public static void testEmbedGen() {
        String template="if-for:#{[if,ifForArg],test=\"_item!=null && _item>=10 && _root.list!=null\",ref=\"tplIfFor\"}\n" +
                "for-if:#{[for,list],jump=\"true\",prefix=\"[\",suffix=\"]\",separator=\"\n\t,\",ref=\"tplForIf\"}";
        String tplIfFor="for in if:{#{[for,_root.list],separator=\"\n\t,\",prefix=\"[\",suffix=\"]\",template=\"${_item@Math.sin}[abs=${_item@Math.abs}]\"}}";
        String tplForIf="#{[if,_item],test=\"_item>=4\",template=\"if in for:${_item@Math.exp} with ${_item}\"}";
        Map<String,Object> params=new HashMap<>();
        params.put("list",ContainerUtil.arrList(1,-2,3,4,5,-6));
        params.put("col",ContainerUtil.arrList(1,2,"aaa","bbb"));
        params.put("ifForArg",10);
        params.put("forIfArg",12);
        params.put("tplIfFor",tplIfFor);
        params.put("tplForIf",tplForIf);

        String result=RegexGenerator.render(template,params);

        System.out.println("\ntemplate:\n"+template);
        System.out.println("\nparams:\n"+params);
        System.out.println("\nresult:\n"+result);

        System.out.println(RegexGenerator.OBJECT_FIELD_ROUTE_REGEX);
    }

    public static void noneEmbedRender() {
        String forTemplate="\n\trecord{first:${ctx.first},last:${ctx.last},index:${ctx.index},id:${item.id},name:${item.name},age:${item.age},grade:${item.grade},root:${root.head}}";
        String template=" head meta ${head}" +
                " #{[for,args],ref=\"forArgsTpl\",separator=\",\",prefix=\"[\",suffix=\"]\",size=\"8\"}" +
                " tail meta ${tail} " +
                " args2=#{[for,args2],blank=\"true\"} " +
                " if #{[if,ifarg],test=\"item!=null && item>10\",template=\"if-arg-v=${item},${root.head}\"}";

        Map<String,Object> params=new HashMap<>();
        List args=new ArrayList();
        args.add(ContainerUtil.hashMapKvs("id",1,"name","zhang","age",22,"grade","82"));
        args.add(ContainerUtil.hashMapKvs("id","2","name","li","age",23,"grade",88));
        params.put("args",args);
        params.put("args2",new int[0]);
        params.put("forArgsTpl",forTemplate);
        params.put("ifarg",12);

        params.put("head","HEAD");
        params.put("tail","TAIL");
        String result=RegexGenerator.render(template,params);
        System.out.println("template:----------\n"+template);
        System.out.println("result:----------\n"+result);

        System.out.println(IfGenerate.CONDITION_REGEX);

        List<Pair<String,Boolean>> bconds=new ArrayList<>();
        // true && true && false && true || false && true || true && false
        bconds.add(new Pair<>("=",true));
        bconds.add(new Pair<>("&&",true));
        bconds.add(new Pair<>("&&",false));
        bconds.add(new Pair<>("&&",true));
        bconds.add(new Pair<>("||",false));
        bconds.add(new Pair<>("&&",true));
        bconds.add(new Pair<>("||",true));
        bconds.add(new Pair<>("&&",false));
        boolean rs=IfGenerate.calcBooleanResult(bconds);
        System.out.println("rs:"+rs);

        System.out.println(new BigDecimal("1.12").compareTo(new BigDecimal("1.1200")));
    }


    private static void genTestDefaultValueMapper() {
        String template="iterable: ${iterable} \n" +
                "map: ${map} \n" +
                "array: ${array} \n" +
                "string: ${string} \n" +
                "date: ${date} \n" +
                "int: ${int} long: ${long} short: ${short} byte: ${byte} char: ${char} bigInt: ${bigInt} \n" +
                "float: ${float} double: ${double} bigDec: ${bigDec} \n" +
                "bool: ${bool} \n" +
                "obj: ${obj} \n" +
                "igen: ${igen} \n";
        Map<String, Object> param=new HashMap<>();

        List iterable=new ArrayList<>();
        ContainerUtil.collection(iterable,1,"120",12.125);
        param.put("iterable",iterable);

        Map<String,Object> map=new HashMap<>();
        ContainerUtil.mapKvs(map,"age",12,"name","zhang");
        param.put("map",map);

        Object[] array={1,2,5,8};
        param.put("array",array);

        param.put("string","this is String");

        param.put("date",new Timestamp(System.currentTimeMillis()));

        param.put("int",10086);

        param.put("long",19911466788995L);

        param.put("short",(short)65535);

        param.put("byte",(byte)255);

        param.put("char",'A');

        param.put("bigInt",new BigInteger("99999"));

        param.put("float",12.125f);

        param.put("double",1763.66552478d);

        param.put("bigDec",new BigDecimal("6666.666666"));

        param.put("bool",true);

        param.put("obj",new Pair<String,Integer>("key",1));

        IGeneratable igen=new ForGenerator<int[]>(new int[]{1,1,2,5,4},",","[","]");
        param.put("igen",igen);

        String result=RegexGenerator.generate(template,param);
        System.out.println("template:\t"+template);
        System.out.println("result:  \t"+result);
    }


    private static void simpleGenerate() {
        Map<String, Object> param=new HashMap<>();
        param.put("col","name");
        param.put("tableName","Student");
        Map<String,Object> order=ContainerUtil.hashMapKvs("col","age","desc","asc");
        Map<String,Object> page=ContainerUtil.hashMapKvs("index",10,"limit",30);
        param.put("order", order);
        param.put("page",page);
        String template="select ${col} from ${tableName} order by ${order.col} ${order.desc} limit ${page.index},${page.limit}";

        String rs= RegexGenerator.generate(template, param, new IMap<Object, String>() {
            @Override
            public String map(Object val) {
                return String.valueOf(val);
            }
        });
        System.out.println(rs);
    }


}
