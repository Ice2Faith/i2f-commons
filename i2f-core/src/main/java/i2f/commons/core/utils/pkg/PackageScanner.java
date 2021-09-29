package i2f.commons.core.utils.pkg;


import i2f.commons.core.utils.pkg.core.PackageBaseScanner;
import i2f.commons.core.utils.pkg.data.ClassMetaData;
import i2f.commons.core.utils.pkg.data.ClassMetaTree;
import i2f.commons.core.utils.pkg.filter.IClassFilter;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ltb
 * @date 2021/9/23
 */
public class PackageScanner {
    private static volatile boolean isInitialed=false;
    private static volatile List<ClassMetaData> classNames = new ArrayList<>();
    private static volatile List<ClassMetaData> usualClassNames=new ArrayList<>();

    private static volatile boolean USE_TREE_MODE=true;
    private static volatile ClassMetaTree tree;

    public static void initWorkEnv(boolean useDefault,String ... basePackages){
        List<ClassMetaData> sumClassNames=new ArrayList<>(PackageBaseScanner.CLASS_NAMES_COUNT);
        List<ClassMetaData> useClassNames=new ArrayList<>(PackageBaseScanner.CLASS_NAMES_COUNT);
        List<ClassMetaData> defClassNames=new ArrayList<>();
        List<ClassMetaData> pkgClassNames=new ArrayList<>();
        if(useDefault){
            defClassNames = PackageBaseScanner.scanAllClassNames();
        }
        if(basePackages!=null && basePackages.length>0){
            pkgClassNames=PackageBaseScanner.scanClassNamesBasePackages(basePackages);
        }
        Set<String> uqSet=new HashSet<>(PackageBaseScanner.CLASS_NAMES_COUNT);
        for(ClassMetaData item : defClassNames){
            String key=item.getClassName()+item.getClazz();
            if(!uqSet.contains(key)){
                uqSet.add(key);
                sumClassNames.add(item);
                if(isUsualClass(item)){
                    useClassNames.add(item);
                }
            }
        }
        for(ClassMetaData item : pkgClassNames){
            String key=item.getClassName()+item.getClazz();
            if(!uqSet.contains(key)){
                uqSet.add(key);
                sumClassNames.add(item);
                if(isUsualClass(item)){
                    useClassNames.add(item);
                }
            }
        }

        classNames=sumClassNames;
        usualClassNames=useClassNames;
        isInitialed=true;
        if(USE_TREE_MODE){
            tree=new ClassMetaTree(usualClassNames);
        }
    }

    public static boolean isUsualClass(ClassMetaData item){
        Class clazz=item.getClazz();
        String className=item.getClassName();
        if(clazz==null){ //类成功加载
            return false;
        }
        if(className.contains("-")){ //是合法类名
            return false;
        }
        if(className.contains("$")){ //不是匿名类
            return false;
        }
        if(!Modifier.isPublic(clazz.getModifiers())){ // 是公开类
            return false;
        }
        return true;
    }

    public static List<ClassMetaData> scanClasses(IClassFilter filter, Class entryClazz){
        String clazzName= entryClazz.getName();
        int lidx=clazzName.lastIndexOf(".");
        int fidx=clazzName.indexOf(".");
        String pkgName=lidx>=0?clazzName.substring(0,lidx):clazzName;
        String rootPkgName=fidx>=0?clazzName.substring(0,fidx):clazzName;
        if(!isInitialed){
            initWorkEnv(true,rootPkgName);
        }
        return scanClasses(filter,pkgName);
    }

    public static List<ClassMetaData> scanClasses(IClassFilter filter, String ... basePacks){
        String[] basePackages=PackageBaseScanner.getShortlyPrefixes(basePacks).toArray(new String[0]);
        if(!isInitialed){
            initWorkEnv(true,basePackages);
        }
        if(USE_TREE_MODE){
            List<ClassMetaData> list=tree.fetchBasePackages(filter, basePackages);
            return list;
        }
        int size=basePackages.length;;
        String[] stdPacks=new String[size];
        for(int i=0;i<basePackages.length;i++){
            if(basePackages[i]!=null && !"".equals(basePackages[i])) {
                stdPacks[i] = basePackages[i] + ".";
            }else{
                stdPacks[i]=null;
            }
        }
        List<ClassMetaData> ret=new ArrayList<>();
        for(ClassMetaData item : usualClassNames){
            String className=item.getClassName();
            int clen=className.length();
            boolean isTarget=false;
            if(basePackages==null || basePackages.length==0){
                isTarget=true;
            }else{
                for(String pack : stdPacks){
                    if(pack==null || "".equals(pack)){
                        isTarget=true;
                        break;
                    }else{
                        int plen=pack.length();
                        if(plen>clen){
                            continue;
                        }
                        if(clen>=plen){
                            if(className.charAt(plen-1)!=pack.charAt(plen-1)){
                                continue;
                            }
                        }
                        if(className.startsWith(pack)){
                            isTarget=true;
                            break;
                        }
                    }


                }
            }
            if(isTarget){
                if(filter==null || filter.save(item)){
                    ret.add(item);
                }
            }

        }
        return ret;
    }

}
