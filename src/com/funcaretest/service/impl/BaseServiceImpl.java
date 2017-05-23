package com.funcaretest.service.impl;

import java.util.HashMap;

import com.funcaretest.domain.MessageEntity;
import com.funcaretest.domain.ResponseResult;
import com.funcaretest.network.ClientManager;
import com.funcaretest.service.BaseService;

public abstract class BaseServiceImpl implements BaseService {

	protected HashMap<Integer, MessageEntity> sendMessageHashMap = new HashMap<>();
	
	public BaseServiceImpl() {//BaseServiceImpl子类 无参实例化的时候就会 被注册监听回调
		addListener();
	}
	
	protected int cmdId;//指令序号
	protected int cmdLevel;//指令级别
	protected int cmdType;//指令类型
	protected int cmdName;//指令名
	protected int charSet;//参数编码
	protected String params = null;//拼接参数字符串
	
	/**控制数据回调 */
	public abstract void onResult(ResponseResult result);
	
	/**发送消息 临时保存发送数据*/
	public abstract void sendMessage(byte[] message, int cmdId,int cmdType,Object object);
	
	@Override
	public void removeListener() {//移除监听不再收到回调
		ClientManager.getInstance().reveicerControl.removeResponseListener(this);
	}
	
	@Override
	public void addListener() {//注册监听收到回调
		ClientManager.getInstance().reveicerControl.addResponseListener(this);
	}
	
	protected void destroy(){
		removeListener();
	}
	
	@Override
	public void onDataResponse(ResponseResult result) {
		if (result.cmdId == this.cmdId) {//回调到指定的BaseImpl 不然会回调到所有已实例化当前类的onDataResponse方法
			onResult(result);
		}
	}

	@Override
	public String toString() {
		return "BaseImpl [cmdLevel=" + cmdLevel + ", cmdType=" + cmdType + ", cmdName=" + cmdName + ", charSet="
				+ charSet + ", params=" + params + "]";
	}

}
