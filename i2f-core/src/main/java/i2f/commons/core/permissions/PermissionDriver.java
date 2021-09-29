package i2f.commons.core.permissions;


import i2f.commons.core.permissions.core.PermissionWorker;

import java.lang.reflect.Method;

public class PermissionDriver {
    public static boolean check(Method method,int[] hasPers){
        return PermissionWorker.isPassed(PermissionWorker.checkPermissions(method,hasPers,null));
    }
    public static boolean check(Method method,String[] hasPers){
        return PermissionWorker.isPassed(PermissionWorker.checkPermissions(method,null,hasPers));
    }
    public static boolean check(Method method,int[] hasPerIds,String[] hasPerNames){
        return PermissionWorker.isPassed(PermissionWorker.checkPermissions(method, hasPerIds, hasPerNames));
    }

}
