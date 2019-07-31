/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author qiaolin
 * @version $Id: EchoServer.java,v 0.1 2019年05月15日 19:51 $Exp
 */
public class EchoServer {


    public static void main(String[] args) throws InterruptedException {

        //  ChannelHandler
        final EchoServerHandler serverHandler = new EchoServerHandler();

        // 创建 EventLoopGroup
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 创建 ServerBootstrap
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(eventLoopGroup)
                // 指定NIO传输得通道
                .channel(NioServerSocketChannel.class)
                // 指定端口
                .localAddress(9090)
                // 添加一个EchoServerHandler到Channel得pipeline
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // EchoServerHandler被标注为@Shareable， 所以我们可以使用相同得实例
                        //这里对所有得客户端来说，都会使用同一个 EchoServerHandler
                        socketChannel.pipeline().addLast(serverHandler);
                    }
                });

        // 异步绑定服务器调用sync 方法阻塞等待直到绑定完成。
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        System.out.println("启动监听链接：" + channelFuture.channel().localAddress());

        // 获取Channel得 CloseFutere. 并且阻塞当前线程直到他完成。
        channelFuture.channel().closeFuture().sync();

        // 关闭EventLoopGroup，释放所有资源。
        eventLoopGroup.shutdownGracefully().sync();
    }




}