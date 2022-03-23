package pers.machi.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyHttpServerSingleton {
    private final Logger logger = LogManager.getLogger(NettyHttpServerSingleton.class);
    private int port = 12354;
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    private NettyHttpServerSingleton() {
    }

    private static NettyHttpServerSingleton instance;

    public static NettyHttpServerSingleton getInstance() {
        if (instance == null) {
            synchronized (NettyHttpServerSingleton.class) {
                if (instance == null) {
                    instance = new NettyHttpServerSingleton();
                }
            }
        }
        return instance;
    }

    public void run() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.handler(new LoggingHandler(LogLevel.INFO));
            b.childHandler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel channel) {
                    ChannelPipeline p = channel.pipeline();
                    p.addLast(new HttpRequestDecoder());
                    p.addLast(new HttpResponseEncoder());
                    p.addLast(new CustomHttpServerHandler());
                }
            });
            b.option(ChannelOption.SO_BACKLOG, 16384);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
