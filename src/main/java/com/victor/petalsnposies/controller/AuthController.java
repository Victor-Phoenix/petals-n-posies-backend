package com.victor.petalsnposies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victor.petalsnposies.dto.LoginRequest;
import com.victor.petalsnposies.dto.LoginResponse;
import com.victor.petalsnposies.security.JwtUtil;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
	
	  @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    @Lazy
	    private JwtUtil jwtUtil;

	    @PostMapping("/login")
	    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
	        authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(	
	                request.getUsername(), 
	                request.getPassword()
	            )
	        );

	        String token = jwtUtil.generateToken(request.getUsername());
	        return ResponseEntity.ok(new LoginResponse(token));
	    }
	    
}
