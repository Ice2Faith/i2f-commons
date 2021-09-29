package i2f.commons.component.netty.extension;

import com.google.gson.JsonObject;
import i2f.commons.component.json.gson.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/8/18
 */
@ChannelHandler.Sharable
public abstract class AbstractHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    protected abstract void doRequest(HttpWebRequest request,HttpWebResponse response);
    /*
     * 处理请求
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        HttpWebRequest request=new HttpWebRequest();
        request.setFullHttpRequest(fullHttpRequest);
        request.setMethod(fullHttpRequest.method());
        request.setHeaders(fullHttpRequest.headers());
        request.setUri(URI.create(fullHttpRequest.uri()));

        HttpWebResponse response=new HttpWebResponse();

        HttpMethod method=fullHttpRequest.method();
        if (method == HttpMethod.GET
            || method==HttpMethod.POST
            || method==HttpMethod.PUT
            || method==HttpMethod.DELETE) {
            getGetParamsFromChannel(fullHttpRequest,request);
            getPostParamsFromChannel(fullHttpRequest,request);
            doRequest(request,response);
        } else {
            response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setContent(null);
        }
        FullHttpResponse fullHttpResponse=makeFullHttpResponse(response);
        // 发送响应
        channelHandlerContext.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    protected FullHttpResponse makeFullHttpResponse(HttpWebResponse response){
        ByteBuf content=response.getContent();
        if(content==null){
            content= Unpooled.copiedBuffer("", Charset.forName("UTF-8"));
        }
        FullHttpResponse ret = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, response.getStatus(), content);
        if (response.getContent() != null) {
            ret.headers().set("Content-Type", "text/plain;charset=UTF-8");
            ret.headers().set("Content_Length", ret.content().readableBytes());
            HttpHeaders headers=response.getHeaders();
            if(headers.size()>0){
                for(Map.Entry<String, String> item : headers){
                    ret.headers().set(item.getKey(),item.getValue());
                }
            }
        }
        return ret;
    }

    /*
     * 获取GET方式传递的参数
     */
    private Map<String, Object> getGetParamsFromChannel(FullHttpRequest fullHttpRequest,HttpWebRequest request) {

        Map<String, Object> params = new HashMap<String, Object>();

        if (fullHttpRequest.method() == HttpMethod.GET) {
            // 处理get请求
            QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
            Map<String, List<String>> paramList = decoder.parameters();
            for (Map.Entry<String, List<String>> entry : paramList.entrySet()) {
                params.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        request.setUrlParams(params);
        return params;
    }

    /*
     * 获取POST方式传递的参数
     */
    private Map<String, Object> getPostParamsFromChannel(FullHttpRequest fullHttpRequest,HttpWebRequest request) {

        Map<String, Object> params = new HashMap<String, Object>();

        if (fullHttpRequest.method() == HttpMethod.POST) {
            // 处理POST请求
            String strContentType = fullHttpRequest.headers().get("Content-Type").trim();
            if (strContentType.contains("x-www-form-urlencoded")) {
                params  = getFormParams(fullHttpRequest);
            } else if (strContentType.contains("application/json")) {
                try {
                    params = getJSONParams(fullHttpRequest);
                } catch (UnsupportedEncodingException e) {

                }
            }
        }
        request.setBodyParams(params);
        return params;
    }

    /*
     * 解析from表单数据（Content-Type = x-www-form-urlencoded）
     */
    private Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();

        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }

        return params;
    }

    /*
     * 解析json数据（Content-Type = application/json）
     */
    private Map<String, Object> getJSONParams(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
        Map<String, Object> params = new HashMap<String, Object>();

        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String strContent = new String(reqContent, "UTF-8");

        JsonObject jsonObj= JsonUtil.formJson(strContent);
        for (String key : jsonObj.keySet()) {
            params.put(key, jsonObj.get(key));
        }

        return params;
    }

}
