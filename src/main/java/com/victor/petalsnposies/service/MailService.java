package com.victor.petalsnposies.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.victor.petalsnposies.controller.FlowerController;
import com.victor.petalsnposies.dto.EmailRequestDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;
//
//    MailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }

	public void sendPlainText(EmailRequestDTO emailRequest) {
		SimpleMailMessage message = new SimpleMailMessage();
		
//		message.setTo(emailRequest.getTo());
//		message.setSubject(emailRequest.getSubject());
//		message.setText(emailRequest.getBody());
		String body =  "Email: " + emailRequest.getEmail() +"\n";
		body = body + "Name: "+ emailRequest.getName() + "\n";
		
		body = body + "Event Date: "+ emailRequest.getEventDate() + "\n";
	
		body = body + "Venue : "+ emailRequest.getVenue()+ "\n";
		
		body = body + "Message: "+ emailRequest.getMessage() + "\n";
		
		message.setText(body);
		message.setFrom("victortran16@gmail.com");
		message.setTo("victortran16@gmail.com");
		System.out.println(body);
		try {
		    mailSender.send(message);
		} catch (Exception e) {
		    e.printStackTrace();
		}

	
		
	}
	
	public void sendHtml(String to, String subject, String htmlBody) throws MessagingException{
//		MimeMessage message = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
//		helper.setTo(to);
//		helper.setSubject(subject);
//		helper.setText(htmlBody,true);
//		mailSender.send(message);
		
	}
}
