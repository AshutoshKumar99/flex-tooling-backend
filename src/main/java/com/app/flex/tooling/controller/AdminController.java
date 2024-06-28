package com.app.flex.tooling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.flex.tooling.request.InputRequest;
import com.app.flex.tooling.response.Response;
import com.app.flex.tooling.service.AdminService;

@RestController
@RequestMapping("/api/v1/secure/admin")
public class AdminController {

	@Autowired
	AdminService adminService;

	@PostMapping("/createUser")
	public ResponseEntity<Response> createUser(@RequestBody InputRequest inputRequest) {
		return ResponseEntity.ok(adminService.createInternalUser(inputRequest));
	}

	@PostMapping("/user/list")
	public ResponseEntity<Response> getUserList(@RequestBody InputRequest inputRequest) {
		return ResponseEntity.ok(adminService.getUserList(inputRequest));
	}

}
