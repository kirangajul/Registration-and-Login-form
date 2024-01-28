package com.kiran.exception;

public class EmailAlreadyRegisteredException extends Exception {

	private String email;
	
	public EmailAlreadyRegisteredException (String message, String email) {
		super(message);
		
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
}
