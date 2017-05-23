package com.funcaretest.util;

public class SocketConstant {
	public static final String ADDRESS_IP = "";//IP地址
	public static final int ADDRESS_PORT = 0;//端口
	public static final int WIFI_CONNECTED_TIMEOUT = 30 * 1000;
	public static final String HEART_MESSAGE_CONTENT = "<HEART>";
	public static final int HEART_MESSAGE_SEND_TIME = 40 * 1000;//心跳时间 毫秒
	
	public static final int SOCKET_COMMAND_USER_LOGIN = 0x01;//登陆
	public static final int SOCKET_COMMAND_GET_LOGIN_READY_DATA = 0x02;//获取登陆准备数据
	public static final int SOCKET_COMMAND_SET_USER_LOGINOUT = 0X1002;//用户在其他地方登陆,当前用户主动退出
}
