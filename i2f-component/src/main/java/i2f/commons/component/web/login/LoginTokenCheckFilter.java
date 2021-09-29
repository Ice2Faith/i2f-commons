package i2f.commons.component.web.login;

import i2f.commons.component.web.utils.NetworkUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class LoginTokenCheckFilter implements Filter {
    protected String tokenName="token";
    public LoginTokenCheckFilter(){

    }

    public LoginTokenCheckFilter(String tokenName) {
        this.tokenName = tokenName;
    }

    public String setTokenName(String tokenName){
        String old=this.tokenName;
        this.tokenName=tokenName;
        return old;
    }
    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public final void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request=(HttpServletRequest)req;
        HttpServletResponse response=(HttpServletResponse)resp;

        beforeCheck(request,response);

        String path=request.getRequestURI();
        if(isWhiteListUrl(path,request)){
            chain.doFilter(req, resp);
            return;
        }

        String token= NetworkUtil.reqToken(request);

        if(!isValidToken(token,request,response)){
            responseNotLogin(request,response);
            return;
        }

        chain.doFilter(req, resp);
    }

    protected void beforeCheck(HttpServletRequest request,HttpServletResponse response){
        try{
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
        }catch(Exception e){

        }
    }

    protected boolean isWhiteListUrl(String url,HttpServletRequest request){
        return false;
    }

    protected abstract boolean isValidToken(String token,HttpServletRequest request,HttpServletResponse response);

    protected void responseNotLogin(HttpServletRequest request,HttpServletResponse response){
        try{
            response.setStatus(401);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("<script>alert('Not Login,Please login.');</script>");
        }catch(Exception e){

        }
    }

}
