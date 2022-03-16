package i2f.commons.core.utils.safe;

import i2f.commons.core.data.interfaces.IFilter;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/3/15 8:33
 * @desc
 */
public class Checker {
    public static ConcurrentHashMap<String, IFilter> validators=new ConcurrentHashMap<>();
    public static void registerValidator(String name,IFilter filter){
        validators.put(name,filter);
    }
    public static void registerValidator(IFilter filter){
        String cname=filter.getClass().getSimpleName();
        String name = cname.substring(0,1).toLowerCase()+cname.substring(1);
        validators.put(name,filter);
    }
    public static IFilter getValidator(String validatorName){
        return validators.get(validatorName);
    }
    public static IFilter getValidator(Class<IFilter> validatorClass){
        String cname=validatorClass.getSimpleName();
        String name = cname.substring(0,1).toLowerCase()+cname.substring(1);
        return validators.get(name);
    }

    public static CheckWorker thr(){
        return new CheckWorker(true);
    }
    public static CheckWorker chk(){
        return new CheckWorker(false);
    }

    public static class CheckException extends RuntimeException{
        public CheckException() {
        }

        public CheckException(String message) {
            super(message);
        }

        public CheckException(String message, Throwable cause) {
            super(message, cause);
        }

        public CheckException(Throwable cause) {
            super(cause);
        }
    }

    public static class CheckWorker{
        protected boolean thr=false;
        protected boolean ok=true;
        protected String message;
        protected Throwable ex;
        public CheckWorker(boolean thr){
            this.thr=thr;
        }

        public CheckWorker thr(){
            this.thr=true;
            return chk();
        }
        public boolean rs(){
            return ok;
        }
        public CheckWorker chk(){
            if(!ok){
                if(thr){
                    throw new CheckException(message,ex);
                }
            }
            return this;
        }
        public CheckWorker message(String message){
            this.message=message;
            return chk();
        }

