/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioDatagramChannel;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 *  服务端引导
 * @author qiaolin
 * @version $Id: ServerBootstrapTest.java,v 0.1 2019年06月05日 10:41 $Exp
 */
public class ServerBootstrapTest {


    @Test
    public void test1() throws InterruptedException {

        // 事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 服务端引导
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(eventLoopGroup)

                .channel(NioServerSocketChannel.class)

                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("接收到了：" + msg);
                    }

                });


        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8080)).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("bind success ");
                }else{
                    System.out.println("bind failure");
                    future.cause().printStackTrace();
                }
            }
        });

        channelFuture.channel().closeFuture().sync();
    }


    /**
     *  无连接协议 Datagram
     */
    @Test
    public void test2() throws InterruptedException {

        EventLoopGroup eventLoopGroup = new OioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)

                .channel(OioDatagramChannel.class)

                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                        System.out.println(msg);
                    }
                });

        ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(80)).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println(future.isSuccess());
            }
        });

        eventLoopGroup.shutdownGracefully();
    }




}