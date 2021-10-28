package i2f.commons.core.utils.generator.regex;

import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.generator.regex.core.ObjectFinder;
import i2f.commons.core.utils.generator.regex.core.impl.ForGenerate;
import i2f.commons.core.utils.generator.regex.core.impl.IfGenerate;
import i2f.commons.core.utils.generator.regex.core.impl.IncludeGenerate;
import i2f.commons.core.utils.generator.regex.core.impl.ValGenerate;
import i2f.commons.core.utils.generator.regex.data.JsonControlMeta;
import i2f.commons.core.utils.generator.regex.impl.DefaultValueMapper;
import i2f.commons.core.utils.generator.regex.impl.FileTemplateLoader;
import i2f.commons.core.utils.reflect.core.resolver.base.ClassResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * rollback
 * stop
 * point
 * --------------------
 * stop back
 */

/**
 * @author ltb
 * @date 2021/10/18
 */
public class RegexGenerator {
    /**
     * match for :
     * standard var defined name for c/c++/java
     * such as:
     * a
     * _
     * _ac
     * a_c
     * a52
     * a_52
     */
    public static final String DECLARE_NAME_REGEX ="[a-zA-Z_][a-zA-Z0-9_]*";

    /**
     * match for:
     * @com.i2f.CommUtil.getData
     * @String.valueOf
     * @size
     * @length
     */
    public static final String REFERENCE_METHOD_REGEX="\\@"+DECLARE_NAME_REGEX+"(\\."+DECLARE_NAME_REGEX+")*";

    /**
     * match for :
     * standard object attribute route expression
     * abc
     * _abc
     * _123
     * abc.de
     * abc[1]
     * abc.de[2].cf
     */
    public static final String OBJECT_FIELD_ROUTE_REGEX =DECLARE_NAME_REGEX+"(\\[\\d+\\])?(\\."+DECLARE_NAME_REGEX+"(\\[\\d+\\])?)*"+"("+REFERENCE_METHOD_REGEX+")?";


    /**
     * match for :
     * ${abc} ${_abc} ${_123} ${abc.de} ${abc[1]} ${abc.de[2].cf}
     */
    public static final String JSON_PARAM_REGEX="\\$\\{"+ OBJECT_FIELD_ROUTE_REGEX +"\\}";

    /**
     * match for :
     * standard C/C++ or java defined a String value,such as
     * "a"
     * "a "
     * "a\"b"
     * "a\'b "
     */
    public static final String QUOTE_STRING_REGEX ="\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"";

    public static final String SINGLE_QUOTE_STRING_REGEX ="\'([^\'\\\\]*(\\\\.[^\'\\\\]*)*)\'";

    /**
     * match for :
     * #{[for,obj.args[2].key],separator=,prefix=,suffix=,size="8"}
     */
    public static final String JSON_CONTROL_REGEX ="\\#\\{\\["+ DECLARE_NAME_REGEX +"\\,"+ OBJECT_FIELD_ROUTE_REGEX +"\\](\\,"+ DECLARE_NAME_REGEX +"="+ QUOTE_STRING_REGEX +")*\\}";

    public static JsonControlMeta inflateJsonControlRegexString(String str){
        str=str.trim();
        if(!str.matches(JSON_CONTROL_REGEX)){
            return null;
        }
        str=str.substring(2,str.length()-1);
        JsonControlMeta meta=new JsonControlMeta();

        String mainCtrlReg="\\["+DECLARE_NAME_REGEX +"\\,"+ OBJECT_FIELD_ROUTE_REGEX+"\\]";
        Pattern pattern=Pattern.compile(mainCtrlReg);
        Matcher matcher=pattern.matcher(str);
        if(matcher.find()){
            MatchResult result=matcher.toMatchResult();
            String group=matcher.group();
            group=group.substring(1,group.length()-1);
            String[] arr=group.split(",",2);
            if(arr.length>=1){
                meta.action=arr[0];
            }
            if(arr.length>=2){
                meta.routeExpression=arr[1];
            }
        }

        Map<String,String> params=new HashMap<>();
        String paramsCtrlReg=DECLARE_NAME_REGEX +"="+ QUOTE_STRING_REGEX;
        pattern=Pattern.compile(paramsCtrlReg);
        matcher= pattern.matcher(str);
        while(matcher.find()){
            MatchResult result=matcher.toMatchResult();
            String group=matcher.group();
            int idx=group.indexOf("=");
            String key=group.substring(0,idx);
            String val=group.substring(idx+1);
            val=val.substring(1,val.length()-1);
            params.put(key,val);
        }

        meta.parameters=params;

        return meta;
    }

