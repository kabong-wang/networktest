package com.funcaretest.domain;

/**
 * 
 * 存放临时的当前的数据
 * 
 */
public class SystemStoreData {
	
	private UserInfo currentUserInfo = new UserInfo();
	
	private SystemStoreData(){}
	
    public static final class SystemStoreDataInstance{
    	public static final SystemStoreData systemStoreData = new SystemStoreData();
    }

	public UserInfo getCurrentUserInfo() {
		if(currentUserInfo!=null){
			return currentUserInfo;
		}else{
			System.out.println("SystemStoreData -> getCurrentUserInfo currentUserInfo="+null);
			return null;
		}
	}

    public synchronized void setCurrentUserInfo(UserInfo userInfo){
    	if(userInfo == null){
    		userInfo = new UserInfo();
    	}
    	this.currentUserInfo.copyUserInfo(userInfo);
    }
    
    /**清除所有临时数据*/
    public void clearAllData(){
    	currentUserInfo = null;//清空 当前登陆用户
    }

}
