package com.funcaretest.test;

import java.nio.ByteBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 
 * ByteBuf与ByteBuffer与byte数组之间相互转换
 */
public class ByteBufToByteBuffer {

	// ByteBuffer转byte
	public static byte[] byteBufferToByte(ByteBuffer byteBuffer) {
		byte[] datas = null;
		int length = byteBuffer.capacity();
		if (length <= 0) {
			return null;
		}
		if (byteBuffer.hasArray()) {
			datas = byteBuffer.array();
		}
		return datas;
	}

	// ByteBuf转byte
	public static byte[] byteBufToByte(ByteBuf byteBuf) {
		byte[] datas = null;
		int length = byteBuf.capacity();
		if (length <= 0) {
			return null;
		}
		if (byteBuf.hasArray()) {
			datas = byteBuf.array();
		}
		return datas;
	}

	// byte转ByteBuffer
	public static ByteBuffer byteToByteBuffer(byte[] datas) {
		ByteBuffer byteBuffer = null;
		if (datas == null) {
			return null;
		}
		int length = datas.length;
		if (length <= 0) {
			return null;
		}

		byteBuffer = ByteBuffer.allocate(length);
		byteBuffer = ByteBuffer.wrap(datas, 0, length);
		return byteBuffer;
	}

	// byte转ByteBuf
	public static ByteBuf byteToByteBuf(byte[] datas) {
		ByteBuf byteBuf = null;
		if (datas == null) {
			return null;
		}
		int length = datas.length;
		if (length <= 0) {
			return null;
		}

		byteBuf = Unpooled.copiedBuffer(datas);
		// 或者使用如下方式
		// byteBuf = Unpooled.buffer(length);
		// byteBuf.readBytes(datas);

		return byteBuf;
	}

	// Bytebuf转ByteBuffer (也可以通过先转成字节 再转成ByteBuffer)
	public static ByteBuffer bytebufToByteBuffer(ByteBuf byteBuf) {
		ByteBuffer byteBuffer = null;
		if (byteBuf == null) {
			return null;
		}
		ByteBuf tempByteBuf = byteBuf.copy();// 重新复制一个ByteBuf
		byteBuffer = tempByteBuf.nioBuffer();
		return byteBuffer;

	}

	// ByteBuffer转ByteBuf (也可以通过先转成字节 再转成ByteBuf)
	public static ByteBuf bytebufferToByteBuf(ByteBuffer byteBuffer) {
		ByteBuf byteBuf = null;
		if (byteBuffer == null) {
			return null;
		}
		ByteBuffer tempByteBuffer = ByteBuffer.wrap(byteBuffer.array(), 0, byteBuffer.capacity());// byteBuffer复制成一个新的
																									// position为0
		byteBuf = Unpooled.copiedBuffer(tempByteBuffer);// tempByteBuffer转ByteBuf
		return byteBuf;

	}
	
	
	public static void displayBytes(byte[] datas){
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < datas.length; i++) {
			stringBuffer.append(String.format("0x%02X", datas[i]));
			if (i < datas.length - 1) {
				stringBuffer.append(",");
			}
		}
		System.out.println(stringBuffer.toString());
	}

	public static void main(String[] args) {
		byte[] datas = null;
		try {
			datas = "0135456大象【】《》，。,.&".getBytes("UTF-8");
			displayBytes(datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//ByteBuffer与byte之间相互转换
		ByteBuffer byteBuffer = byteToByteBuffer(datas);
		displayBytes(byteBufferToByte(byteBuffer));
		
		//ByteBuf与byte之间相互转换
		ByteBuf byteBuf = byteToByteBuf(datas);
		displayBytes(byteBufToByte(byteBuf));
		
		//ByteBuffer与ByteBuf相互转化
		ByteBuffer tempByteBuffer = bytebufToByteBuffer(byteBuf);
		displayBytes(byteBufferToByte(tempByteBuffer));
		
		//ByteBuf与ByteBuffer相互转化
		ByteBuf tempByteBuf = bytebufferToByteBuf(byteBuffer);
		displayBytes(byteBufToByte(tempByteBuf));
	}

}
