package com.funcaretest.service.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.HandlerBase;

import com.funcaretest.domain.MessageEntity;
import com.funcaretest.domain.ResponseResult;
import com.funcaretest.domain.SystemStoreData;
import com.funcaretest.domain.SystemStoreData.SystemStoreDataInstance;
import com.funcaretest.domain.TableData;
import com.funcaretest.domain.UserInfo;
import com.funcaretest.network.ClientManager;
import com.funcaretest.service.LoginService;
import com.funcaretest.util.CommandUtils;
import com.funcaretest.util.Constant;
import com.funcaretest.util.PublicTools;
import com.funcaretest.util.SocketConstant;
import com.funcaretest.util.StringUtils;

public class LoginServiceImpl extends BaseServiceImpl implements LoginService {

	private LoginServiceImpl() {
	}

	public static class LoginServiceImplInstance {
		public final static LoginServiceImpl loginServiceImpl = new LoginServiceImpl();
	}

	@Override
	public void login(UserInfo userInfo) {
		if (userInfo == null) {
			throw new NullPointerException();
		}

		cmdId = CommandUtils.getSendCmdID();
		cmdLevel = CommandUtils.m_nCmdLevel_high;
		cmdType = CommandUtils.m_nCmdType_CL_UpData;//只需要处理结果
		cmdName = CommandUtils.m_nCmdName_Login;
		charSet = CommandUtils.m_nCodeType_Unicode;
		params = packParamData(SocketConstant.SOCKET_COMMAND_USER_LOGIN,userInfo);
		ByteBuffer requsetParamByteBuffer = CommandUtils.createDataContentPack(cmdId, cmdLevel, cmdType, cmdName,
				charSet, params);
		byte[] message = requsetParamByteBuffer.array();
		sendMessage(message, cmdId, SocketConstant.SOCKET_COMMAND_USER_LOGIN, userInfo);
	}

	/**
	 * 获取登陆准备数据 只需传递一个用户名
	 */
	@Override
	public void getLoginReadyData(String username) {
		if (StringUtils.isEmpty(username)) {
			throw new NullPointerException();
		}
		cmdId = CommandUtils.getSendCmdID();
		cmdLevel = CommandUtils.m_nCmdLevel_high;
		cmdType = CommandUtils.m_nCmdType_CL_DownData;//需要数据库发数据过来
		cmdName = CommandUtils.m_nCmdName_GetLoginReadyData;
		charSet = CommandUtils.m_nCodeType_Unicode;
		params = packParamData(SocketConstant.SOCKET_COMMAND_GET_LOGIN_READY_DATA,username);
		ByteBuffer requsetParamByteBuffer = CommandUtils.createDataContentPack(cmdId, cmdLevel, cmdType, cmdName, charSet,params);
		byte[] message = requsetParamByteBuffer.array();
		sendMessage(message,cmdId,SocketConstant.SOCKET_COMMAND_GET_LOGIN_READY_DATA,username);
	}

	@Override
	public String packParamData(int cmdName, Object obj) {
		if (obj == null) {
			throw new NullPointerException();
		}

		StringBuilder param = new StringBuilder();

		switch (cmdName) {
		case SocketConstant.SOCKET_COMMAND_USER_LOGIN:// 登陆
			if (obj instanceof UserInfo) {
				UserInfo userInfo = (UserInfo) obj;
				String username = userInfo.getUserName();
				String password = userInfo.getPassword();
				if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
					return null;
				} else {
					param.append(username);
					param.append(CommandUtils.m_strSep_Feild);
					param.append(password);
					param.toString();
				}
			} else {
				throw new ClassCastException();
			}
			break;
		case SocketConstant.SOCKET_COMMAND_GET_LOGIN_READY_DATA:// 获取登陆准备数据
			if(obj instanceof String){
				param.append(obj.toString());
			}else {
				throw new ClassCastException();
			}
			break;
		default:
			break;
		}

		
		String paramStr = param.toString(); 
		if(StringUtils.isEmpty(paramStr)){
			return null;
		}
		
