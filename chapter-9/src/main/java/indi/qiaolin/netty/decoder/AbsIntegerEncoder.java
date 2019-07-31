/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 *  负数转绝对值 编码器
 * @author qiaolin
 * @version $Id: AbsIntegerEncoder.java,v 0.1 2019年06月05日 19:08 $Exp
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int length = 4;
        while(msg.readableBytes() >= length){
            int abs = Math.abs(msg.readInt());
            out.add(abs);
        }

    }

}