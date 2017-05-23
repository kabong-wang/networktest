package com.funcaretest.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//通信通道
public class ClientHandler extends ChannelInboundHandlerAdapter {
	
	private ClientManager clientManager = null;
	
	public ClientHandler(){
		this.clientManager = ClientManager.getInstance();
	}
	
	/**
	 * 连接上
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		clientManager.setChannelHandlerContext(ctx);
	}

	/**
	 * 中途断开
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		if(clientManager !=null){
			clientManager.clientDisConnect();
			if(true){//如果有网就重连
				clientManager.reConnect();
			}
		}
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		clientManager.destroy();
		cause.printStackTrace();
		ctx.close();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	@Override
//	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//		super.channelRegistered(ctx);
//	}
//
//	@Override
//	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//		super.channelUnregistered(ctx);
//	}
//
//	@Override
//	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		super.channelRead(ctx, msg);
//	}
//
//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		super.channelReadComplete(ctx);
//	}
//
//	@Override
//	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//		super.userEventTriggered(ctx, evt);
//	}
//
//	@Override
//	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
//		super.channelWritabilityChanged(ctx);
//	}

}
