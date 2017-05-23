package com.funcaretest.domain;

public class LoginReadyData {
	
	private String nPublishMessageCount;
	private String nFenceCount;
	private String nUserRFIDCount;
	private String nUserStructCount;
	private String nCarOwnFenceCount;
	private String nSubUserCount;
	private String nCarCount;
	private String nUserCount;
	private String nPOICount;
	private String nUserOwnCarCount;
	
	
	
	public LoginReadyData() {
		this.nPublishMessageCount = "0";
		this.nFenceCount = "0";
		this.nUserRFIDCount = "0";
		this.nUserStructCount = "0";
		this.nCarOwnFenceCount = "0";
		this.nSubUserCount = "0";
		this.nCarCount = "0";
		this.nUserCount = "0";
		this.nPOICount = "0";
		this.nUserOwnCarCount = "0";
	}
	public String getnPublishMessageCount() {
		return nPublishMessageCount;
	}
	public void setnPublishMessageCount(String nPublishMessageCount) {
		this.nPublishMessageCount = nPublishMessageCount;
	}
	public String getnFenceCount() {
		return nFenceCount;
	}
	public void setnFenceCount(String nFenceCount) {
		this.nFenceCount = nFenceCount;
	}
	public String getnUserRFIDCount() {
		return nUserRFIDCount;
	}
	public void setnUserRFIDCount(String nUserRFIDCount) {
		this.nUserRFIDCount = nUserRFIDCount;
	}
	public String getnUserStructCount() {
		return nUserStructCount;
	}
	public void setnUserStructCount(String nUserStructCount) {
		this.nUserStructCount = nUserStructCount;
	}
	public String getnCarOwnFenceCount() {
		return nCarOwnFenceCount;
	}
	public void setnCarOwnFenceCount(String nCarOwnFenceCount) {
		this.nCarOwnFenceCount = nCarOwnFenceCount;
	}
	public String getnSubUserCount() {
		return nSubUserCount;
	}
	public void setnSubUserCount(String nSubUserCount) {
		this.nSubUserCount = nSubUserCount;
	}
	public String getnCarCount() {
		return nCarCount;
	}
	public void setnCarCount(String nCarCount) {
		this.nCarCount = nCarCount;
	}
	public String getnUserCount() {
		return nUserCount;
	}
	public void setnUserCount(String nUserCount) {
		this.nUserCount = nUserCount;
	}
	public String getnPOICount() {
		return nPOICount;
	}
	public void setnPOICount(String nPOICount) {
		this.nPOICount = nPOICount;
	}
	public String getnUserOwnCarCount() {
		return nUserOwnCarCount;
	}
	public void setnUserOwnCarCount(String nUserOwnCarCount) {
		this.nUserOwnCarCount = nUserOwnCarCount;
	}
	@Override
	public String toString() {
		return "LoginReadyData [nPublishMessageCount=" + nPublishMessageCount + ", nFenceCount=" + nFenceCount
				+ ", nUserRFIDCount=" + nUserRFIDCount + ", nUserStructCount=" + nUserStructCount
				+ ", nCarOwnFenceCount=" + nCarOwnFenceCount + ", nSubUserCount=" + nSubUserCount + ", nCarCount="
				+ nCarCount + ", nUserCount=" + nUserCount + ", nPOICount=" + nPOICount + ", nUserOwnCarCount="
				+ nUserOwnCarCount + "]";
	}
	
	
	
}
