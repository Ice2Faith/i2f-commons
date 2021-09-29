package i2f.commons.component.web.utils;


import i2f.commons.core.utils.safe.CheckUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class NetworkUtil {
    public static String getIP(HttpServletRequest request){
        String ip=request.getHeader("x-forwarded-for");
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("Proxy-Client-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("X-Real-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (inet !=null) {
                    ip = inet.getHostAddress();
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    public static String sessionSet(HttpSession session,String key,String val){
        String str=(String)session.getAttribute(key);
        session.setAttribute(key,val);
        return str;
    }
    public static String sessionGet(HttpSession session,String key){
        String val=(String)session.getAttribute(key);
        return val;
    }
    public static String sessionDel(HttpSession session,String key){
        String val=(String)session.getAttribute(key);
        session.removeAttribute(key);
        return val;
    }

    public static String sessionTokenSet(HttpSession session,String tokenName,String val,long timeoutMillSecond){
        String old=sessionSet(session,tokenName,val);
        sessionTokenExpire(session,tokenName,timeoutMillSecond);
        return old;
    }
    public static String sessionTokenGet(HttpSession session,String tokenName){
        if(!sessionTokenExist(session,tokenName)){
            return null;
        }
        return sessionGet(session,tokenName);
    }
    public static void sessionTokenExpire(HttpSession session,String tokenName,long timeoutMillSecond){
        if(!sessionTokenExist(session,tokenName)){
            return;
        }
        String tokenExpire="expire_"+tokenName;
        if(timeoutMillSecond>=0){
            long tsmp=new Date().getTime()+timeoutMillSecond;
            session.setAttribute(tokenExpire,tsmp);
        }else{
            session.removeAttribute(tokenExpire);
        }
    }
    public static boolean sessionTokenExist(HttpSession session,String tokenName){
        String tokenExpire="expire_"+tokenName;
        String val=sessionGet(session,tokenName);
        if(CheckUtil.isEmptyStr(val,false)){
            sessionDel(session,tokenName);
            session.removeAttribute(tokenExpire);
            return false;
        }

        Long expire=(Long)session.getAttribute(tokenExpire);
        if(CheckUtil.notNull(expire)){
            long now=new Date().getTime();
            if(now>expire){
                sessionDel(session,tokenName);
                session.removeAttribute(tokenExpire);
                return false;
            }
        }

        return true;
    }

    public static String reqToken(HttpServletRequest request){
        return reqToken(request,"token");
    }
    public static String reqToken(HttpServletRequest request,String tokenName){
        String token=request.getHeader(tokenName);
        if(CheckUtil.isEmptyStr(token,true)){
            token= request.getParameter(tokenName);
        }
        return token;
    }
}
