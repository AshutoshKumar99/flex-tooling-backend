package com.app.flex.tooling.response;

import java.io.Serializable;
import java.util.List;

import com.app.flex.tooling.helper.LoginHelper;
import com.app.flex.tooling.helper.UserDetailsHelper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ResponseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;
	private LoginHelper userDetails;
	
	private List<UserDetailsHelper> userDetailsList;
	
	private String otp;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public List<UserDetailsHelper> getUserDetailsList() {
		return userDetailsList;
	}

	public void setUserDetailsList(List<UserDetailsHelper> userDetailsList) {
		this.userDetailsList = userDetailsList;
	}

	public LoginHelper getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(LoginHelper userDetails) {
		this.userDetails = userDetails;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