		return paramStr;
	}

	@Override
	public void sendMessage(byte[] message, int id, int type, Object object) {
		MessageEntity messageEntity = new MessageEntity();
		messageEntity.messageId = id;
		messageEntity.comType = type;
		messageEntity.obj = object;
		messageEntity.sendTime = System.currentTimeMillis();
		sendMessageHashMap.put(id, messageEntity);

		ClientManager.getInstance().clientManager.sendMessage(message);
	}

	@Override
	public void onResult(ResponseResult result) {
		System.out.println("LoginServiceImpl -> onResult ResponseResult = "+result);
		if (result.result) {//返回成功
			if (result.cmdName == SocketConstant.SOCKET_COMMAND_USER_LOGIN) {// 是返回的登陆成功
				recordUserNameAndPassword(result);//记录用户名密码
				getLoginPrepareData(result);//准备获取登陆准备数据
			}else if(result.cmdName == SocketConstant.SOCKET_COMMAND_GET_LOGIN_READY_DATA){//是返回的 获取到登陆准备数据成功
				System.out.println("LoginServiceImpl -> onResult result = "+result);
				parseLoginReadyData(result);
			}
		}
	}

	/** 自动登陆 (如果之前有记住用户名和密码) */
	public void autioLogin() {
		String loginConfigXmlPath = Constant.LOGIN_CONFIG_XML_PATH;
		Map<Object, Object> map = PublicTools.getXMLInfo(loginConfigXmlPath);
		if (map == null) {
			System.out.println("not record username and password");
			//这里可以回到登陆界面
			return;
		}

		Object username = map.get(Constant.LOGIN_CONFIG_KEY_USERNAME);
		Object password = map.get(Constant.LOGIN_CONFIG_KEY_PASSWORD);
		if (password == null || username == null) {
			System.out.println("not record password or username");
			//这里可以回到登陆界面
			return;
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(username.toString());
		userInfo.setPassword(password.toString());
		LoginServiceImplInstance.loginServiceImpl.login(userInfo);// 登陆
	}

	/** 如果登陆成功那就存储 用户名和密码  */
	private void recordUserNameAndPassword(ResponseResult result) {
		int id = result.cmdId;
		MessageEntity messageEntity = sendMessageHashMap.get(id);

		if (messageEntity == null) {
			System.out.println("no messageentity in sendMessageHashMap");
			return;
		}

		if (messageEntity.obj == null) {
			System.out.println("messageEntity.obj is null");
			return;
		}

		UserInfo userInfo = null;
		if (messageEntity.obj instanceof UserInfo) {
			userInfo = (UserInfo) messageEntity.obj;
		} else {
			System.out.println("messageEntity.obj not is a UserInfo");
		}

		String username = userInfo.getUserName();
		String password = userInfo.getPassword();
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			System.out.println("record failed username or password is empty");
			return;
		}
		PublicTools.addXMLElement(Constant.LOGIN_CONFIG_XML_PATH, Constant.LOGIN_CONFIG_KEY_USERNAME, username);
		PublicTools.addXMLElement(Constant.LOGIN_CONFIG_XML_PATH, Constant.LOGIN_CONFIG_KEY_PASSWORD, password);
		
		setCurrentUserInfo(userInfo);
	}
	
	/**将登陆成功后的用户、密码信息   放入全局数据中的 当前用户中*/
	public void setCurrentUserInfo(UserInfo userInfo){
		if(userInfo == null){
			throw new NullPointerException();
		}
		
		SystemStoreDataInstance.systemStoreData.setCurrentUserInfo(userInfo);
	}
	
	/**准备 获取登陆准备数据*/
	public void getLoginPrepareData(ResponseResult result){
		int id = result.cmdId;
		MessageEntity messageEntity = sendMessageHashMap.get(id);

		if (messageEntity == null) {
			System.out.println("no messageentity in sendMessageHashMap");
			return;
		}

		if (messageEntity.obj == null) {
			System.out.println("messageEntity.obj is null");
			return;
		}
		
		UserInfo userInfo = null;
		if (messageEntity.obj instanceof UserInfo) {
			userInfo = (UserInfo) messageEntity.obj;
		} else {
			System.out.println("messageEntity.obj not is a UserInfo");
			throw new ClassCastException();
		}

		String username = userInfo.getUserName();
		if(StringUtils.isEmpty(username)){
			throw new NullPointerException();
		}
		
		//获取登陆准备数据
		getLoginReadyData(username);
	}
	
	/**解析从后台获取到的登陆准备数据  主要是解析TableData数据*/
	private void parseLoginReadyData(ResponseResult result){
		int id = result.cmdId;
		MessageEntity messageEntity = sendMessageHashMap.get(id);
		System.out.println("LoginServiceImpl -> parseLoginReadyData messageEntity = "+messageEntity);
		System.out.println("LoginServiceImpl -> parseLoginReadyData result = "+result);
		Object resultObj = result.resultObj;
		if(resultObj == null){
			throw new NullPointerException();
		}
		
		if(resultObj instanceof TableData){
			TableData tableData = (TableData)resultObj;
			ArrayList<String> filedNames = tableData.m_arrFieldName;//字段名集合
			ArrayList<ArrayList<String>> filedValues = tableData.m_arrRecodes;// 字段值集合 的集合
			
			System.out.println("LoginServiceImpl -> parseLoginReadyData -> PublicTools.parseFiledAndValueToMaps");
			ArrayList<Map<String,String>> mapLists = PublicTools.parseFiledAndValueToMaps(filedNames,filedValues);
			if(mapLists == null){
				throw new NullPointerException();
			}
			System.out.println("LoginServiceImpl -> parseLoginReadyData mapLists = " + mapLists);
		}else{
			throw new ClassCastException();
		}
	}
	
}