    public static String render(String template,Map<String,Object> param){
        return render(template, param,null);
    }
    public static String render(String template, Map<String,Object> param, List<String> basePackages){
        IMap<Object,String> mapper=new DefaultValueMapper();
        return render(template, param,mapper,basePackages);
    }
    /**
     * 在generate基础之上，前置一层进行支持for解析
     *
     * for表达式
     * #{[for,obj.args[2].key@String.valueOf],separator="",prefix="",suffix="",template="",blank="",jump="",ref=""}
     * for必填，JSON取值表达式必填，
     * json表达式讲解:
     * obj.args[2].key@String.valueOf
     * 含义：标识取绑定参数的obj属性 的args可迭代对象的第2个元素 的key 并应用String类的valueOf方法进行对前面key对象的转换
     * separator/prefix/suffix可选
     * template指定循环的模板，_item表达式取得循环的个体，_root表达式取得渲染模板的渲染参数，_ctx表达式取得循环的环境变量
     * _ctx变量详解：
     * 包含三个属性：
     * first:是否是第一个元素
     * last:是否是最后一个元素
     * index:当前元素的逻辑索引
     * _item变量详解：
     * _item变量就是指定的JSON表达式对应的可迭代对象的当前循环迭代对象
     * 满足如下表达式：
     * _item==obj.args[2].key[_ctx.index]
     * _root变量详解：
     * _root变量就是调用render方法传入的对象本身
     * blank指定true/false,标识在循环变量为空白，无元素时，是否还需要前后缀
     * jump指定true/false,标识如果循环变量解析为模板之后为空白字符串，那么将会跳过该结果，string.trim()==""
     * ref指定循环的模板从param中的哪个key取得
     *
     * if表达式
     * #{[if,env.args[2]],test="_item!=null && _item.key!=null || _item.val==2 || _item.str=='hello' || _item.id==_item.parentId",template=""}
     * if必填，JSON取值表达式必填
     * test表达书部分：
     * item指代传入的JSON表达式对应的对象，示例中的env.args[2]==_item
     * 表达式中不支持括号优先级
     * 单个表达式左值和有值均可以使用JSON表达式进行取值
     * 左右值支持true、false、null三个特殊值，以及数值和单引号包含的字符串，当然JSON表达式对应对象也行
     * template部分，和for解析部分一致,但是没有_ctx环境变量(_ctx变量为空对象)
     *
     * tpl表达式
     * #{[tpl,templateId],template="",load="",key=""}
     * tpl必填
     * templateId必填，将用于管理所有模板的ID，
     * 这个ID可用于其他表达式使用ref时指定，取值方式：_tpl.templateId
     * 也就是说会添加一个模板的收纳根_tpl
     * template值即为模板字符串
     * load可以指定一个模板加载器类，IMap<String,String>的实现类，
     *  key的值就作为load这个接口实现类的入参
     *  load可以不指定，默认会使用默认的文件加载类，key指定文件路径即可
     *
     * include表达式
     * #{[include,env.args[2]],ref=""}
     * 类似tpl表达式，tpl表达式是声明一个模板
     * 而include表达式是引入一个模板，因此include表达式用于引入tpl表达式声明的模板
     * 使用env.args[2]作为该模板的_item参数，_root参数依旧保留
     *
     * val表达式
     * #{[val,env.args[2]],mapper=""}
     * 用于使用自定义的转换器转换传入的JSON表达式值
     * 如果不指定mapper，将使用继承的mapper进行转换，也就是说可以使用此表达式规避一些现有框架对${}表达式的冲突
     * mapper是一个IMap<Object,String>接口的实现类
     *
     * @param template
     * @param param
     * @return
     */
    public static String render(String template,Map<String,Object> param,IMap<Object,String> mapper,List<String> basePackages){
        Map<String,Object> preparedParam=new HashMap<>((int)(param.size()*1.5));
        for(Map.Entry<String, Object> item : param.entrySet()){
            preparedParam.put(item.getKey(),item.getValue());
        }
        StringBuilder builder=new StringBuilder((int)(template.length()/0.7));
        Pattern pattern=Pattern.compile(JSON_CONTROL_REGEX);
        Matcher matcher=pattern.matcher(template);

        int tmpIdx=0;
        int lidx=0;
        while (matcher.find()){
            MatchResult result=matcher.toMatchResult();
            builder.append(template.substring(lidx,result.start()));
            String group=matcher.group();
            lidx=result.end();
            JsonControlMeta meta=inflateJsonControlRegexString(group);

            if("for".equals(meta.action)){
                String separator=meta.parameters.get("separator");
                if(separator==null){
                    separator="";
                }
                String prefix=meta.parameters.get("prefix");
                if(prefix==null){
                    prefix="";
                }
                String suffix=meta.parameters.get("suffix");
                if(suffix==null){
                    suffix="";
                }
                String jump=meta.parameters.get("jump");
                String tpl=meta.parameters.get("template");
                String blank=meta.parameters.get("blank");

                String tplId=meta.parameters.get("ref");
                if(tplId!=null){
                    Object optp=ObjectFinder.getObjectByDotKeyWithReference(preparedParam,tplId);
                    if(optp!=null){
                        Object val=optp;
                        tpl= mapper.map(val);
                    }
                }

                Object obj= ObjectFinder.getObjectByDotKeyWithReference(preparedParam,meta.routeExpression,basePackages);

                ForGenerate genFor=new ForGenerate();
                genFor.template=tpl;
                genFor.root=preparedParam;
                genFor.data=obj;
                genFor.mapper=mapper;
                genFor.separator=separator;
                genFor.prefix=prefix;
                genFor.suffix=suffix;
                genFor.blank=blank;
                genFor.jump=jump;
                genFor.basePackages=basePackages;

                String key="_for_tmp_"+tmpIdx;
                builder.append("${").append(key).append("}");
                preparedParam.put(key,genFor);
            }else if("if".equals(meta.action)){

                String test=meta.parameters.get("test");
                if(test==null){
                    test="";
                }

                String tpl=meta.parameters.get("template");

                String tplId=meta.parameters.get("ref");
                if(tplId!=null){
                    Object optp=ObjectFinder.getObjectByDotKeyWithReference(preparedParam,tplId);
                    if(optp!=null){
                        Object val=optp;
                        tpl= mapper.map(val);
                    }
                }

                Object obj= ObjectFinder.getObjectByDotKeyWithReference(preparedParam,meta.routeExpression,basePackages);

                IfGenerate genIf=new IfGenerate();
                genIf.root=preparedParam;
                genIf.data=obj;
                genIf.mapper=mapper;
                genIf.template=tpl;
                genIf.test=test;
                genIf.basePackages=basePackages;

                String key="_if_tmp_"+tmpIdx;
                builder.append("${").append(key).append("}");
                preparedParam.put(key,genIf);
            }else if("tpl".equals(meta.action)){
                String tplId=meta.routeExpression;
                String tpl=meta.parameters.get("template");

                String key=meta.parameters.get("key");
                if(key!=null && !"".equals(key)) {
                    String load = meta.parameters.get("load");
                    String ltpl = loadTemplate(load, key);
                    if(ltpl!=null && !"".equals(ltpl)){
                        tpl=ltpl;
                    }
                }
                if(!preparedParam.containsKey("_tpl")){
                    preparedParam.put("_tpl",new HashMap<String,String>());
                }
                Map<String,String> tpls=(Map<String,String>)preparedParam.get("_tpl");
                tpls.put(tplId,tpl);
            }else if("include".equals(meta.action)){
                String ref=meta.parameters.get("ref");
                Object obj= ObjectFinder.getObjectByDotKeyWithReference(preparedParam,meta.routeExpression,basePackages);

                String tpl=null;
                Map<String,String> tpls=(Map<String,String>)preparedParam.get("_tpl");
                if(tpls!=null){
                    if(ref!=null && !"".equals(ref)){
                        tpl=tpls.get(ref);
                    }
                }

                IncludeGenerate gen=new IncludeGenerate();
                gen.basePackages=basePackages;
                gen.root=preparedParam;
                gen.mapper=mapper;
                gen.data=obj;
                gen.template=tpl;

                String key="_include_tmp_"+tmpIdx;
                builder.append("${").append(key).append("}");
                preparedParam.put(key,gen);
            }else if("val".equals(meta.action)){
                String userMapper=meta.parameters.get("mapper");
                Object obj= ObjectFinder.getObjectByDotKeyWithReference(preparedParam,meta.routeExpression,basePackages);


                ValGenerate gen=new ValGenerate();
                gen.basePackages=basePackages;
                gen.root=preparedParam;
                gen.mapper=mapper;
                gen.data=obj;
                gen.userMapper=userMapper;

                String key="_val_tmp_"+tmpIdx;
                builder.append("${").append(key).append("}");
                preparedParam.put(key,gen);
            }

            tmpIdx++;
        }
        builder.append(template.substring(lidx));

        String preparedTemplate=builder.toString();
        return generate(preparedTemplate,preparedParam,mapper,basePackages);
    }

