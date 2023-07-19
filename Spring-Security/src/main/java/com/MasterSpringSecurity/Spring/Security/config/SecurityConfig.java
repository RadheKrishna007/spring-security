package com.MasterSpringSecurity.Spring.Security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.MasterSpringSecurity.Spring.Security.JwtSecurity.JwtAuthenticationEntryPoint;
import com.MasterSpringSecurity.Spring.Security.JwtSecurity.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
	
	
	@Autowired
	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired 
	JwtAuthenticationFilter jwtAuthenticationFilter;
	

	// in memory configuration
	
/*	@Bean
	public UserDetailsService userDetailService() {
		
		// user create
		UserDetails normal = User.builder()
			.username("Ram")
			.password(passwordEncoder().encode("ram"))
			.roles("NORMAL")
			.build();
		
		UserDetails admin = User.builder()
				.username("Shiv")
				.password(passwordEncoder().encode("shiv"))
				.roles("ADMIN")
				.build();
		
		// InMemoryUserDetailsManager - is implementaion of UserDetailsService interface
		
		return new InMemoryUserDetailsManager(normal,admin); 
		
	} */
	
	
	// DAO implementation
	
	@Autowired
	private UserDetailsService userDetailService;
	
	// Basic Auth which can be accessed from postman or any client(react/angular)
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf()
			.disable()
			.cors()
			.disable()
			.authorizeRequests()
			.antMatchers("/auth/logging")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
		
	}
	
	
	// DAO Implementaion
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.userDetailService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
	}
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}
	
}
