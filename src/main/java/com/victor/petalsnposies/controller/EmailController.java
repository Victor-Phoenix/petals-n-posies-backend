package com.victor.petalsnposies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victor.petalsnposies.dto.EmailRequestDTO;
import com.victor.petalsnposies.service.MailService;

@RestController()
@RequestMapping("/api/email")
public class EmailController {
	@Autowired 
	private 	MailService mailService;
	
	@PostMapping("/sendEmail")
	public String sendEmail(@RequestBody EmailRequestDTO request) {
		
		mailService.sendPlainText(request);
		return "Email sent sucessfully";
	}
}
