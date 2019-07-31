/*
 * frxs Inc.  湖南兴盛优选电子商务有限公司.
 * Copyright (c) 2017-2019. All Rights Reserved.
 */
package indi.qiaolin.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

/**
 *  内嵌通道 测试
 * @author qiaolin
 * @version $Id: EmbeddedChannelTest.java,v 0.1 2019年06月05日 15:37 $Exp
 */
public class EmbeddedChannelTest {

    /**
     * 测试FixedLengthFrameDecoder
     */
    @Test
    public void test1(){
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }

        ByteBuf input = buffer.duplicate();

        // 准备通道
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        // 写入IN
        boolean writeInbound = embeddedChannel.writeInbound(input.retain());
        // 检查是否成功
        Assert.assertTrue(writeInbound);

        // 是否完成
        Assert.assertTrue(embeddedChannel.finish());


        // 验证是否切片
        ByteBuf read = embeddedChannel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), read);
        read.release();

        read = embeddedChannel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), read);
        read.release();

        read = embeddedChannel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), read);
        read.release();

        Assert.assertNull(embeddedChannel.readInbound());
        buffer.release();

    }


    /**
     * 入站通道   测试写入部分数据。
     */
    @Test
    public void test2(){
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }

        ByteBuf input = buffer.duplicate();

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        // 设置一个引用数
        System.out.println("是否写入成功：" + embeddedChannel.writeInbound(input.readBytes(2)));
        System.out.println("是否写入成功：" + embeddedChannel.writeInbound(input.readBytes(7)));
        System.out.println("是否完成：" + embeddedChannel.finish());

        ByteBuf read = embeddedChannel.readInbound();
        System.out.println("是否相等：" + (buffer.readSlice(3).equals(read)));
        read.release();

        read = embeddedChannel.readInbound();
        System.out.println("是否相等：" + (buffer.readSlice(3).equals(read)));
        read.release();

        read = embeddedChannel.readInbound();
        System.out.println("是否相等：" + (buffer.readSlice(3).equals(read)));
        read.release();

        System.out.println("是否为空，没得读了：" + embeddedChannel.readInbound());
        buffer.release();
    }

    /**
     *  测试出站
     */
    @Test
    public void test3(){
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buffer.writeInt(i * -1);
        }

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new AbsIntegerEncoder());

        System.out.println("是否写入成功：" + embeddedChannel.writeOutbound(buffer));
        System.out.println("通道是否完成：" + embeddedChannel.finish());

        for (int i = 1; i < 11; i++) {
            System.out.println("读取到：" + embeddedChannel.readOutbound());
        }

    }


    /**
     *  测试异常处理
     */
    @Test
    public void test4(){

        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }

        // 获取副本
        ByteBuf input = buffer.duplicate();

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FrameChunkDecoder(3));

        System.out.println(" 写入成功：" + embeddedChannel.writeInbound(input.readBytes(2)));
//        System.out.println("通道是否完成：" + embeddedChannel.finish());
        try {
            embeddedChannel.writeInbound(input.readBytes(4));
        } catch (TooLongFrameException e) {
            System.out.println("报错了：" + e.getMessage());
        }
//        System.out.println("通道是否完成：" + embeddedChannel.finish());


        System.out.println("写入成功：" + embeddedChannel.writeInbound(input.readBytes(3)));
        // 调用finish 会导致不可以再写入数据
        System.out.println("通道是否完成：" + embeddedChannel.finish());


        ByteBuf read = embeddedChannel.readInbound();
        System.out.println("是否相等：" + buffer.readSlice(2).equals(read));
        read.release();

        read = embeddedChannel.readInbound();
        System.out.println("是否相等：" + buffer.skipBytes(4).readBytes(3).equals(read));
        read.release();
        buffer.release();

    }

}