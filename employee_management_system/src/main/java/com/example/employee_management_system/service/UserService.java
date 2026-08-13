package com.example.employee_management_system.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.employee_management_system.dto.RegisterRequest;
import com.example.employee_management_system.entity.User;
import com.example.employee_management_system.repository.UserRepository;
import com.example.employee_management_system.util.OtpGenerator;

@Service
public class UserService {

	private UserRepository userRepository;
	private EmailService emailService;
	
	// constructor injection
	public UserService(UserRepository userRepository, EmailService emailService) {
		super();
		this.userRepository = userRepository;
		this.emailService = emailService;
	}
	
	public String registerRequest(RegisterRequest registerRequest) {
		Optional<User> ou = userRepository.findByEmail(registerRequest.getEmail());
		if(ou.isPresent()) {
			return "emailid already exists";
		}
		else {
			User user = new User();
			user.setName(registerRequest.getName());
			user.setEmail(registerRequest.getEmail());
			user.setPassword(registerRequest.getPassword());
			user.setRole("ROLE_USER");
			user.setVerified(false);
			
			String otp = OtpGenerator.generateOtp();
			user.setOtp(otp);
			
			user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
			
			
			userRepository.save(user);
			// *************
		
			emailService.sendOtp(registerRequest.getEmail(), otp);
			return "otp sent";
			
			
		}
	}


	
}
