package com.funcaretest.network;

import java.nio.ByteBuffer;
import java.util.List;

import com.funcaretest.domain.ResponseResult;
import com.funcaretest.util.CommandUtils;
import com.funcaretest.util.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder{
	    public static final String HEART_MESSAGE_CONTENT = "<HEART>";
	    private int receiveLen = -1;//接收的长度
	    private ByteBuffer byteBuffer = null;
	    
	    @Override
	    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
	        int contentLength = -1;
	        int length = in.readableBytes();
	        if (length < 0) {
	            return;
	        }
	        in.markReaderIndex();
	        byte[] req = new byte[length];
	        //in.getBytes(0, req);
	        in.readBytes(req);
	        //////////////////////////////////////
	        StringBuffer stringBuffer = new StringBuffer();
	        stringBuffer.append("{");
	        for(int i=0;i<req.length;i++){
	            stringBuffer.append(String.format("0x%02X",req[i]));
	            stringBuffer.append(",");
	        }
	        System.out.println("MessageDecoder decode:"+stringBuffer.toString());
	        //////////////////////////////////////
	        ByteBuffer receiveBuffer = ByteBuffer.wrap(req);
	        handleReceiveData(receiveBuffer);
	    }

	    //处理解码器中ByteBuf转成的ByteBuffer
	    private void handleReceiveData(ByteBuffer receiveByteBuffer) {
	        if (receiveByteBuffer == null || receiveByteBuffer.capacity() < 1) {
	            return;
	        }
	        ByteBuffer tempByteBuffer = null;//用于处理半包的 临时数据包
	        receiveByteBuffer.position(0);
	        if (receiveByteBuffer.capacity() == HEART_MESSAGE_CONTENT.length()) {//是不是心跳 (这样判断有误 receiveByteBuffer.toString()得不到心跳字符串)
	            String receiveStr = receiveByteBuffer.toString();
	            if (receiveStr.equals(HEART_MESSAGE_CONTENT)) {
	                receiveByteBuffer.position(0);
	                return;//心跳 不处理
	            }
	        }

	        if (byteBuffer != null && byteBuffer.capacity() > 1) {//如果存在半包 （如果是正常包 bytebuffer会被清理置null）
	            byteBuffer.position(0);
	            tempByteBuffer = ByteBuffer.allocate(byteBuffer.capacity());
	            tempByteBuffer.put(byteBuffer);
	            byteBuffer.clear();
	            byteBuffer = ByteBuffer.allocate(tempByteBuffer.capacity() + receiveByteBuffer.capacity());//创建一个 半包+新数据包 的包
	            tempByteBuffer.position(0);
	            byteBuffer.put(tempByteBuffer);
	            byteBuffer.position(tempByteBuffer.capacity());
	            byteBuffer.put(receiveByteBuffer);//将半包+新数据包 给byteBuffer (如果这个包还是半包 那么还会走到这层判断里面来)
	            tempByteBuffer.clear();
	            tempByteBuffer = null;
	        } else {
	            int nTemp;
	            int nLen = receiveByteBuffer.capacity();
	            int i = 0;
	            receiveByteBuffer.position(i);
	            for (i = 0; i < nLen; i++) {//让receiveByteBuffer中每个字节数 和 0xff 相与
	                nTemp = receiveByteBuffer.get();
	                nTemp &= 0xff;//与一个字节的16进制数据 做比较前 作 &0xff 操作
	                if (nTemp == CommandUtils.m_PACKAGE_HEAD) {//碰到包头 就跳出这个循环
	                    break;//这个循环是为了得到 包头 的位置 i
	                }
	            }
	            if (i <= nLen) {//包头位置肯定没有数据包的长度位置 这里还是做出了判断
	                byteBuffer = ByteBuffer.allocate(nLen - i);//开辟一个receiveByteBuffer中包头位置到这个包长度之间的空间
	                byteBuffer.put(receiveByteBuffer.array(), i, (nLen - i));//从receiveByteBuffer中取出包头位置之后的剩下的数据
	            }
	        }
	        if (byteBuffer != null) {
	            if (byteBuffer.capacity() > 6) {//最起码要是一个心跳包（心跳是<HEART>）
	                byteBuffer.position(1);//跳过包头
	                int headOffset = byteBuffer.get();//数据段偏移量 整个包头的长度
	                int contentLength = byteBuffer.getInt();//取到数据内容长度 (这个数据是大端数据 需转小端数据 需高低位转换)
	                headOffset &= CommandUtils.m_PACKAGE_HEAD_LENGTH;
	                contentLength = StringUtils.InvertUintBit(contentLength);//(高低位数据转换)
	                if (headOffset > 255 || contentLength > 1024 * 1000) {//包头长度>255 或者数据结构内容与数据信息长度大于1000kb
	                    byteBuffer.clear();
	                    return;//数据有问题 不予处理
	                } else {
	                    int recvId = StringUtils.InvertUintBit(byteBuffer.getInt());//包序号
	                    //  检查校验位（头校验）
	                    if (headOffset > 10 && byteBuffer.capacity() > headOffset) {//包头数据正确 也有数据结构描述信息
	                        byteBuffer.position(0);
	                        int checkNum = 0;
	                        for (int i = 0; i < headOffset - 1; i++) {
	                            checkNum ^= byteBuffer.get();//头信息里面 头校验之前所有字节异或
	                        }
	                        checkNum &= 0xff;
	                        int checkBit = byteBuffer.get();//头校验值
	                        checkBit &= 0xff;
	                        if(checkNum != checkBit){//比较计算出来的头校验 和头信息里面的头校验是否一致 （不一致就丢掉）
	                            byteBuffer.clear();
	                            return;
	                        }
	                    }
	                }
	            }
	            if(byteBuffer.capacity() > 1) {
	                byteBuffer.position(1);
	                int headOffset = byteBuffer.get();
	                int contentLength = byteBuffer.getInt();
	                contentLength = StringUtils.InvertUintBit(contentLength);
	                headOffset &= CommandUtils.m_PACKAGE_HEAD_LENGTH;
	                int allLen = headOffset + contentLength;//计算 包头长+数据内容长度 （一条完整数据的长度）
	                if (allLen >= CommandUtils.m_PACKAGE_HEAD_LENGTH * 2 &&
	                        byteBuffer.capacity() >= allLen) {//如果 是一个完整的或 大于一个完整的包  （否则就是半包 byteBuffer留下来和新的包一起处理）
	                    try {
//	                        if (onReceiveMessageHandler != null) {
//	                            ByteBuffer arrByOther = ByteBuffer.allocate(allLen);//重新分配一个 包头长+数据内容长度 的数据包
//	                            byteBuffer.position(0);
//	                            arrByOther.put(byteBuffer.array(), 0, allLen);//复制 原数据包中 包头长+数据内容长度 字节的数据 （这是一个完整的数据）
//
//	                            Message receMessage = onReceiveMessageHandler
//	                                    .obtainMessage();
//	                            receMessage.what = SocketManager.WIFI_MESSAGE_RESPONSE_MESSAGE;//让SocketManager中的handleReceiveMessage方法来处理arrByOther
//	                            receMessage.obj = arrByOther;
//	                            receMessage.sendToTarget();
//	                        }
                            ByteBuffer arrByOther = ByteBuffer.allocate(allLen);//重新分配一个 包头长+数据内容长度 的数据包
                            byteBuffer.position(0);
                            arrByOther.put(byteBuffer.array(), 0, allLen);//复制 原数据包中 包头长+数据内容长度 字节的数据 （这是一个完整的数据）
                            
                            //交给ClientManager处理数据回调
                            ClientManager.getInstance().onReceiveMessage(arrByOther);
	                    	
	                    } catch (Exception e) {
	                        byteBuffer.clear();
	                        e.printStackTrace();
	                    }
	                    if (byteBuffer.capacity() > allLen) {//考虑到粘包（这里考虑到多个数据包粘在一起的情况 剩下的包 递归处理）
	                        ByteBuffer lastByteBuffer = ByteBuffer.allocate(byteBuffer.capacity() - allLen);
	                        lastByteBuffer.put(byteBuffer.array(), allLen, byteBuffer.capacity() - allLen);
	                        byteBuffer.clear();
	                        byteBuffer = null;
	                        handleReceiveData(lastByteBuffer);
	                    } else {
	                        receiveLen = -1;
	                        byteBuffer.clear();
	                        byteBuffer = null;
	                    }
	                }else{
	                	//否则就是一个半包    byteBuffer就不是null的  会同下一个数据包一起处理 （byteBuffer是一个全局变量会记录这个包  这里不做任何操作）
	                }
	            }
	        }
	    }

}
