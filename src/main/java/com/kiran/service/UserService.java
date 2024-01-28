package com.kiran.service;

import com.kiran.entity.User;
import com.kiran.entity.VerificationToken;
import com.kiran.exception.EmailAlreadyRegisteredException;
import com.kiran.model.UserModel;

public interface UserService {

	User registerUser(UserModel userModel) throws EmailAlreadyRegisteredException;

	void saveVerificationTokenForUser(String token, User user);

	String validateVerificationToken(String token);

	VerificationToken generateNewVerificationToken(String oldToken);

}
