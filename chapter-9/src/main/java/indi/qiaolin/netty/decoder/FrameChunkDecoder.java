/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * @author qiaolin
 * @version $Id: FrameChunkDecoder.java,v 0.1 2019年06月06日 10:24 $Exp
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {
    private final int maxLength;

    public FrameChunkDecoder(int maxLength){
        this.maxLength = maxLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int size = in.readableBytes();
        if(size > maxLength){
            in.clear();
            throw new TooLongFrameException("长度过大！" + size);
        }
        out.add(in.readBytes(size));
    }


}