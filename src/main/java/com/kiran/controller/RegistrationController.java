package com.kiran.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiran.entity.User;
import com.kiran.entity.VerificationToken;
import com.kiran.event.RegistrationCompleteEvent;
import com.kiran.exception.EmailAlreadyRegisteredException;
import com.kiran.model.LoginRequest;
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
	
//	@Autowired
//	private AuthenticationManager authenticationManager;
	


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
		try {
			User user = service.registerUser(userModel);
		} catch (EmailAlreadyRegisteredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return e.getMessage();
		}
		//publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
		return "Success";
	}

//	@PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Optionally, you can handle the authentication success here or just return a success response
//        // For example:
//        return ResponseEntity.ok("Login successful! Redirect to the home page or handle as needed.");
//    }

	
	private String applicationUrl(HttpServletRequest request) {

		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
