package com.example.employee_management_system.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.employee_management_system.dto.VerifyOtpRequest;
import com.example.employee_management_system.entity.User;
import com.example.employee_management_system.repository.UserRepository;

@Service
public class OtpVerifyService {
	
	private UserRepository userRepository;
	
	public OtpVerifyService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public String otpVerification(VerifyOtpRequest verifyOtpRequest) {
		Optional<User> optional = userRepository.findByEmail(verifyOtpRequest.getEmail());
		if (optional.isPresent()) {
			User user = optional.get();
			if (!user.getOtp().equals(verifyOtpRequest.getOtp())) {
				return "invalid otp";
			}
			if (LocalDateTime.now().isAfter(user.getOtpExpiryTime())) {
				return "otp got expired";
			}
			else {
				user.setVerified(true);
				user.setOtp(null);
				user.setOtpExpiryTime(null);
				userRepository.save(user);
				return "otp verified successfully";	
			}
		}
		else {
			return "user not found with email " + verifyOtpRequest.getEmail();
		}
		
	}

}
