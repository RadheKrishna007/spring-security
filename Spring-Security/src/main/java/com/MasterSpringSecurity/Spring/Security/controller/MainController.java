package com.MasterSpringSecurity.Spring.Security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class MainController {
	
	@GetMapping
	public String getName() {
		return "Name is Pawan";
	}

}
