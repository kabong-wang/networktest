package com.funcaretest.test;

import java.nio.ByteBuffer;

import com.funcaretest.domain.UserInfo;
import com.funcaretest.service.impl.LoginServiceImpl;
import com.funcaretest.service.impl.LoginServiceImpl.LoginServiceImplInstance;
import com.funcaretest.util.CommandUtils;
import com.funcaretest.util.PublicTools;
import com.funcaretest.util.SocketConstant;
import com.funcaretest.util.StringUtils;

public class PackTest {

	private static final int PACK_HEAD_LENGTH = 11;

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
	public static ByteBuffer createCommandHeadPack(int cmdLevel, int cmdType, int cmdName, int charSet) {
		ByteBuffer headPack = null;
		int cmdHead = createCommandHead(cmdLevel, cmdType, cmdName);
		if (cmdHead != 0) {
			headPack = ByteBuffer.allocate(CommandUtils.m_PACKAGE_HEAD_LENGTH);
			headPack.putInt(StringUtils.InvertUintBit(CommandUtils.getSendCmdID()));// 指令序号
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
	public static ByteBuffer createDataParamPack(String param, int charSet) {
		ByteBuffer dataParam = null;
		ByteBuffer tempBuffer = null;
		if (param != null) {
			if (charSet == CommandUtils.m_nCodeType_Unicode) {// Unicode
				tempBuffer = StringUtils.UTF8ToUnicode(param);
			} else if (charSet == CommandUtils.m_nCodeType_UTF8) {// UTF-8
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
	public static ByteBuffer createDataContentPack(int cmdLevel, int cmdType, int cmdName, int charSet, String param) {
		ByteBuffer dataContent = null;
		ByteBuffer dataHead = createCommandHeadPack(cmdLevel, cmdType, cmdName, charSet);
		ByteBuffer dataParam = createDataParamPack(param, charSet);

		if (dataParam != null && dataHead != null) {
			int dataContentLength = dataHead.capacity() + dataParam.capacity();// 数据说明、参数包长度

			dataContent = ByteBuffer.allocate(dataContentLength + CommandUtils.m_PACKAGE_HEAD_LENGTH);// 再多一个包头长度
			dataContent.put((byte) CommandUtils.m_PACKAGE_HEAD);// 添加包头
			dataContent.put((byte) CommandUtils.m_PACKAGE_HEAD_LENGTH);// 添加包头偏移量

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

	public static void main(String[] args) {
		int cmdHead = createCommandHead(CommandUtils.m_nCmdLevel_high, CommandUtils.m_nCmdType_CL_UpData,
				CommandUtils.m_nCmdName_Login);

		PublicTools.displayIntToBinaryStr(cmdHead);
		int cmdLevel = CommandUtils.m_nCmdLevel_high;
		int cmdType = CommandUtils.m_nCmdType_CL_UpData;
		int cmdName = CommandUtils.m_nCmdName_Login;
		int charSet = CommandUtils.m_nCodeType_Unicode;

		LoginServiceImpl loginImpl = LoginServiceImplInstance.loginServiceImpl;
		String username = "";
		String password = "";
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(username);
		userInfo.setPassword(password);
		String param = loginImpl.packParamData(SocketConstant.SOCKET_COMMAND_USER_LOGIN,userInfo);
		ByteBuffer requestParam = createDataContentPack(cmdLevel, cmdType, cmdName, charSet, param);
		PublicTools.displayHexBytes(PublicTools.byteBufferToByte(requestParam));

		username = "";
		password = "";
		userInfo.setUserName(username);
		userInfo.setPassword(password);
		requestParam = null;
		param = loginImpl.packParamData(SocketConstant.SOCKET_COMMAND_USER_LOGIN,userInfo);
		requestParam = createDataContentPack(cmdLevel, cmdType, cmdName, charSet, param);
		PublicTools.displayHexBytes(PublicTools.byteBufferToByte(requestParam));
	}

}
