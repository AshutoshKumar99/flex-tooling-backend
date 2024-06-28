package com.app.flex.tooling.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secure")
public class DemoController {

	@GetMapping("/demo")
	public ResponseEntity<String> demoStatus(){
		return ResponseEntity.ok("Secure Path Called");
	}
}
