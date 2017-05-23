package com.funcaretest.util;

import java.io.File;

public class Constant {

	/**资源文件 resources 目录*/
	public static final String RESOURCES_PATH = "." + File.separator+"resources"+File.separator;
	
	/** 记录用户登录名密码的xml文件 一个根节点 里面仅仅是一些简单的类似键值对信息 没有复杂嵌套 */
	public static final String LOGIN_CONFIG_XML = "login.xml";
	/** 记录用户登录名密码的xml文件的根标签 */
	 public static final String LOGIN_CONFIG_XML_ROOT = "login";
	 
	/** 记录用户登录名密码的xml文件 中键名 */
	public static final String LOGIN_CONFIG_KEY_USERNAME = "username";
	public static final String LOGIN_CONFIG_KEY_PASSWORD = "password";
	public static final String LOGIN_CONFIG_KEY_STRTEID = "strTEID";
	
	/**资源文件 resources 中的login.xml路径*/
	public static final String LOGIN_CONFIG_XML_PATH = RESOURCES_PATH+LOGIN_CONFIG_XML;
	
	

}
