package com.funcaretest.task;

import com.funcaretest.domain.ResponseResult;
import com.funcaretest.domain.TableData;
import com.funcaretest.listener.ResponseListener;

/**
 * 
 * 一个接口回调任务 测试用
 *
 */
public class HandleResponseTask implements Runnable {

	private ResponseResult responseResult;
	private ResponseListener responseListener;

	public HandleResponseTask(ResponseListener responseListener, ResponseResult responseResult) {
		this.responseListener = responseListener;
		this.responseResult = responseResult;
	}

	@Override
	public void run() {
		if (responseResult != null && responseListener != null) {
			responseListener.onDataResponse(responseResult);
		}
	}

}
