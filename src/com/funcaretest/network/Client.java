package com.funcaretest.network;

import java.util.concurrent.TimeUnit;

import com.funcaretest.util.SocketConstant;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 客户端
 *
 */
public class Client {

	private static EventLoopGroup workGroup;
	private static Bootstrap bootstrap;
	private static ChannelFuture channelFuture;
	private static ChannelFutureListener channelFutureListener = null;
	private static int connectCount = 0;

	/**是否连接上*/
	private boolean isConnected = false;
	
	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	// 初始化连接
	public void initDoConnect() {
		new Thread(){

			@Override
			public void run() {
				workGroup = new NioEventLoopGroup();
				bootstrap = new Bootstrap();

				try {
					bootstrap.group(workGroup);
					bootstrap.channel(NioSocketChannel.class);
					bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
					bootstrap.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel socketChannel) {
							socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
							socketChannel.pipeline().addLast(new MessageDecoder());
							socketChannel.pipeline().addLast(new MessageEncoder());
							socketChannel.pipeline().addLast(new ClientHandler());
						}
					});
					doConnect();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if(workGroup!=null){
							workGroup.shutdownGracefully().sync();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			
		
		}.start();

	}

	// 开始连接
	private void doConnect() {
		System.out.println("Client->doConnect connect to server");
		try {
			channelFuture = bootstrap.connect(SocketConstant.ADDRESS_IP, SocketConstant.ADDRESS_PORT).sync();
			channelFutureListener = new ChannelFutureListener() {
				public void operationComplete(ChannelFuture f) throws Exception {
					isConnected = f.isSuccess();
					if (isConnected) {
						System.out.println("Client->doConnect: Started Tcp Client");
						// 连接上服务器时候的逻辑处理
						ClientManager.getInstance().onConnectedServer();
					} else {
						System.out.println("Client->doConnect: Started Tcp Client Failed");
						f.channel().eventLoop().schedule(new Runnable() {
							@Override
							public void run() {
								doConnect();
							}
						}, 3L, TimeUnit.SECONDS);
					}
				}
			};
			channelFuture.addListener(channelFutureListener);
			// 等待客户端链路关闭
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workGroup.shutdownGracefully();
		}
	}
	
	//关闭连接 （销毁连接的时候）
	public void clientDisConnect(){
        try {
            System.out.println("Client->clientDisConnect: Disconnect");
            if (channelFuture != null) {
                channelFuture.channel().closeFuture();
            }
            if (workGroup != null) {
            	workGroup.shutdownGracefully();
            }
            channelFuture = null;
            workGroup = null;
            isConnected = false;
            connectCount = 0;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

	public void reConnect() {
		System.out.println("Client->reConnect");
		clientDisConnect();//重连前断开连接 初始化一些数据
		if (connectCount<1000) {//重连一百次后就不再重连
			connectCount++;
			initDoConnect();
		}
	}
	
	public void destroy(){
		clientDisConnect();
		workGroup = null;
		bootstrap = null;
		channelFuture = null;
		channelFutureListener = null;
		connectCount = 0;
	}

}
