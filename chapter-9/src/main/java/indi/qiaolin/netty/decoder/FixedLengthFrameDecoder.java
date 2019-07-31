/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 *  固定长度帧解码器
 * @author qiaolin
 * @version $Id: FixedLengthFrameDecoder.java,v 0.1 2019年06月05日 14:58 $Exp
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength){
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 检查是否有足够的字节可以读取，以便生成下一个帧
        while(in.readableBytes() >= frameLength){
            // 读取指定字节生成一个新的ByteBuf
            out.add(in.readBytes(frameLength));
        }
    }

}