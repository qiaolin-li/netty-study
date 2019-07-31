/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 *  引导测试
 * @author qiaolin
 * @version $Id: BootstrapTest.java,v 0.1 2019年06月05日 9:58 $Exp
 */
public class BootstrapTest {


    @Test
    public void test1() throws InterruptedException {

        // 事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 引导类
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
                .group(eventLoopGroup)
                // 设置通道的类型,通道的类型一定要和 事件循环一样， 否则  java.lang.IllegalStateException: incompatible event loop type: io.netty.channel.nio.NioEventLoop
                .channel(NioSocketChannel.class)
                // 设置处理器
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("received ：" + msg.toString(Charset.forName("UTF-8")));
                    }

                });

        // 链接到服务，并同步等待到完成。
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress( 8080)).sync();
        channelFuture.addListener(new ChannelFutureListener() {

            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("connection success ！");
                }else{
                    System.out.println("connection  failure !");
                    future.cause().printStackTrace();
                }

            }

        });

        Thread.sleep(10000);

    }


}