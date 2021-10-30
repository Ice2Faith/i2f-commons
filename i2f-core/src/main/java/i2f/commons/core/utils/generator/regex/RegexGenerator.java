package i2f.commons.core.utils.generator.regex;

import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.generator.regex.core.ObjectFinder;
import i2f.commons.core.utils.generator.regex.core.impl.*;
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
     * fmt表达式
     * #{[fmt,env.args[2]],format="",values=""}
     * 用于对传入JSON表达式对象进行格式化模板
     * 参照String.format方法
     * format即为格式化字符串
     * values即为取值，可以不指定，则表达式的取值作为参数
     * 举例：
     * #{[fmt,env.args[2]],format="%02x-%02x",values="_item[0],_item[1]"}
     * 所以函数等价于：
     * String.format("%02x-%02x",env.args[2][0],env.args[2][1])
     * 而实际也是这样运行的
     * 特别注意的是，参数之间用逗号分隔，参数直接是路由表达式，不需要添加${}包裹
     *
     * datefmt表达式
     * #{[datefmt,env.args[2]],format=""}
     * 类似fmt表达式，但是只针对时间类型，应用SimpleDateFormat进行格式化
     * format参数即为合法的SimpleDataFormat格式化参数
     * 举例：
     * ${[datefmt,env.date],format="yyyy-MM-DD HH:mm:ss SSS"}
     *
     * trim表达式
     * #{[trim,env.data],prefix="",suffix="",sensible="",trimBefore="",trimAfter="",template="",ref=""}
     * 作用是去除env.data在渲染之后的字符串结果，去除前缀和后缀，并且可以设置是否大小写敏感
     * prefix:要去除的前缀，可以是多个，多个用|分隔
     * suffix:要去除的后缀，同prefix
     * sensible:是否对前后缀的大小写敏感，true时区分大小写，false不区分，默认区分
     * trimBefore:是否在去除前后缀之前进行trim，true去除，false不去除，默认不去除
     * trimAfter:是否在去除前后缀之后进行trim，同trimBefore
     * template和ref参数参考for或if表达式，不指定时直接是接入对象
     * 举例：
     * #{[trim,env.data],prefix="and|or",suffix=",",sensible="false",trimBefore="true"}
     *
     * cmd表达式
     * #{[cmd,env.data],command="",show="",charset=""}
     * 用于执行CMD命令行，基于Process调用
     * 将command命令进行渲染之后得到的命令，交给Process执行，当show为true时，
     * 将执行的回显作为结果渲染到模板中，否则只执行
     * command命令，可进行渲染
     * show是否捕获结果渲染到模板中
     * charset指定命令行输出的字符编码，不指定则使用UTF-8
     *
     * define表达式
     * #{[define,valueId],value=""}
     * 用于在模板中定义模板变量，以供之后模板渲染使用
     * 将在_def节点下，因此你需要这样取值：${_def.valueId}
     * valueId，就作为你之后JSON表达式取值的属性KEY
     * value,执行表达式的值，可进行渲染
     *
     * fori表达式
     * #{[fori,env.data],begin="",end="",step="",condition="",format="",separator="",prefix="",suffix="",template="",blank="",jump="",ref=""}
     * for表达式对应的是遍历一个可迭代对象
     * 而fori表达式是一个循环
     * 由数值（long）begin开始以步长step向end进行变化，以condition指定的逻辑比较符进行比较是否结束循环
     * 其余参数和for表达式含义一致
     * 表达式表达含义等价于：
     * for(long i=begin;i condition end;i+=step)
     * begin:指定开始数值，不指定时为0
     * end:指定结束数值，必须指定
     * step:指定循环的步长，不指定时为1
     * condition:指定循环判定条件运算符，默认为 !=
     * format:指定i的格式化模式，默认为直接转换为String，针对fori循环来说，对于这个i可能对于你来说是有意义的，且可能有格式化需求，比如固定长度补0的需求，因此使用format进行控制
     * 在_ctx变量中，在for的基础上，新增如下参数可用：
     * _ctx.fmti: 是一个String类型的值，也就是把i按照给定的format进行格式化之后的String
     * _ctx.i:是一个long类型的值，也就是当前循环下的i直接值，特别注意和_ctx.index的区别
     * -ctx.i是fori的循环变量i,而_ctx.index则是逻辑上实际渲染的第几个的索引
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
            }else if("fmt".equals(meta.action)){
                String format=meta.parameters.get("format");
                String values=meta.parameters.get("values");
                Object obj= ObjectFinder.getObjectByDotKeyWithReference(preparedParam,meta.routeExpression,basePackages);


                FmtGenerate gen=new FmtGenerate();
                gen.basePackages=basePackages;
                gen.root=preparedParam;
                gen.mapper=mapper;
                gen.data=obj;
                gen.format=format;
                gen.values=values;

                String key="_fmt_tmp_"+tmpIdx;
                builder.append("${").append(key).append("}");
                preparedParam.put(key,gen);
            }else if("datefmt".equals(meta.action)){
                String format=meta.parameters.get("format");
                Object obj= ObjectFinder.getObjectByDotKeyWithReference(preparedParam,meta.routeExpression,basePackages);

                DatefmtGenerate gen=new DatefmtGenerate();
                gen.basePackages=basePackages;
                gen.root=preparedParam;
                gen.mapper=mapper;
                gen.data=obj;
                gen.format=format;

                String key="_datefmt_tmp_"+tmpIdx;
                builder.append("${").append(key).append("}");
                preparedParam.put(key,gen);
            }else if("trim".equals(meta.action)){
                String prefix=meta.parameters.get("prefix");
                String suffix=meta.parameters.get("suffix");
                String sensible=meta.parameters.get("sensible");
                String trimBefore=meta.parameters.get("trimBefore");
                String trimAfter=meta.parameters.get("trimAfter");
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

                TrimGenerate gen=new TrimGenerate();
                gen.basePackages=basePackages;
                gen.root=preparedParam;
                gen.mapper=mapper;
                gen.data=obj;
                gen.prefix=prefix;
                gen.suffix=suffix;
                gen.sensible=sensible;
                gen.trimBefore=trimBefore;
                gen.trimAfter=trimAfter;
                gen.template=tpl;

                String key="_trim_tmp_"+tmpIdx;
                builder.append("${").append(key).append("}");
                preparedParam.put(key,gen);
            }else if("define".equals(meta.action)){
                String objId=meta.routeExpression;
                String value=meta.parameters.get("value");

                value=render(value,preparedParam,mapper,basePackages);

                if(!preparedParam.containsKey("_def")){
                    preparedParam.put("_def",new HashMap<String,String>());
                }
                Map<String,String> vals=(Map<String,String>)preparedParam.get("_def");
                vals.put(objId,value);
            }else if("cmd".equals(meta.action)){
                Object obj= ObjectFinder.getObjectByDotKeyWithReference(preparedParam,meta.routeExpression,basePackages);
                String command=meta.parameters.get("command");
                String show=meta.parameters.get("show");
                String charset=meta.parameters.get("charset");

                CmdGenerate gen=new CmdGenerate();
                gen.basePackages=basePackages;
                gen.root=preparedParam;
                gen.mapper=mapper;
                gen.data=obj;
                gen.command=command;
                gen.show=show;
                gen.charset=charset;


                String key="_cmd_tmp_"+tmpIdx;
                builder.append("${").append(key).append("}");
                preparedParam.put(key,gen);
            }if("fori".equals(meta.action)){
                String begin=meta.parameters.get("begin");
                String end=meta.parameters.get("end");
                String step=meta.parameters.get("step");
                String condition=meta.parameters.get("condition");
                String format=meta.parameters.get("format");
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

                ForiGenerate genFor=new ForiGenerate();
                genFor.template=tpl;
                genFor.root=preparedParam;
                genFor.data=obj;
                genFor.mapper=mapper;
                genFor.begin=begin;
                genFor.end=end;
                genFor.step=step;
                genFor.condition=condition;
                genFor.format=format;
                genFor.separator=separator;
                genFor.prefix=prefix;
                genFor.suffix=suffix;
                genFor.blank=blank;
                genFor.jump=jump;
                genFor.basePackages=basePackages;

                String key="_fori_tmp_"+tmpIdx;
                builder.append("${").append(key).append("}");
                preparedParam.put(key,genFor);
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
