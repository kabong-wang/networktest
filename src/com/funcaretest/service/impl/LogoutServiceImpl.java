package com.funcaretest.service.impl;

import com.funcaretest.domain.ResponseResult;
import com.funcaretest.network.ClientManager;
import com.funcaretest.service.LogoutService;
import com.funcaretest.util.Constant;
import com.funcaretest.util.PublicTools;

public class LogoutServiceImpl extends BaseServiceImpl implements LogoutService {

	private LogoutServiceImpl() {
	}

	public static class LogoutServiceImplInstance {
		public final static LogoutServiceImpl logoutServiceImpl = new LogoutServiceImpl();
	}

	/**
	 * 挤出或退出登陆  
	 */
	@Override
	public void logout() {
		System.out.println("LogoutServiceImpl -> user logout");
		PublicTools.clearXMLElement(Constant.LOGIN_CONFIG_XML_PATH, Constant.LOGIN_CONFIG_KEY_PASSWORD);//清除密码 
		ClientManager.getInstance().closeClientChannel();//这里断开后也会重连 （由于 退出登陆 就取消了记住密码 所以不会自动登陆  重连就不会断开与服务器的链接 只是没有登录）
	}

	@Override
	public void onResult(ResponseResult result) {
			
	}

	@Override
	public void sendMessage(byte[] message, int cmdId, int cmdType, Object object) {
		
	}

}
