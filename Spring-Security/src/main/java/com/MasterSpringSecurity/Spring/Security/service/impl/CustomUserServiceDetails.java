package com.MasterSpringSecurity.Spring.Security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.MasterSpringSecurity.Spring.Security.entity.User;
import com.MasterSpringSecurity.Spring.Security.repository.UserRepository;

@Service
public class CustomUserServiceDetails implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepo.findByEmail(username).orElseThrow(() -> new RuntimeException("User Not Found"));
		return user;
	}
	
}
