package com.funcaretest.listener;

import com.funcaretest.domain.ResponseResult;

/**
 * 
 *处理连接上后台后的回调
 *和收到数据后的回调
 */
public interface ISocketReceiverListener {
	/**处理连接上后台的回调*/
	public abstract void onConnectedServer();
	/**处理数据回调*/
	public abstract void onReceiveMessage(ResponseResult responseResult);
}
