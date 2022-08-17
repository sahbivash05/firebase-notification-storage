package com.example.notificationapp.RetroFitBuilder;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("platformId")
	private String platformId;

	@SerializedName("id")
	private int id;

	@SerializedName("userId")
	private String userId;

	@SerializedName("token")
	private String token;
	@SerializedName("socialMediaId")
	private int socialMediaId;

	public int getSocialMediaId() {
		return socialMediaId;
	}

	public void setSocialMediaId(int socialMediaId) {
		this.socialMediaId = socialMediaId;
	}

	public void setPlatformId(String platformId){
		this.platformId = platformId;
	}

	public String getPlatformId(){
		return platformId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}