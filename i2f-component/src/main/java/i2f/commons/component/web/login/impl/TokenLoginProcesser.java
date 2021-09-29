package i2f.commons.component.web.login.impl;

import i2f.commons.component.web.utils.NetworkUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public abstract class TokenLoginProcesser extends BaseLoginProcesser {
    protected String tokenName="token";
    public TokenLoginProcesser(){

    }

    public TokenLoginProcesser(String tokenName) {
        this.tokenName = tokenName;
    }

    public String setTokenName(String tokenName){
        String old=this.tokenName;
        this.tokenName=tokenName;
        return old;
    }
    @Override
    public boolean isLogined4Controller(HttpServletRequest request, HttpServletResponse response, Method method) {
        String token= NetworkUtil.reqToken(request,tokenName);
        return isValidToken(token,request,response,method);
    }

    protected abstract boolean isValidToken(String token,HttpServletRequest request,HttpServletResponse response,Method method);
}
