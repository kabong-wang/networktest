package com.funcaretest.domain;

public class UserInfo {
	
	private String userName;
	private String password;
	
	public UserInfo(){}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String username) {
		this.userName = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void copyUserInfo(UserInfo userInfo){
		this.setUserName(userInfo.getUserName());
		this.setPassword(userInfo.getPassword());
	}
	
	
	@Override
	public String toString() {
		return "UserInfo [userName=" + userName + ", password=" + password + "]";
	}
	
}
