package com.victor.petalsnposies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
        		.requestMatchers("/flower/addFlower").authenticated()
        		.requestMatchers("/flower/update").authenticated()
        		.requestMatchers("/flower/delete/**").authenticated()            

        		.requestMatchers("/api/orders/getAll").authenticated()
        		.anyRequest().permitAll()
        ).formLogin(form -> form.permitAll());
		return http.build();
		//		http.authorizeHttpRequests((requests) ->
//			requests.requestMatchers("/", "weddings-events" , "about").permitAll()r
//				)
	}
	
	@Bean 
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	
	public UserDetailsService userDetailsService(PasswordEncoder encoder) {
		String password = encoder.encode("password");
		UserDetails user = User.builder().username("victor").password(password).build();
		return new InMemoryUserDetailsManager(user);
	}
}
