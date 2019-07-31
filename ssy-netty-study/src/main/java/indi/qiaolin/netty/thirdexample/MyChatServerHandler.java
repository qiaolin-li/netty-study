/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty.thirdexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 *  聊天处理器
 * @author qiaolin
 * @version $Id: MyChatServerHandler.java,v 0.1 2019年06月25日 9:55 $Exp
 */
@ChannelHandler.Sharable
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    /** ChannelGroup 用于存放Channel */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 将用户发过来的消息发送给其他人
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if(ch != channel){
                ch.writeAndFlush(channel.remoteAddress() + " 发送的消息：" + msg + "\n");
            }else{
                ch.writeAndFlush("[自己] " + msg + "\n");
            }
        });
    }

    /** 新服务加入 */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 给其他人发送消息, 这里channelGroup会群发
        channelGroup.writeAndFlush("[用户]" + channel.remoteAddress() + " 加入\n");
        // 将加入的channel放入channelGroup
        channelGroup.add(channel);
    }

    /** 通道被删除时， 这里不用管channelGroup中的当前通道，因为他会自己删除 */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[用户]" + channel.remoteAddress() + " 离开\n");
        System.out.println("当前活跃人数：" + channelGroup.size());
    }

    /** 通道可用的时候 */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线！");
    }

    /** 通道不可用的时候 */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线！");
    }

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}