package com.app.flex.tooling.request;

import java.io.Serializable;

public class Request implements Serializable {

	private static final long serialVersionUID = 1L;

	private RequestData requestData;

	public RequestData getRequestData() {
		return requestData;
	}

	public void setRequestData(RequestData requestData) {
		this.requestData = requestData;
	}

}
