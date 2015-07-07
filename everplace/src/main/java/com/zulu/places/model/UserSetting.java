package com.zulu.places.model;


public class UserSetting {

	private static UserSetting instance;

	public static UserSetting getInstance() {
		if (instance == null)
			instance = new UserSetting();
		return instance;
	}

	private String userName;
	private String password;
	private Boolean remembered = false;
	private Boolean loggedIn = false;
	private String userId;
	private String userFullName;
	private String ipServer;
	public boolean isConnectInternet = false;
	private String token;
	private String phone;
	private String avatar;
	private String company;
	private String email;
	private int clubId;
	private int targetMode;
	
	public Boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(Boolean isLoggedIn) {
		this.loggedIn = isLoggedIn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isRemembered() {
		return remembered;
	}

	public void setRemembered(Boolean isRemembered) {
		this.remembered = isRemembered;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserFullName() {
		return this.userFullName;
	}

	public String getIpServer() {
		return ipServer;
	}

	public void setIpServer(String ipServer) {
		this.ipServer = ipServer;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getTargetMode() {
		return targetMode;
	}

	public void setTargetMode(int targetMode) {
		this.targetMode = targetMode;
	}
}
