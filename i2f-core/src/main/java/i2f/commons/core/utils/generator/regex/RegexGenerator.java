package i2f.commons.core.utils.generator.regex;

import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.generator.regex.core.ObjectFinder;
import i2f.commons.core.utils.generator.regex.core.impl.ForGenerate;
import i2f.commons.core.utils.generator.regex.core.impl.IfGenerate;
import i2f.commons.core.utils.generator.regex.data.JsonControlMeta;
import i2f.commons.core.utils.generator.regex.impl.DefaultValueMapper;

import java.util.HashMap;
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
        IMap<Object,String> mapper=new DefaultValueMapper();
        return render(template, param,mapper);
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
     * @param template
     * @param param
     * @return
     */
    public static String render(String template,Map<String,Object> param,IMap<Object,String> mapper){
        Map<String,Object> preparedParam=new HashMap<>();
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
                    if(param.containsKey(tplId)){
                        Object val=param.get(tplId);
                        tpl= mapper.map(val);
                    }
                }

                Object obj= ObjectFinder.getObjectByDotKeyWithReference(param,meta.routeExpression);

                ForGenerate genFor=new ForGenerate();
                genFor.template=tpl;
                genFor.root=param;
                genFor.data=obj;
                genFor.mapper=mapper;
                genFor.separator=separator;
                genFor.prefix=prefix;
                genFor.suffix=suffix;
                genFor.blank=blank;
                genFor.jump=jump;

                System.out.println("genFor:"+genFor);

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
                    if(param.containsKey(tplId)){
                        Object val=param.get(tplId);
                        tpl= mapper.map(val);
                    }
                }

                Object obj= ObjectFinder.getObjectByDotKeyWithReference(param,meta.routeExpression);

                IfGenerate genIf=new IfGenerate();
                genIf.root=param;
                genIf.data=obj;
                genIf.mapper=mapper;
                genIf.template=tpl;
                genIf.test=test;

                System.out.println("genIf:"+genIf);
                String key="_if_tmp_"+tmpIdx;
                builder.append("${").append(key).append("}");
                preparedParam.put(key,genIf);
            }

            tmpIdx++;
        }
        builder.append(template.substring(lidx));

        String preparedTemplate=builder.toString();
System.out.println("prepare:"+preparedTemplate);
        return generate(preparedTemplate,preparedParam,mapper);
    }

    /**
     * 仅支持解析JSON变量进行模板替换
     * @param template
     * @param param
     * @return
     */
    public static String generate(String template,Object param){
        IMap<Object,String> mapper=new DefaultValueMapper();
        return generate(template, param,mapper);
    }

    /**
     * 用param进行模板解析为目标字符串,
     * 仅支持解析JSON变量进行模板替换
     * @param template 模板字符串
     * @param param 模板参数
     * @param mapper 参数映射器
     * @return 模板渲染结果
     */
    public static String generate(String template, Object param, IMap<Object,String> mapper){
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
            Object val= ObjectFinder.getObjectByDotKeyWithReference(param,regItem);
            String part=mapper.map(val);
            builder.append(part);
        }
        builder.append(template.substring(lidx));

        return builder.toString();
    }


}
