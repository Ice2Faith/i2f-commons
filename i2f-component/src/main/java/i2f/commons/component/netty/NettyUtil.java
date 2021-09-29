package i2f.commons.component.netty;

import i2f.commons.component.netty.extension.impl.HttpRequestDispatchAdapter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author ltb
 * @date 2021/8/11
 */
public class NettyUtil {
    public static ChannelInitializer<NioSocketChannel> httpChannelInitializer(SimpleChannelInboundHandler<FullHttpRequest> httpHandler){
        ChannelInitializer<NioSocketChannel> initializer=new ChannelInitializer<NioSocketChannel>() {
            protected void initChannel(NioSocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
//                //1.Netty提供的针对Http的编解码
//                pipeline.addLast(new HttpServerCodec());
//                //2.自定义处理Http的业务Handler
//                pipeline.addLast(httpHandler);

                // 请求解码器
                pipeline.addLast("http-decoder", new HttpRequestDecoder());
                // 将HTTP消息的多个部分合成一条完整的HTTP消息
                pipeline.addLast("http-aggregator", new HttpObjectAggregator(65535));
                // 响应转码器
                pipeline.addLast("http-encoder", new HttpResponseEncoder());
                // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
                pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                // 自定义处理handler
                pipeline.addLast("http-server", httpHandler);
            }
        };
        return initializer;
    }
    public static void startNettyServer(int port,ChannelInitializer<NioSocketChannel> channelInitializer) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer);

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    public static void startHttpNettyServer(int port,SimpleChannelInboundHandler<FullHttpRequest> httpHandler) throws InterruptedException {
        ChannelInitializer<NioSocketChannel> httpInitializer=httpChannelInitializer(httpHandler);
        startNettyServer(port,httpInitializer);
    }

    public static void startNettyHttpServer(int port,String ... basePackages) throws InterruptedException {
        HttpRequestDispatchAdapter httpHandler=new HttpRequestDispatchAdapter();
        HttpRequestDispatchAdapter.addMappingByScanPackage(basePackages);
        ChannelInitializer<NioSocketChannel> httpInitializer=httpChannelInitializer(httpHandler);
        startNettyServer(port,httpInitializer);
    }
}
