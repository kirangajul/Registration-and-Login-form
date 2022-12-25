package com.kiran.service;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kiran.dao.UserDao;
import com.kiran.dao.VerificationTokenDao;
import com.kiran.entity.User;
import com.kiran.entity.VerificationToken;
import com.kiran.model.UserModel;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao dao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private VerificationTokenDao tokenDao;
	
	@Override
	public User registerUser(UserModel userModel) {
		
		User user = new User();
			user.setEmail(userModel.getEmail());
			user.setFirstName(userModel.getFirstName());
			user.setLastName(userModel.getLastName());
			user.setRole("USER");
			user.setPassword(passwordEncoder.encode(userModel.getPassword()));
			
			dao.save(user);
		return user;

	}

	@Override
	public void saveVerificationTokenForUser(String token, User user) {
		// TODO Auto-generated method stub
		VerificationToken verificationToken = new VerificationToken(user, token);
		tokenDao.save(verificationToken);
	}

	@Override
	public String validateVerificationToken(String token) {
		
		VerificationToken verificationToken = tokenDao.findByToken(token);
		
		if(verificationToken == null) {
			return "invalid";
		}
		
		User user = verificationToken.getUser();
		
		Calendar cal = Calendar.getInstance();
		
		if(verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0	)	{
			tokenDao.delete(verificationToken);
			return "expired";
		}
		
		user.setEnabled(true);
		dao.save(user);
		return "valid";
	}

	@Override
	public VerificationToken generateNewVerificationToken(String oldToken) {
		VerificationToken verificationToken = tokenDao.findByToken(oldToken);
		verificationToken.setToken(UUID.randomUUID().toString());
		tokenDao.save(verificationToken);
		return verificationToken;
		
	}

	
}
