/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty.fristexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * Http内容处理器
 * @author qiaolin
 * @version $Id: TestHttpServerHandler.java,v 0.1 2019年06月24日 10:48 $Exp
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        System.out.println(msg.getClass());
        System.out.println("远程地址：" + ctx.channel().remoteAddress());


        if(msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest) msg;
            System.out.println("请求d方法：" + request.method());
            System.out.println("uri:" + request.uri());

            URI uri = new URI(request.uri());
            System.out.println("uri:" + uri.getPath());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("匹配上了！");
                return;
            }

            ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);

            // 完整得响应对象，包含http协议、响应状态、响应内容
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            // 设置响应头部
            response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().add(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将响应写出去
            ctx.writeAndFlush(response);

            // 关闭远程通道
            ctx.channel().close();

        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active ");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel registered");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel inactive");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel un registered");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler add");
    }
}