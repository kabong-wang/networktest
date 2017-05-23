package com.funcaretest.domain;

import java.io.Serializable;

public class ResponseResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean result;//操作结果
	public int cmdId;//指令序号
	public int cmdLevel;//指令优先级
	public int cmdType;//指令类型
	public int cmdName;//指令名称 （对应SocketConstant中定义的命令）
	public int cmdAction;//处理结果
	public int cmdCodeType;//数据编码类型
	public int offset;//内容偏移量
	public Object resultObj;//返回的参数字符串（TableData 或字符串）
	//public ByteBuffer resultDataByteBuffer;//返回的二进制数据
	//public String resultDataString;//返回的参数字符串
	//public TableData resultData;//结构化的表数据

	public ResponseResult(){
		result = false;
	}

	@Override
	public String toString() {
		return "ResponseResult{" +
				"result=" + result +
				", cmdId=" + cmdId +
				", cmdLevel=" + cmdLevel +
				", cmdType=" + cmdType +
				", cmdName=" + cmdName +
				", cmdAction=" + cmdAction +
				", cmdCodeType=" + cmdCodeType +
				", offset=" + offset +
				", resultObj=" + resultObj +
				'}';
	}
}
