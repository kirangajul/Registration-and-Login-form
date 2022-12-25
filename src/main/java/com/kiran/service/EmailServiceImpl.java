package com.kiran.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}") private String mailSender;
	
	
	@Override
	public void sendSimpleMail(Object recipient, String url) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mailSender);
		message.setTo((String) recipient);
		message.setText("Click the link to verify your account : "+url);
		message.setSubject("Registration Verification");
		javaMailSender.send(message);
		
		
	}

}
