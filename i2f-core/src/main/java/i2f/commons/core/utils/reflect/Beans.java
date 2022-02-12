package i2f.commons.core.utils.reflect;


import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/1/26 14:41
 * @desc
 */
public class Beans {
    public static volatile ConcurrentHashMap<Class,Set<Constructor>> initsCache=new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<Class,Set<Field>> fieldsCache=new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<Class,Set<Method>> methodsCache=new ConcurrentHashMap<>();
    private Class mClazz;
    private Object mObj;
    public Beans(){}
    public Beans(Object val){
        this.obj(val);
    }
    public Beans(Class clz,Object ... args){
        this.clazz(clz,args);
    }

    public void obj(Object val){
        this.mObj=val;
        if(this.mObj!=null){
            mClazz=this.mObj.getClass();
        }
    }
    public Object obj(){
        return mObj;
    }
    public Class clazz(){
        return mClazz;
    }
    public void clazz(Class clz,Object ... args){
        this.mClazz=clz;
        Constructor cons=init(args);
        if(cons==null){
            throw new ReflectException("not found init constructor for class:"+ clz.getName());
        }
        try{
            this.mObj=cons.newInstance(args);
        }catch(Exception e){
            throw new ReflectException(e);
        }
    }

    public Constructor init(Object ... args){
        if(mClazz==null){
            return null;
        }
        Set<Constructor> list=new HashSet<>();
        if(initsCache.containsKey(mClazz)){
            list=initsCache.get(mClazz);
        }else{
            list=findAllConstructors(mClazz);
            initsCache.put(mClazz, list);
        }
        Constructor cons=null;
        for(Constructor item : list){
            Class[] types=item.getParameterTypes();
            if(canAdaptArgs(types,args)){
                cons=item;
                break;
            }
        }
        return cons;
    }

    public String simpleName(){
        if(mObj==null){
            return null;
        }
        Class clazz= mObj.getClass();
        return clazz.getSimpleName();
    }
    public String name(){
        if(mObj==null){
            return null;
        }
        Class clazz= mObj.getClass();
        return clazz.getName();
    }
    public String packageName(){
        if(mObj==null){
            return null;
        }
        Class clazz= mObj.getClass();
        String name= clazz.getName();
        int idx=name.lastIndexOf(".");
        if(idx>=0){
            return name.substring(0,idx);
        }
        return name;
    }

    public Object field(String fieldName) {
        if(mObj==null){
            return null;
        }
        if(mObj instanceof Map){
            return ((Map)mObj).get(fieldName);
        }
        Class clazz=mObj.getClass();
        Set<Field> set=fields(false,fieldName);
        if(set.size()!=1){
            throw new ReflectException("wait one but find "+set.size()+".");
        }
        Field fd=set.iterator().next();
        try{
            fd.setAccessible(true);
            return fd.get(mObj);
        }catch(Exception e){
            throw new ReflectException(e);
        }
    }

    public boolean field(String fieldName,Object val){
        if(mObj==null){
            return false;
        }
        if(mObj instanceof Map){
            ((Map)mObj).put(fieldName,val);
            return true;
        }
        Class clazz=mObj.getClass();
        Set<Field> set=fields(false,fieldName);
        if(set.size()!=1){
            throw new ReflectException("wait one but find "+set.size()+".");
        }
        Field fd=set.iterator().next();
        try{
            fd.setAccessible(true);
            fd.set(mObj,val);
            return true;
        }catch(Exception e){
            throw new ReflectException(e);
        }
    }

    public Object get(String fieldName){
        if(mObj==null){
            return false;
        }
        if(mObj instanceof Map){
            return ((Map)mObj).get(fieldName);
        }
        Method mtd=getter(fieldName);
        try{
            if(mtd!=null){
                return mtd.invoke(mObj);
            }
        }catch(Exception e){
            throw new ReflectException(e);
        }
        return null;
    }

    public boolean set(String fieldName,Object val){
        if(mObj==null){
            return false;
        }
        if(mObj instanceof Map){
            ((Map)mObj).put(fieldName,val);
            return true;
        }
        Method mtd=setter(fieldName);
        try{
            if(mtd!=null){
                mtd.invoke(mObj,val);
                return true;
            }
        }catch(Exception e){
            throw new ReflectException(e);
        }
        return false;
    }

