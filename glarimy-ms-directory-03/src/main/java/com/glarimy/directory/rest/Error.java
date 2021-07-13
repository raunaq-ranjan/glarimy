package com.glarimy.directory.rest;

import java.util.HashMap;
import java.util.Map;

public class Error {
	private int status;
	private String message;
	private Map<String, String> errors;

	public Error(int status, String message) {
		this.status = status;
		this.message = message;
		this.errors = new HashMap<String, String>();
	}

	public void addError(String field, String error) {
		this.errors.put(field, error);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

}
