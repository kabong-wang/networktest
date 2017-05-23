package com.funcaretest.domain;

/**
 * 
 * 用于记录每次发送的消息 的消息序号  类型  发送数据内容  发送时间
 *
 */
public class MessageEntity {
	/**消息序号*/
	public int messageId = -1;
	/** 类型*/
	public int comType = 0;
	/**发送数据内容*/
	public Object obj;
	/** 发送时间*/
	public long sendTime;
	
	@Override
	public String toString() {
		return "MessageEntity [messageId=" + messageId + ", comType=" + comType + ", obj=" + obj + ", sendTime="
				+ sendTime + "]";
	}
}
