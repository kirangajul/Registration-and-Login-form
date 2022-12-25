package com.kiran.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiran.entity.User;
import com.kiran.entity.VerificationToken;
import com.kiran.event.RegistrationCompleteEvent;
import com.kiran.model.UserModel;
import com.kiran.service.EmailService;
import com.kiran.service.UserService;

@RestController
public class RegistrationController {

	@Autowired
	private UserService service;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private EmailService emailService;

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello World!";
	}

	@GetMapping("/verifyRegistration")
	public String verifyRegistration(@RequestParam("token") String token) {
		String result = service.validateVerificationToken(token);
		if (result.equalsIgnoreCase("valid")) {
			return "User verified Succesfully";

		}
		return "bad user";
	}

	@GetMapping("/resendVerifyToken")
	public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
		VerificationToken verificationToken = service.generateNewVerificationToken(oldToken);
		User user = verificationToken.getUser();
		emailService.sendSimpleMail(user.getEmail(), applicationUrl(request)+"/verifyRegistration?token="+verificationToken.getToken());
		return "Verification Email sent";

	}

	@PostMapping("/register")
	public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
		User user = service.registerUser(userModel);
		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
		return "Success";
	}

	private String applicationUrl(HttpServletRequest request) {

		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
