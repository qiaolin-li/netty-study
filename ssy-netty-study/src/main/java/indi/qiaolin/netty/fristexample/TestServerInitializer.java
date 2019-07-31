/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty.fristexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author qiaolin
 * @version $Id: TestServerInitializer.java,v 0.1 2019年06月24日 10:38 $Exp
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 获取pipeline （管道）
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 给管道增加ChannelHandler
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());

        pipeline.addLast("TestHttpServerHandler", new TestHttpServerHandler());
    }

}