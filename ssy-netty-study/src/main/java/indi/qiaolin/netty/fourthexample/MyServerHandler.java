/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty.fourthexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 *  处理器
 * @author qiaolin
 * @version $Id: MyServerHandler.java,v 0.1 2019年06月25日 11:41 $Exp
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        // 如果是空闲状态事件
        if(evt instanceof IdleStateEvent){

            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲！";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲！";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲！";
                    break;
            }

            Channel channel = ctx.channel();
            System.out.println(channel.remoteAddress() + " 超时事件：" + eventType);
            // 关闭这个通道
            channel.close();

        }

    }

}