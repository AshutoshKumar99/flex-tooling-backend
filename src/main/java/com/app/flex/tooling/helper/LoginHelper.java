package com.app.flex.tooling.helper;

import java.io.Serializable;

public interface LoginHelper extends Serializable{

	static final long serialVersionUID = 1L;
	
	String getFirstName();
	String getLastName();
	String getEmail();
	String getMobileNo();
	String getRole();
	Integer getUserId();
	String getEmployeeId();
	String getPersonId();
}
