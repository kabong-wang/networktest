package com.funcaretest.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.funcaretest.domain.ResponseResult;
import com.funcaretest.domain.TableData;

//指令工具
public class CommandUtils {

	// 数据分隔符号
	public final static String m_strSep_Type = "\\x10"; // 参数类型 分隔符
	public final static String m_strSep_Feild = "\u0008"; // 字段 参数分隔符
	public final static String m_strSep_Row = "\u0011"; // 行 参数分隔符
	public final static String m_strSep_Table = "\\x1b"; // 表 参数分隔符

	// ----- 发送指令序号
	private static int m_nSendCmdID = 0;
	// ----- 是否初始化映射表
	private static boolean isInit = false;
	// ----- 命令映射表
	private static HashMap<Integer,Integer> commandHashMap = new HashMap<>();

	// ----- 文字编码
	public final static int m_nCodeType_Unicode = 0x01; // Unicode字符集
	public final static int m_nCodeType_UTF8 = 0x02; // UTF8字符集

	// 指令优先级
	public final static int m_nCmdLevel_Low = 0x00; // 指令级别低
	public final static int m_nCmdLevel_high = 0x01; // 指令级别高

	// 指令类型
	public final static int m_nCmdType_CL_DownData = 0x20; // 从数据库下载数据
	public final static int m_nCmdType_CL_UpData = 0x21; // 向数据库提交数据，只需要返回处理结果
	public final static int m_nCmdType_CL_QuestServer = 0x22; // 向主服务请求数据


	/* 协议头部内容 */
	public final static int m_PACKAGE_HEAD = 0xff; // 包头
	public final static int m_PACKAGE_HEAD_LENGTH = 11; // 包头长

	// -----数据库指令序号
	public final static int m_nCmdName_Login = 0x01; // 登陆
	public final static int m_nCmdName_GetUser = 0x02; // 获取用户数据
	public final static int m_nCmdName_GetSubUser = 0x03; // 获取子用户数据
	public final static int m_nCmdName_GetCar = 0x04; // 获取车辆信息
	public final static int m_nCmdName_GetUserOwnCar = 0x05; // 获取用户拥有车辆
	public final static int m_nCmdName_AddSubUser = 0x06; // 添加子用户
	public final static int m_nCmdName_DelSubUser = 0x07; // 删除子用户
	public final static int m_nCmdName_ModUser = 0x08; // 修改用户资料
	public final static int m_nCmdName_AddCar = 0x09; // 添加用户车辆
	public final static int m_nCmdName_DelCar = 0x0A; // 删除用户车辆
	public final static int m_nCmdName_ModCar = 0x0B; // 修改车辆资料
	public final static int m_nCmdName_AddCarToSubUser = 0x0C; // 给子用户分配车辆
	public final static int m_nCmdName_DelCarFromSubUser = 0x0D; // 移除子用户的车辆
	public final static int m_nCmdName_GetTrackReadyData = 0x0E; // 获取车辆时间范围内的轨迹总数
	public final static int m_nCmdName_GetTrack = 0x0F; // 获取单辆车的轨迹
	public final static int m_nCmdName_GetLoginReadyData = 0x10; // 获取登陆准备数据
	public final static int m_nCmdName_GetTrackAlarmReadyData = 0x11; // 获取车辆报警数据的准备数据
	public final static int m_nCmdName_GetTrackAlarm = 0x12;// 获取车辆报警数据


	public final static int m_nCmdName_GetTrackDayDriveData = 0x1C; // 获取车辆日运行报表
	public final static int m_nCmdName_GetPhotoReadyData = 0x24; // 获取照片准备数据
	public final static int m_nCmdName_GetPhoto = 0x25; // 获取照片

	public final static int m_nCmdName_SearchUserByTEID = 0x2D; // 搜索用户通过序列号

	public final static int m_nCmdName_DelFenceFromUser = 0x31; //0X31 删除电子围栏    4.1.1.17	删除围栏  0x31
	public final static int m_nCmdName_GetUserFence = 0x32; // 获取电子围栏   4.1.1.18	获取围栏
	public final static int m_nCmdName_GetUserCarFence = 0x35; // 获取用户所有车辆的电子围栏   4.1.1.19	获取设备围栏
	public final static int m_nCmdName_GetFenceAlarmReadyData = 0x36; // 获取围栏报警准备数据  4.1.1.20	获取围栏报警信息准备数据
	public final static int m_nCmdName_GetFenceAlarm = 0x37; // 获取围栏报警数据  4.1.1.21	获取围栏报警信息
	public final static int m_nCmdName_LoginIMEI = 0x7B; // IMEI登陆
	public final static int m_nCmdName_GetCarInfo = 0x7C; // 获取车辆信息
	public final static int m_nCmdName_GetUserStruct = 0x7D; // 获取用户结构信息

