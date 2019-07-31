/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 客户端
 * @author qiaolin
 * @version $Id: EchoClient.java,v 0.1 2019年05月16日 9:43 $Exp
 */
public class EchoClient {

    public static void main(String[] args) throws InterruptedException {
        // 事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 创建 Bootstrap
        Bootstrap bootstrap = new Bootstrap();

        // 给Bootstrap指定 EventLoopGroup
        bootstrap.group(eventLoopGroup)
                // 指定适用NIO传输得Channel类型
                .channel(NioSocketChannel.class)
                // 指定远程服务端地址
                .remoteAddress(new InetSocketAddress("localhost", 9090))
                // 在创建 Channel时，像ChannelPipeline中添加一个EchoClientHandler实例
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new EchoClientHandler());
                    }
                });

        // 连接到远程服务器，阻塞等待链接完成
        ChannelFuture future = bootstrap.connect().sync();

        // 阻塞，知道Channel关闭
        future.channel().closeFuture().sync();

        // 关闭线程池并且释放所有资源
        eventLoopGroup.shutdownGracefully().sync();
    }



}