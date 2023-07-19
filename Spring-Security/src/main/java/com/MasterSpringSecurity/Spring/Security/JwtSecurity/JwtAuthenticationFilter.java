package com.MasterSpringSecurity.Spring.Security.JwtSecurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	JwtHelper jwtHelper;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String requestHeader = request.getHeader("Authorization");						
		logger.info("Header : {}",requestHeader);
		String username = null;
		String token = null;
		if(requestHeader != null && requestHeader.startsWith("Bearer")) {
			token = requestHeader.substring(7);
			try {
				
				username = this.jwtHelper.getUsernameFromToken(token);
				
			} catch(IllegalArgumentException e) {
				logger.info("IllegalArgumentException occured");
				e.printStackTrace();
			} catch(ExpiredJwtException e) {
				logger.info("ExpiredJwtException occured");
				e.printStackTrace();
			} catch(MalformedJwtException e) {
				logger.info("MalformedJwtException occured");
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			
		} else {
			logger.info("Invalid token");
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
			
			if(validateToken) {
				
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			} else {
				logger.info("Validation failed!!");
			}
			
			
		}
		
		filterChain.doFilter(request, response);
		
	}

}
