package com.MasterSpringSecurity.Spring.Security.controller;

import java.security.Principal;

import javax.management.BadAttributeValueExpException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MasterSpringSecurity.Spring.Security.JwtSecurity.JwtHelper;
import com.MasterSpringSecurity.Spring.Security.dtos.JwtRequest;
import com.MasterSpringSecurity.Spring.Security.dtos.JwtResponse;
import com.MasterSpringSecurity.Spring.Security.entity.User;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private UserDetailsService userDetailService;
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private JwtHelper helper;
	
	
	@PostMapping("/logging")
	public ResponseEntity<JwtResponse> authenticateRequest(@RequestBody JwtRequest request){
		this.doAuthenticate(request.getEmail(), request.getPassword());
		UserDetails userDetails = userDetailService.loadUserByUsername(request.getEmail());
		String token = this.helper.generateToken(userDetails);
		User user = User.builder().email(userDetails.getUsername())
					  .password(userDetails.getPassword()).build();
		JwtResponse response = JwtResponse.builder().jwtToken(token).user(user).build();
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	private void doAuthenticate(String email, String password) {
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		
		try {
			manager.authenticate(authentication);
		} catch(Exception e) {
			logger.info("Invalid username or password!! {}",e.getMessage());
		}
		
	}
	
	

	// get name only for current user
	@GetMapping("/current")
	public ResponseEntity<String> getCurrentName(Principal principal) {
		String name = principal.getName();
		return new ResponseEntity<>(name,HttpStatus.OK);
	}
	
	// get detail information for current user
	@GetMapping("/currentDetails")
	public ResponseEntity<UserDetails> getDetailName(Principal principal) {
		String name = principal.getName();
		return new ResponseEntity<>(userDetailService.loadUserByUsername(name),HttpStatus.OK);
	}
	
}
