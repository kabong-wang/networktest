package com.funcaretest.service;

import com.funcaretest.domain.UserInfo;

public interface LoginService extends BaseService{
	
	/**登陆*/
	public abstract void login(UserInfo userInfo);
	
	/**
	 * 获取登陆准备数据 只需传一个username
	 * @param username 用户名
	 */
	public abstract void getLoginReadyData(String username);
	
	/**
	 * 拼接请求数据
	 * @param cmdNamd 对应SocketConstant中定义的指令名
	 * @param obj 要传递的参数 可以使一个字符串 可以是一个对象
	 * @return
	 */
	public abstract String packParamData(int cmdNamd,Object obj);
	
	
}
