package com.funcaretest.listener;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import com.funcaretest.domain.ResponseResult;

/**
 * 
 *消息回调观察者
 */
public class ResponseObservable {

	private CopyOnWriteArrayList<ResponseListener> listenerList = new CopyOnWriteArrayList<>();
	
	public CopyOnWriteArrayList<ResponseListener> getCopyOnWriteArrayList(){
		return this.listenerList;
	}

	/**添加订阅者*/
	public synchronized void addObserver(ResponseListener listener) {
		if (listener == null)
			throw new NullPointerException();
		if (!listenerList.contains(listener)) {
			listenerList.add(listener);
		}
	}

	/**移除订阅者*/
	public synchronized void removeObserver(ResponseListener listener) {
		if (listener == null)
			throw new NullPointerException();
		if (listenerList.contains(listener)) {
			listenerList.remove(listener);
		}
	}
	
	/**移除所有订阅者*/
	public synchronized void clearObserver() {
		listenerList.clear();
	}
	
	/**通知所有订阅者*/
	public synchronized void notifyObservable(ResponseResult result){
		 for (Iterator<ResponseListener> rlIterator = listenerList.iterator(); rlIterator.hasNext(); ) {
			 ResponseListener listener = (ResponseListener) rlIterator
                     .next();
			 listener.onDataResponse(result);
         }
	}

}