    public Map<String,Object> fields(String ... fieldNames){
        Map<String,Object> map=new HashMap<>();
        for(String item : fieldNames){
            Object val=field(item);
            map.put(item,val);
        }
        return map;
    }

    public void fields(Map<String,Object> map){
        if(map==null){
            return;
        }
        for(Map.Entry<String,Object> entry : map.entrySet()){
            field(entry.getKey(),entry.getValue());
        }
    }

    public Map<String,Object> gets(String ... fieldNames){
        Map<String,Object> map=new HashMap<>();
        for(String item : fieldNames){
            Object val=get(item);
            map.put(item,val);
        }
        return map;
    }
    public void sets(Map<String,Object> map){
        if(map==null){
            return;
        }
        for(Map.Entry<String,Object> entry : map.entrySet()){
            set(entry.getKey(),entry.getValue());
        }
    }

    public Method getter(String fieldName){
        if(mObj==null){
            return null;
        }
        Class clazz=mObj.getClass();
        Set<Field> set=fields(false,fieldName);
        if(set.size()!=1){
            throw new ReflectException("wait one but find "+set.size()+".");
        }
        Field fd=set.iterator().next();
        Class type=fd.getType();
        if(boolean.class.equals(type) || Boolean.class.equals(type)){
            String mname="is"+firstUpper(fieldName);
            Set<Method> mts=methods(false,mname);
            if(mts.size()!=1){
                throw new ReflectException("wait one but find "+set.size()+".");
            }
            return mts.iterator().next();
        }
        String mname="get"+firstUpper(fieldName);
        Set<Method> mts=methods(false,mname);
        if(mts.size()!=1){
            throw new ReflectException("wait one but find "+set.size()+".");
        }
        return mts.iterator().next();
    }
    public Method setter(String fieldName){
        if(mObj==null){
            return null;
        }
        Class clazz=mObj.getClass();
        Set<Field> set=fields(false,fieldName);
        if(set.size()!=1){
            throw new ReflectException("wait one but find "+set.size()+".");
        }
        Field fd=set.iterator().next();
        Class type=fd.getType();
        String mname="set"+firstUpper(fieldName);
        Set<Method> mts=methods(false,mname);
        if(mts.size()!=1){
            throw new ReflectException("wait one but find "+set.size()+".");
        }
        return mts.iterator().next();
    }

    public Set<Field> fields(boolean like,String ... names){
        Set<Field> ret=new HashSet<>();
        if(mObj==null){
            return ret;
        }

        Class clazz=mObj.getClass();
        Set<Field> list=new HashSet<>();
        if(fieldsCache.containsKey(clazz)){
            list=fieldsCache.get(clazz);
        }else{
            list=findAllFields(clazz);
            fieldsCache.put(clazz, list);
        }

        for (Field item : list) {
            if (names.length == 0) {
                ret.add(item);
                continue;
            }
            String fname = item.getName();
            for (String name : names) {
                if (like) {
                    if (fname.contains(name)) {
                        ret.add(item);
                        break;
                    }
                } else {
                    if (fname.equals(name)) {
                        ret.add(item);
                        break;
                    }
                }
            }
        }

        return ret;
    }
    public Set<Method> methods(boolean like,String ... names){
        Set<Method> ret=new HashSet<>();
        if(mObj==null){
            return ret;
        }
        Class clazz=mObj.getClass();

        Set<Method> list=new HashSet<>();
        if(methodsCache.containsKey(clazz)){
            list=methodsCache.get(clazz);
        }else{
            list=findAllMethods(clazz);
            methodsCache.put(clazz, list);
        }

        for(Method item : list){
            if(names.length==0){
                ret.add(item);
                continue;
            }
            String fname=item.getName();
            for(String name : names){
                if(like){
                    if(fname.contains(name)){
                        ret.add(item);
                        break;
                    }
                }else{
                    if(fname.equals(name)){
                        ret.add(item);
                        break;
                    }
                }
            }
        }

        return ret;
    }


    public Object invoke(String methodName,Object ... args){
        if(mObj==null){
            return null;
        }
        Class clazz=mObj.getClass();
        Method method=findMethod(clazz,methodName,args);
        if(method==null){
            throw new ReflectException("not found method ["+methodName+"] in class "+clazz.getName());
        }
        try{
            return method.invoke(mObj,args);
        }catch(Exception e){
            throw new ReflectException(e);
        }
    }

