package com.funcaretest.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Object>{

	@Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] body = (byte[])msg;
        ///////////////////////////////////////////////
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        for(int i=0;i<body.length;i++){
            stringBuffer.append(String.format("0x%X",body[i]));
            stringBuffer.append(",");
        }
        System.out.println("MessageEncoder encode:"+stringBuffer.toString());
        ///////////////////////////////////////////////
        out.writeBytes(body);//消息体中包含我们要发送的数据
    }

}
