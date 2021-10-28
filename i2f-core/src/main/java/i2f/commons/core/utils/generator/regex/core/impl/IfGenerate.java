package i2f.commons.core.utils.generator.regex.core.impl;

import i2f.commons.core.data.Pair;
import i2f.commons.core.data.Triple;
import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.generator.regex.RegexGenerator;
import i2f.commons.core.utils.generator.regex.core.IGenerate;
import i2f.commons.core.utils.generator.regex.core.ObjectFinder;
import i2f.commons.core.utils.reflect.core.resolver.base.ConvertResolver;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ltb
 * @date 2021/10/20
 */
@Data
public class IfGenerate implements IGenerate {
    public IMap<Object,String> mapper;
    public Object root;
    public Object data;
    public String template;
    public String test;
    public List<String> basePackages;

    @Override
    public String gen() {
        boolean isPass=test();
        if(!isPass){
            return "";
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

    private boolean test(){
        if(test==null){
            return true;
        }
        test=test.trim();
        if("".equals(test)){
            return true;
        }

        List<Pair<String,String>> conds=new ArrayList<>(6);

        Pattern pattern=Pattern.compile("\\s+(&&|\\|\\|)\\s+");
        Matcher matcher=pattern.matcher(test);
        String lastGroup="&&";
        int lidx=0;
        while (matcher.find()){
            MatchResult result=matcher.toMatchResult();
            String cond=test.substring(lidx,result.start());
            lidx=result.end();
            String group=matcher.group();
            Pair<String,String> pair=new Pair<>(lastGroup,cond);
            lastGroup=group;
            conds.add(pair);
        }
        String cond=test.substring(lidx);
        if(!"".equals(cond)){
            Pair<String,String> pair=new Pair<>(lastGroup,cond);
            conds.add(pair);
        }

        String regex="";
        List<Pair<String, Triple<String,String,String>>> legalConds=new ArrayList<>(conds.size());
        for(Pair<String,String> item : conds){
            String cnd=item.getVal();
            cnd=cnd.trim();
            if("".equals(cnd)){
                continue;
            }
            if(!cnd.matches(CONDITION_REGEX)){
                continue;
            }
            item.val=cnd;
            item.key=item.key.trim();
            Triple<String,String,String> trp=inflateCond(item.val);

            Pair<String,Triple<String,String,String>> pair=new Pair<>();
            pair.key=item.key.trim();
            pair.val=trp;
            legalConds.add(pair);
        }

        Map<String,Object> param=new HashMap<>(16);
        param.put("_item",data);
        param.put("_root",root);
        Map<String,Object> ctx=new HashMap<>(16);

        param.put("_ctx",ctx);
        return runCond(legalConds,param);
    }

    public static boolean runCond(List<Pair<String, Triple<String,String,String>>> conds,Object item){
        List<Pair<String,Boolean>> bconds=new ArrayList<>(conds.size());
        for(Pair<String,Triple<String,String,String>> cur : conds){
            boolean brs=getCondResult(cur.val,item);
            Pair<String,Boolean> bcur=new Pair<>(cur.key,brs);
            bconds.add(bcur);
        }

        boolean retVal = calcBooleanResult(bconds);

        return retVal;

    }

    public static boolean calcBooleanResult(List<Pair<String, Boolean>> bconds) {
        Stack<Boolean> valStack=new Stack<>();
        Stack<String> sigStack=new Stack<>();
        int i=0;
        while(i< bconds.size()){
            Pair<String,Boolean> cur= bconds.get(i);
            if(i==0){
                sigStack.add("=");
                valStack.add(cur.val);
                i++;
                continue;
            }
            String cope=cur.key;
            String tope=sigStack.peek();
            if(("&&".equals(cope) && "||".equals(tope))
            || ("&&".equals(cope) && "=".equals(tope))){
                Boolean tval=valStack.pop();
                Boolean cval=cur.val;
                Boolean rval=tval&&cval;
                valStack.push(rval);
                i++;
                continue;
            }
            valStack.push(cur.val);
            sigStack.push(cur.key);
            i++;
        }
        boolean retVal=valStack.pop();
        while(!valStack.isEmpty()){
          boolean sval=valStack.pop();
          retVal=retVal||sval;
        }
        return retVal;
    }

    public static boolean getCondResult(Triple<String,String,String> trp,Object item){
        String ope=trp.sec;
        Object param=item;
        Object left=parseValue(trp.fst,param);
        Object right=parseValue(trp.trd,param);


        if("==".equals(ope)){
            // all is null
            if(left == right){
                return true;
            }
            //any is null
            if(left==null || right==null){
                return false;
            }
            // all not null

            //can to number
            if(ConvertResolver.canConvert2Number(left)
                    && ConvertResolver.canConvert2Number(right)){
                BigDecimal lv=ConvertResolver.convert2Number(left);
                BigDecimal rv=ConvertResolver.convert2Number(right);
                return lv.compareTo(rv)==0;
            }
            // can to date
            if(ConvertResolver.canConvert2(Date.class,left)
            && ConvertResolver.canConvert2(Date.class,right)){
                Object lv=ConvertResolver.tryConvertType(Date.class,left);
                Object rv=ConvertResolver.tryConvertType(Date.class,right);
                return lv.equals(rv);
            }
            // can to string,or other obj
            return left.equals(right);
        }else if("!=".equals(ope)){
            // all is null
            if(left == right){
                return false;
            }
            //any is null
            if(left==null || right==null){
                return true;
            }
            // all not null

            //can to number
            if(ConvertResolver.canConvert2Number(left)
                    && ConvertResolver.canConvert2Number(right)){
                BigDecimal lv=ConvertResolver.convert2Number(left);
                BigDecimal rv=ConvertResolver.convert2Number(right);
                return lv.compareTo(rv)==0;
            }
            // can to date
            if(ConvertResolver.canConvert2(Date.class,left)
                    && ConvertResolver.canConvert2(Date.class,right)){
                Object lv=ConvertResolver.tryConvertType(Date.class,left);
                Object rv=ConvertResolver.tryConvertType(Date.class,right);
                return lv.equals(rv);
            }
            // can to string,or other obj
            return !left.equals(right);
        }else if(">".equals(ope)){
            // all is null
            if(left == right){
                return false;
            }
            //any is null
            if(left==null || right==null){
                return false;
            }
            // all not null

            //can to number
            if(ConvertResolver.canConvert2Number(left)
                    && ConvertResolver.canConvert2Number(right)){
                BigDecimal lv=ConvertResolver.convert2Number(left);
                BigDecimal rv=ConvertResolver.convert2Number(right);
                return lv.compareTo(rv)>0;
            }

            // can to date
            if(ConvertResolver.canConvert2(Date.class,left)
                    && ConvertResolver.canConvert2(Date.class,right)){
                Date lv=(Date)ConvertResolver.tryConvertType(Date.class,left);
                Date rv=(Date)ConvertResolver.tryConvertType(Date.class,right);
                return lv.compareTo(rv)>0;
            }
            // can to string,or other obj
            if(left instanceof String
            &&right instanceof String){
                String lv=(String)left;
                String rv=(String)right;
                return lv.compareTo(rv)>0;
            }
            if(left.getClass().equals(right.getClass())){
                if(left instanceof Comparable){
                    Comparable cl=(Comparable)left;
                    Comparable cr=(Comparable)right;
                    return cl.compareTo(cr)>0;
                }
            }
            return false;
        }else if("<".equals(ope)){
            // all is null
            if(left == right){
                return false;
            }
            //any is null
            if(left==null || right==null){
                return false;
            }
            // all not null

            //can to number
            if(ConvertResolver.canConvert2Number(left)
                    && ConvertResolver.canConvert2Number(right)){
                BigDecimal lv=ConvertResolver.convert2Number(left);
                BigDecimal rv=ConvertResolver.convert2Number(right);
                return lv.compareTo(rv)<0;
            }

            // can to date
            if(ConvertResolver.canConvert2(Date.class,left)
                    && ConvertResolver.canConvert2(Date.class,right)){
                Date lv=(Date)ConvertResolver.tryConvertType(Date.class,left);
                Date rv=(Date)ConvertResolver.tryConvertType(Date.class,right);
                return lv.compareTo(rv)<0;
            }
            // can to string,or other obj
            if(left instanceof String
                    &&right instanceof String){
                String lv=(String)left;
                String rv=(String)right;
                return lv.compareTo(rv)<0;
            }
            if(left.getClass().equals(right.getClass())){
                if(left instanceof Comparable){
                    Comparable cl=(Comparable)left;
                    Comparable cr=(Comparable)right;
                    return cl.compareTo(cr)<0;
                }
            }
            return false;
        }else if(">=".equals(ope)){
            // all is null
            if(left == right){
                return true;
            }
            //any is null
            if(left==null || right==null){
                return false;
            }
            // all not null

            //can to number
            if(ConvertResolver.canConvert2Number(left)
                    && ConvertResolver.canConvert2Number(right)){
                BigDecimal lv=ConvertResolver.convert2Number(left);
                BigDecimal rv=ConvertResolver.convert2Number(right);
                return lv.compareTo(rv)>=0;
            }

            // can to date
            if(ConvertResolver.canConvert2(Date.class,left)
                    && ConvertResolver.canConvert2(Date.class,right)){
                Date lv=(Date)ConvertResolver.tryConvertType(Date.class,left);
                Date rv=(Date)ConvertResolver.tryConvertType(Date.class,right);
                return lv.compareTo(rv)>=0;
            }
            // can to string,or other obj
            if(left instanceof String
                    &&right instanceof String){
                String lv=(String)left;
                String rv=(String)right;
                return lv.compareTo(rv)>=0;
            }
            if(left.getClass().equals(right.getClass())){
                if(left instanceof Comparable){
                    Comparable cl=(Comparable)left;
                    Comparable cr=(Comparable)right;
                    return cl.compareTo(cr)>=0;
                }
            }
            return false;
        }else if("<=".equals(ope)){
            // all is null
            if(left == right){
                return true;
            }
            //any is null
            if(left==null || right==null){
                return false;
            }
            // all not null

            //can to number
            if(ConvertResolver.canConvert2Number(left)
            && ConvertResolver.canConvert2Number(right)){
                BigDecimal lv=ConvertResolver.convert2Number(left);
                BigDecimal rv=ConvertResolver.convert2Number(right);
                return lv.compareTo(rv)<=0;
            }

            // can to date
            if(ConvertResolver.canConvert2(Date.class,left)
                    && ConvertResolver.canConvert2(Date.class,right)){
                Date lv=(Date)ConvertResolver.tryConvertType(Date.class,left);
                Date rv=(Date)ConvertResolver.tryConvertType(Date.class,right);
                return lv.compareTo(rv)<=0;
            }
            // can to string,or other obj
            if(left instanceof String
                    &&right instanceof String){
                String lv=(String)left;
                String rv=(String)right;
                return lv.compareTo(rv)<=0;
            }
            if(left.getClass().equals(right.getClass())){
                if(left instanceof Comparable){
                    Comparable cl=(Comparable)left;
                    Comparable cr=(Comparable)right;
                    return cl.compareTo(cr)<=0;
                }
            }
            return false;
        }else if("instanceof".equals(ope)){
            if(left==null){
                return false;
            }
            if(right==null){
                return false;
            }
            if(!(right instanceof Class)){
                return false;
            }
            Class lclazz=left.getClass();
            Class rclazz=(Class)right;
            return rclazz.isInstance(left);
        }else if("match".equals(ope)){
            String sleft=String.valueOf(left);
            String sright=String.valueOf(right);
            return sleft.matches(sright);
        }

        return false;
    }

    public static Object parseValue(String str,Object param){
        if(str.matches(NUMBER_REGEX)){
            return new BigDecimal(str);
        }else if(str.matches(RegexGenerator.SINGLE_QUOTE_STRING_REGEX)){
            return str.substring(1,str.length()-1);
        }else if(str.matches(RegexGenerator.OBJECT_FIELD_ROUTE_REGEX)){
            return ObjectFinder.getObjectByDotKeyWithReference(param,str);
        }
        return null;
    }

    public static Triple<String,String,String> inflateCond(String cond){
        Triple<String,String,String> trp=new Triple<>();
        Pattern pattern=Pattern.compile(OPERATOR_REGEX);
        Matcher matcher=pattern.matcher(cond);
        if(matcher.find()){
            MatchResult result=matcher.toMatchResult();
            String group=matcher.group();
            trp.sec=group.trim();
            trp.fst=cond.substring(0, result.start()).trim();
            trp.trd=cond.substring(result.end()).trim();
        }

        return trp;
    }

    public static final String NUMBER_REGEX ="[+|-]?(0|[1-9][0-9]*(.[0-9]+)?)";
    public static final String OPERATOR_REGEX ="==|\\!=|\\>=|\\<=|\\>|\\<|instanceof|match";
    public static final String CONDITION_REGEX=RegexGenerator.OBJECT_FIELD_ROUTE_REGEX+"\\s*("+ OPERATOR_REGEX +")\\s*(("+RegexGenerator.OBJECT_FIELD_ROUTE_REGEX+")|("+ NUMBER_REGEX +")|("+RegexGenerator.SINGLE_QUOTE_STRING_REGEX+"))";
}
