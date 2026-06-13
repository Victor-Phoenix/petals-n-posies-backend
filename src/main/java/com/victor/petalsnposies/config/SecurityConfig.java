package com.victor.petalsnposies.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.victor.petalsnposies.security.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;
	
	
	@Value("${user.username}")
	private String username;
	
	@Value("${user.password}")
	private String password;
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		 .cors(cors -> cors.configurationSource(request -> {
	            var config = new org.springframework.web.cors.CorsConfiguration();
	            config.setAllowedOrigins(List.of(
	                "http://localhost:5173",
	                "https://petals-n-posies.vercel.app"
	            ));
	            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","PATCH"));
	            config.setAllowedHeaders(List.of("*"));
	            return config;
	        }))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
        		.requestMatchers("/auth/login").permitAll()
        		.requestMatchers("/flower/addFlower").authenticated()
        		.requestMatchers("/flower/update").authenticated()
        		.requestMatchers("/flower/delete/**").authenticated()
        		.requestMatchers("/api/orders/getAll").authenticated()
        		.anyRequest().permitAll())
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	
		return http.build();

	}
	
	@Bean 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	@Bean	
	public UserDetailsService userDetailsService(PasswordEncoder encoder) {
		String passcode= encoder.encode(password);
		UserDetails user = User.builder().username(username).password(passcode).build();
		return new InMemoryUserDetailsManager(user);
	}
}