        public CheckWorker isnull(Object obj){
            if(!ok){
                return chk();
            }
            if(obj==null){
                this.ok=false;
                this.message="argument not allow is null!";
            }
            return chk();
        }
        public CheckWorker isnull(Object obj,String message){
            if(!ok){
                return chk();
            }
            if(obj==null){
                ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker exnull(Object ... objs){
            if(!ok){
                return chk();
            }
            for(Object item : objs){
                if(item==null){
                    this.ok=false;
                    this.message="argument not allow is null!";
                    break;
                }
            }
            return chk();
        }
        public CheckWorker exnullMsg(String message,Object ... objs){
            if(!ok){
                return chk();
            }
            for(Object item : objs){
                if(item==null){
                    this.ok=false;
                    this.message=message;
                    break;
                }
            }
            return chk();
        }

        public CheckWorker isempty(String obj){
            if(!ok){
                return chk();
            }
            if(obj==null || "".equals(obj)){
                ok=false;
                this.message="argument not allow is empty!";
            }
            return chk();
        }
        public CheckWorker isempty(Object obj,String message){
            if(!ok){
                return chk();
            }
            if(obj==null || "".equals(obj)){
                ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker exempty(String ... objs){
            if(!ok){
                return chk();
            }
            for(String item : objs){
                if(item==null || "".equals(item)){
                    ok=false;
                    this.message="argument not allow is empty!";
                    break;
                }
            }
            return chk();
        }
        public CheckWorker exemptyMsg(String message,String ... objs){
            if(!ok){
                return chk();
            }
            for(String item : objs){
                if(item==null || "".equals(item)){
                    ok=false;
                    this.message=message;
                    break;
                }
            }
            return chk();
        }

        public CheckWorker iswhen(boolean condition,String message){
            if(!ok){
                return chk();
            }
            if(condition){
                ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker exwhenMsg(String message,boolean ... conditions){
            if(!ok){
                return chk();
            }
            for(boolean item : conditions){
                if(item){
                    ok=false;
                    this.message=message;
                    break;
                }
            }
            return chk();
        }

        public CheckWorker notwhen(boolean condition,String message){
            if(!ok){
                return chk();
            }
            if(!condition){
                ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker eq(Object obj,Object cmp){
            if(!ok){
                return chk();
            }
            if(obj==null && cmp==null){
                this.ok=false;
                this.message="object require equal target!";
                return chk();
            }
            if(obj!=null){
                if(obj.equals(cmp)){
                    this.ok=false;
                    this.message="object require equal target!";
                }
            }else if(cmp!=null){
                if(cmp.equals(obj)){
                    this.ok=false;
                    this.message="object require equal target!";
                }
            }
            return chk();
        }
        public CheckWorker eqMsg(String message,Object obj,Object cmp){
            if(!ok){
                return chk();
            }
            if(obj==null && cmp==null){
                this.ok=false;
                this.message=message;
                return chk();
            }
            if(obj!=null){
                if(obj.equals(cmp)){
                    this.ok=false;
                    this.message=message;
                }
            }else if(cmp!=null){
                if(cmp.equals(obj)){
                    this.ok=false;
                    this.message=message;
                }
            }
            return chk();
        }
        public CheckWorker neq(Object obj,Object cmp){
            if(!ok){
                return chk();
            }
            if(obj!=null){
                if(!obj.equals(cmp)){
                    this.ok=false;
                    this.message="object require equal target!";
                }
            }else if(cmp!=null){
                if(!cmp.equals(obj)){
                    this.ok=false;
                    this.message="object require equal target!";
                }
            }
            return chk();
        }
        public CheckWorker neqMsg(String message,Object obj,Object cmp){
            if(!ok){
                return chk();
            }
            if(obj!=null){
                if(!obj.equals(cmp)){
                    this.ok=false;
                    this.message=message;
                }
            }else if(cmp!=null){
                if(!cmp.equals(obj)){
                    this.ok=false;
                    this.message=message;
                }
            }
            return chk();
        }
        public CheckWorker exeq(Object obj,Object ... cmps){
            if(!ok){
                return chk();
            }
            for(Object item : cmps){
                if(item==null && obj==null){
                    continue;
                }
                if(obj!=null){
                    if(obj.equals(item)){
                        this.ok=false;
                        this.message="object require equal target!";
                    }
                }else if(item!=null){
                    if(item.equals(obj)){
                        this.ok=false;
                        this.message="object require equal target!";
                    }
                }
            }
            return chk();
        }
        public CheckWorker exeqMsg(String message,Object obj,Object ... cmps){
            if(!ok){
                return chk();
            }
            for(Object item : cmps){
                if(item==null && obj==null){
                    continue;
                }
                if(obj!=null){
                    if(obj.equals(item)){
                        this.ok=false;
                        this.message=message;
                    }
                }else if(item!=null){
                    if(item.equals(obj)){
                        this.ok=false;
                        this.message=message;
                    }
                }
            }
            return chk();
        }

        public CheckWorker gt(Number num,Number lower){
            if(!ok){
                return chk();
            }
            BigDecimal bnum = new BigDecimal(String.valueOf(num));
            BigDecimal blower = new BigDecimal(String.valueOf(lower));
            if(bnum.compareTo(blower)<0){
                this.ok=false;
                this.message="num("+bnum+") require gather than lower("+blower+")!";
            }
            return chk();
        }
        public CheckWorker lt(Number num,Number upper){
            if(!ok){
                return chk();
            }
            BigDecimal bnum = new BigDecimal(String.valueOf(num));
            BigDecimal bupper = new BigDecimal(String.valueOf(upper));
            if(bnum.compareTo(bupper)>0){
                this.ok=false;
                this.message="num("+bnum+") require lower than upper("+bupper+")!";
            }
            return chk();
        }
        public CheckWorker gte(Number num,Number lower){
            if(!ok){
                return chk();
            }
            BigDecimal bnum = new BigDecimal(String.valueOf(num));
            BigDecimal blower = new BigDecimal(String.valueOf(lower));
            if(bnum.compareTo(blower)<=0){
                this.ok=false;
                this.message="num("+bnum+") require gather than lower("+blower+")!";
            }
            return chk();
        }
        public CheckWorker lte(Number num,Number upper){
            if(!ok){
                return chk();
            }
            BigDecimal bnum = new BigDecimal(String.valueOf(num));
            BigDecimal bupper = new BigDecimal(String.valueOf(upper));
            if(bnum.compareTo(bupper)>=0){
                this.ok=false;
                this.message="num("+bnum+") require lower than upper("+bupper+")!";
            }
            return chk();
        }
        public CheckWorker between(Number num,Number lower,Number upper){
            if(!ok){
                return chk();
            }
            BigDecimal bnum = new BigDecimal(String.valueOf(num));
            BigDecimal blower = new BigDecimal(String.valueOf(lower));
            BigDecimal bupper = new BigDecimal(String.valueOf(upper));
            if(bnum.compareTo(blower)>=0 && bnum.compareTo(bupper)<0){
                this.ok=false;
                this.message="num("+bnum+") require between than ["+blower+" to "+upper+")!";
            }
            return chk();
        }

        public CheckWorker except(Throwable ex){
            if(!ok){
                return chk();
            }
            this.ok=false;
            this.message=ex.getMessage();
            this.ex=ex;
            return chk();
        }
        public CheckWorker exceptMsg(String message,Throwable ex){
            if(!ok){
                return chk();
            }
            this.ok=false;
            this.message=message;
            this.ex=ex;
            return chk();
        }

        public CheckWorker match(String patten,String str){
            if(!ok){
                return chk();
            }
            if(str==null || patten==null){
                this.ok=false;
                this.message="argument not allow is null when match!";
                return chk();
            }
            if(str.matches(patten)){
                this.ok=false;
                this.message="string not match patten!";
            }
            return chk();
        }
        public CheckWorker matchMsg(String message,String patten,String str){
            if(!ok){
                return chk();
            }
            if(str==null || patten==null){
                this.ok=false;
                this.message="argument not allow is null when match!";
                return chk();
            }
            if(str.matches(patten)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker notmatch(String patten,String str){
            if(!ok){
                return chk();
            }
            if(str==null || patten==null){
                this.ok=false;
                this.message="argument not allow is null when match!";
                return chk();
            }
            if(!str.matches(patten)){
                this.ok=false;
                this.message="string not match patten!";
            }
            return chk();
        }
        public CheckWorker notmatchMsg(String message,String patten,String str){
            if(!ok){
                return chk();
            }
            if(str==null || patten==null){
                this.ok=false;
                this.message="argument not allow is null when match!";
                return chk();
            }
            if(!str.matches(patten)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker exmatch(String patten,String ... strs){
            if(!ok){
                return chk();
            }
            if(patten==null){
                this.ok=false;
                this.message="argument not allow is null when match!";
                return chk();
            }
            for(String item : strs){
                if(item==null){
                    this.ok=false;
                    this.message="argument not allow is null when match!";
                    return chk();
                }
                if(item.matches(patten)){
                    this.ok=false;
                    this.message="string not match patten!";
                    break;
                }
            }
            return chk();
        }
        public CheckWorker exmatchMsg(String message,String patten,String ... strs){
            if(!ok){
                return chk();
            }
            if(patten==null){
                this.ok=false;
                this.message="argument not allow is null when match!";
                return chk();
            }
            for(String item : strs){
                if(item==null){
                    this.ok=false;
                    this.message="argument not allow is null when match!";
                    return chk();
                }
                if(item.matches(patten)){
                    this.ok=false;
                    this.message=message;
                    break;
                }
            }
            return chk();
        }

        public CheckWorker isInteger(String str){
            if(!ok){
                return chk();
            }
            if(str==null){
                this.ok=false;
                this.message="argument not allow is null!";
                return chk();
            }
            if(str.matches("^[+|-]?\\d+$")){
                this.ok=false;
                this.message="string require like integer!";
            }
            return chk();
        }
        public CheckWorker isFloat(String str){
            if(!ok){
                return chk();
            }
            if(str==null){
                this.ok=false;
                this.message="argument not allow is null!";
                return chk();
            }
            if(str.matches("^[+|-]?\\d+(\\.\\d+)?$")){
                this.ok=false;
                this.message="string require like float!";
            }
            return chk();
        }

        public CheckWorker isIntegerMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(str==null){
                this.ok=false;
                this.message="argument not allow is null!";
                return chk();
            }
            if(str.matches("^[+|-]?\\d+$")){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker isFloatMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(str==null){
                this.ok=false;
                this.message="argument not allow is null!";
                return chk();
            }
            if(str.matches("^[+|-]?\\d+(\\.\\d+)?$")){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker notInteger(String str){
            if(!ok){
                return chk();
            }
            if(str==null){
                this.ok=false;
                this.message="argument not allow is null!";
                return chk();
            }
            if(!str.matches("^[+|-]?\\d+$")){
                this.ok=false;
                this.message="string require like integer!";
            }
            return chk();
        }
        public CheckWorker notFloat(String str){
            if(!ok){
                return chk();
            }
            if(str==null){
                this.ok=false;
                this.message="argument not allow is null!";
                return chk();
            }
            if(!str.matches("^[+|-]?\\d+(\\.\\d+)?$")){
                this.ok=false;
                this.message="string require like float!";
            }
            return chk();
        }

        public CheckWorker notIntegerMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(str==null){
                this.ok=false;
                this.message="argument not allow is null!";
                return chk();
            }
            if(!str.matches("^[+|-]?\\d+$")){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker notFloatMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(str==null){
                this.ok=false;
                this.message="argument not allow is null!";
                return chk();
            }
            if(!str.matches("^[+|-]?\\d+(\\.\\d+)?$")){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isArray(Object obj){
            if(!ok){
                return chk();
            }
            if(obj==null){
                this.ok=false;
                this.message="argument require not null!";
                return chk();
            }
            Class clazz = obj.getClass();
            if(clazz.isArray()){
                this.ok=false;
                this.message="argument not allow is array!";
            }
            return chk();
        }

        public CheckWorker validate(String validatorName,Object obj,String message){
            if(!ok){
                return chk();
            }
            if(getValidator(validatorName).choice(obj)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker validate(Class<IFilter> validatorClass,Object obj,String message){
            if(!ok){
                return chk();
            }
            if(getValidator(validatorClass).choice(obj)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

    }

}
