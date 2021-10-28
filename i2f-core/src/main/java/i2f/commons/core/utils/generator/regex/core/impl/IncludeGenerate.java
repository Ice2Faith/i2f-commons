package i2f.commons.core.utils.generator.regex.core.impl;

import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.generator.regex.RegexGenerator;
import i2f.commons.core.utils.generator.regex.core.IGenerate;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/10/28
 */
@Data
public class IncludeGenerate implements IGenerate {
    public IMap<Object,String> mapper;
    public Object root;
    public Object data;
    public String template;
    public List<String> basePackages;

    @Override
    public String gen() {
        if(data==null){
            return mapper.map(data);
        }
        if(template!=null){
            String tpl=template.trim();
            if("".equals(tpl)){
                template=null;
            }
        }
        int slen=template==null?64:Math.max(template.length(),64);

        StringBuilder builder=new StringBuilder(slen);
        if(template!=null){
            Map<String,Object> param=new HashMap<>(16);
            param.put("_item",data);
            param.put("_root",root);
            Map<String,Object> ctx=new HashMap<>(16);

            param.put("_ctx",ctx);
            String str= RegexGenerator.render(template,param,mapper,basePackages);
            builder.append(str);
        }else{
            String str= mapper.map(data);
            builder.append(str);
        }
        return builder.toString();
    }
}
