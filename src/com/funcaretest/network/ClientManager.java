package com.funcaretest.network;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.funcaretest.domain.ResponseResult;
import com.funcaretest.listener.ISocketReceiverControl;
import com.funcaretest.listener.ResponseListener;
import com.funcaretest.util.CommandUtils;
import com.funcaretest.util.SocketConstant;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * 连接管理类
 *
 */
public class ClientManager {

	public volatile static ClientManager clientManager = null; 
	private Client client = null;
	public ISocketReceiverControl reveicerControl = null;
	private ExecutorService executorHandleRequest = Executors.newSingleThreadExecutor();// 用于处理发送消息请求 顺序发送
	private ScheduledExecutorService heartSendService = Executors.newSingleThreadScheduledExecutor();//用于发送心跳
	private ExecutorService executorHandleResponse = Executors.newSingleThreadExecutor();//处理接收的数据回调
	
	
	private ClientManager() {
		reveicerControl = new ISocketReceiverControl();
	}

	public static ClientManager getInstance(){
		if(clientManager == null){
			synchronized (ClientManager.class) {
				if(clientManager == null){
					clientManager = new ClientManager();
				}
			}
		}
		return clientManager;
	}
	

	private ChannelHandlerContext channelHandlerContext = null;

	public ChannelHandlerContext getChannelHandlerContext() {
		return channelHandlerContext;
	}

	public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
		this.channelHandlerContext = channelHandlerContext;
	}

	/**关闭通道 （断开通信 用于退出登陆或挤出登陆  到登陆界面 而不是退出整个应用）*/
	public void closeClientChannel() {
		try {
			if (channelHandlerContext != null) {
				channelHandlerContext.disconnect();
				channelHandlerContext.close();
				channelHandlerContext = null;
			}
			
			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 初始化连接*/
	public void initSocket() {
		client = new Client();
		client.initDoConnect();
	}

	/**连接成功  刚连接上服务器后的逻辑处理*/
	public void onConnectedServer(){
		reveicerControl.onConnectedServer();
	}
	
	/**数据总控制回调*/
	public void onReceiveMessage(final ByteBuffer responseByteBuf) {
		Runnable handleReponseTask = new Runnable(){
			@Override
			public void run() {
				ResponseResult responseResult = CommandUtils.decodeResponse(responseByteBuf);//直接得到结果数据 并交给ClientManager处理发给订阅者
				reveicerControl.onReceiveMessage(responseResult);//回调数据（这里无法确切的告知某个订阅者，只好告知所有注册的订阅者，订阅者根据cmdId来区别是不是发给自己的）
			}
			
		};
		executorHandleResponse.execute(handleReponseTask);
	}

	/**添加监听回调*/
	public void addResponseListener(ResponseListener listener) {
		reveicerControl.addResponseListener(listener);
	}

	/**删除监听回调*/
	public void removeResponseListener(ResponseListener listener) {
		reveicerControl.removeResponseListener(listener);
	}

	/**清空监听回调*/
	public void clearResponseListener() {
		reveicerControl.clearResponseListener();
	}

	/**得到监听回调的长度*/
	public int getResponseObservableSize() {
		return reveicerControl.getResponseObservableSize();
	}

	/**断开连接（销毁连接的时候）*/
	public void clientDisConnect() {
		if (client != null) {
			client.clientDisConnect();
		}
	}

	/**重新连接*/
	public void reConnect() {
		if (client != null) {
			client.reConnect();
		}
	}

	/**是否连接上*/
	public boolean isConnect() {
		if (client != null) {
			return client.isConnected();
		}
		return false;
	}

	/**发送消息*/
	public void sendMessage(final byte[] message) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (channelHandlerContext != null) {
					channelHandlerContext.write(message);
					channelHandlerContext.flush();
				}
			}
		};

		executorHandleRequest.execute(runnable);
	}

	/**发送心跳 一般 连接成功后就开始发送心跳*/
	public void sendHeart() {
		if (isConnect()) {//初始化发送心跳前判断是否有连接
			Runnable runnable = new Runnable() {
				public void run() {
					if(isConnect()){//发送时候也判断是否有连接
						byte[] heart = SocketConstant.HEART_MESSAGE_CONTENT.getBytes();
						sendMessage(heart);// 发送心跳
					}
				}
			};
			if(heartSendService==null){
				throw new NullPointerException();
			}
			// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
			heartSendService.scheduleAtFixedRate(runnable, 1000, SocketConstant.HEART_MESSAGE_SEND_TIME, TimeUnit.MILLISECONDS);
		} else {
			System.out.println("not connect");
		}
	}
	
	/**停止发送心跳*/
	public void stopSendHeart(){
		try{
			heartSendService.shutdownNow();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void destroy(){
		client.destroy();
		reveicerControl = null;
		executorHandleRequest.shutdown();
		executorHandleRequest = null;
		clientManager = null;
	}

	public static void main(String[] args) {
		// 测试连接
		// ClientManager clientManager = ClientManagerInstance.clientManager;
		// clientManager.initSocket();
		// while (true) {
		// try {
		// Thread.sleep(3000);
		// if (clientManager.channelHandlerContext != null) {
		// System.out.println(clientManager.channelHandlerContext.toString());
		// }
		// if (clientManager.isConnect()) {
		// System.out.println("isCoonect:" + true);
		// } else {
		// System.out.println("isCoonect:" + false);
		// }
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
	}

}
