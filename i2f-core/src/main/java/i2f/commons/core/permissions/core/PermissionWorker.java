package i2f.commons.core.permissions.core;


import i2f.commons.core.permissions.annotations.RequirePermissions;
import i2f.commons.core.permissions.data.PermissionSet;

import java.lang.reflect.Method;
import java.util.Vector;

public class PermissionWorker {
    public static Vector<RequirePermissions> getAllRequires(Method method){
        Class clazz=method.getDeclaringClass();
        Vector<RequirePermissions> clsPers=getAllRequires(clazz);

        RequirePermissions mAnn=method.getAnnotation(RequirePermissions.class);
        if(mAnn!=null){
            clsPers.add(mAnn);
        }
        mAnn=method.getDeclaredAnnotation(RequirePermissions.class);
        if(mAnn!=null){
            clsPers.add(mAnn);
        }

        return clsPers;
    }
    public static Vector<RequirePermissions> getAllRequires(Class clazz){
        Vector<RequirePermissions> ret=new Vector<>();
        if(clazz.equals(Object.class)){
            return ret;
        }
        RequirePermissions mAnn=(RequirePermissions)clazz.getAnnotation(RequirePermissions.class);
        if(mAnn!=null){
            ret.add(mAnn);
        }
        mAnn=(RequirePermissions)clazz.getDeclaredAnnotation(RequirePermissions.class);
        if(mAnn!=null){
            ret.add(mAnn);
        }

        Vector<RequirePermissions> prtPers=getAllRequires(clazz.getSuperclass());
        if(prtPers.size()>0){
            for(RequirePermissions item : prtPers){
                ret.add(item);
            }
        }
        return ret;
    }

    public static PermissionSet parseRequirePermissions(Vector<RequirePermissions> pers){
        PermissionSet ret=new PermissionSet();
        for(RequirePermissions item : pers){
            if(item==null){
                continue;
            }
            int[] curIds= item.ids();
            if(curIds!=null && curIds.length>0){
                for(Integer pid : curIds){
                    if(pid!=null){
                        ret.ids.add(pid);
                    }
                }
            }
            String[] curNames= item.names();
            if(curNames!=null && curNames.length>0){
                for(String pname : curNames){
                    if(pname!=null && !"".equals(pname)){
                        ret.names.add(pname);
                    }
                }
            }
        }
        return ret;
    }

    public static PermissionSet checkPermissions(PermissionSet requiresPers,PermissionSet hasPers){
        if(requiresPers==null){
            return null;
        }
        PermissionSet ret=new PermissionSet();

        if(requiresPers.ids!=null && requiresPers.ids.size()>0){
            for(Integer item : requiresPers.ids){
                if(hasPers==null || !hasPers.ids.contains(item)){
                    ret.ids.add(item);
                }
            }
        }
        if(requiresPers.names!=null && requiresPers.names.size()>0){
            for(String item : requiresPers.names){
                if(hasPers==null || !hasPers.names.contains(item)){
                    ret.names.add(item);
                }
            }
        }
        if(ret.ids.size()==0 && ret.names.size()==0){
            return null;
        }
        return ret;
    }

    public static PermissionSet checkPermissions(Method method,PermissionSet hasPers){
        Vector<RequirePermissions> persAnno=getAllRequires(method);
        PermissionSet requirePers=parseRequirePermissions(persAnno);
        return checkPermissions(requirePers,hasPers);
    }

    public static PermissionSet checkPermissions(Class clazz,PermissionSet hasPers){
        Vector<RequirePermissions> persAnno=getAllRequires(clazz);
        PermissionSet requirePers=parseRequirePermissions(persAnno);
        return checkPermissions(requirePers,hasPers);
    }

    public static PermissionSet checkPermissions(Class clazz,int[] hasIds,String[] hasNames){
        PermissionSet hasPers=new PermissionSet(hasIds,hasNames);
        Vector<RequirePermissions> persAnno=getAllRequires(clazz);
        PermissionSet requirePers=parseRequirePermissions(persAnno);
        return checkPermissions(requirePers,hasPers);
    }

    public static PermissionSet checkPermissions(Method method,int[] hasIds,String[] hasNames){
        PermissionSet hasPers=new PermissionSet(hasIds,hasNames);
        Vector<RequirePermissions> persAnno=getAllRequires(method);
        PermissionSet requirePers=parseRequirePermissions(persAnno);
        return checkPermissions(requirePers,hasPers);
    }

    public static boolean isPassed(PermissionSet result){
        return null==result;
    }
}