    public Object staticInvoke(Class clazz,String methodName,Object ... args){
        Method method=findMethod(clazz,methodName,args);
        if(method==null){
            throw new ReflectException("not found method ["+methodName+"] in class "+clazz.getName());
        }
        if(!Modifier.isStatic(method.getModifiers())){
            throw new ReflectException("wait a static method ,but found is not.");
        }
        try{
            return method.invoke(null,args);
        }catch(Exception e){
            throw new ReflectException(e);
        }
    }

    protected Method findMethod(Class clazz,String methodName,Object ... args){
        Set<Method> list=methods(false,methodName);
        if(list.size()==0){
            throw new ReflectException("wait least one method but found "+list.size());
        }
        if(list.size()==1){
            return list.iterator().next();
        }

        Method method=null;
        for(Method item : list){
            Class[] types=item.getParameterTypes();
            if(canAdaptArgs(types,args)){
                method=item;
                break;
            }
        }
        return method;
    }

    protected boolean canAdaptArgs(Class[] targetTypes,Object[] args){
        if(targetTypes.length==0 && targetTypes.length==args.length){
            return true;
        }
        if(targetTypes.length==0 && args.length!=0){
            return false;
        }
        Class[] types=targetTypes;
        Class[] argsTypes=new Class[args.length];
        for(int i=0;i<args.length;i+=1){
            if(args[i]!=null){
                argsTypes[i]=args[i].getClass();
            }else{
                argsTypes[i]=null;
            }
        }
        int compareTypeCount= types.length;
        boolean isVarList=false;
        //变长参数
        if(types[types.length-1].isArray()){
            compareTypeCount--;
            isVarList=true;
        }
        if(args.length<compareTypeCount){
            return false;
        }
        boolean canAdapt=true;
        for(int i=0;i<compareTypeCount;i++){
            if(!isDefaultConvertibleType(types[i],argsTypes[i])){
                canAdapt=false;
                break;
            }
        }
        if(canAdapt){
            if(args.length==compareTypeCount){
                return true;
            }else{
                if(isVarList){
                    boolean canVar=true;
                    Class varType=types[types.length-1];
                    for (int i = compareTypeCount; i < args.length; i++) {
                        if(!isDefaultConvertibleType(varType,argsTypes[i])){
                            canVar=false;
                            break;
                        }
                    }
                    if(canVar){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isDefaultConvertibleType(Class targetType,Class srcType){
        if(srcType==null){
            if(isBaseType(targetType)){
                return false;
            }
            return true;
        }
        if(targetType==null){
            return false;
        }
        if(targetType==Object.class){
            return true;
        }
        if(isInTypes(srcType,targetType)){
            return true;
        }
        if(isInTypes(targetType,Integer.class,int.class)
                && isInTypes(srcType,int.class,Integer.class,short.class,byte.class)){
            return true;
        }
        if(isInTypes(targetType,Long.class,long.class)
                && isInTypes(srcType,Long.class,long.class,int.class,Integer.class,short.class,byte.class)){
            return true;
        }
        if(isInTypes(targetType,Short.class,short.class)
                && isInTypes(srcType,Long.class,long.class,int.class,Integer.class,short.class,byte.class)){
            return true;
        }
        if(isInTypes(targetType,Byte.class,byte.class)
                && isInTypes(srcType,Long.class,long.class,int.class,Integer.class,short.class,byte.class)){
            return true;
        }
        if(isInTypes(targetType,Character.class,char.class)
                && isInTypes(srcType,Character.class,char.class)){
            return true;
        }
        if(isInTypes(targetType,Float.class,float.class)
                && isInTypes(srcType,Float.class,float.class,Long.class,long.class,int.class,Integer.class,short.class,byte.class)){
            return true;
        }
        if(isInTypes(targetType,Double.class,double.class)
                && isInTypes(srcType,Double.class,double.class,Float.class,float.class,Long.class,long.class,int.class,Integer.class,short.class,byte.class)){
            return true;
        }
        if(isInTypes(targetType, Date.class)
        && isInTypes(srcType,java.sql.Date.class,java.sql.Timestamp.class,java.sql.Time.class)){
            return true;
        }
        return false;
    }

    protected boolean isBaseType(Class target){
        if(isInTypes(target,int.class,long.class,short.class,char.class,byte.class,float.class,double.class)){
            return true;
        }
        return false;
    }

    protected boolean isInTypes(Class target,Class ... types){
        if(target==null){
            return false;
        }
        for(Class item : types){
            if(item.equals(target)){
                return true;
            }
            //该方法用于判定，父类target是否派生出了子类item
            if(target.isAssignableFrom(item)){
                return true;
            }
        }
        return false;
    }

    public Object find(String routeExp){
        return findJson(routeExp,mObj);
    }

    public void find(String routeExp,Object val){

    }

    protected Set<Constructor> findAllConstructors(Class clazz){
        Set<Constructor> ret=new HashSet<>();
        if(clazz==null){
            return ret;
        }
        Constructor[] fds1=clazz.getDeclaredConstructors();
        Constructor[] fds2=clazz.getConstructors();
        Constructor[][] fdsarr=new Constructor[2][];
        fdsarr[0]=fds1;
        fdsarr[1]=fds2;
        for(Constructor[] fds : fdsarr){
            for(Constructor item : fds) {
                ret.add(item);
            }
        }
        return ret;
    }

    protected Set<Field> findAllFields(Class clazz) {
        Set<Field> ret=new HashSet<>();
        if(clazz==null){
            return ret;
        }
        Field[] fds1=clazz.getDeclaredFields();
        Field[] fds2=clazz.getFields();
        Field[][] fdsarr=new Field[2][];
        fdsarr[0]=fds1;
        fdsarr[1]=fds2;
        for(Field[] fds : fdsarr){
            for(Field item : fds) {
                ret.add(item);
            }
        }
        return ret;
    }
    protected Set<Method> findAllMethods(Class clazz) {
        Set<Method> ret=new HashSet<>();
        if(clazz==null){
            return ret;
        }
        Method[] fds1=clazz.getDeclaredMethods();
        Method[] fds2=clazz.getMethods();
        Method[][] fdsarr=new Method[2][];
        fdsarr[0]=fds1;
        fdsarr[1]=fds2;
        for(Method[] fds : fdsarr) {
            for (Method item : fds) {
                ret.add(item);
            }
        }
        return ret;
    }

    protected Object findJson(String routeExp,Object val){
        if(val==null){
            return val;
        }
        if(routeExp==null){
            return val;
        }
        routeExp=routeExp.trim();
        if("".equals(routeExp)){
            return val;
        }
        String[] arr=routeExp.split("\\.",2);
        String attr=arr[0];
        if(attr.contains("[")){
            int idx=attr.indexOf("[");
            String pidx=attr.substring(idx);
            attr=attr.substring(0,idx);
            Beans bean=new Beans(val);
            Object nval=bean.field(attr);
            pidx=pidx.trim();
            pidx=pidx.substring(1,pidx.length()-1);
            if(pidx.matches("\\d+")){
                idx=Integer.parseInt(pidx);
                if(nval==null){
                    return nval;
                }
                Class nvalClazz=nval.getClass();
                if(nvalClazz.isArray()){
                    nval= Array.get(nval,idx);
                    if(arr.length>1){
                        return findJson(arr[1],nval);
                    }else{
                        return nval;
                    }
                }else if(nval instanceof Collection){
                    Collection col=(Collection)nval;
                    Object[] parr=col.toArray();
                    nval=parr[idx];
                    if(arr.length>1){
                        return findJson(arr[1],nval);
                    }else{
                        return nval;
                    }
                }
            }else{
                String key=pidx;
                if(nval instanceof Map){
                    Map pmap=(Map)nval;
                    nval=pmap.get(key);
                    if(arr.length>1){
                        return findJson(arr[1],nval);
                    }else{
                        return nval;
                    }
                }else{
                    nval=findJson(key,nval);
                    if(arr.length>1){
                        return findJson(arr[1],nval);
                    }else{
                        return nval;
                    }
                }
            }

        }else{
            Beans bean=new Beans(val);
            Object nval=bean.field(attr);
            if(arr.length>1){
                return findJson(arr[1],nval);
            }else{
                return nval;
            }
        }
        return val;
    }

    protected String firstUpper(String str){
        if(str==null || str.length()==0){
            return  str;
        }
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }

    @Override
    public int hashCode() {
        if(this.mObj!=null){
            return mObj.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this.mObj!=null){
            return mObj.equals(obj);
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        if(this.mObj!=null){
            return mObj.toString();
        }
        return super.toString();
    }
}
