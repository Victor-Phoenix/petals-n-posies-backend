package com.victor.petalsnposies.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.victor.petalsnposies.dto.EmailRequestDTO;
@Service
public class MailService {
	
	@Value("${resend.api.key}")
	String resendPass;
	
	

	public void sendPlainText(EmailRequestDTO emailRequest) {
		Resend resend = new Resend(resendPass);
		CreateEmailOptions params = CreateEmailOptions.builder()
				.from("Petals N Posies <onboarding@resend.dev>")
				.to("victortran16@gmail.com")
				.subject("Wedding Event")
				.text("Email: " + emailRequest.getEmail() + "\n" +
		                  "Name: " + emailRequest.getName() + "\n" +
		                  "Event Date: " + emailRequest.getEventDate() + "\n" +
		                  "Venue: " + emailRequest.getVenue() + "\n" +
		                  "Message: " + emailRequest.getMessage())
		.build();
		
				
		 	try {
	            CreateEmailResponse data = resend.emails().send(params);
	            System.out.println(data.getId());
	        } catch (ResendException e) {
	            e.printStackTrace();
	        }
				
		
	}
	

}
