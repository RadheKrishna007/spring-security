package com.MasterSpringSecurity.Spring.Security.dtos;

import com.MasterSpringSecurity.Spring.Security.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtResponse {

	private String jwtToken;
	private User user;
	
}
