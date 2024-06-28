package com.app.flex.tooling.request;

import java.io.Serializable;

public class InputRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private Request request;

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
}
