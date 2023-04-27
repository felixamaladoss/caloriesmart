package com.calories.spring.core.payload.response;

import java.util.List;

public class UserInfoResponse {
	private Long id;
	private String username;
	private String email;
	private String jwt_token;
	private List<String> roles;

	public UserInfoResponse(Long id, String username, String email, String jwt_token, List<String> roles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.jwt_token = jwt_token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getJwt_token() {
		return jwt_token;
	}

	public void setJwt_token(String jwt_token){
		this.jwt_token = jwt_token;
	}


}
