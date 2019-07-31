/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author qiaolin
 * @version $Id: EchoClientHandler.java,v 0.1 2019年05月16日 9:37 $Exp
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    // 当通道可用时
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       // 发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("我是客户端发来得哦!", CharsetUtil.UTF_8));

    }

    // 当服务端传送过来得消息可读时
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("接收到服务端：" + byteBuf.toString(CharsetUtil.UTF_8));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();

        // 关闭通道
        ctx.close();
    }
}