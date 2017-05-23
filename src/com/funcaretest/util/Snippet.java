package com.funcaretest.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Snippet {

	//模拟心跳数据包
	public static void sendHeart() {
		Runnable runnable = new Runnable() {
			public void run() {
				byte[] heart = SocketConstant.HEART_MESSAGE_CONTENT.getBytes();
				System.out.println(StringUtils.getStrDateTime(System.currentTimeMillis()));
				PublicTools.displayHexBytes(heart);
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		service.scheduleAtFixedRate(runnable, 1000, SocketConstant.HEART_MESSAGE_SEND_TIME, TimeUnit.MILLISECONDS);
	}

	public static void main(String[] args) {
		System.out.println(StringUtils.getStrDateTime(System.currentTimeMillis()));
		sendHeart();
	}

}