    private static String loadTemplate(String loaderClass,String loadKey){
        IMap<String,String> loader=null;
        if(loaderClass!=null && !"".equals(loaderClass)){
            Class clazz= ClassResolver.getClazz(loaderClass);
            if(clazz!=null){
                loader=(IMap<String,String>)ClassResolver.instance(clazz);
            }
        }else{
            loader=new FileTemplateLoader();
        }
        if(loader!=null){
            return loader.map(loadKey);
        }
        return null;
    }

    /**
     * 仅支持解析JSON变量进行模板替换
     * @param template
     * @param param
     * @return
     */
    public static String generate(String template,Object param){
        IMap<Object,String> mapper=new DefaultValueMapper();
        return generate(template, param,mapper,null);
    }

    /**
     * 用param进行模板解析为目标字符串,
     * 仅支持解析JSON变量进行模板替换
     * @param template 模板字符串
     * @param param 模板参数
     * @param mapper 参数映射器
     * @return 模板渲染结果
     */
    public static String generate(String template, Object param, IMap<Object,String> mapper,List<String> basePackages){
        StringBuilder builder=new StringBuilder((int)(template.length()*1.5));
        Pattern pattern=Pattern.compile(JSON_PARAM_REGEX);
        Matcher matcher=pattern.matcher(template);

        int lidx=0;
        while (matcher.find()){
            MatchResult result=matcher.toMatchResult();
            builder.append(template.substring(lidx,result.start()));
            lidx=result.end();
            String group=matcher.group();
            String regItem= group.substring(2,group.length()-1);
            Object val= ObjectFinder.getObjectByDotKeyWithReference(param,regItem,basePackages);
            String part=mapper.map(val);
            builder.append(part);
        }
        builder.append(template.substring(lidx));

        return builder.toString();
    }


}