	public final static int m_nCmdName_Register = 0x90; //注册  4.1.1.25	用户注册
	public final static int m_nCmdName_AddFence = 0x91;//添加电子围栏  4.1.1.22	添加围栏
	public final static int m_nCmdName_ModUserPassword = 0x92; // 修改用户密码  4.1.1.26	修改用户密码
	public final static int m_nCmdName_BindCar = 0x94; // 绑定设备  4.1.1.27	绑定设备
	public final static int m_nCmdName_GetTrackDayStep = 0x95; // 获取某天的总里程数  4.1.1.28	获取设备一天的步数
	public final static int m_nCmdName_DeleteUserCar = 0x97; // 删除设备子用户  4.1.1.8	删除子用户 --现用于解绑设备
	//add 1014  ------------------- 0x98  获取设备相关子用户
	public final static int m_nCmdName_GetUserByCar = 0x98;  //  获取设备相关子用户  //m_nCmdName_GetUserCar  m_nCmdName_GetUserByCar
	public final static int m_nCmdName_DelCarFromUser = 0x99;  //主用户解绑设备  
	public final static int m_nCmdName_DelCarAndMoveAuthority = 0x9A;  //解绑设备并转移权限  
	public final static int m_nCmdName_GetTrackDaySleep = 0x9B; // 获取某天的睡眠数  	获取设备一天的睡眠
	public final static int m_nCmdName_GetDeviceOfflineSpeeking = 0x9C; // 获取设备的离线消息

	// ----- 请求服务端指令
	public final static int m_nCmdName_QuestMonitorCar = 0x0101; // 请求监控车辆
	public final static int m_nCmdName_CancelMonitorCar = 0x0103; // 取消监控车辆
	public final static int m_nCmdName_GetPosition = 0x0102; // 获取车辆定位
	public final static int m_nCmdName_CtrlTE_CallLocate = 0x0201;//下发车辆上报定位指令
	public final static int CMD_NAME_CL_CTRL_SET_THROUGH = 0x0300; // 下发透传指令
	public final static int CMD_NAME_CL_CTRL_SET_THROUGH_SUCCESS = 0x030B; // 下发透传指令成功
	public final static int m_nCmdName_RemoteControlCapture = 0x030E;//远程监拍
	public final static int m_nCmdName_Speaking = 0x030F;//语音对讲
	public static final int m_nCmdName_modify_baby_info = 0x0310;//修改宝贝资料
	public final static int m_nCmdName_Get_Device_Online_State = 0x0311; // 获取车辆在线状态

	// ----- 终端上传
	public final static int m_nCmdName_TE_Offline = 0x1000; // 机器掉线
	public final static int m_nCmdName_TELocate = 0x1001; // 车辆上传最新定位
	public final static int m_nCmdName_NewPhoto = 0x1003; // 最新照片
	public final static int m_nCmdName_Hand = 0x1004; // 握手
	public final static int m_nCmdName_UpdateTEParam = 0x1005; // 终端更新参数
	public final static int m_nCmdName_TEUploadPhoto = 0x101F; // 终端上传监拍图片
	public final static int m_nCmdName_TEResponseSpeaking = 0x1020; // 终端上传语音接收结果
	public final static int m_nCmdName_Family_Member_Changed = 0x1023; // 通知设备家族成员信息改变
	public final static int m_nCmdName_Device_On_Line = 0x1024; // 设备上线通知消息

	public final static int m_nCmdName_Reply_Locate = 0x1501;		// 应答点名
	public final static int m_nCmdName_SetUserLogout = 0x2002;// 用户在其他地方登陆,当前用户主动退出
	public final static int m_nCmdName_UserCarFenceAlarm = 0x2006; // 车辆围栏报警
	public final static int m_nCmdName_UserCarStateAlarm = 0x200C; // 紧急报警

