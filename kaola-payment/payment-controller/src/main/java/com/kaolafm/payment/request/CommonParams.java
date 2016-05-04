package com.kaolafm.payment.request;

import java.io.Serializable;

public class CommonParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3301324282914979703L;

	private String uid;// 用户ID
	private String udid;// 设备ID
	private String installid;// 安装Id
	private String sessionid;// SESSION Id
	private String appid;// 产品线
	private String channel;// 渠道
	private String version;// 版本号
	private String devicetype;// 设备类型
	private String resolution;// 分辨率
	private String network;// 网络状况
	private Double lat; // 纬度
	private Double lng; // 经度
	private String ip;
	private String suppermode;
	private String imsi;
	private String operator;
	private String token;

	public CommonParams() {

	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getInstallid() {
		return installid;
	}

	public void setInstallid(String installid) {
		this.installid = installid;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSuppermode() {
		return suppermode;
	}

	public void setSuppermode(String suppermode) {
		this.suppermode = suppermode;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "CommonParams [uid=" + uid + ", udid=" + udid + ", installid=" + installid + ", sessionid=" + sessionid
				+ ", appid=" + appid + ", channel=" + channel + ", version=" + version + ", devicetype=" + devicetype
				+ ", resolution=" + resolution + ", network=" + network + ", suppermode=" + suppermode + "]";
	}

}
