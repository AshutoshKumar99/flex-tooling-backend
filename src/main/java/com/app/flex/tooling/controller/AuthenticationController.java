package com.app.flex.tooling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.flex.tooling.request.InputRequest;
import com.app.flex.tooling.response.Response;
import com.app.flex.tooling.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	@Autowired
	AuthenticationService authenticationService;

	@GetMapping("/check")
	public ResponseEntity<String> applicationStatus(){
		return ResponseEntity.ok("Application Working....");
	}
	
	@PostMapping("/registerUser")
	public ResponseEntity<Response> registerUser(@RequestBody InputRequest inputRequest) {
		return ResponseEntity.ok(authenticationService.registerUser(inputRequest));
	}

	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody InputRequest inputRequest) {
		return ResponseEntity.ok(authenticationService.login(inputRequest));
	}
	
	@PostMapping("/send/otp")
	public ResponseEntity<Response> generateOtp(@RequestBody InputRequest inputRequest) {
		return ResponseEntity.ok(authenticationService.generateOtp(inputRequest));
	}
	
	@PostMapping("/validate/otp")
	public ResponseEntity<Response> validateOtp(@RequestBody InputRequest inputRequest) {
		return ResponseEntity.ok(authenticationService.validateOtp(inputRequest));
	}
	
	@PostMapping("/update/password")
	public ResponseEntity<Response> updatedPassword(@RequestBody InputRequest inputRequest) {
		return ResponseEntity.ok(authenticationService.updatedPassword(inputRequest));
	}
}
