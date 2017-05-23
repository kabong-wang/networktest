package com.funcaretest.util;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

import com.funcaretest.domain.ResponseResult;

public class ResponseCallable implements Callable<ResponseResult>{
	
	private ByteBuffer content = null;
	
	public ResponseCallable(ByteBuffer content){
		this.content = content;
	}

	@Override
	public ResponseResult call() throws Exception {
		ResponseResult responseResult = null;
		if(content!=null){
			responseResult = CommandUtils.decodeResponse(content);
		}
		return responseResult;
	}
	
}
