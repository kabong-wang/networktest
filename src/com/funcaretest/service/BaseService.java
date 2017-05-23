package com.funcaretest.service;

import com.funcaretest.listener.ResponseListener;

public interface BaseService extends ResponseListener{
	
	/**移除监听 不再收到回调*/
	public abstract void removeListener();
	
	/**注册监听 收到回调*/
	public abstract void addListener();
}