	public final static int m_nCmdName_GetTrackComplete = 0x8000; // 获取足迹成功信息
	public final static int m_nCmdName_GetTrackAlarmComplete = 0x8001; // 获取报警成功信息
	public final static int m_nCmdName_GetSpeakingMessageComplete = 0x8002; // 获取语音信息
	public final static int m_nCmdName_GetWalkNumberComplete = 0x8003; // 获取语音信息
	
	
	private static void initCommandMap(){//封装 指令别名-真指令名  键值对
		if(!isInit){
			isInit = true;
			commandHashMap.put(SocketConstant.SOCKET_COMMAND_USER_LOGIN,m_nCmdName_Login);  //用户登录
			commandHashMap.put(SocketConstant.SOCKET_COMMAND_GET_LOGIN_READY_DATA,m_nCmdName_GetLoginReadyData);//获取登陆准备数据
			commandHashMap.put(SocketConstant.SOCKET_COMMAND_SET_USER_LOGINOUT,m_nCmdName_SetUserLogout);//挤出登陆
		}
	}
	
	// 获取发送指令ID
	public static synchronized int getSendCmdID() {
		m_nSendCmdID++;
		return m_nSendCmdID;
	}
	
	/**
	 * 根据CommanUtils中定义的指令序号 得到SocketConstant中的指令名
	 * @param serverCommand CommandUtils中定义的指令名
	 * @return
	 */
	private static int getCommand(int serverCommand){
		if(!isInit){
			initCommandMap();
		}
		System.out.println("CommandUtils getCommand serverCommand=0x"+Integer.toHexString(serverCommand));
		Set set = commandHashMap.entrySet();
		ArrayList arr = new ArrayList<>();
		Iterator it = set.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			if(entry.getValue().equals(serverCommand)) {
				int s = (int)entry.getKey();
				return s;
			}
		}
		return -1;
	}

	// 拼装指令头
	public static int createCommandHead(int cmdLevel, int cmdType, int cmdName) {
		int cmdHead = 0;

		// 指令级别 一字节 占在int从左到右的第一个字节的地方
		cmdLevel = (cmdLevel << 24) & 0xFF000000;// 移动到开头一字节处
		cmdHead = cmdHead + cmdLevel;

		// 指令类型 一字节 占在int从左到右的第二个字节的地方
		cmdType = (cmdType << 16) & 0x00FF0000;// 移动到开头二字节处
		cmdHead = cmdHead + cmdType;

		// 指令名 两字节 占在int从左到右的 第三、四个字节的地方
		cmdName = StringUtils.InvertUintBit(cmdName);// 高低位倒置
		cmdName = (cmdName >> 16) & 0x0000FFFF;// 移动到第三、四字节
		cmdHead = cmdHead + cmdName;

		return cmdHead;
	}

	/**
	 * 拼装数据内容结构说明信息数据包
	 * 
	 * @param cmdLevel
	 * @param cmdType
	 * @param cmdName
	 * @param charSet
	 *            编码
	 * @return
	 */
	private static ByteBuffer createCommandHeadPack(int cmdId,int cmdLevel, int cmdType, int cmdName, int charSet) {
		ByteBuffer headPack = null;
		int cmdHead = createCommandHead(cmdLevel, cmdType, cmdName);
		if (cmdHead != 0) {
			headPack = ByteBuffer.allocate(m_PACKAGE_HEAD_LENGTH);
			headPack.putInt(StringUtils.InvertUintBit(cmdId));// 指令序号
			headPack.putInt(cmdHead);// 指令优先级、指令类型、指令名
			headPack.put((byte) 0);// 请求类型
			headPack.put((byte) charSet);// 编码
			headPack.put((byte) (headPack.position() + 1));// 偏移量
		} else {
			return null;
		}
		return headPack;
	}

	/**
	 * 创建参数数据内容结构数据包
	 * 
	 * @param param
	 *            拼接的参数字符串
	 * @param charSet
	 *            后台接收的数据编码类型
	 * @return
	 */
	private static ByteBuffer createDataParamPack(String param, int charSet) {
		ByteBuffer dataParam = null;
		ByteBuffer tempBuffer = null;
		if (param != null) {
			if (charSet == m_nCodeType_Unicode) {// Unicode
				tempBuffer = StringUtils.UTF8ToUnicode(param);
			} else if (charSet == m_nCodeType_UTF8) {// UTF-8
				tempBuffer = PublicTools.byteToByteBuffer(param.getBytes());
			}

			// 拼接两个自定义结尾符号
			dataParam = ByteBuffer.allocate(tempBuffer.capacity() + 2);
			for (int i = 0; i < tempBuffer.capacity(); i++) {
				dataParam.put(tempBuffer.get(i));
			}
			dataParam.put((byte) 0);
			dataParam.put((byte) 0);
		} else {
			return null;
		}
		return dataParam;
	}

	// 拼装最终发送到后台的ByteBuffer
	public static ByteBuffer createDataContentPack(int cmdId,int cmdLevel, int cmdType, int cmdName, int charSet, String param) {
		ByteBuffer dataContent = null;
		ByteBuffer dataHead = createCommandHeadPack(cmdId,cmdLevel, cmdType, cmdName, charSet);
		ByteBuffer dataParam = createDataParamPack(param, charSet);

		if (dataParam != null && dataHead != null) {
			int dataContentLength = dataHead.capacity() + dataParam.capacity();// 数据说明、参数包长度

			dataContent = ByteBuffer.allocate(dataContentLength + m_PACKAGE_HEAD_LENGTH);// 再多一个包头长度
			dataContent.put((byte) m_PACKAGE_HEAD);// 添加包头
			dataContent.put((byte) m_PACKAGE_HEAD_LENGTH);// 添加包头偏移量

			ByteBuffer tempBuffer = ByteBuffer.allocate(20);// 分配一个20个长度临时的buffer
			tempBuffer.putInt(StringUtils.InvertUintBit(dataContentLength));// 临时存放数据内容结构说明和参数数据包总长
			tempBuffer.putInt(StringUtils.InvertUintBit(1));// 临时存放 包头包序号 默认1

			for (int i = 0; i < tempBuffer.position(); i++) {// 将数据说明参数包、包头包序号放入dataContent
				dataContent.put(tempBuffer.get(i));
			}

			byte checkSum = 0;
			for (int i = 0; i < dataContent.position(); i++) {// 头校验
				checkSum ^= dataContent.get(i);
			}

			dataContent.put(checkSum);// 放入头校验

			for (int i = 0; i < dataHead.position(); i++) {// 依次一字节一字节放入数据说明包
				dataContent.put(dataHead.get(i));
			}

			for (int i = 0; i < dataParam.position(); i++) {// 依次一字节一字节放入参数包
				dataContent.put(dataParam.get(i));
			}

			return dataContent;
		} else {
			return null;
		}
	}

	/**
	 * 检查数据包
	 * 
	 * @param response
	 *            数据包
	 * @return
	 */
	public static boolean checkContent(ByteBuffer response) {
		boolean result = false;

		if(response == null){
			throw new NullPointerException();
		}
		
		response.position(0);
		if (response.capacity() < 7) {//至少是一个心跳包 <HEART>
			return false;
		}
		
		int head = response.get();
		head = head&0xFF;//-1&0xFF 得到的是255 不然字节判断会出问题
		if (head != m_PACKAGE_HEAD) {// 判断包头
			return false;
		}

		int headOffset = response.get();
		headOffset &= m_PACKAGE_HEAD_LENGTH;
		int contentLength = response.getInt();
		contentLength = StringUtils.InvertUintBit(contentLength);
		if (headOffset > 255 || contentLength > 1024 * 1000) {// 判断包长度是否正常
			return false;
		}

		int resId = response.getInt();
		resId = StringUtils.InvertUintBit(resId);
		byte checkSum = 0;
		for (int i = 0; i < response.position(); i++) {
			checkSum ^= response.get(i);
		}
		if (checkSum != response.get()) {// 判断校验位是否正确
			return false;
		}

		result = true;// 以上判断通过即数据包无误

		return result;
	}

	/**
	 * 解析数据参数信息数据包
	 * @param content 数据参数信息数据包
	 * @return
	 */
	public static ResponseResult parseContent(ByteBuffer content){
		ResponseResult responseResult = new ResponseResult();
		if(content == null){
			throw new NullPointerException();
		}
		int cmdId = content.getInt();//取得指令序号
		cmdId = StringUtils.InvertUintBit(cmdId);
		int cmdLevel = content.get();//指令级别
		int cmdType = content.get();//指令类型
		int cmdName = content.getChar();//这里指令名   存的比较特殊 倒置十六位 先以Int取出 再倒置 右移16位
		cmdName = StringUtils.InvertUintBit(cmdName);
		cmdName = cmdName>>16;
		
		if(cmdName <= 0 ){//指令名不对 (自定义指定的指令名好像都是大于0的)
			return null;
		}
		
		int cmdAction = content.get();//取得处理结果
		int cmdCodeType = content.get();//取得编码类型
		int offset = content.get();//取得偏移量
		
		//初始化responseResult
		responseResult.cmdId = cmdId;
		responseResult.cmdLevel = cmdLevel;
		responseResult.cmdType = cmdType;
		responseResult.result = (cmdAction == 1);
		responseResult.cmdAction = cmdAction;
		responseResult.cmdCodeType = cmdCodeType;
		responseResult.cmdName = getCommand(cmdName);
		responseResult.offset = offset;
		
		if(content.position()<content.capacity()){//如果还有数据 那就是参数部分数据了 需封装成TableData
			
			ByteBuffer resultDataByteBuffer = null;
			String resultDataString = null;
			
			//如果指令名是 定位、新照片、围栏报警、运行数据 
			if(responseResult.cmdName == m_nCmdName_GetPosition
					|| responseResult.cmdName == m_nCmdName_NewPhoto
					|| responseResult.cmdName == m_nCmdName_UserCarFenceAlarm
					|| responseResult.cmdName == m_nCmdName_GetTrackDayDriveData){
				
				resultDataByteBuffer = ByteBuffer.allocate(content.capacity() + 4);//比原 数据内容说明参数数据包长度 还多4个字节
				resultDataByteBuffer.put(content.array(), 0,content.capacity());//将原 数据内容说明参数数据包 放进来
				resultDataByteBuffer.position(content.position());//移动到 原 数据内容说明参数数据包 的偏移量的地方
				resultDataByteBuffer.put(resultDataByteBuffer.capacity() - 4,(byte) 0x08);//将偏移量后的那个字节 赋值0x08

				resultDataByteBuffer.position(content.position());//移动到 原 数据内容说明参数数据包 的偏移量的地方
			}else{
				//得到参数信息  数据包
				resultDataByteBuffer = ByteBuffer.wrap(content.array(), content.position(), content.capacity()- content.position());
			}
			
			if(cmdCodeType == m_nCodeType_Unicode){//如果是Unicode编码
				resultDataString = StringUtils.UnicodeToUTF8(resultDataByteBuffer);
				System.out.println("CommandUtils parseContent resultDataString = "+ resultDataString);
				if (responseResult.cmdType == m_nCmdType_CL_DownData
						|| responseResult.cmdType == m_nCmdType_CL_UpData) {//如果是从数据库下载或提交数据  返回的就是TableData
					responseResult.resultObj = new TableData(
							resultDataString);//将具体内容数据包的UTF-8字符串 转变为TableData
				}else{//不需要组装的字符串 字节赋值给resultObj
					responseResult.resultObj = resultDataString;
				}
			}else if(cmdCodeType == m_nCodeType_UTF8){//如果是UTF8编码
				try {
					resultDataString = new String(resultDataByteBuffer.array(),"UTF-8");
					responseResult.resultObj = resultDataString;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			
		}else{
			responseResult.resultObj = null;
		}
		
		return responseResult;
	}
	
	/**
	 * 将后台发来的  "一个完整的数据包"  变成ResponseResult
	 * 
	 * @param response
	 * @return
	 */
	public static ResponseResult decodeResponse(ByteBuffer response) {
		ResponseResult responseResult = null;
		if(response == null){
			throw new NullPointerException();
		}
		if (!checkContent(response)) {
			return null;
		}
		
		ByteBuffer contentByteBuffer = null;
		response.position(1);//跳过包头
		int headOffset = response.get();
		headOffset &= m_PACKAGE_HEAD_LENGTH;
		int contentLength = response.getInt();
		contentLength = StringUtils.InvertUintBit(contentLength);
		if(headOffset + contentLength > response.capacity() ){
			return null;
		}
		contentByteBuffer = ByteBuffer.wrap(response.array(), headOffset, contentLength);
		responseResult = parseContent(contentByteBuffer);

		return responseResult;

	}
	

}
