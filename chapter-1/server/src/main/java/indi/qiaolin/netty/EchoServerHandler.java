/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @author qiaolin
 * @version $Id: EchoServerHandler.java,v 0.1 2019年05月15日 19:52 $Exp
 */

// 标志一个 ChannelHandler可以呗多个Channel安全共享
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    // 通道可读得时候
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("获取到消息：" + byteBuf.toString(Charset.forName("UTF-8")));

        ByteBuf byteBuf1 = Unpooled.copiedBuffer("我已收到" + byteBuf.toString(Charset.forName("UTF-8")), CharsetUtil.UTF_8);
        ctx.write(byteBuf1);
//        ctx.pipeline().fireChannelRead( Unpooled.copiedBuffer("酷酷酷", CharsetUtil.UTF_8));
    }

    // 读完成得时候
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        // 为什么在上面 ctx.write方法得时候 不调用 Flush ， 因为有可能他没有写完，channelRead可能会被调用多次
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    // 发生异常时
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印堆栈异常
        cause.printStackTrace();

        // 关闭该channel
        ctx.close();

    }
}