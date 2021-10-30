package i2f.commons.core.utils.generator.regex.core.impl;

import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.generator.regex.RegexGenerator;
import i2f.commons.core.utils.generator.regex.core.IGenerate;
import lombok.Data;

import java.util.*;

/**
 *
 * @author ltb
 * @date 2021/10/20
 */
@Data
public class ForiGenerate implements IGenerate {
    public IMap<Object,String> mapper;
    public Object root;
    public Object data;
    public String begin;
    public String end;
    public String step;
    public String condition;
    public String format;
    public String separator=",";
    public String prefix;
    public String suffix;
    public String template;
    public String blank;
    public String jump;
    public List<String> basePackages;

    @Override
    public String gen() {
        return proxyFori();
    }

    public boolean isPass(long lbegin, String scond, long lend){
        if(scond==null){
            return false;
        }
        scond=scond.trim();
        if("".equals(scond)){
            return false;
        }
        if("==".equals(scond)){
            return lbegin==lend;
        }
        if("!=".equals(scond)){
            return lbegin!=lend;
        }
        if("<>".equals(scond)){
            return lbegin!=lend;
        }
        if(">".equals(scond)){
            return lbegin>lend;
        }
        if("<".equals(scond)){
            return lbegin<lend;
        }
        if(">=".equals(scond)){
            return lbegin>=lend;
        }
        if("<=".equals(scond)){
            return lbegin<=lend;
        }
        return false;
    }
    private  String proxyFori(){
        long lbegin=0L;
        long lend=0L;
        long lstep=1L;
        String scondition="!=";
        String sformat="%d";
        if(begin!=null){
            try{
                lbegin=Long.parseLong(begin.trim());
            }catch(Exception e){

            }
        }
        if(end!=null){
            try{
                lend=Long.parseLong(end.trim());
            }catch(Exception e){

            }
        }
        if(step!=null){
            try{
                lstep=Long.parseLong(step.trim());
            }catch(Exception e){

            }
        }
        if(condition!=null){
            scondition=condition.trim();
            if("".equals(scondition)){
                scondition="!=";
            }
        }
        if(format!=null){
            sformat=format.trim();
            if("".equals(scondition)){
                sformat="%d";
            }
        }

        if(template!=null){
            String tpl=template.trim();
            if("".equals(tpl)){
                template=null;
            }
        }

        if(blank!=null){
            String blk=blank.trim().toLowerCase();
            if("true".equals(blk)){
                if(!isPass(lbegin,scondition,lend)){
                    return "";
                }
            }

        }

        StringBuilder builder=new StringBuilder(256);
        if(prefix!=null){
            builder.append(prefix);
        }

        boolean isJump=false;
        if(jump!=null){
            jump=jump.trim();
            if("true".equals(jump)){
                isJump=true;
            }
            if("false".equals(jump)){
                isJump=false;
            }
        }

        int idx=0;
        boolean isFirst=true;
        while (isPass(lbegin,scondition,lend)){

            String str="";
            if(template!=null){
                Map<String,Object> param=new HashMap<>(16);
                param.put("_item",data);
                param.put("_root",root);
                Map<String,Object> ctx=new HashMap<>(16);
                ctx.put("first",isFirst);
                ctx.put("last", !isPass(lbegin+lstep,scondition,lend));
                ctx.put("index",idx);
                String fmt=String.format(sformat,lbegin);
                ctx.put("fmti",fmt);
                ctx.put("i",lbegin);

                param.put("_ctx",ctx);
                str= RegexGenerator.render(template,param,mapper,basePackages);
            }else{
                str= mapper.map(data);
            }
            boolean isLegal=false;
            if(!isJump){
                isLegal=true;
            }else{
                str=str.trim();
                if(!"".equals(str)){
                    isLegal=true;
                }
            }
            if(isLegal)
            {
                if(!isFirst){
                    if(separator!=null){
                        builder.append(separator);
                    }
                }
                builder.append(str);
                isFirst=false;
            }
            lbegin+=lstep;
            idx++;
        }

        if(suffix!=null){
            builder.append(suffix);
        }
        return builder.toString();
    }
}
