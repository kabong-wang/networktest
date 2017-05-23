package com.funcaretest.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.funcaretest.domain.ResponseResult;
import com.funcaretest.network.ClientManager;
import com.funcaretest.service.impl.LoginServiceImpl.LoginServiceImplInstance;
import com.funcaretest.service.impl.LogoutServiceImpl.LogoutServiceImplInstance;
import com.funcaretest.util.SocketConstant;

/**
 * 控制连接成功时的处理 控制接收数据预处理
 */
public class ISocketReceiverControl implements ISocketReceiverListener {

	private ResponseObservable responseObservable = new ResponseObservable();// 控制数据回调
	ExecutorService executorHandleResult = Executors.newSingleThreadExecutor();// 用单线程的线程池 顺序执行

	// 添加监听回调
	public void addResponseListener(ResponseListener listener) {
		responseObservable.addObserver(listener);
	}

	// 删除监听回调
	public void removeResponseListener(ResponseListener listener) {
		responseObservable.removeObserver(listener);
	}

	// 清空监听回调
	public void clearResponseListener() {
		responseObservable.clearObserver();
	}

	// 得到监听回调的长度
	public int getResponseObservableSize() {
		return responseObservable.getCopyOnWriteArrayList().size();
	}

	@Override
	public void onConnectedServer() {
		// TODO 连接成功后 发送心跳
		 sendHeart();
		// (如果有保存用户名和密码)就执行登陆操作
		LoginServiceImplInstance.loginServiceImpl.autioLogin();
	}

	@Override
	public void onReceiveMessage(ResponseResult responseResult) {
		// TODO 总的控制数据 所有收到的数据最先经过这个回调 是否告知serviceimpl在这里操作
		
		//特殊指令特殊处理
		if(responseResult.cmdName==SocketConstant.SOCKET_COMMAND_SET_USER_LOGINOUT){//挤出登陆
			System.out.println("LogoutServiceImpl -> The current user is logged in elsewhere");
			LogoutServiceImplInstance.logoutServiceImpl.logout();
		}else{//非特殊指令 就回调
			notifyObservable(responseResult);
		}
	}

	// 后台返回数据的回调 告知所有serviceimpl
	public void notifyObservable(final ResponseResult result) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				responseObservable.notifyObservable(result);
			}
		};

		executorHandleResult.execute(runnable);

	}

	/** 发送心跳 连接成功后就开始发送心跳 */
	public void sendHeart() {
//		Runnable runnable = new Runnable() {
//			public void run() {
//				byte[] heart = SocketConstant.HEART_MESSAGE_CONTENT.getBytes();
//				ClientManager.getInstance().sendMessage(heart);// 发送心跳
//			}
//		};
//		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
//		service.scheduleAtFixedRate(runnable, 1000, SocketConstant.HEART_MESSAGE_SEND_TIME, TimeUnit.MILLISECONDS);
		
		//使用ClientManager来发心跳
		ClientManager.getInstance().sendHeart();
	}

}
