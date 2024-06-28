package com.app.flex.tooling.response;

import java.io.Serializable;

public class Response implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean status;
	private String statusCode;
	private String message;
	private ResponseObject responseObject;

	public Response() {
	}

	public Response(boolean status, String statusCode, String message) {
		this.status = status;
		this.statusCode = statusCode;
		this.message = message;
	}

	public Response(boolean status, String statusCode, String message, ResponseObject responseObject) {
		this.status = status;
		this.statusCode = statusCode;
		this.message = message;
		this.responseObject = responseObject;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseObject getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(ResponseObject responseObject) {
		this.responseObject = responseObject;
	}
}
