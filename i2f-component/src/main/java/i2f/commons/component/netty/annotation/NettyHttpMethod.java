package i2f.commons.component.netty.annotation;

import io.netty.handler.codec.http.HttpMethod;

/**
 * @author ltb
 * @date 2021/8/18
 */
public enum NettyHttpMethod {
    GET(HttpMethod.GET),POST(HttpMethod.POST),PUT(HttpMethod.PUT),DELETE(HttpMethod.DELETE);
    private HttpMethod method;
    private NettyHttpMethod(HttpMethod method){
        this.method=method;
    }
    public HttpMethod getMethod(){
        return method;
    }
}
