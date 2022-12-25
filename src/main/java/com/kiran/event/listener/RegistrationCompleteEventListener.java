package com.kiran.event.listener;

import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


import com.kiran.entity.User;
import com.kiran.event.RegistrationCompleteEvent;
import com.kiran.service.EmailService;
import com.kiran.service.UserService;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>{

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		//create the verification token for the user with link
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.saveVerificationTokenForUser(token, user);
		//send mail to the user
		
		String url = event.getApplicationUrl() + "/verifyRegistration?token=" +token;
		
		//send verification email
		
		String recipient = user.getEmail();
		emailService.sendSimpleMail(recipient, url);
		
		//LoggerFactory.getLogger(RegistrationCompleteEvent.class).info("Click the link to verify your account : {} ", url);

		
	}

}
